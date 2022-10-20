package com.digital2.service;

import static com.digital2.lucene.DataHandler.findHardly;
import static com.digital2.lucene.DataHandler.findListHardly;
import static com.digital2.lucene.DataHandler.write;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

import com.digital2.schema.Address;
import com.digital2.schema.Inventory;
import com.digital2.schema.Phone;
import com.digital2.schema.Prepurchase;
import com.digital2.schema.Product;
import com.digital2.schema.PurchaseProduct;

@Component
public class PrepurchaseService {
	
	@Resource
	ProductService productSvc;
	@Resource
	PersonService personSvc;
	@Resource
	AddressService addressSvc;
	@Resource
	PhoneService phoneSvc;
	@Resource
	InventoryService inventorySvc;
	@Resource
	CartService cartSvc;
	
	//주문 상세정보 입력
	public Prepurchase addPurchaseDetail(Prepurchase prepurchase, String personId) throws Exception{
	
		prepurchase.setPrepurchaseId(System.currentTimeMillis());
		prepurchase.setPersonId(Long.parseLong(personId));
		List<Document> prepurchaseDocList = new ArrayList<Document>();
	
		try {	
			if(checkPrepurchase(prepurchase) == true) {	
				//주소
				Address searchAddress = addressSvc.addressSearch(prepurchase.getAddress().getAddressDetail());
					
				if(searchAddress.getAddressDetail() != null) {
					prepurchase.setAddress(searchAddress);
				}
				else { //기존에 저장되어있지 않은 주소의 경우
					Address newAddr = new Address(System.currentTimeMillis(), prepurchase.getAddress().getAddressDetail());
					addressSvc.addressInsert(newAddr);
					newAddr = addressSvc.addressSearch(prepurchase.getAddress().getAddressDetail());
					prepurchase.setAddress(newAddr);
				}
							
				//전화번호
				Phone searchPhone = phoneSvc.phoneSearch(prepurchase.getPhone().getPhoneNumber());
				if(searchPhone.getPhoneNumber() != null) {
					prepurchase.setPhone(searchPhone);
				}
				else { //기존에 저장되어있지 않은 전화번호의 경우
					Phone newPhone = new Phone(System.currentTimeMillis(), prepurchase.getPhone().getPhoneNumber());
					phoneSvc.phoneInsert(newPhone);
					newPhone = phoneSvc.phoneSearch(prepurchase.getPhone().getPhoneNumber());
					prepurchase.setPhone(newPhone);
				}
					
				Document prepurchaseDoc = new Document();
				prepurchaseDoc.add(new TextField("prepurchaseid", "" + prepurchase.getPrepurchaseId(), Store.YES));
				prepurchaseDoc.add(new TextField("prepurchasepersonid", "" + personId, Store.YES));
				//전화번호와 주소는 id만 저장
				prepurchaseDoc.add(new TextField("prepurchaseaddressid", "" + prepurchase.getAddress().getAddressId(), Store.YES));
				prepurchaseDoc.add(new TextField("prepurchasephoneid", "" + prepurchase.getPhone().getPhoneId(), Store.YES));
				prepurchaseDocList.add(prepurchaseDoc);
					
				for(PurchaseProduct c : prepurchase.getPurchaseProductList()) {
					prepurchaseDocList.add(setPartyPrepurchase(prepurchase.getPrepurchaseId(), c));
				}
						
				for (Document document : prepurchaseDocList) { 
					write(document);
				}
			}
					
			return getPrepurchase(""+prepurchase.getPrepurchaseId());
				
		}catch(Exception e) {
				throw e;
		}
	}
		
	public Document setPartyPrepurchase(Long prepurchaseId, PurchaseProduct purchaseProduct) { //구매하는 상품이 여러개이므로 party
		Document partyPrepurchaseDoc = new Document();
			
		partyPrepurchaseDoc.add(new TextField("partyprepurchaseid", "" + prepurchaseId, Store.YES));
		partyPrepurchaseDoc.add(new TextField("partyprepurchaseproductid", "" + purchaseProduct.getProductId(), Store.YES));
		partyPrepurchaseDoc.add(new TextField("partyprepurchaseproductquantity", "" + purchaseProduct.getProductQuantity(), Store.YES));
		return partyPrepurchaseDoc;
	}
	public List<Document> getPartyPrepurchase(Long prepurchaseId) {

		List<Document> partyPrepurchaseDocList = findListHardly("partyprepurchaseid", ""+prepurchaseId);

		return partyPrepurchaseDocList;
	}
		
