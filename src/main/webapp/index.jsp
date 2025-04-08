<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.tomcat.domain.presentation.dto.BoardFindAllResponse" %>
<html>
<head>
    <title>📋 게시판</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
        }
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ccc;
        }
        th {
            background-color: #f0f0f0;
        }
    </style>
</head>
<body>
<h1>📋 게시판</h1>

<table>
    <tr>
        <th>번호</th>
        <th>제목</th>
        <th>작성자</th>
        <th>작성일</th>
    </tr>
    <%
        List<BoardFindAllResponse> posts = (List<BoardFindAllResponse>) request.getAttribute("posts");
        if (posts != null && !posts.isEmpty()) {
            int idx = 1;
            for (BoardFindAllResponse post : posts) {
    %>
    <tr>
        <td><%= idx++ %></td>
        <td><%= post.getTitle() %></td>
        <td>작성자 없음</td>
        <td>작성일 없음</td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="4">게시글이 없습니다.</td>
    </tr>
    <%
        }
    %>
</table>

</body>
</html>
