package com.digital2.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digital2.schema.ErrorMsg;
import com.digital2.schema.Inventory;
import com.digital2.schema.Person;
import com.digital2.schema.Product;
import com.digital2.service.InventoryService;
import com.digital2.service.ProductService;
import com.digital2.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "재고", description = "Inventory Related API")
@RequestMapping(value = "/rest/inventory")

public class InventoryController {
	
	@Resource
	ProductService productSvc;
	@Resource
	InventoryService inventorySvc;
	
	//인벤토리Id로 인벤토리 검색
	@RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고 id로 재고 검색", notes = "인벤토리 id로 인벤토리를 검색하는 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Inventory.class),
			@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	public ResponseEntity<?> findInventoryById(@ApiParam(value = "inventoryId") @PathVariable String keyword) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();

		ErrorMsg errors = new ErrorMsg();
		
		try {
			Inventory inventory = inventorySvc.inventorySearchById(""+keyword);
			return new ResponseEntity<Inventory>(inventory, header, HttpStatus.valueOf(200));  // ResponseEntity를 활용한 응답 생성
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}

	}

	//재고 수량 변경. 입력한 id에 해당하는 재고를 입력한 수량으로 변경
	@RequestMapping(value = "/updateQuantity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "재고관리", notes = "재고관리를 위한 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Inventory.class),
			@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	public ResponseEntity<?> updateQuantity(
			@Parameter(name = "재고관리를 위한 정보", description = "", required = true) @RequestBody Inventory inventory) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		Inventory inven_res = new Inventory();
		try {
			if(inventorySvc.updateQuantity(inventory)) {
				inven_res = inventorySvc.inventorySearchById(""+inventory.getInventoryId());
			}
			
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		
		return new ResponseEntity<Inventory>(inven_res, header, HttpStatus.valueOf(200)); // ResponseEntity를 활용한 응답 생성
	}
}