	//주문상세 오류 확인
	public boolean checkPrepurchase(Prepurchase prepurchase) throws Exception{
		Map<Long, Long> checkInvenQuantity = new HashMap<Long, Long>(); //카트에 있는 같은 아이템들의 개수가 총 몇개인지 확인
		
		for(PurchaseProduct purchaseProduct : prepurchase.getPurchaseProductList()) {
			Product product = productSvc.productSearchById(purchaseProduct.getProductId());
			if(product.getProductName() == null){
				throw new Exception("존재하지 않는 상품입니다.");
			}
			if(purchaseProduct.getProductQuantity() == 0) {
				throw new Exception("수량이 입력되지 않은 상태입니다.");
			}
				
			//주문상세에 있는 같은 아이템들의 개수가 총 몇개인지 확인
			Inventory inventory = inventorySvc.inventorySearchById(""+product.getInventoryId());
			if(purchaseProduct.getProductQuantity() > inventory.getQuantity()) {
				throw new Exception("재고가 부족합니다.");
			}
			
			if(checkInvenQuantity.get(purchaseProduct.getProductId()) == null) {
				checkInvenQuantity.put(purchaseProduct.getProductId(), purchaseProduct.getProductQuantity());
			}
			else {
				long curProductQuantity = checkInvenQuantity.get(purchaseProduct.getProductId()); //현재 주문상세에 담긴 하나의 상품의 개수
				if((curProductQuantity + purchaseProduct.getProductQuantity()) > inventory.getQuantity()) {
					throw new Exception("재고가 부족합니다.");
				}
				checkInvenQuantity.replace(purchaseProduct.getProductId(), curProductQuantity + purchaseProduct.getProductQuantity());
			}
		}
		if(prepurchase.getPhone().getPhoneNumber() == null) {
			throw new Exception("전화번호가 입력되지 않았습니다.");
		}

		if(prepurchase.getAddress().getAddressDetail() == null) {
			throw new Exception("주소가 입력되지 않았습니다.");
		}
		return true;
	}
	
	//주문 상세 조회
	public Prepurchase getPrepurchase(String prepurchaseId) throws Exception {
		String key = "prepurchaseid";
		String value = prepurchaseId;

		Document prepurchaseDoc = findHardly(key, value);
		try {
			Prepurchase prepurchase = new Prepurchase();
			
			if (prepurchaseDoc != null) {
				prepurchase.setPrepurchaseId(Long.parseLong(prepurchaseDoc.get("prepurchaseid")));
				prepurchase.setPersonId(Long.parseLong(prepurchaseDoc.get("prepurchasepersonid")));
				Long addressId = Long.parseLong(prepurchaseDoc.get("prepurchaseaddressid"));
				prepurchase.setAddress(new Address(addressId, addressSvc.addressSearchById(""+addressId).getAddressDetail()));
				Long phoneId = Long.parseLong(prepurchaseDoc.get("prepurchasephoneid"));
				prepurchase.setPhone(new Phone(phoneId, phoneSvc.phoneSearchById(""+phoneId).getPhoneNumber()));
		
			
				List<PurchaseProduct> prepurchaseProductList = new ArrayList<>();
				List<Document> partyprepurchase = getPartyPrepurchase(Long.parseLong(prepurchaseDoc.get("prepurchaseid")));
				for(Document doc : partyprepurchase){
					
					PurchaseProduct purchaseProduct = new PurchaseProduct();
					
					purchaseProduct.setProductId(Long.parseLong(doc.get("partyprepurchaseproductid")));
					purchaseProduct.setProductQuantity(Long.parseLong(doc.get("partyprepurchaseproductquantity")));
					
					prepurchaseProductList.add(purchaseProduct);
				}
				
				prepurchase.setPurchaseProductList(prepurchaseProductList);
			}
			return prepurchase;
		}catch(Exception e) {
			throw e;
		}
	}

	//유저의 전체 주문상세 가져오기
	public List<Prepurchase> getAllPrepurchase(String personId) throws Exception{
		String key = "prepurchasepersonid";
		String value = personId;

		List<Document> prepurchaseDocList = findListHardly(key, value);
		
		List<Prepurchase> prepurchaseList = new ArrayList<>();
		
		try {
			for(Document prepurchaseDoc : prepurchaseDocList) {
				Prepurchase prepurchase = new Prepurchase();
				
				prepurchase.setPrepurchaseId(Long.parseLong(prepurchaseDoc.get("prepurchaseid")));
				prepurchase.setPersonId(Long.parseLong(prepurchaseDoc.get("prepurchasepersonid")));
				Long addressId = Long.parseLong(prepurchaseDoc.get("prepurchaseaddressid"));
				prepurchase.setAddress(new Address(addressId, addressSvc.addressSearchById(""+addressId).getAddressDetail()));
				Long phoneId = Long.parseLong(prepurchaseDoc.get("prepurchasephoneid"));
				prepurchase.setPhone(new Phone(phoneId, phoneSvc.phoneSearchById(""+phoneId).getPhoneNumber()));
				
				
				List<PurchaseProduct> prepurchaseProductList = new ArrayList<>();
				List<Document> partyprepurchase = getPartyPrepurchase(Long.parseLong(prepurchaseDoc.get("prepurchaseid")));
				for(Document doc : partyprepurchase){
					PurchaseProduct purchaseProduct = new PurchaseProduct();
					
					purchaseProduct.setProductId(Long.parseLong(doc.get("partyprepurchaseproductid")));
					purchaseProduct.setProductQuantity(Long.parseLong(doc.get("partyprepurchaseproductquantity")));
					
					prepurchaseProductList.add(purchaseProduct);
				}
				
				prepurchase.setPurchaseProductList(prepurchaseProductList);
				prepurchaseList.add(prepurchase);
			}
			return prepurchaseList;
		}catch(Exception e) {
			throw e;
		}
	}

}
