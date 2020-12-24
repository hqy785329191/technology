package com.shop.manage.system.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 处理文件的工具类
 * @author Mr.joey
 */
public class FileUtils {
    /**
     * 将输入流转换为文件
     *
     * @param inputStream 输入流
     * @param file        文件对象
     * @throws IOException 异常
     */
    public static void inputStreamToFile(InputStream inputStream, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        inputStream.close();
    }
}

