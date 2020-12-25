package com.shop.manage.system.business;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.shop.manage.system.support.ServerResponse;
import com.shop.manage.system.util.DateUtil;
import com.shop.manage.system.util.FileUtils;
import com.shop.manage.system.util.FtpUtil;
import org.apache.tomcat.util.http.fileupload.ProgressListener;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 业务聚合类
 * @author Mr.joey
 */
@Component
public class UploadOrDownloadBusiness {

    private static final Logger log = LoggerFactory.getLogger(UploadOrDownloadBusiness.class);

    /**
     * ftp 配置信息
     */
    @Value("${UploadOrDownload.ftp-ip}")
    private String ftpIp;

    @Value("${UploadOrDownload.ftp-port}")
    private Integer ftpPort;

    @Value("${UploadOrDownload.ftp-userName}")
    private String ftpUserName;

    @Value("${UploadOrDownload.ftp-pwd}")
    private String ftpPwd;

    @Value("${UploadOrDownload.ftp-path}")
    private String ftpPath;

    @Value("${UploadOrDownload.resource-fileName}")
    private String resourceFileName;


    /**
     * linux 使用xftp 上传文件
     * @param files
     * @param request
     * @param response
     * @throws IOException
     */
    public void sftpUploadFiles(MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String imgUrl = "";
        //用于响应结果
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> mateMap = new HashMap<>();
        List<Map<String, Object>> dataUrlList =new ArrayList();

        response.setCharacterEncoding("utf-8");

        if (files == null || files.length<=0) {
            log.info("未选择文件！");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(ServerResponse.createByFailure("未选择文件！", ServerResponse.UNKNOWN_ERROR_CODE)));
            out.flush();
            out.close();
            return;
        }


        for(int i =0 ;i<files.length ; i++){
            MultipartFile file = files[i];

            String filepath = file.getOriginalFilename();
            String ext = filepath.substring(filepath.lastIndexOf("."));

            if (!".png".equals(ext) && !".PNG".equals(ext) && !".jpg".equals(ext) && !".JPG".equals(ext) && !".JPEG".equals(ext) && !".jpeg".equals(ext)) {
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(ServerResponse.createByFailure("文件类型不正确！只能上传图片。", ServerResponse.UNKNOWN_ERROR_CODE)));
                out.flush();
                out.close();
                return;
            }

            String fileExt = filepath.substring(filepath.lastIndexOf(".") + 1).toLowerCase();
            String newFileName = UUID.randomUUID().toString() + "." + fileExt;
            String localPath = "";
            // 将MultipartFile转换为File后存在本地
            File newfile = null;
            try {
                InputStream inputStream = file.getInputStream();
                newfile = new File(newFileName);
                FileUtils.inputStreamToFile(inputStream, newfile);
                localPath = newfile.getAbsolutePath().substring(0, newfile.getAbsolutePath().lastIndexOf("/"));
            } catch (IOException e) {
                log.error("文件转换异常。异常={}",e);
            }

