package com.digital2.schema;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Prepurchase { //주문 상세정보 입력을 위한 클래스

	@ApiModelProperty(required = false, position = 1, example = "1", dataType = "long", notes = "prepurchaseID")
	private long prepurchaseId;
	
	@ApiModelProperty(required = false, position = 2, example = "1", dataType = "long", notes = "고객personID")
	private long personId;
	
	@ApiModelProperty(required = true, position = 3,  example = "[{\"productId\":1,\"productQuantity\":\"1\"}]", dataType = "array", notes="구매할 상품")
	private List<PurchaseProduct> purchaseProductList;
	
	@ApiModelProperty(required = true, position = 4,  example = "{\"phoneId\":1,\"phoneNumber\":\"전화번호\"}", dataType = "object", notes="전화번호 정보")
	private Phone phone;
	
	@ApiModelProperty(required = true, position = 5,  example = "{\"addressId\":1,\"addressDetail\":\"주소\"}", dataType = "object", notes="주소 정보")
	private Address address;
	
	
	
	public long getPrepurchaseId() {
		return prepurchaseId;
	}
	public void setPrepurchaseId(long prepurchaseId) {
		this.prepurchaseId = prepurchaseId;
	}
	public List<PurchaseProduct> getPurchaseProductList() {
		return purchaseProductList;
	}
	public void setPurchaseProductList(List<PurchaseProduct> purchaseProductList) {
		this.purchaseProductList = purchaseProductList;
	}
	
	
	public long getPersonId() {
		return personId;
	}
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	public Phone getPhone() {
		return phone;
	}
	public void setPhone(Phone phone) {
		this.phone = phone;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	
}
