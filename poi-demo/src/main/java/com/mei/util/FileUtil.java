package com.mei.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @description: 文件相关操作接口
 * @author: Administrator
 * @date: Created in 2019/11/16 13:40
 * @version:
 * @modified By:
 */
@Slf4j
public class FileUtil {

    /**
     * 获取文件的绝对路径
     *
     * @param simplePath
     * @return
     */
    public static String getFilePath(String simplePath) {
        File f = new File(FileUtil.class.getResource("/").getPath());
        StringBuffer buf = new StringBuffer(f.getAbsolutePath());
        buf.append(File.separator).append(simplePath);
        return buf.toString();
    }

    /**
     * 读取文本文件->字符串
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String readFileTXT(String path) {
        StringBuffer sb = new StringBuffer();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(path));
            byte[] tempbytes = new byte[1024];
            int byteread = 0;
            while ((byteread = fileInputStream.read(tempbytes)) != -1) {
                String str = new String(tempbytes, 0, byteread);
                sb.append(str);
            }
        } catch (Exception e) {
            log.info("读取异常：", e);
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.info("关闭异常：", e);
                }
            }
            return sb.toString();
        }

        //finally会把前面所有return 值覆盖

    }


    /**
     * 获取data流
     *
     * @param path
     */
    public DataInputStream readFileGetDataStream(String path) {

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(path));
        } catch (Exception e) {
            log.info("读取文件异常", e);
        }

        DataInputStream in = new DataInputStream(fileInputStream);

        return in;

    }

    /**
     * 获取file流
     *
     * @param path
     * @return
     */
    public FileInputStream readeFileGetFileStream(String path) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(path));
        } catch (Exception e) {
            log.info("读取文件异常", e);
        }
        return fileInputStream;
    }

    /**
     * 将字节流转换为字符流
     *
     * @param inputStream
     * @return
     */
    public InputStreamReader readeFileGeReaderStream(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
        } catch (Exception e) {
            log.info("转换异常", e);
        }
        return inputStreamReader;
    }


    public static void main(String[] args) {
        String path = FileUtil.getFilePath("ticket.json");
        String data = FileUtil.readFileTXT(path);
        System.out.println(data);
    }

}
