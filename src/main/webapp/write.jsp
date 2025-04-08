<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <title>게시글 작성</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      padding: 20px;
    }
    form {
      max-width: 400px;
    }
    label {
      display: block;
      margin-top: 10px;
      font-weight: bold;
    }
    input[type="text"] {
      width: 100%;
      padding: 8px;
      margin-top: 5px;
      box-sizing: border-box;
    }
    button {
      margin-top: 15px;
      padding: 10px 20px;
    }
  </style>
</head>
<body>

<h1>📝 게시글 작성</h1>

<form action="/app" method="post">
  <label for="title">제목</label>
  <input type="text" id="title" name="title" placeholder="제목을 입력하세요" />

  <label for="content">내용</label>
  <input type="text" id="content" name="content" placeholder="내용을 입력하세요" />

  <button type="submit">전송</button>
</form>

</body>
</html>
