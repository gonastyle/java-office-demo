package com.mei;

import com.mei.opencsv.OpenCsvUtil;
import com.mei.util.FileUtil;

import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        test();
    }

    public static void test() {
        String path = FileUtil.getFilePath("西刺代理·.csv");
        List<String[]> list = OpenCsvUtil.readCsv(path);

        for (String[] arr : list) {
            StringBuffer buffer = new StringBuffer();
            for (String str : arr) {
                buffer.append(str).append("\t");
            }
            System.out.println(buffer);
        }
    }


    public static void test1() {
        String[] header = {"代理IP地址", "端口", "服务器地址", "是否匿名", "类型", "存活时间", "验证时间"};
        String path = FileUtil.getFilePath("西刺代理·.csv");
        List<String[]> list = OpenCsvUtil.readCsv(path);
        OpenCsvUtil.writeCsv("copy.csv", null, list);
    }
}
