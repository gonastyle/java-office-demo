package com.mei.opencsv;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 操作CSV文件的接口
 * @Author: Administrator
 * @Date: 2019/11/21 11:23
 * @Version: V1.0
 **/
@Slf4j
public class OpenCsvUtil {

    /**
     * <读取CSV文件>
     *
     * @param path
     * @return
     */
    public static List<String[]> readCsv(String path) {
        List<String[]> datalist = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "GBK");
            /**
             * line 从第几行开始读
             * separator 分隔符，一般为逗号
             */
            CSVReader csvReader = new CSVReader(inputStreamReader, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.DEFAULT_ESCAPE_CHARACTER, 0);
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                datalist.add(row);
            }
            csvReader.close();
        } catch (Exception e) {
            log.info("读取文件异常", e);
        }

        return datalist;

    }

    /**
     * list 数据写入CSV
     *
     * @param path
     * @param headers
     * @param list
     */
    public static void writeCsv(String path, String[] headers, List<String[]> list) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "GBK");
            CSVWriter writer = new CSVWriter(outputStreamWriter, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER);
            //写表头
            if (headers != null) {
                writer.writeNext(headers);
            }
            //写数据
            writer.writeAll(list);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.info("操作异常", e);
        }

    }


    /**
     * <读取CSV文件>
     * 针对流进行处理：web
     *
     * @param path
     * @return
     */
    public static List<String[]> readCsv(String path, InputStream inputStream) {
        List<String[]> datalist = new ArrayList<>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
            /**
             * line 从第几行开始读
             * separator 分隔符，一般为逗号
             */
            CSVReader csvReader = new CSVReader(inputStreamReader, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.DEFAULT_ESCAPE_CHARACTER, 0);
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                datalist.add(row);
            }
            csvReader.close();
        } catch (Exception e) {
            log.info("读取文件异常", e);
        }

        return datalist;

    }

    /**
     * list 数据写入CSV
     * 针对流写入CSV：web
     *
     * @param headers
     * @param list
     */
    public static void writeCsv(String[] headers, List<String[]> list, OutputStream outputStream) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "GBK");
            CSVWriter writer = new CSVWriter(outputStreamWriter, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER);
            //写表头
            if (headers != null) {
                writer.writeNext(headers);
            }
            //写数据
            writer.writeAll(list);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.info("操作异常", e);
        }
    }

    public static void main(String[] args) {

    }
}
