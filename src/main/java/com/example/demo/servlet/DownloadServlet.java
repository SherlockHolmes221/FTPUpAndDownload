package com.example.demo.servlet;

import com.example.demo.utils.FtpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "download", urlPatterns = "/download/*")
public class DownloadServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String filename = req.getParameter("filename");

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer=resp.getWriter();
		JSONObject jsonObject = new JSONObject();
		String message = "";
		if(FtpUtils.downloadFile("/test", filename, "./tempfile")){
			message = "下载成功";

			//保存
			String filePath = "./tempfile";
			File saveFile = new File(filePath,filename);
			saveFile.delete();


		}else {
			message = "下载失败";
		}

		try {
			jsonObject.put("status","200");
			jsonObject.put("message",message);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		writer.write(jsonObject.toString());
		writer.close();

//		File file = new File(path + filename);
//		if(file.exists()){
//			resp.setContentType("application/x-msdownload");
//			resp.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
//			InputStream inputStream = new FileInputStream(file);
//			ServletOutputStream ouputStream = resp.getOutputStream();
//			byte b[] = new byte[1024];
//			int n ;
//			while((n = inputStream.read(b)) != -1){
//				ouputStream.write(b,0,n);
//			}
//
//			ouputStream.close();
//			inputStream.close();

		
		
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
	}

}
