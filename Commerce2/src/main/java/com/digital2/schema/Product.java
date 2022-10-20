package com.digital2.schema;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.digital2.schema.Category;

@ApiModel
public class Product {

	@ApiModelProperty(required = false, position = 1, example = "1", dataType = "long")
	private long productId;
	
	@ApiModelProperty(required = true, position = 2, example = "1", dataType = "long")
	private long categoryId;
	
	@ApiModelProperty(required = true, position = 3, example = "1", dataType = "long")
	private long inventoryId;
	
	@ApiModelProperty(required = true, position = 4, example = "10000", dataType = "long")
	private long price;
	
	@ApiModelProperty(required = true, position = 5, example = "상품명", dataType = "string")
	private String productName;
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}
	
	
	/*
	public static void main (String [] args) throws Exception {
		
		Class cl = Class.forName("com.digital2.schema.Product");
		Object obj = cl.newInstance();
		
		Method [] methods = cl.getMethods();
		
		for (Method method : methods) {
			System.out.println(method.getName());
			Parameter [] params = method.getParameters();
		}
	}
	*/
}
