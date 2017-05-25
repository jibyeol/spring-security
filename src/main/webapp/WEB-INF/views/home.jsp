<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Spring Security Example</title>
    </head>
    <body>
        <h1>Welcome! </h1>
<sec:authentication property="principal.username"/> <br>
        <p>Click <a href="/main">here</a> to go main page.</p>
        <a href="/register/form">회원가입</a>
    </body>
</html>