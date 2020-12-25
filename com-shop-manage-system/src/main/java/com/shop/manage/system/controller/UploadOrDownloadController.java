package com.shop.manage.system.controller;

import com.alibaba.fastjson.JSON;
import com.shop.manage.system.annotation.AspectIgnoreLog;
import com.shop.manage.system.business.UploadOrDownloadBusiness;
import com.shop.manage.system.util.FtpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@AspectIgnoreLog
public class UploadOrDownloadController {

    private static final Logger log = LoggerFactory.getLogger(UploadOrDownloadController.class);

    @Autowired
    UploadOrDownloadBusiness uploadOrDownloadBusiness;

    /**
     * linux 使用xftp 上传文件
     *
     * @param files
     * @param request
     * @param response
     */
    @ApiOperation(value = "linux 使用xftp 上传文件接口", notes = "")
    @PostMapping("/sftpUploadFiles")
    public void sftpUploadFiles(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) {
        try {
            uploadOrDownloadBusiness.sftpUploadFiles(files, request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * linux 使用xftp 下载文件,压缩成 zip
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "linux 使用xftp 下载文件接口", notes = "")
    @GetMapping("/sftpDownloadFiles")
    public void sftpDownloadFiles(HttpServletRequest request, HttpServletResponse response) {
        try {
            uploadOrDownloadBusiness.sftpDownloadFilesForZip(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 应用部署所在服务器，上传文件
     *
     * @param files
     * @param request
     * @param response
     */
    @ApiOperation(value = "应用部署所在服务器，上传文件接口", notes = "")
    @PostMapping("/localServerUploadFiles")
    public void localServerUploadFiles(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) {
        try {
            uploadOrDownloadBusiness.localServerUploadFiles(files, request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 采用sftp 连接 linux 下载文件
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "采用sftp 连接 linux 下载文件", notes = "")
    @GetMapping("/sftpDownloadFile")
    public void sftpDownloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            uploadOrDownloadBusiness.sftpDownloadFile(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 下载项目中 resource 目录下的文件
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "下载项目中 resource 目录下的文件", notes = "")
    @GetMapping("/resourceDirDownloadFile")
    public void resourceDirDownloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            uploadOrDownloadBusiness.resourceDirDownloadFile(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
