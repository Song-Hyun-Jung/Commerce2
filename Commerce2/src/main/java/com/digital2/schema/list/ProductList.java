package com.digital2.schema.list;

import java.util.List;

import com.digital2.schema.Product;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;

@ArraySchema
public class ProductList {
	
	@ApiModelProperty(required = false, position = 3, example = "", dataType = "array")
	private List<Product> products;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
