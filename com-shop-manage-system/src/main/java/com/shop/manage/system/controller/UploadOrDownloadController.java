package com.shop.manage.system.controller;

import com.shop.manage.system.business.UploadOrDownloadBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 上传、下载相关api
 * @author Mr.joey
 */
@Api(tags = "UploadOrDownloadController 上传、下载api")
@RestController
@RequestMapping("/uploadOrDownload")
public class UploadOrDownloadController {

    private static final Logger log = LoggerFactory.getLogger(UploadOrDownloadController.class);

    @Autowired
    UploadOrDownloadBusiness uploadOrDownloadBusiness;

    /**
     * linux 使用xftp 上传文件
     * @param files
     * @param request
     * @param response
     */
    @ApiOperation(value = "linux 使用xftp 上传文件接口", notes = "")
    @PostMapping("/uploadFiles")
    public void uploadFiles(MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) {
        try {
            uploadOrDownloadBusiness.uploadFiles(files,request,response);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }
}
