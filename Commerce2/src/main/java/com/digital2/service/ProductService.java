package com.digital2.service;

import static com.digital2.lucene.DataHandler.findListHardly;
import static com.digital2.lucene.DataHandler.write;
import static com.digital2.lucene.DataHandler.wildCardQuery;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

import com.digital2.schema.Product;

import static com.digital2.lucene.DataHandler.findHardly;


@Component
public class ProductService {
	
	@Resource
	CategoryService categorySvc;
	@Resource
	InventoryService inventorySvc;
	
	//상품id로 검색
	public Product productSearchById(long productId) throws Exception {
			
		String key = "productid";
		String value = ""+productId;

		Document doc = findHardly(key, value);

		Product product = new Product();

		if (doc != null) {
			product.setProductId(Long.parseLong(doc.get("productid")));
			product.setPrice(Long.parseLong(doc.get("price")));
			product.setProductName(doc.get("productname"));
			product.setCategoryId(Long.parseLong(doc.get("catid")));
			product.setInventoryId(Long.parseLong(doc.get("inventoryid")));
				
			return product;
		} else {
			return product;
		}
		
	}
	
	//상품 검색
	public Product productSearch(String productName) throws Exception {
		
		String key = "productname";
		String value = productName;

		Document doc = findHardly(key, value);

		Product product = new Product();

		if (doc != null) {
			product.setProductId(Long.parseLong(doc.get("productid")));
			product.setPrice(Long.parseLong(doc.get("price")));
			product.setProductName(doc.get("productname"));
			product.setCategoryId(Long.parseLong(doc.get("catid")));
			product.setInventoryId(Long.parseLong(doc.get("inventoryid")));
			
			return product;
		} else {
			return product;
		}
	
	}
	
	//고객이 키워드를 입력했을 때 키워드가 포함된 상품 검색
	public List<Product> searchByKeyword(String searchKeyword) throws Exception {
			
		String key = "productname";
		String value = searchKeyword;
	
		List<Document> docList = wildCardQuery(key, value);

		List<Product> productList = new ArrayList<Product>();

		for(Document doc : docList) {
			
			Product product = new Product();
				
			product.setProductId(Long.parseLong(doc.get("productid")));
			product.setPrice(Long.parseLong(doc.get("price")));
			product.setProductName(doc.get("productname"));
			product.setCategoryId(Long.parseLong(doc.get("catid")));
			product.setInventoryId(Long.parseLong(doc.get("inventoryid")));
				
			productList.add(product);
		}
				
		return productList;
		
	}

	//카테고리별 상품 검색
	public List<Product> searchByCategory(String categoryName) throws Exception {
		
		//카테고리 이름에 해당하는 카테고리id 가져오기
		String categoryKey = "categoryname";
		String categoryValue = categoryName;

		Document categoryDoc = findHardly(categoryKey, categoryValue);
		
		String categoryId = categoryDoc.get("categoryid");
		
		//가져온 카테고리 id에 해당하는 상품들 찾기
		String key = "catid";
		String value = categoryId;
		
		List<Document> docList = findListHardly(key, value);
		
		List<Product> productList = new ArrayList<Product>();
		
		for(Document doc : docList) {
				
			Product product = new Product();
					
			product.setProductId(Long.parseLong(doc.get("productid")));
			product.setPrice(Long.parseLong(doc.get("price")));
			product.setProductName(doc.get("productname"));
			product.setCategoryId(Long.parseLong(doc.get("catid")));
			product.setInventoryId(Long.parseLong(doc.get("inventoryid")));
					
			productList.add(product);
		}
			
		return productList;
			
	}
	
	
	//상품 등록
	public boolean productInsert(Product product) throws Exception {

		try {
			if (productSearch(product.getProductName()).getProductName() != null) {
				throw new Exception("이미 등록된 상품 입니다.");
			}

			product.setProductId(System.currentTimeMillis());
			Thread.sleep(10);
			product.setInventoryId(System.currentTimeMillis());
			
			List<Document> doclist = setPluralDoc(product);

			for (Document document : doclist) { //document저장
				write(document);
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	//상품 등록시 카테고리, 인벤토리 등록
	public List<Document> setPluralDoc(Product product) throws Exception {
		
		List<Document> result = new ArrayList<Document>();
		Document productDoc = new Document();
		Document invenDoc = new Document();
		
		
		if(categorySvc.categorySearchById(""+product.getCategoryId()).getCategoryId() == 0) {
			throw new Exception("카테고리가 없습니다.");
		}
		

		productDoc.add(new TextField("productid", "" + product.getProductId(), Store.YES));
		productDoc.add(new TextField("productname", "" + product.getProductName(), Store.YES));
		productDoc.add(new TextField("price", "" + product.getPrice(), Store.YES)); //addressId는 저장x
		productDoc.add(new TextField("catid", "" + product.getCategoryId(), Store.YES));
		productDoc.add(new TextField("inventoryid", "" + product.getInventoryId(), Store.YES));
		result.add(productDoc);

		//재고 등록
		invenDoc.add(new TextField("invid", "" + product.getInventoryId(), Store.YES));
		invenDoc.add(new TextField("quantity", "0", Store.YES)); //기본 재고는 0개
		
		result.add(invenDoc);
		return result; //document 리스트로 person, partyAddress(address가 여러개일경우 여러개가됨) 반환
	}

}
