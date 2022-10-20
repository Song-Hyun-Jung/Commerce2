package com.digital2.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digital2.schema.ErrorMsg;
import com.digital2.schema.Person;
import com.digital2.schema.SuccessMsg;
import com.digital2.service.AuthService;
import com.digital2.service.PersonService;
import com.digital2.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "고객", description = "Person Related API")
@RequestMapping(value = "/rest/person")

public class PersonController {

	@Resource
	PersonService personSvc;
	@Resource
	AuthService authSvc;
	
	//로그인  ID로 회원가입 여부 검색
	@RequestMapping(value = "/inquiry/{keyword}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "회원가입 여부 검색", notes = "로그인id로 회원가입 여부 검색하는 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Person.class),
			@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	public ResponseEntity<?> findPerson(@ApiParam(value = "loginId") @PathVariable String keyword) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();

		ErrorMsg errors = new ErrorMsg();
		
		try {
			Person person = personSvc.personSearch(keyword);
			return new ResponseEntity<Person>(person, header, HttpStatus.valueOf(200)); // ResponseEntity를 활용한 응답 생성
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}

	}
	
	//회원가입(유저정보, 주소, 전화번호 추가)
	@RequestMapping(value = "/signUp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "회원가입", notes = "회원가입을 위한 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Person.class),
			@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	public ResponseEntity<?> signUp(
			@Parameter(name = "회원가입을 위한 고객정보", description = "", required = true) @RequestBody Person person) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		Person person_res = new Person();
		try {
			if(personSvc.signUp(person)) {
				person_res = personSvc.personSearch(person.getLoginId());
			}
			
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		
		return new ResponseEntity<Person>(person_res, header, HttpStatus.valueOf(200)); // ResponseEntity를 활용한 응답 생성
	}
	
	//로그인->토큰
	@RequestMapping(value = "/logIn", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "로그인", notes = "로그인을 위한 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Person.class),
			@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	public ResponseEntity<?> logIn(
			@Parameter(name = "로그인을 위한 정보", description = "", required = true) HttpServletRequest req, @RequestParam("loginId") String loginId, @RequestParam("password") String password) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		try {
			Long token = personSvc.login(loginId, password);
			
			//로그인 성공시 token, personId 반환
			return new ResponseEntity<SuccessMsg>(new SuccessMsg("로그인 하였습니다. token: " + token), header, HttpStatus.valueOf(200));
		
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
}
