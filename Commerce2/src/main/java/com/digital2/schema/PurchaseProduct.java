package com.digital2.schema;

import io.swagger.annotations.ApiModelProperty;

public class PurchaseProduct { //주문 상품 넣기위한 클래스
	@ApiModelProperty(required = true, position = 1, example = "111", dataType = "long", notes = "상품ID")
	private long productId;
	@ApiModelProperty(required = true, position = 2, example = "담을 개수", dataType = "long", notes = "장바구니에 담을 상품의 개수")
	private long productQuantity;
	
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public long getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(long productQuantity) {
		this.productQuantity = productQuantity;
	}
	
	
}
