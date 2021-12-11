# 영화 추천 웹 서비스

## ER-Diagram
![ERDD](https://user-images.githubusercontent.com/74301428/145659747-04b20c6c-d9b2-4d76-a7b7-e0bae03f47f7.jpg)

* Member_vector : 사용자들이 평가한 영화들의 감독, 배우, 장르, 시놉시스에 들어있는 명사, 형용사 등 단어의 빈도수를 저장한다.
* Dictionary : 1번에 필요한 모든 단어들을 저장한다. 영화 데이터를 업데이트 할 때 이 테이블에 없는 단어는 자동으로 추가된다.
* Evaluation : 사용자가 평가한 영화와 별점을 저장한다.

## 기능
### 회원 관리
  * 로그인  
![login](https://user-images.githubusercontent.com/74301428/145662022-14b8a88d-fd55-409e-9c75-7e579d26604d.JPG)  
  * 회원가입  
![join](https://user-images.githubusercontent.com/74301428/145662024-da24dafa-2e20-42dc-83b6-608541921849.JPG)  
  * 이메일 인증번호
  * 중복 아이디 / 닉네임 확인
### 영화 관련
  * 검색 : 검색 내용을 Movie 테이블에서 찾고 그 정보를 받아온다.
  * 평가 : Movie 테이블에서 영화를 무작위로 받아와 사용자에게 평가할 수 있게 한다.
  * 댓글

## 영화 추천 방법  
![select](https://user-images.githubusercontent.com/74301428/145662026-f3111c14-1b00-45ff-89a0-d11468776d3e.JPG)  
 1. 영화들의 제목, 배우, 장르, 줄거리 등을 명사로 나눠 DB에 저장
 2. 유저가 평점을 준 영화의 단어들을 벡터로 취급함
 3. 다른 유저들과의 Cosine 유사도를 구해 유사도가 가장 높은 유저의 영화 목록 중 본인이 안본 영화로 추천  
![similarity](https://user-images.githubusercontent.com/74301428/145661273-526b694f-cdd1-4bdb-be0d-117d7a396c53.jpg)  

## 영화 데이터 업데이트
  네이버 영화 인기순위에서 매일 00시에 크롤링해온다.
  
## 사용 기술
 * 발표 당시 서버는 EC2 프리티어에 DB는 RDS에 올려서 서비스
 * Back-End : Spring Boot, JPA
 * Front-End : ThymeLeft, JavaScript

## To do
 * [ ] 영화 추천 알고리즘의 개선 (사용자가 없을 때 어떻게 추천할지, 유사도가 가장 높은 사용자의 영화만 계속 추천되는 현상 등)
 * [ ] 별점 줄 때 영화 리스트가 랜덤으로 나오는데, 쿼리마다 테이블을 다시 정렬해 영화가 중복해서 등장하는 오류 수정
