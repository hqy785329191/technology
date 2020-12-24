package com.shop.manage.system.business;

import com.alibaba.fastjson.JSON;
import com.shop.manage.system.support.ServerResponse;
import com.shop.manage.system.util.DateUtil;
import com.shop.manage.system.util.FileUtils;
import com.shop.manage.system.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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


    /**
     * 上传文件
     * @param files
     * @param request
     * @param response
     * @throws IOException
     */
    public void uploadFiles(MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws IOException {

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
                localPath = newfile.getAbsolutePath().substring(0, newfile.getAbsolutePath().lastIndexOf(File.separator));
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
}
