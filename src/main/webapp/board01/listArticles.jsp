<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.cls1{text-decoration: none;}
.cls2{text-align: center;
	  font-size: 30px;}

</style>
</head>
<body>
	<%-- ${articleList } --%>
	<br>

	<form>
		<table align="center" border="2" width="80%">
			<tr height="10" , align="center" bgcolor="lightgreen">
				<td>글번호</td>
				<td>작성자</td>
				<td>제목</td>
				<td>작성일</td>
			</tr>
			<c:choose>
				<c:when test="${empty articleList }">
					<tr height="10">
						<td colspan="4">
							<p align="center">
								<b><span style="font-size: 9pt">등록된 글이 없습니다.</span></b>
							</p>
						</td>
					</tr>
				</c:when>

				<c:otherwise>
					<c:forEach var="article" items="${articleList }"
						varStatus="articleNum">
						<%-- ${article }<br> --%>
						<%-- ${articleNum.count } --%>
						<tr align="center">
							<%-- ${article } --%>
							<td width="5%">${articleNum.count }</td>
							<td width="10%">${article.id }</td>
							<td align="left" width="35%"><span style="padding-right: 30px"></span> 
								<c:choose>
									<c:when test="${article.level>1 }">
										<c:forEach begin="1" end="${article.level }" step="1">
											<span style="padding-left: 20px"></span>
										</c:forEach>
										<span style="font-size: 12px;">[답변]</span>
										<a class="cls1" href="">${article.title }</a>
									</c:when>
									<c:otherwise>
										<a class="cls1" href="">${article.title }</a>
									</c:otherwise>
								</c:choose>
							</td>
							<td width="10%"><fmt:formatDate value="${article.writeDate }"/></td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>
	</form>
	<a class="cls2" href="http://localhost:8080/pro17_1/board/articleForm.do"><b><p align="center">글쓰기</p></b></a>
</body>
</html>