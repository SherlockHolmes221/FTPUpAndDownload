package com.example.demo.servlet;

import com.example.demo.utils.FtpUtils;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet(name = "smartdownload", urlPatterns = "/smartdownload/*")
public class SmartDownloadServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {


		String filename = req.getParameter("filename");
		String filePath = "./tempfile";

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		if(FtpUtils.downloadFile("/test", filename, "./tempfile")){

			SmartUpload su = new SmartUpload();
			su.initialize(getServletConfig(), req, resp);
			su.setContentDisposition(null);
			try {
				su.downloadFile(filePath + "/" +filename);
				System.out.println("download");
			} catch (SmartUploadException e) {
				e.printStackTrace();
			}

		}

		File saveFile = new File(filePath,filename);
		saveFile.delete();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
	}

}
