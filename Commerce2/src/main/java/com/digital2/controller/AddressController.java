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

import com.digital2.schema.Address;
import com.digital2.schema.Category;
import com.digital2.schema.ErrorMsg;
import com.digital2.schema.Person;
import com.digital2.schema.list.ProductList;
import com.digital2.service.AddressService;
import com.digital2.service.PersonService;
import com.digital2.service.CategoryService;
import com.digital2.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "주소", description = "Address Related API")
@RequestMapping(value = "/rest/address")

public class AddressController {

	@Resource
	AddressService addrSvc;
	
	//상세주소로 주소를 검색
	@RequestMapping(value = "/inquiry/{keyword}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "주소 찾기 서비스", notes = "주소 검색하는 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Address.class),
			@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	public ResponseEntity<?> findAddress(@ApiParam(value = "주소") @PathVariable String keyword) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();

		ErrorMsg errors = new ErrorMsg();
		
		try {
			Address address = addrSvc.addressSearch(keyword);
			return new ResponseEntity<Address>(address, header, HttpStatus.valueOf(200));  // ResponseEntity를 활용한 응답 생성
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}

	}
}
