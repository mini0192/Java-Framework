<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.tomcat.domain.presentation.dto.BoardFindAllResponse" %>
<html>
<head>
    <title>게시판</title>
    <style>
        /* Reset margin and padding for consistency */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7f6;
            color: #333;
            padding: 40px;
        }

        h1 {
            font-size: 32px;
            font-weight: bold;
            color: #2c3e50;
            text-align: center;
            margin-bottom: 30px;
        }

        .table-container {
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
            padding: 20px;
            overflow: hidden;
            width: 100%;
            max-width: 800px;
            margin: 0 auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 15px;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #5c6bc0;
            color: white;
            font-weight: bold;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tr:hover {
            background-color: #ecf0f1;
            cursor: pointer;
        }

        .no-posts {
            text-align: center;
            color: #7f8c8d;
            font-size: 16px;
            padding: 20px 0;
        }

        .add-post-btn {
            display: block;
            width: 200px;
            padding: 10px;
            margin: 30px auto 0;
            background-color: #5c6bc0;
            color: white;
            border: none;
            border-radius: 4px;
            text-align: center;
            font-size: 18px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .add-post-btn:hover {
            background-color: #3f4a87;
        }
    </style>
</head>
<body>

<h1>게시판</h1>

<div class="table-container">
    <table>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
        </tr>
        <%
            List<BoardFindAllResponse> posts = (List<BoardFindAllResponse>) request.getAttribute("posts");
            if (posts != null && !posts.isEmpty()) {
                int idx = 1;
                for (BoardFindAllResponse post : posts) {
        %>
        <tr>
            <td><%= idx++ %></td>
            <td><a href="/app/board/<%= post.getId() %>"><%= post.getTitle() %></a></td>
            <td><%= post.getWriter() %></td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="3" class="no-posts">게시글이 없습니다.</td>
        </tr>
        <%
            }
        %>
    </table>
</div>

<a href="/app/board/write" class="add-post-btn">새 게시글 작성</a>

</body>
</html>
