package com.mei;

import com.mei.excel.PoiExcelUtil;
import com.mei.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * float 小数位，6位有效
 * double 小数位，15位有效
 */
public class App {

    public static void main(String[] args) throws IOException {
        test();
        test1();
    }


    /**
     * 写测试
     */
    public static void test() {

        String path = FileUtil.getFilePath("西刺代理.xlsx");

        String[] header = {"序号", "IP地址", "端口", "存活时间"};

        List<Object[]> dataList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Object[] row = {i + 1, 12.1111222233334446d, true, new Date()};
            dataList.add(row);
        }

        PoiExcelUtil.writeExcel(path, "西刺代理", header, dataList);

    }

    /**
     * 读测试
     */
    public static void test1() {

        String path = FileUtil.getFilePath("西刺代理.xlsx");
        List<ArrayList<Object>> datalist = PoiExcelUtil.readExcel(path);
        for (ArrayList<Object> objects : datalist) {
            System.out.println(objects);
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for (int i = 3; i < datalist.size(); i++) {
            System.out.println(datalist.get(i));
        }
    }


}

