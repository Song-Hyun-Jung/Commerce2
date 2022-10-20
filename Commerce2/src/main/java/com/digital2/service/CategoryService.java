package com.digital2.service;


import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.springframework.stereotype.Component;

import com.digital2.schema.Category;

import static com.digital2.lucene.DataHandler.write;
import static com.digital2.lucene.DataHandler.findHardly;

@Component
public class CategoryService {

	//카테고리 명으로 카테고리 검색
	public Category categorySearch(String categoryName) throws Exception {

		String key = "categoryname";
		String value = categoryName;

		Document doc = findHardly(key, value);

		Category category = new Category();
		
		if (doc != null) {
			category.setCategoryId(Long.parseLong(doc.get("categoryid")));
			category.setCategoryName(doc.get("categoryname"));
		}
		
		return category;
	}

	//카테고리 id로 카테고리 검색
	public Category categorySearchById(String categoryId) throws Exception {

		String key = "categoryid";
		String value = categoryId;

		Document doc = findHardly(key, value);

		Category category = new Category();
		
		if (doc != null) {
			category.setCategoryId(Long.parseLong(doc.get("categoryid")));
			category.setCategoryName(doc.get("categoryname"));
		}
		
		return category;
	}

	
	//카테고리 추가
	public boolean categoryInsert(Category category) throws Exception {

		try {
			if (categorySearch(category.getCategoryName()).getCategoryName() != null) {
				throw new Exception("이미 등록된 카테고리 입니다.");
			}

			category.setCategoryId(System.currentTimeMillis());
			Document catDoc = new Document();
			catDoc.add(new TextField("categoryid", ""+category.getCategoryId(), Store.YES));
			catDoc.add(new TextField("categoryname", ""+category.getCategoryName(), Store.YES));
			
			write(catDoc);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
}