            Date nowDate = new Date();
            String datePath = DateUtil.dateToString(nowDate, "yyyy/MM/dd");
            FtpUtil ftpUtil = FtpUtil.getFtpUtil(ftpIp,ftpPort, ftpUserName, ftpPwd);
            ftpUtil.initConntectFTP();
            boolean uploadFlag = ftpUtil.uploadFile(localPath, ftpPath, newFileName);
            log.info("上传文件结果：{}", uploadFlag);
            ftpUtil.closeConnectFTP();
            // 无论上传成功失败都删除本地文件
            FtpUtil.deleteFile(newfile);
            if (uploadFlag) {
                imgUrl = ftpPath + datePath + "/" + newFileName;

                //组装响应结果
            /*
            {
                "meta":{
                    "success":true,
                    "message":"操作成功！",
                    "statusCode":200
                },
                "data":[
                    {
                        "applyImageUrl":"http://www.baidu.com"
                    }
                ]
            }
            */
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("applyImageUrl",imgUrl);
                dataUrlList.add(dataMap);
            }

        }

        mateMap.put("success",true);
        mateMap.put("message","操作成功!");
        mateMap.put("statusCode",200);
        resultMap.put("meta",mateMap);
        resultMap.put("data",dataUrlList);

        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(resultMap));
        out.flush();
        out.close();
    }



    /**
     * 本服务器上传文件
     * @param files
     * @param request
     * @param response
     * @throws IOException
     */
    public void localServerUploadFiles(MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws IOException {

        //上传到服务器的某个目录下
        String filePath = "D:/test/";

        Date nowDate = new Date();
        String datePath = DateUtil.dateToString(nowDate, "yyyy/MM/dd");

        filePath = filePath + datePath + "/";

        String imgUrl = "";
        //用于响应结果
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> mateMap = new HashMap<>();
        List<Map<String, Object>> dataUrlList =new ArrayList();

        response.setCharacterEncoding("utf-8");

        if (files == null || files.length<=0) {
            log.info("未选择文件！");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(ServerResponse.createByFailure("未选择文件！", ServerResponse.UNKNOWN_ERROR_CODE)));
            out.flush();
            out.close();
            return;
        }

        //设置临时文件夹，当上传的内容大小，超过限制条件时，将会生成字节文件放到这个目录下，继续读取
        String tempPath = "D:/tempPath";
        File tempFile = new File(tempPath);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }

        //创建file items工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置缓冲区大小
        factory.setSizeThreshold(1024 * 100);
        //设置临时文件路径
        factory.setRepository(tempFile);
        //创建文件上传处理器
        ServletFileUpload upload = new ServletFileUpload(factory);

        //监听文件上传进度、解决乱码问题
        watchUploadProcess(upload);
        //默认、单个文件为 10 M
        upload.setFileSizeMax(1024 * 1024 * 10);
        //如果不配置，默认情况下，目前设置为100M
        upload.setSizeMax(1024 * 1024  * 100);

        for(int i =0 ;i<files.length ; i++){
            MultipartFile file = files[i];

            String filepath = file.getOriginalFilename();
            String ext = filepath.substring(filepath.lastIndexOf("."));

            if (!".png".equals(ext) && !".PNG".equals(ext) && !".jpg".equals(ext) && !".JPG".equals(ext) && !".JPEG".equals(ext) && !".jpeg".equals(ext)) {
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(ServerResponse.createByFailure("文件类型不正确！只能上传图片。", ServerResponse.UNKNOWN_ERROR_CODE)));
                out.flush();
                out.close();
                return;
            }

            String fileExt = filepath.substring(filepath.lastIndexOf(".") + 1).toLowerCase();
            String newFileName = UUID.randomUUID().toString() + "." + fileExt;

            // 将MultipartFile转换为File后存在本地
            File newfile = null;
            try {
                InputStream inputStream = file.getInputStream();
                newfile = new File(newFileName);
                FileUtils.inputStreamToFile(inputStream, newfile);

                // 解决中文问题，图片显示问题
                File dest = new File(filePath + newFileName);
                // 检测是否存在目录，如果不存在，则创建
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                //执行转存

                file.transferTo(dest);

            } catch (IOException e) {
                log.error("文件转换异常。异常={}",e);
            }

            log.info("上传文件结果：{}", filePath + newFileName);

            // 无论上传成功失败都删除本地文件
            FtpUtil.deleteFile(newfile);
            imgUrl = filePath + newFileName;

                //组装响应结果
            /*
            {
                "meta":{
                    "success":true,
                    "message":"操作成功！",
                    "statusCode":200
                },
                "data":[
                    {
                        "applyImageUrl":"http://www.baidu.com"
                    }
                ]
            }
            */
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("imageUrl",imgUrl);
                dataUrlList.add(dataMap);
            }


        mateMap.put("success",true);
        mateMap.put("message","操作成功!");
        mateMap.put("statusCode",200);
        resultMap.put("meta",mateMap);
        resultMap.put("data",dataUrlList);

        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(resultMap));
        out.flush();
        out.close();
    }


    /**
     * 采用sftp 连接 linux，下载文件，压缩成 zip
     * @param request
     * @param response
     */
    public void sftpDownloadFilesForZip(HttpServletRequest request, HttpServletResponse response) throws Exception {


        //模拟直接获取本地文件
        String filePath1 = "D:/";
        String fileName1 = "aa.jpg";

        String filePath2 = "D:/";
        String fileName2 = "bb.jpg";

        List<String> downLoadUrlList = new ArrayList<>();
        downLoadUrlList.add(filePath1);
        downLoadUrlList.add(filePath2);

        List<String> downLoadFileNameList = new ArrayList<>();
        downLoadFileNameList.add(fileName1);
        downLoadFileNameList.add(fileName2);



        request.setCharacterEncoding("utf-8");

        //连接 linux 服务器
        FtpUtil ftpUtil = FtpUtil.getFtpUtil(ftpIp,ftpPort, ftpUserName, ftpPwd);
        ftpUtil.initConntectFTP();

        response.setContentType("APPLICATION/OCTET-STREAM;charset=utf-8");
        //使用端口id，生成zip
        response.setHeader("Content-Disposition","attachment; filename="+this.getZipFilename()+".zip");
        System.out.println("in BatchDownload................");
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

        //压缩包中，不能有重复的文件，所以这里需要过滤
        List<String> checkRepeat = new ArrayList<String>();
        List<File> fileList = new ArrayList<File>();
        if(!CollectionUtils.isEmpty(downLoadFileNameList)){
            for(int i=0;i<downLoadFileNameList.size();i++){

                //文件名重复，将不进行重复打包
                if(!checkRepeat.contains(downLoadFileNameList.get(i))){

                    checkRepeat.add(downLoadFileNameList.get(i));

                    //尝试创建文件对象。确定文件是否存在
                    try {
                        File file = ftpUtil.getFile(downLoadUrlList.get(i),downLoadFileNameList.get(i));
                        if(file!=null){
                            fileList.add(file);
                        }
                    }catch (Exception e){
                        log.info("当前文件的路径，有服务器无法找到，请确认！路径 =={},异常={}",downLoadUrlList.get(i)+"/"+downLoadFileNameList.get(i),e);
                    }
                }
            }

        }
        //根据路径，进行切割，获取文件在服务器上的相对路径
        File[] files = null;
        if(!CollectionUtils.isEmpty(fileList)){

            System.out.println("fileNameList====="+ JSON.toJSONString(fileList));

            files = new File[fileList.size()];
            for(int i=0;i<fileList.size();i++){
                files[i]=fileList.get(i);
            }
        }
        if(files!=null){
            zipFile(files, "", zos);
        }
        zos.flush();
        zos.close();

        //关闭sftp 连接
        ftpUtil.closeConnectFTP();
    }


    /**
     * 采用sftp 连接 linux 下载文件
     * @param request
     * @param response
     */
    public void sftpDownloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {


        //模拟直接获取本地文件
        String filePath1 = "D:/";
        String fileName1 = "aa.jpg";

        request.setCharacterEncoding("utf-8");

        //连接 linux 服务器
        FtpUtil ftpUtil = FtpUtil.getFtpUtil(ftpIp,ftpPort, ftpUserName, ftpPwd);
        ftpUtil.initConntectFTP();

        response.setContentType("APPLICATION/OCTET-STREAM;charset=utf-8");
        //使用端口id，生成zip
        response.setHeader("Content-Disposition","attachment; filename="+filePath1+"/"+fileName1);
        System.out.println("in sftpDownloadFile................");

        ftpUtil.getFile(filePath1,fileName1);

        //关闭sftp 连接
        ftpUtil.closeConnectFTP();
    }

    /**
     * 下载项目中 resource 目录下的文件
     * @param request
     * @param response
     */
    public void resourceDirDownloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            InputStream inputStream = UploadOrDownloadBusiness.class.getClassLoader().getResourceAsStream(resourceFileName);
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(resourceFileName, "UTF-8"));
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if(b!=-1) {
                    out.write(buffer, 0, b);
                }
            }
            inputStream.close();
            out.flush();
            out.close();
            log.info("下载resource 目录下的文件流程完成------");
        } catch (IOException e) {
            log.error("下载resource 目录下的文件流程异常，{}",e);
        }
    }

    /**
     * 把文件，打成  zip
     * @param subs
     * @param baseName
     * @param zos
     * @throws IOException
     */
    private void zipFile(File[] subs, String baseName, ZipOutputStream zos) throws IOException {
        if(subs!=null){
            for (int i=0;i<subs.length;i++) {
                FileInputStream fis = null;
                try {
                    File f=subs[i];
                    zos.putNextEntry(new ZipEntry(baseName + f.getName()));
                    fis = new FileInputStream(f);
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    fis.close();
                }catch (Exception e){
                    log.error("文件异常，将继续执行=="+e.getMessage(),e);
                }finally {
                    if(fis!=null){
                        fis.close();
                    }
                }

            }
        }

    }

    /**
     * 根据时间，生成zip 名
     * @return
     */
    private String getZipFilename(){
        Date date=new Date();
        String s=date.getTime()+".zip";
        return s;
    }

    /**
     * 监听文件上传进度
     * @param upload 上传文件监听器
     */
    private void watchUploadProcess(ServletFileUpload upload) {
        //监听文件上传进度
        ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long pBytesRead, long pContentLength, int pItems) {
                log.info("正在读取文件： " + pItems);
                if (pContentLength == -1) {
                    log.info("已读取： " + pBytesRead + " 剩余0");
                } else {
                    log.info("文件总大小：" + pContentLength + " 已读取：" + pBytesRead);
                }
            }
        };
        upload.setProgressListener(progressListener);

        //解决上传文件名的中文乱码
        upload.setHeaderEncoding("UTF-8");
    }
}
