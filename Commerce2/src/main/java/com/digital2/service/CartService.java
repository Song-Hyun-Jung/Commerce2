package com.digital2.service;

import static com.digital2.lucene.DataHandler.delete;
import static com.digital2.lucene.DataHandler.findListHardly;
import static com.digital2.lucene.DataHandler.write;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

import com.digital2.schema.Cart;
import com.digital2.schema.PurchaseProduct;


@Component
public class CartService {
	@Resource
	ProductService productSvc;
	
	//전제 장바구니 가져오기
	public List<Cart> getCart(String personId) {
		String key = "cartpersonid";
		String value = personId;

		
		List<Document> docList = findListHardly(key, value);

		List<Cart> cartList = new ArrayList<Cart>();
		

		for(Document doc : docList){
			Cart cart = new Cart();
			cart.setCartId(Long.parseLong(doc.get("cartid")));
			cart.setPersonId(Long.parseLong(doc.get("cartpersonid")));
			cart.setProductId(Long.parseLong(doc.get("cartproductid")));
			cart.setProductQuantity(Long.parseLong(doc.get("cartproductquantity")));
			cartList.add(cart);
		}
		
		
		return cartList;
	}

	
	//장바구니에 아이템 추가
	public boolean addCart(Cart cart, String personId) throws Exception{ 
		try {
			if(productSvc.productSearchById(cart.getProductId()).getProductName() == null){
				throw new Exception("존재하지 않는 상품입니다.");
			}
			if(cart.getProductQuantity() == 0) {
				throw new Exception("수량이 입력되지 않은 상태입니다.");
			}
			
			long cartId = System.currentTimeMillis();
			
			Document cartDoc = new Document();
			
			cartDoc.add(new TextField("cartid", "" + cartId, Store.YES));
			cartDoc.add(new TextField("cartpersonid", "" + personId, Store.YES));
			cartDoc.add(new TextField("cartproductid", "" + cart.getProductId(), Store.YES));
			cartDoc.add(new TextField("cartproductquantity", "" + cart.getProductQuantity(), Store.YES));
		    
			write(cartDoc);
			return true;
		} catch (Exception e) {
			throw e;
		}
			
	}
	
	//장바구니에서 구매한 아이템 삭제
	public boolean deleteCart(List<PurchaseProduct> purchaseProducts, String personId) throws Exception{
		String key = "cartpersonid";
		String value = personId;
		try {
			List<Document> docList = findListHardly(key, value);
			
			for(Document doc : docList){
				 for(PurchaseProduct pd : purchaseProducts) {
					if(pd.getProductId() == Long.parseLong(doc.get("cartproductid"))) {
						String cartId = doc.get("cartid");
						delete("cartid", ""+cartId);
					}
				 }
			}
			return true;
		
		}catch(Exception e) {
			throw e;
		}
	}
}
