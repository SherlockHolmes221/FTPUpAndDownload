<%--<%@ page contentType="text/html;charset=UTF-8"%>--%>
<%--<html>--%>
  <%--<head>--%>
    <%--<title>$Title$</title>--%>
  <%--</head>--%>

  <%--<body>--%>
  <%--<form action="http://120.78.195.149/loadfile/smartupload" method="post" enctype="multipart/form-data">--%>
      <%--请选择文件：<input id="myfile" name="myfile" type="file"/>--%>
      <%--<input type="submit" value="提交"/>--%>
  <%--</form>--%>
  <%--<a href="http://120.78.195.149/loadfile/smartdownload?filename=中文.docx">下载中文.docx</a>--%>
  <%--<br>--%>
  <%--<a href="http://120.78.195.149/loadfile/filelist">获取文件列表</a>--%>
  <%--</body>--%>
<%--</html>--%>



<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
  <title>$Title$</title>
</head>

<body>
<form action="http://localhost:8080/Users/quxian/Library/ApacheTomcat/webapps/smartupload" method="post" enctype="multipart/form-data">
  请选择文件：<input id="myfile" name="myfile" type="file"/>
  <input type="submit" value="提交"/>
</form>
<a href="http://localhost:8080/Users/quxian/Library/ApacheTomcat/webapps/smartdownload?filename=中文.docx">下载中文.docx</a>
<br>
<a href="http://localhost:8080/Users/quxian/Library/ApacheTomcat/webapps/filelist">获取文件列表</a>
</body>
</html>
