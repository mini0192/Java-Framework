<%@ page import="com.tomcat.domain.presentation.dto.BoardFindByIdResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시글 확인</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7f6;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            background-color: #ffffff;
            border-radius: 12px;
            padding: 40px;
            width: 100%;
            max-width: 600px;
            box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.1);
        }

        h1 {
            font-size: 28px;
            font-weight: bold;
            color: #2c3e50;
            margin-bottom: 30px;
            text-align: center;
        }

        .post-detail {
            font-size: 18px;
            color: #333;
            margin-bottom: 20px;
        }

        .post-detail span {
            font-weight: bold;
            color: #2c3e50;
        }

        .button-container {
            text-align: center;
            margin-top: 20px;
        }

        .button-container a {
            text-decoration: none;
            background-color: #5c6bc0;
            color: white;
            padding: 12px 24px;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .button-container a:hover {
            background-color: #3f4a87;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>게시글 확인</h1>

    <%
        BoardFindByIdResponse post = (BoardFindByIdResponse) request.getAttribute("post");
    %>
    <div class="post-detail">
        <p><span>제목:</span> ${post.getTitle()}</p>
        <p><span>작성자:</span> ${post.getWriter()}</p>
        <p><span>내용:</span> ${post.getContent()}</p>
    </div>

    <div class="button-container">
        <a href="/app/board">목록으로 돌아가기</a>
    </div>
</div>

</body>
</html>
