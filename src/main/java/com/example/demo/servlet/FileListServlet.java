package com.example.demo.servlet;

import com.example.demo.utils.FtpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//获取文件列表
@WebServlet(name = "filelist", urlPatterns = "/filelist/*")
public class FileListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        List<String> fileList = FtpUtils.getFileList("/test");
        for(String s : fileList ){
            System.out.println(s);
        }


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status","200");
            jsonObject.put("filename",fileList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PrintWriter out = resp.getWriter();
        out.write(jsonObject.toString());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
