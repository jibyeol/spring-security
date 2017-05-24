<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>register</title>
</head>
<body>

	<form method="post">
		아이디 : <input type="text" name="userId"><br>
		비밀번호 : <input type="password" name="password"><br>
		권한 : <input type="text" name="role" value="ROLE_USER"><br>
		닉네임 : <input type="text" name="nick" ><br>
		<input type="submit" value="확인">
		<sec:csrfInput />
	</form>

</body>
</html>