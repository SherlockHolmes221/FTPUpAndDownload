package demo.servlet;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import demo.utils.FtpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


public class SmartUploadServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(req,response);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String filePath = "./tempfile";
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdir();
		}

		SmartUpload su = new SmartUpload();
		su.initialize(getServletConfig(), req, resp);
		su.setMaxFileSize(1024*1024*10);
		su.setTotalMaxFileSize(1024*1024*100);

		su.setAllowedFilesList("txt,doc,docx,xls,jpg");

		System.out.println("set");

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer=resp.getWriter();

		JSONObject jsonObject = new JSONObject();
		String message = "";

		try {
			su.setDeniedFilesList("rar,jsp,js");
		} catch (SQLException e) {
			System.out.println("error");
			e.printStackTrace();
		}
		try {
			su.upload();
			int count = su.save(filePath);
			for(int i = 0;i<count;i++){
				com.jspsmart.upload.File tempFile = su.getFiles().getFile(0);
				System.out.println("upload");
				System.out.println("---------------------------");
				System.out.println(tempFile.getFieldName());
				System.out.println(tempFile.getFieldName());
				System.out.println(tempFile.getSize());
				System.out.println(tempFile.getFileExt());
				System.out.println(tempFile.getFilePathName());
				System.out.println("---------------------------");

				if(FtpUtils.uploadFile("/test", tempFile.getFilePathName(),
						filePath+"/"+tempFile.getFilePathName())){
					message = "上传成功";
					System.out.println(filePath+"/"+tempFile.getFilePathName());
					File saveFile = new File(filePath,tempFile.getFilePathName());
                    saveFile.delete();
				}else {
					message = "上传失败";
				}
			}
		} catch (SmartUploadException e) {
			e.printStackTrace();
			System.out.println("error");
		}
		try {
			jsonObject.put("status","200");
			jsonObject.put("message",message);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		writer.write(jsonObject.toString());
		writer.close();
	}
}
