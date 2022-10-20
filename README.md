# Commerce2
SpringBoot + Lucene + Swagger
<br/><br/>
<div>
  <h3>Swagger 구조</h3><br/>
  <img width="935" alt="structure1" src="https://user-images.githubusercontent.com/57795722/196342773-46360495-580c-4bf6-a29d-3409bc06d8d8.png" /><br/>
  <img width="935" alt="structure2" src="https://user-images.githubusercontent.com/57795722/196342779-d3969515-25b5-4630-9034-e1ee85def437.png"/><br/>
  <img width="939" alt="structure3" src="https://user-images.githubusercontent.com/57795722/196342781-77b6f09c-3d3b-4df5-9683-6f093bfc599c.png"/><br/>
  <img width="939" alt="structure4" src="https://user-images.githubusercontent.com/57795722/196342784-5ed4ddcc-1ff3-425d-8087-698a379767cc.png"/><br/>
</div>
<hr/>
<div>
  <h3>로그인</h3><br/>
  *person-controller에서 로그인을 한다</br>
  <img width="935" alt="login" src="https://user-images.githubusercontent.com/57795722/196342768-9c6808c3-dc0f-4975-adcd-1d9de7ce34aa.png"><br/>
  ->결과</br>
  <img width="406" alt="loginResult" src="https://user-images.githubusercontent.com/57795722/196342771-d49265db-623f-43d4-8f0a-7c1c6c33212a.png"><br/>
  *결과로 받아온 token값을 Authorize에 넣어준다.<br/>
  <img width="162" alt="AuthorizeBtn" src="https://user-images.githubusercontent.com/57795722/196342766-ed0d2baa-7490-48ce-b1cc-6ea0ede90544.png">
  <img width="538" alt="Authorize" src="https://user-images.githubusercontent.com/57795722/196342786-9f2a9fa0-0e86-4abe-86dc-1064cf57b0cd.png">
</div>
<hr/>
<div>
  <h3>회원가입</h3><br/>
  *personId, addressId, phoneId는 System.currentTimeMillis로 생성<br/>
  *회원가입 결과<br/>
  <img width="715" alt="join" src="https://user-images.githubusercontent.com/57795722/196345952-e07c4df1-c722-4c89-9b28-fd72c28056fc.png">
</div>
<hr/>
<div>
  <h3>전화번호 & 주소 검색</h3>
  *번호로 검색한다.<br/>
  <img width="358" alt="searchPhone" src="https://user-images.githubusercontent.com/57795722/196345982-f9ef7672-182c-4db3-9706-41b9487f48fa.png"><br/>
  *주소도 전화번호 검색과 동일하게 동작한다. 주소의 경우 상세주소로 검색함.<br/>
</div>
<hr/>
<div>
  <h3>카테고리 등록 및 검색</h3><br/>
  <h4>카테고리 등록</h4>
  *categoryId도 System.currentTimeMillis로 생성</br>
  <img width="544" alt="insertCategory" src="https://user-images.githubusercontent.com/57795722/196346004-14e20825-71ba-4386-af66-36a74e753e7f.png"><br/>
  <h4>카테고리 검색</h4>
  *카테고리 명으로 검색<br/>
  <img width="838" alt="searchCategory" src="https://user-images.githubusercontent.com/57795722/196346011-c0341eb4-b4eb-4d02-8780-3a94ac2e1e07.png">
</div>
<hr/>
<div>
  <h3>상품 등록</h3><br/>
  *categoryId, price, productName을 입력한다. inventoryId와 productId는 System.currentTimeMillis로 생성<br/>
  <img width="160" alt="insertProduct" src="https://user-images.githubusercontent.com/57795722/196347823-171b799a-6ded-4a2f-8cad-1d2248c229e0.png">
  <img width="279" alt="insertProductResult" src="https://user-images.githubusercontent.com/57795722/196347824-0b094f34-ad02-4362-89ff-1e2cd5978cbe.png"><br/>
  <h3>상품검색</h3>
  <h4>관리자 상품검색</h4>
  *정확한 상품명을 입력해야 검색이 된다.<br/>
  <img width="843" alt="searchProduct" src="https://user-images.githubusercontent.com/57795722/196347820-62a61193-be7c-443b-a41e-afc0593ea156.png">
  <h4>키워드 상품검색</h4>
  *상품명에 키워드가 포함되어있는 모든 상품이 검색된다. <br/>
  <img width="725" alt="searchKeyword" src="https://user-images.githubusercontent.com/57795722/196347818-933455d3-74c5-4aa8-8b4a-040b7522ff61.png">
  <h4>카테고리별 상품 검색</h4>
  *카테고리명에 해당하는 모든 상품이 검색된다. <br/>
  <img width="792" alt="searchByCate" src="https://user-images.githubusercontent.com/57795722/196347815-f8fe4f6a-b8e5-46ad-831f-6a3e23d4ca26.png">
