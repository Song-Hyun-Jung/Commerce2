package com.digital2.service;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

import com.digital2.schema.Phone;

import static com.digital2.lucene.DataHandler.findHardly;
import static com.digital2.lucene.DataHandler.write;

@Component
public class PhoneService {

	//번호로 전화번호 검색
	public Phone phoneSearch(String phoneNumber) throws Exception {

		String key = "phonenumber";
		String value = phoneNumber;

		Document doc = findHardly(key, value);

		Phone phone = new Phone();
		
		if (doc != null) {
			phone.setPhoneId(Long.parseLong(doc.get("phoneid")));
			phone.setPhoneNumber(doc.get("phonenumber"));
		}
		
		return phone;
	}
	public Phone phoneSearchById(String phoneid) throws Exception {

		String key = "phoneid";
		String value = phoneid;

		Document doc = findHardly(key, value);

		Phone phone = new Phone();
		
		if (doc != null) {
			phone.setPhoneId(Long.parseLong(doc.get("phoneid")));
			phone.setPhoneNumber(doc.get("phonenumber"));
		}
		
		return phone;
	}

	//전화번호 추가
	public boolean phoneInsert(Phone phone) throws Exception {

		try {
			if (phoneSearch(phone.getPhoneNumber()).getPhoneNumber() != null) {
				throw new Exception("이미 등록된 전화번호 정보 입니다.");
			}

			phone.setPhoneId(System.currentTimeMillis());
			Document phoneDoc = new Document();
			phoneDoc.add(new TextField("phoneid", ""+phone.getPhoneId(), Store.YES));
			phoneDoc.add(new TextField("phonenumber", ""+phone.getPhoneNumber(), Store.YES));
			
			write(phoneDoc);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
