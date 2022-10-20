package com.digital2.schema;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Inventory {
	@ApiModelProperty(required = false, position = 1, example = "1", dataType = "long", notes = "인벤토리 ID")
	private long inventoryId;
	@ApiModelProperty(required = true, position = 3, example = "1", dataType = "long", notes = "상품 수량")
	private long quantity;
	
	public long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	
	
}
