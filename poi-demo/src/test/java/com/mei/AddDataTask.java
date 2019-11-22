package com.mei;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Administrator
 * @Date: 2019/11/22 17:04
 * @Version: V1.0
 **/
public class AddDataTask implements Runnable {

    private int start;

    private int end;

    private List<Object[]> datalist;

    public AddDataTask(int start, int end, List<Object[]> datalist) {
        this.start = start;
        this.end = end;
        this.datalist = datalist;
    }

    @Override
    public void run() {

        for (int i = start; i < end; i++) {
            Object[] row = {i + 1, 12.1111222233334446d, true, new Date()};
            datalist.add(row);
        }

        System.out.println(String.format("%s -任务结束:%s - %s", Thread.currentThread().getName(), start, end));

    }
}
