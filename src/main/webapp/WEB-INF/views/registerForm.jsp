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
		���̵� : <input type="text" name="userId"><br>
		��й�ȣ : <input type="password" name="password"><br>
		���� : <input type="text" name="role" value="ROLE_USER"><br>
		�г��� : <input type="text" name="nick" ><br>
		<input type="submit" value="Ȯ��">
		<sec:csrfInput />
	</form>

</body>
</html>