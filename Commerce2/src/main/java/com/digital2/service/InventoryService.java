package com.digital2.service;

import static com.digital2.lucene.DataHandler.findHardly;
import static com.digital2.lucene.DataHandler.update;

import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.springframework.stereotype.Component;

import com.digital2.schema.Inventory;


@Component
public class InventoryService {
	
	@Resource
	ProductService productSvc;
	
	//인벤토리 id로 인벤토리 검색
	public Inventory inventorySearchById(String inventoryId) throws Exception {

		String key = "invid";
		String value = inventoryId;

		Document doc = findHardly(key, value);

		Inventory inventory = new Inventory();
		
		if (doc != null) {
			inventory.setInventoryId(Long.parseLong(doc.get("invid")));
			inventory.setQuantity(Long.parseLong(doc.get("quantity")));
		}
		
		return inventory;
	}

	//재고 변경. 인벤토리id와 수량을 받아와서 업데이트
	public boolean updateQuantity(Inventory inventory) throws Exception {
		try{
			if (inventorySearchById("" + inventory.getInventoryId()).getInventoryId() == 0) { //상품이 등록될때 인벤토리도 생성됨. 인벤토리가 없다는 것은 상품도 없는 것으로 간주
				throw new Exception("상품의 인벤토리가 등록되지 않은 상태입니다.");
			}
			
			Document updateInventoryDoc = new Document();
			updateInventoryDoc.add(new TextField("invid", ""+inventory.getInventoryId(), Store.YES));
			updateInventoryDoc.add(new TextField("quantity", ""+inventory.getQuantity(), Store.YES));
			
			update("invid", ""+inventory.getInventoryId(), updateInventoryDoc);
			return true;
		} catch(Exception e) {
			throw e;
		}
	}
	
	//구매시 수량 차감
	public boolean subtractQuantity(Inventory inventory, long purchaseQuantity) throws Exception {
		try{
			if (inventorySearchById("" + inventory.getInventoryId()).getInventoryId() == 0) { //상품이 등록될때 인벤토리도 생성됨. 인벤토리가 없다는 것은 상품도 없는 것으로 간주
				throw new Exception("상품의 인벤토리가 등록되지 않은 상태입니다.");
			}
			if(inventory.getQuantity() - purchaseQuantity < 0) {
				throw new Exception("물량이 없습니다.");
			}
			
			Document updateInventoryDoc = new Document();
			updateInventoryDoc.add(new TextField("invid", ""+inventory.getInventoryId(), Store.YES));
			updateInventoryDoc.add(new TextField("quantity", ""+(inventory.getQuantity() - purchaseQuantity), Store.YES));
			
			update("invid", ""+inventory.getInventoryId(), updateInventoryDoc);
			return true;
		} catch(Exception e) {
			throw e;
		}
	}
}
