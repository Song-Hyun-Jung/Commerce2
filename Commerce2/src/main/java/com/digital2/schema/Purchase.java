package com.digital2.schema;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;

@ArraySchema
public class Purchase { //구매

	@ApiModelProperty(required = false, position = 1, example="1", dataType="long", notes="purchase ID")
	private long purchaseId;
	
	@ApiModelProperty(required = true, position = 2, example="1", dataType="long", notes="prepurchase ID")
	private long prepurchaseId;

	@ApiModelProperty(required = false, position = 3, example = "111", dataType = "long", notes="person ID")
	private long personId;

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

	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}
	
	
	
}
