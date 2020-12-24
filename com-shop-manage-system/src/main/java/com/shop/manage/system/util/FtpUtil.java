package com.shop.manage.system.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.shop.manage.system.contant.ProjectContant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Properties;

/**
 * ftp 工具类
 * @author Mr.joey
 */
public class FtpUtil {

    private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);

    private String ip;
    private int port;
    private String userName;
    private String password;
    private ChannelSftp sftp;

    /**
     * 构造器
     *
     * @param ip
     * @param port
     * @param userName
     * @param password
     */
    private FtpUtil(String ip, int port, String userName, String password) {
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    /**
     * 初始化连接
     */
    public void initConntectFTP() {
        this.sftp = initConnectFTP(ip, port, userName, password);
    }


    /**
     * 关闭连接
     */
    public void closeConnectFTP() {
        closeConnectFTP(this.sftp);
    }

    /**
     * 根据文件路径和文件名获取文件
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public File getFile(String filePath, String fileName) {
        return getFile(this.sftp, filePath, fileName);
    }

    /**
     * 将文件上传到ftp服务器
     * @param localFilePath  本地文件路径
     * @param ftpPath        上传到的目标路径
     * @param localFileName  文件名
     * @return
     */
    public boolean uploadFile(String localFilePath, String ftpPath, String localFileName) {
        return uploadFile(this.sftp, localFilePath, ftpPath, localFileName);
    }

    /**
     * 获取FtpUtil实例
     *
     * @param ip
     * @param port
     * @param userName
     * @param password
     * @return
     */
    public static FtpUtil getFtpUtil(String ip, int port, String userName, String password) {
        return new FtpUtil(ip, port, userName, password);
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file != null) {
            file.delete();
        }
    }

    /**
     * 将文件转换为base64编码
     *
     * @param file
     * @return
     */
    public static String transFileToBase64(File file) {
        if (file == null) {
            return null;
        }
        try {
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return new String(Base64.getEncoder().encode(buffer), ProjectContant.ENCODING);
        } catch (FileNotFoundException e) {
            log.error("文件[{}]不存在", file.getName(), e);
        } catch (IOException e) {
            log.error("获取文件[{}]IO异常", file.getName(), e);
        }
        return null;
    }

    /**
     * 初始化连接
     *
     * @param ip
     * @param port
     * @param userName
     * @param password
     * @return
     */
    private static ChannelSftp initConnectFTP(String ip, int port, String userName, String password) {
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(userName, ip, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;
            if (sftp.isConnected()) {
                log.info("创建sftp连接成功，用户名[{}]，端口号[{}]", userName, ip);
            } else {
                log.error("创建sftp连接失败，用户名[{}]，端口号[{}]", userName, ip);
            }
            return sftp;
        } catch (JSchException e) {
            log.error("创建sftp连接异常，用户名[{}]，端口号[{}]", userName, ip, e);
        }
        return null;
    }

    private static boolean uploadFile(ChannelSftp sftp, String localFilePath, String ftpPath, String localFileName){
        if (sftp == null || !sftp.isConnected()){
            return false;
        }
        boolean uploadFlag = true;
        try {
            if (!isExistDir(sftp, ftpPath)){
                sftp.mkdir(ftpPath);
                sftp.cd(ftpPath);
            }
            sftp.put(localFilePath  + File.separator + localFileName, localFileName);
        } catch (Exception e){
            uploadFlag = false;
            log.error("ftp 上传文件出错："+e.getMessage(), e);
        }
        return uploadFlag;
    }

    /**
     * 目录是否存在
     * @param path
     * @return
     */
    private static boolean isExistDir(ChannelSftp sftp, String path){
        try {
            sftp.cd(path);
            return true;
        } catch(SftpException e){
            return false;
        }
    }

    /**
     * 关闭连接
     *
     * @param sftp
     */
    private static void closeConnectFTP(ChannelSftp sftp) {
        if (sftp != null && sftp.isConnected()) {
            try {
                sftp.getSession().disconnect();
                log.info("关闭session成功");
            } catch (JSchException e) {
                log.error("关闭session异常成功", e.getMessage());
            }
            sftp.disconnect();
            log.info("关闭sftp连接成功");
        }
    }

    /**
     * 获取文件
     *
     * @param sftp
     * @param filePath
     * @param fileName
     * @return
     */
    private static File getFile(ChannelSftp sftp, String filePath, String fileName) {
        try {
            sftp.cd(filePath);
            File file = new File(fileName);
            OutputStream output = new FileOutputStream(file);
            sftp.get(fileName, output);
            output.close();
            return file;
        } catch (Exception e) {
            log.error("获取文件{}异常", filePath + fileName, e);
        }
        return null;
    }
}
