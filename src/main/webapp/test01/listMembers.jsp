<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%-- ${memberLists } --%>
</head>
<body>

<h1 align="center">회원 목록 페이지</h1><br>
	<table align="center" border="1">
		<tr align="center" bgcolor="green">
			<td width="7%">아이디</td>
			<td width="7%">비밀번호</td>
			<td width="7%">이름</td>
			<td width="7%">이메일</td>
			<td width="7%">가입일</td>
			<td width="7%">수정</td>
			<td width="7%">삭제</td>
		</tr>


		<c:forEach var="member" items="${memberLists }">
			<tr align="center">
				<td>${member.id }</td>
				<td>${member.pwd }</td>
				<td>${member.name }</td>
				<td>${member.email }</td>
				<td>${member.joinDate }</td>
				<td><a href="http://localhost:8080/pro17_1/mem/modMemberForm.do?id=${member.id}">수정</a></td>
				<td><a href="http://localhost:8080/pro17_1/mem/delMember.do?id=${member.id}">삭제</a></td>
			</tr>
		</c:forEach>
	</table>

	<a href="http://localhost:8080/pro17_1/test01/memberForm.jsp"><p align="center">회원추가</p></a>

</body>
</html>