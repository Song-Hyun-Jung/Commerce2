package com.digital2.schema;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel
public class PurchaseResult {//구매 결과를 보여주기 위한 클래스
	
	long purchaseId;
	long prepurchaseId;
	List<PurchaseProduct> purchaseProductList = new ArrayList<>();
	long totalPrice;
	long personId;
	
	
	public long getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(long purchaseId) {
		this.purchaseId = purchaseId;
	}
	
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
	public long getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(long totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public long getPersonId() {
		return personId;
	}
	public void setPersonId(long personId) {
		this.personId = personId;
	}

	
	
	
	
}
