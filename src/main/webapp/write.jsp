<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <title>게시글 작성</title>
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: #e2e2e2;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      margin: 0;
    }

    .form-container {
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

    label {
      font-size: 16px;
      color: #2c3e50;
      margin-bottom: 8px;
      display: block;
      font-weight: 600;
    }

    input[type="text"], textarea {
      width: 100%;
      padding: 12px;
      margin-top: 10px;
      border-radius: 6px;
      border: 1px solid #dcdfe1;
      background-color: #f8f9fa;
      font-size: 16px;
      color: #495057;
      transition: all 0.3s ease;
    }

    input[type="text"]:focus, textarea:focus {
      border-color: #5c6bc0;
      background-color: #ffffff;
      outline: none;
    }

    textarea {
      resize: none;
      min-height: 120px;
      padding: 12px 15px;
    }

    /* 추가된 간격 */
    #title {
      margin-bottom: 20px; /* 제목과 작성자 사이 간격 */
    }

    #writer {
      margin-bottom: 20px; /* 작성자와 내용 사이 간격 */
    }

    button {
      background-color: #5c6bc0;
      color: #ffffff;
      padding: 14px;
      margin-top: 20px;
      border: none;
      border-radius: 6px;
      font-size: 16px;
      width: 100%;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    button:hover {
      background-color: #3f4a87;
    }

    .form-container p {
      text-align: center;
      font-size: 14px;
      color: #7f8c8d;
      margin-top: 15px;
    }

    .form-container p a {
      color: #5c6bc0;
      text-decoration: none;
    }

    .form-container p a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>

<div class="form-container">
  <h1>게시글 작성</h1>

  <form action="/app/board" method="post">
    <label for="title">제목</label>
    <input type="text" id="title" name="title" placeholder="제목을 입력하세요" required />

    <label for="writer">작성자</label>
    <input type="text" id="writer" name="writer" placeholder="작성자를 입력하세요" required />

    <label for="content">내용</label>
    <textarea id="content" name="content" placeholder="내용을 입력하세요" required></textarea>

    <button type="submit">제출</button>
  </form>

  <p>게시글을 작성한 후, <strong>제출</strong> 버튼을 클릭하세요.</p>
</div>

</body>
</html>
