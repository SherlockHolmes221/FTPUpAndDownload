#FTP上传下载接口文档

- baseurl: http://203.195.139.136/loadfile/

 
 - 查看所有文件列表
 
简述 |uri操作
---|---
查看所有文件列表 | GET /filelist
下载文件 | GET /smartdownload?{filename}
上传文件 | POST /smartupload


- 请求示例1：
```
http://203.195.139.136/loadfile/filelist
```
返回：
```
{"filename":"[1.jpg, 1.txt, 122.jpg, 1222.jpg, 123.txt, 2.jpg, test.txt, 中文.docx, 二面记录.docx, 柯南.jpg]","status":"200"}
```

- 请求示例2：
```
http://203.195.139.136/loadfile/smartdownload?filename=1.jpg
```
返回：下载文件1.jpg

- 请求示例3：
```
http://203.195.139.136/loadfile/filelist
```
返回：
```
{"message":"上传成功","status":"200"}
```
测试html文件：
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="http://203.195.139.136/loadfile/smartupload" method="post" enctype="multipart/form-data">
    请选择文件：<input id="myfile" name="myfile" type="file"/>
    <input type="submit" value="提交"/>
</form>
<a href="http://203.195.139.136/loadfile/smartdownload?filename=1.jpg">从服务器下载一张图片</a>
<br>
<a href="http://203.195.139.136/loadfile/filelist">获取文件列表</a>
</body>
</html>
```

