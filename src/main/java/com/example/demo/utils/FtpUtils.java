package com.example.demo.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class FtpUtils {
    //ftp服务器地址
    public static String hostname = "120.78.195.149";
    //ftp服务器端口号默认为21
    public static Integer port = 21 ;
    //ftp登录账号
    public static String username = "test";
    //ftp登录密码
    public static String password = "123456";

    public static FTPClient ftpClient = null;

    /**
     * 初始化ftp服务器
     */
    private static void initFtpClient() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            System.out.println("connecting...ftp服务器:"+hostname+":"+port);
            ftpClient.connect(hostname, port); //连接ftp服务器
            ftpClient.login(username, password); //登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            if(!FTPReply.isPositiveCompletion(replyCode)){
                System.out.println("connect failed...ftp服务器:"+hostname+":"+port);
            }
            System.out.println("connect successfu...ftp服务器:"+hostname+":"+port);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param pathname ftp服务保存地址
     * @param fileName 上传到ftp的文件名
     * @param originfilename 待上传文件的名称（绝对地址） *
     * @return
     */
    public static boolean uploadFile(String pathname, String fileName,String originfilename){
        boolean flag = false;
        InputStream inputStream = null;
        try{
            System.out.println("开始上传文件");
            inputStream = new FileInputStream(new File(originfilename));
            initFtpClient();
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            CreateDirecroty(pathname);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            ftpClient.logout();
            flag = true;
            System.out.println("上传文件成功");
        }catch (Exception e) {
            System.out.println("上传文件失败");
            e.printStackTrace();
        }finally{
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    /**
     * 上传文件
     * @param pathname ftp服务保存地址
     * @param fileName 上传到ftp的文件名
     * @param inputStream 输入文件流
     * @return
     */
    public static boolean uploadFile(String pathname, String fileName,InputStream inputStream){
        boolean flag = false;

        // 设置PassiveMode传输
        ftpClient.enterLocalPassiveMode();
        // 设置以二进制流的方式传输
        ftpClient.setControlEncoding("GBK");

        try{
            System.out.println("开始上传文件");
            initFtpClient();
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            CreateDirecroty(pathname);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            ftpClient.logout();
            flag = true;
            System.out.println("上传文件成功");
        }catch (Exception e) {
            flag = false;
            System.out.println("上传文件失败");
            e.printStackTrace();
        }finally{
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    //改变目录路径
    public static boolean changeWorkingDirectory(String directory) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                System.out.println("进入文件夹" + directory + " 成功！");

            } else {
                System.out.println("进入文件夹" + directory + " 失败！开始创建文件夹");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }

    //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
    public static boolean CreateDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(path)) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        System.out.println("创建目录[" + subDirectory + "]失败");
                        changeWorkingDirectory(subDirectory);
                    }
                } else {
                    changeWorkingDirectory(subDirectory);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    //判断ftp服务器文件是否存在
    public static boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    //创建目录
    public static boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                System.out.println("创建文件夹" + dir + " 成功！");

            } else {
                System.out.println("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /** * 下载文件 *
     * @param pathname FTP服务器文件目录 *
     * @param filename 文件名称 *
     * @param localpath 下载后的文件路径 *
     * @return */
    public  static boolean downloadFile(String pathname, String filename, String localpath){
        boolean flag = false;
        OutputStream os=null;
//
//        // 设置PassiveMode传输
//        ftpClient.enterLocalActiveMode();
//        // 设置以二进制流的方式传输
//        ftpClient.setControlEncoding("GBK");

        try {
            System.out.println("开始下载文件");
            initFtpClient();
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            System.out.println(filename);
            System.out.println(ftpFiles.length);

            for(FTPFile file : ftpFiles){
                System.out.println(file.getName());
                if(filename.equalsIgnoreCase(file.getName())){
                    File localFile = new File(localpath + "/" + file.getName());
                    os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);
                    os.close();
                    break;
                }
            }
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(null != os){
                try {
                    System.out.println("下载文件成功");
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("下载文件失败");
                flag = false;
            }
        }
        return flag;
    }

    /** * 删除文件 *
     * @param pathname FTP服务器保存目录 *
     * @param filename 要删除的文件名称 *
     * @return */
    public static boolean deleteFile(String pathname, String filename){
        boolean flag = false;
        try {
            System.out.println("开始删除文件");
            initFtpClient();
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.dele(filename);
            ftpClient.logout();
            flag = true;
            System.out.println("删除文件成功");
        } catch (Exception e) {
            System.out.println("删除文件失败");
            e.printStackTrace();
        } finally {
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }


    /** * 获取文件列表 *
     * @param pathname FTP服务器文件目录 *
     * @return */
    public  static List<String> getFileList(String pathname){
        List<String> fileList = new ArrayList<>();
        OutputStream os=null;
        try {
            System.out.println("开始下载文件");
            initFtpClient();
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for(FTPFile file : ftpFiles){
                fileList.add(file.getName());
            }
            ftpClient.logout();;
            System.out.println("获取文件列表成功");
        } catch (Exception e) {
            System.out.println(" 获取文件列表失败");
            e.printStackTrace();
        } finally{
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileList;
    }

    public static void main(String[] args) {
        FtpUtils ftp =new FtpUtils();
        ftp.uploadFile("/test", "123.txt", "/Users/quxian/Desktop/test.txt");
        ftp.uploadFile("/test", "1234.txt", "/Users/quxian/Desktop/test.txt");
        //ftp.downloadFile("/test", "456.txt", "/Users/quxian/Desktop/");
        //ftp.deleteFile("/test", "123.txt");
        System.out.println("ok");
    }
}
