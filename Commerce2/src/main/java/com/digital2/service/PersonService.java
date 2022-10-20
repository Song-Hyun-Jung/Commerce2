package com.digital2.service;

import static com.digital2.lucene.DataHandler.findHardly;
import static com.digital2.lucene.DataHandler.findListHardly;
import static com.digital2.lucene.DataHandler.write;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

import com.digital2.schema.Address;
import com.digital2.schema.Person;
import com.digital2.schema.Phone;

@Component
public class PersonService {

	@Resource
	AddressService addressSvc;
	@Resource
	PhoneService phoneSvc;
	@Resource
	AuthService authSvc;

	//회원 검색-로그인 아이디로 검색
	public Person personSearch(String loginId) throws Exception {

		String key = "loginid";
		String value = loginId;

		Document doc = findHardly(key, value);

		Person person = new Person();

		if (doc != null) {
			person.setPersonId(Long.parseLong(doc.get("personid")));
			person.setGender(doc.get("gender"));
			person.setPersonName(doc.get("personname"));
			person.setLoginId(doc.get("loginid"));
			person.setPassword(doc.get("password"));
			
			List<Document> partyAddressDocList = getPartyAddress(person);
			List<Document> partyPhoneDocList = getPartyPhone(person);
			
			List<Address> addressList = new ArrayList<Address>();
			for (Document partyAddressDoc : partyAddressDocList) {
				
				Address addr = addressSvc.addressSearchById(partyAddressDoc.get("partyaddressid"));
				addressList.add(addr);
			}
			
			List<Phone> phoneList = new ArrayList<Phone>();
			for(Document partyPhoneDoc : partyPhoneDocList) {
				Phone phone = phoneSvc.phoneSearchById(partyPhoneDoc.get("partyphoneid"));
				phoneList.add(phone);
			}
			
			person.setAddressList(addressList);
			person.setPhoneList(phoneList);
			return person;
		} else {
			return person;
		}
	}
	
	//personId로 회원검색
	public Person personSearchById(String personId) throws Exception {

		String key = "personid";
		String value = personId;

		Document doc = findHardly(key, value);

		Person person = new Person();

		if (doc != null) {
			person.setPersonId(Long.parseLong(doc.get("personid")));
			person.setGender(doc.get("gender"));
			person.setPersonName(doc.get("personname"));
			person.setLoginId(doc.get("loginid"));
			person.setPassword(doc.get("password"));
			
			List<Document> partyAddressDocList = getPartyAddress(person);
			List<Document> partyPhoneDocList = getPartyPhone(person);
			
			List<Address> addressList = new ArrayList<Address>();
			for (Document partyAddressDoc : partyAddressDocList) {
				
				Address addr = addressSvc.addressSearchById(partyAddressDoc.get("partyaddressid"));
				addressList.add(addr);
			}
			
			List<Phone> phoneList = new ArrayList<Phone>();
			for(Document partyPhoneDoc : partyPhoneDocList) {
				Phone phone = phoneSvc.phoneSearchById(partyPhoneDoc.get("partyphoneid"));
				phoneList.add(phone);
			}
			
			person.setAddressList(addressList);
			person.setPhoneList(phoneList);
			return person;
		} else {
			return person;
		}
	}
	
	//로그인->로그인 성공시 객체를 반환
	public Long login(String loginId, String password) throws Exception{
		
		try {
			Person person = personSearch(loginId);
			if(person.getLoginId() == null) {
				throw new Exception("가입되지 않은 회원입니다.");
			}
			
			if(person.getPassword().equals(password)) {
				long tokenId = System.currentTimeMillis();
				Thread.sleep(10);
				authSvc.getLoginMap().put(person.getPersonId(), System.currentTimeMillis());
				Map<Long, Long> loginMap = authSvc.getLoginMap(); //<personId, token생성시간>
				authSvc.getAuthMap().put(tokenId, loginMap); //<token, loginMap>
				System.out.println(tokenId + "   " + authSvc.getAuthPersonId(tokenId));
				return tokenId;	
			}
			else {
				throw new Exception("비밀번호 오류입니다.");
			}
		} catch(Exception e) {
			throw e;
		}
	}
	
	//회원가입
	public boolean signUp(Person person) throws Exception {
		
		try {
			if (personSearch(person.getLoginId()).getLoginId() != null) { //로그인아이디는 중복x
				throw new Exception("이미 가입된 회원정보 입니다.");
			}

			person.setPersonId(System.currentTimeMillis());
			List<Document> doclist = setPluralDoc(person);

			for (Document document : doclist) { //document저장
				write(document);
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Document> setPluralDoc(Person person) { 

		List<Address> addrList = person.getAddressList();
		List<Phone> phoneList = person.getPhoneList();

		List<Document> result = new ArrayList<Document>();
		Document personDoc = new Document();

		personDoc.add(new TextField("personname", "" + person.getPersonName(), Store.YES));
		personDoc.add(new TextField("personid", "" + person.getPersonId(), Store.YES));
		personDoc.add(new TextField("gender", "" + person.getGender(), Store.YES)); //addressId는 저장x
		personDoc.add(new TextField("loginid", "" + person.getLoginId(), Store.YES));
		personDoc.add(new TextField("password", "" + person.getPassword(), Store.YES));
		
		result.add(personDoc);
		for (Address addr : addrList) { // partyAddress 설정

			try {
				if (addressSvc.addressInsert(addr)) { //address도 넣고 partyAddress도 저장
					result.add(setPartyAddress(person, addressSvc.addressSearch(addr.getAddressDetail())));
				}
			} catch (Exception e) {
				try {
					result.add(setPartyAddress(person, addressSvc.addressSearch(addr.getAddressDetail())));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		for (Phone phone : phoneList) { // partyPhone 설정

			try {
				if (phoneSvc.phoneInsert(phone)) { //phone도 넣고 partyPhone도 저장
					result.add(setPartyPhone(person, phoneSvc.phoneSearch(phone.getPhoneNumber())));
				}
			} catch (Exception e) {
				try {
					result.add(setPartyPhone(person, phoneSvc.phoneSearch(phone.getPhoneNumber())));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		return result; //document 리스트로 person, partyAddress(address가 여러개일경우 여러개가됨) 반환
	}

	public Document setPartyAddress(Person person, Address address) { //주소가 여러개일 수 있기 때문에 party->데이터 양을 줄일 수 있다. 

		Document partyAddressDoc = new Document(); //하나의 document에 personId와 addressId 저장

		partyAddressDoc.add(new TextField("partyaddressid", "" + address.getAddressId(), Store.YES));
		partyAddressDoc.add(new TextField("partyaddresspersonid", "" + person.getPersonId(), Store.YES));

		return partyAddressDoc;
	}
	
	public List<Document> getPartyAddress(Person person) {

		List<Document> partyAddressDocList = findListHardly("partyaddresspersonid", ""+person.getPersonId());

		return partyAddressDocList;
	}
	
	
	public Document setPartyPhone(Person person, Phone phone) { //전화번호가 여러개일 수 있기 때문에 party
		Document partyPhoneDoc = new Document();
		
		partyPhoneDoc.add(new TextField("partyphoneid", "" + phone.getPhoneId(), Store.YES));
		partyPhoneDoc.add(new TextField("partyphonepersonid", "" + person.getPersonId(), Store.YES));
		
		return partyPhoneDoc;
	}
	public List<Document> getPartyPhone(Person person) {

		List<Document> partyPhoneDocList = findListHardly("partyphonepersonid", ""+person.getPersonId());

		return partyPhoneDocList;
	}
}
