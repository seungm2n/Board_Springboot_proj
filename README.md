## 개발환경

- Springboot 2.7.12
- Spring mvc
- Thymeleaf 템플릿 엔진
- Spring JDBC
- MariaDB - SQL

## 아키텍처
```
                     Spring Core
                     Spring MVC                Spring JDBC
브라우저 --- 요청 ---> Controller ---> Service ---> DAO ---> DB
       <--- 응답  ---  템플릿       <---      <---     <---
                <-------------- layer간에 데이터 전송은 DTO --->
```

## 게시판 만들기

1. 회원가입 - /userRegform
2. 로그인(HttpSession) - /loginform
3. 글쓰기 - /writeform, /write
4. 글 목록보기 (pagination) - /
5. 글 상세보기
6. 글 수정하기
7. 글 삭제하기
8. 로그아웃
9. 관리자 - 어떤 사용자의 글이든 모두 삭제 가능

## 게시판 만드는 순서

1. Controller와 템플릿
2. Service - 비지니스 로직을 처리 ( 하나의 트랙잭션 단위)
3. Service는 비지니스로직을 처리하기위해 데이터를 CRUD 하기위해 DAO를 사용