</div>
<hr/>
<div>
  <h3>재고관리</h3>
  *product를 검색해서 나온 inventoryId를 사용해서 검색, 재고관리함. 처음 product를 등록할때 inventory의 재고 수량은 0.<br/>
  <h4>재고검색</h4>
  <img width="719" alt="searchInventory" src="https://user-images.githubusercontent.com/57795722/196349923-942c6607-3cb6-4551-ac80-9c2311c8ccfd.png">
  <h4>재고관리</h4>
  *바꿀 인벤토리 id와 수량을 입력한다. <br/>
  <img width="721" alt="changeInven" src="https://user-images.githubusercontent.com/57795722/196349928-1bf74d16-f931-47e5-9c8c-18d2f95287e8.png">
</div>
<hr/>
<div>
  <h3>장바구니에 상품 추가</h3>
  *우유로 검색해서 나온 상품들<br/>
  <img width="400" alt="milk" src="https://user-images.githubusercontent.com/57795722/196351208-e1ec1eac-0e5b-438c-a723-326fa97a784d.png"><br/>
  *장바구니에 추가-상품id와 수량을 넣는다. </br>
  <img width="300" alt="insertCart" src="https://user-images.githubusercontent.com/57795722/196351217-eb3a52fa-957e-4b4a-925b-42afff08c9d8.png">
  <img width="300" alt="insertCartResult" src="https://user-images.githubusercontent.com/57795722/196351214-bf23b1e0-7498-482f-a5cb-a34afac3e793.png">
  <h3>장바구니 조회</h3>
  *personId에 해당하는 장바구니를 가져온다.<br/>
  <img width="454" alt="searchCart" src="https://user-images.githubusercontent.com/57795722/196351212-813e35b4-305a-4c57-b3fb-2546f83b4c06.png">
</div>
<hr/>
<div>
  <h3>가주문서 작성</h3>
  *주소 한개, 전화번호 한개, 구매할 상품 id와 수량들을 입력한다. prepurchaseId는 System.currentTimeMillis로 생성<br/>
  *이때 데이터베이스에 없는 주소를 입력하면 새로 저장되며, 기존에 데이터베이스에 있는 주소일 경우 데이터베이스에서 주소정보를 가져온다. 전화번호도 동일하다. <br/>
  *만약 재고보다 입력한 수량이 많을 경우 재고가 부족하다는 메시지가 나온다.<br/>
  <img width="710" alt="insertOrder" src="https://user-images.githubusercontent.com/57795722/196352946-c7ef1fdb-b844-4b33-a5e9-20ee991cf6d6.png">
  <img width="400" alt="insertOrderResult" src="https://user-images.githubusercontent.com/57795722/196352942-7feb7cdf-3a32-4985-af68-f97629f2834c.png">
  <h3>주문</h3>
  *가주문서 결과로 나온 prepurchaseId(가주문서 Id)를 입력한다</br>
  *재고보다 구매할 수량이 많을 경우 재고가 부족하다는 메시지가 나온다. <br/>
  *purchaseId는 System.currentTimeMillis로 생성<br/>
  <img width="300" alt="purchase" src="https://user-images.githubusercontent.com/57795722/196352940-3a0d6914-7e4e-418a-bae6-858528538b13.png">
  <img width="300" alt="purchaseResult" src="https://user-images.githubusercontent.com/57795722/196352939-753b25ed-ab5a-41b9-aedf-8a42d07ff88d.png">
  <br/>
  *구매한 상품은 카트에서 삭제된다.<br/>
  <img width="300" alt="purchaseResultCart" src="https://user-images.githubusercontent.com/57795722/196352932-321f1a3c-add3-461e-aea0-597111191138.png"><br/>
  *구매한 수량만큼 상품의 재고는 감소한다. 100개->98개 <br/>
  <img width="300" alt="purchaseResultInven" src="https://user-images.githubusercontent.com/57795722/196352948-409afd3d-b1e7-4003-8162-7dff6cfcfcb7.png">
</div>
<hr/>
<div>
  <h6>그 밖에 로그인아이디로 회원가입 유무 검색, 유저의 가주문서 조회, 가주문서 id로 검색, 주문 이력 확인 기능이 있다.</h6>
  <h6>로그인, 회원가입, 회원가입유무검색을 제외한 모든 컨트롤러는 인증이 필요하다.</h6>
  <h6>잘못된 productId, cartId, prepurchaseId, personId등을 입력할 경우 잘못된 입력이라는 메시지가 나온다. </h6>
</div>
