package com.digital2.service;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

import com.digital2.schema.Address;

import static com.digital2.lucene.DataHandler.findHardly;
import static com.digital2.lucene.DataHandler.write;


@Component
public class AddressService {

	//상세주소로 Address검색
	public Address addressSearch(String addressdetail) throws Exception {

		String key = "addressdetail";
		String value = addressdetail;

		Document doc = findHardly(key, value);

		Address addr = new Address();
		
		if (doc != null) {
			addr.setAddressId(Long.parseLong(doc.get("addressid")));
			addr.setAddressDetail(doc.get("addressdetail"));
		}
		
		return addr;
	}

	//AddressId로 Address검색
	public Address addressSearchById(String addressid) throws Exception {

		String key = "addressid";
		String value = addressid;

		Document doc = findHardly(key, value);

		Address addr = new Address();
		
		if (doc != null) {
			addr.setAddressId(Long.parseLong(doc.get("addressid")));
			addr.setAddressDetail(doc.get("addressdetail"));
		}
		
		return addr;
	}

	//주소 추가(회원가입시)
	public boolean addressInsert(Address address) throws Exception {

		try {
			if (addressSearch(address.getAddressDetail()).getAddressDetail() != null) {
				throw new Exception("이미 등록된 주소정보 입니다.");
			}

			address.setAddressId(System.currentTimeMillis());
			Document addrDoc = new Document();
			addrDoc.add(new TextField("addressid", ""+address.getAddressId(), Store.YES));
			addrDoc.add(new TextField("addressdetail", ""+address.getAddressDetail(), Store.YES));
			
			write(addrDoc);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
