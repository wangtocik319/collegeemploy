<%@ page language="java" contentType="text/html; charset=utf-8"
<<<<<<< HEAD
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Insert title here</title>
</head>
<body>
<c:forEach items="${collegeList}" var="user">
    ${user.id}<br/>
    ${user.name}<br/>
    ${user.sex}<br/>
</c:forEach>
=======
    pageEncoding="utf-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<c:forEach items="${collegeList}" var="user">
		${user.id}<br/>
		${user.name}<br/>
		${user.sex}<br/>
	</c:forEach>
>>>>>>> fbfe18d1c5300530420e3ecf43f68621616c954e
</body>
</html>