package com.mei;

import com.mei.excel.PoiExcelUtil;
import com.mei.util.TaskPool;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void start() {

        TaskPool taskPool = new TaskPool(300);
        List<Object[]> dataList = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 500000; i = i + 10000) {
            AddDataTask task = new AddDataTask((i / 10000) * 10000, (i / 10000 + 1) * 10000, dataList);
            taskPool.submitTask(task);
        }
        long a = taskPool.getThreadPoolExecutor().getActiveCount();
        long b = taskPool.getThreadPoolExecutor().getTaskCount();
        long c = taskPool.getThreadPoolExecutor().getCompletedTaskCount();
        while (a != 0) {
            a = taskPool.getThreadPoolExecutor().getActiveCount();
            b = taskPool.getThreadPoolExecutor().getTaskCount();
            c = taskPool.getThreadPoolExecutor().getCompletedTaskCount();
            System.out.println(a + "," + b + "," + c);

        }

        System.out.println(a + "," + b + "," + c);
        System.out.println(dataList.size());


        String path = "C:\\Users\\Administrator\\Desktop\\xiciproxy.xlsx";
        String[] header = {"序号", "IP地址", "端口", "存活时间"};
        PoiExcelUtil.writeExcel(path, "西刺代理", header, dataList);
    }

    @Test
    public void read() throws IOException {

    }


}
