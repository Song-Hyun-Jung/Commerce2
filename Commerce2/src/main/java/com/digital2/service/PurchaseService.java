package com.digital2.service;

import static com.digital2.lucene.DataHandler.write;
import static com.digital2.lucene.DataHandler.findListHardly;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

import com.digital2.schema.Inventory;
import com.digital2.schema.Prepurchase;
import com.digital2.schema.Product;
import com.digital2.schema.Purchase;
import com.digital2.schema.PurchaseProduct;
import com.digital2.schema.PurchaseResult;

@Component
public class PurchaseService {
	
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
	@Resource
	PrepurchaseService prepurchaseSvc;

	//구매->인벤토리 수량 차감, 데이터베이스 저장, 장바구니에서 삭제
	public PurchaseResult purchase(Purchase purchase, String personId) throws Exception {
	
		long totalPrice = 0;
		
		long purchaseId = System.currentTimeMillis(); // 구매 id
		long purchasePreId = purchase.getPrepurchaseId(); //주문 상세 id
		
		if(purchasePreId == 0) {
			throw new Exception("주문 상세가 입력되지 않았습니다.");
		}
		
		Prepurchase prepurchase = prepurchaseSvc.getPrepurchase(""+purchasePreId); //주문 상세 객체 가져오기
		System.out.println(prepurchase.getPrepurchaseId());
		if(prepurchase.getPrepurchaseId() == 0) {
			throw new Exception("주문 상세가 잘못되었습니다.");
		}
		
		PurchaseResult purchaseResult = new PurchaseResult();
		purchaseResult.setPurchaseId(purchaseId);
		purchaseResult.setPrepurchaseId(purchasePreId);
		
		
		Document purchaseDoc = new Document();
		purchaseDoc.add(new TextField("purchaseid", "" + purchaseId, Store.YES));
		purchaseDoc.add(new TextField("purchasepreid", "" + purchasePreId, Store.YES));
		purchaseDoc.add(new TextField("purchasepersonid", "" + personId, Store.YES));
		
		List<PurchaseProduct> purchaseProductList = prepurchase.getPurchaseProductList();
		
		//구매수량 인벤토리에서 감소, 총 가격 계산
		for(PurchaseProduct pd : purchaseProductList) {
			Product product = productSvc.productSearchById(pd.getProductId());
			Inventory inventory = inventorySvc.inventorySearchById(""+product.getInventoryId());
			if(inventory.getQuantity() < pd.getProductQuantity() || inventory.getQuantity() <= 0) { //주문 상세 이후에 다른사람의 구매로 재고가 부족할수 있음
				throw new Exception("수량 부족. 구매 실패");
			}
			inventorySvc.subtractQuantity(inventory, pd.getProductQuantity());
			totalPrice += product.getPrice() * pd.getProductQuantity();	
		}
		
		purchaseResult.setPersonId(prepurchase.getPersonId());
		purchaseResult.setPurchaseProductList(purchaseProductList);
		purchaseResult.setTotalPrice(totalPrice);
		
		write(purchaseDoc);
		
		cartSvc.deleteCart(purchaseProductList, personId); //구매한 상품 장바구니에서 삭제
		
		return purchaseResult;
	}

	//유저의 주문내역 확인
	public List<Purchase> getPurchaseHistory(String purchasepersonId) {
		String key = "purchasepersonid";
		String value = purchasepersonId;

		
		List<Document> docList = findListHardly(key, value);

		List<Purchase> purchaseList = new ArrayList<Purchase>();
		

		for(Document doc : docList){
			Purchase purchase = new Purchase();
			purchase.setPersonId(Long.parseLong(doc.get("purchasepersonid")));
			purchase.setPurchaseId(Long.parseLong(doc.get("purchaseid")));
			purchase.setPrepurchaseId(Long.parseLong(doc.get("purchasepreid")));
			
			purchaseList.add(purchase);
		}
		
		
		return purchaseList;
	}
	
	
	
}
