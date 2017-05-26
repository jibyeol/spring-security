<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Spring Security Example</title>
    </head>
    <body>
        <h1>Welcome! </h1>
        
		<sec:authorize access="isAuthenticated()">
			�α��� ���Դϴ�.<br>
			<sec:authentication property="principal.username"/> <br>
			<sec:authentication property="principal.nick"/> <br>
			<form action="/logout" method="post">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<input type="submit" value="�α׾ƿ�">
			</form><br><br>
		</sec:authorize>
		
		<sec:authorize access="isAnonymous()">
			<a href="/login">�α���</a>
        	<a href="/register/form">ȸ������</a>
		</sec:authorize>
		
		<sec:authorize access="hasRole('ADMIN')">
			ADMIN ������ �ֽ��ϴ�.<br>
		</sec:authorize>
		<sec:authorize access="hasRole('USER')">
			USER ������ �ֽ��ϴ�.<br>
		</sec:authorize>

    </body>
</html>