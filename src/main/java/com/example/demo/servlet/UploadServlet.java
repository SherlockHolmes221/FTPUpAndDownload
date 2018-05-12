package com.example.demo.servlet;

import com.example.demo.utils.FtpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "upload", urlPatterns = "/upload/*")
public class UploadServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(req,response);

//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        PrintWriter writer=response.getWriter();
//        writer.write("123");
//        writer.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		//req.setContentType("application/json");
		req.setCharacterEncoding("UTF-8");

		InputStream fileSource = req.getInputStream();

 		//这个名字时是服务器的地址？？？
		String tempFileName = "./tempfile"; //gai

		//读取文件，保存到一个暂时的文件中
		File tempFile = new File(tempFileName);

		//从输入流写入数据到临时文件中
		FileOutputStream outputStream = new FileOutputStream(tempFile);
		byte b[] = new byte[1024];
		int n;
		while(( n = fileSource.read(b)) != -1){
			outputStream.write(b, 0, n);
		}

		outputStream.close();
		fileSource.close();
		System.out.println("tempfile get");


		//获取文件名称
		RandomAccessFile randomFile = new RandomAccessFile(tempFile,"r");
		randomFile.readLine();
		String str = randomFile.readLine();
		int beginIndex = str.lastIndexOf("=") + 2;
		int endIndex = str.lastIndexOf("\"");
		String filename = str.substring(beginIndex, endIndex);

		System.out.println("filename:" + filename);

		//获取开始和结束的位置，定位到文件头
		randomFile.seek(0);
		long startPosition = 0;
		int i = 1;

		//获取文件内容的开始位置
		while(( n = randomFile.readByte()) != -1 && i <=4){
			if(n == '\n'){
				startPosition = randomFile.getFilePointer();
				i ++;
			}
		}

		startPosition = startPosition -1;


		//获取结束位置
		randomFile.seek(randomFile.length());
		long endPosition = randomFile.getFilePointer();
		int j = 1;
		while(endPosition >=0 && j<=2){
			endPosition--;
			randomFile.seek(endPosition);
			if(randomFile.readByte() == '\n'){
				j++;
			}
		}

		endPosition = endPosition -1;

		System.out.println("get position");

		//设置系统保存文件路径-->randomAccessFile
		String realPath = "./tempfile";//gai
		//String realPath = getServletContext().getRealPath("/") + "files"; //gai

		File fileupload = new File(realPath);
		if(!fileupload.exists()){
			fileupload.mkdir();
		}
		File saveFile = new File(realPath,filename);
		RandomAccessFile randomAccessFile = new RandomAccessFile(saveFile,"rw");

		//从临时文件中读取内容
		randomFile.seek(startPosition);
		while(startPosition < endPosition){
			randomAccessFile.write(randomFile.readByte());
			startPosition = randomFile.getFilePointer();
		}

		randomAccessFile.close();
		randomFile.close();
		tempFile.delete();

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer=resp.getWriter();

		System.out.println(saveFile.getAbsolutePath());
		JSONObject jsonObject = new JSONObject();
		String message = "";

		if(FtpUtils.uploadFile("/test", filename, saveFile.getAbsolutePath())){ //gai
			message = "上传成功";

		}else {
			message = "上传失败";
		}

		try {
			jsonObject.put("status","200");
			jsonObject.put("message",message);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		writer.write(jsonObject.toString());
		//saveFile.delete();
		writer.close();
	}
}
