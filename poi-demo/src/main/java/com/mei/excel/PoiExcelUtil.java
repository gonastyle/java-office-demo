package com.mei.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description: [支持多个sheet页创建, 一般5g内存最多创建50万条，否则内存溢出]
 * @Author: Administrator
 * @Date: 2019/11/21 15:45
 * @Version: V1.0
 **/
@Slf4j
public class PoiExcelUtil {
    /**
     * 文件后缀名
     */
    public static final String OFFICE_EXCEL_XLS = "xls";
    public static final String OFFICE_EXCEL_XLSX = "xlsx";

    /**
     * .xls 单表最大行数
     */
    private static int XLS_MAX_ROW = 65536;
    /**
     * .xlsx 单表最大行数
     */
    private static int XLSX_MAX_ROW = 1048576;
    /**
     * 动态判断那种最大值
     */
    private static int MAX_ROW = 0;

    /**
     * 表头占据行数
     */
    private static int HEAD_COUNT = 3;
    /**
     * 日期格式化
     */
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 数据写入xls中
     *
     * @param path
     * @param title
     * @param header
     * @param datalist
     */
    public static void writeExcel(String path, String title, String[] header, List<Object[]> datalist) {
        OutputStream out = null;
        try {
            //工作簿 ： 面向接口编程
            Workbook workbook = null;
            if (path.endsWith(OFFICE_EXCEL_XLS)) {
                workbook = new HSSFWorkbook();
                //xls最大行数
                MAX_ROW = XLS_MAX_ROW;
            } else if (path.endsWith(OFFICE_EXCEL_XLSX)) {
                workbook = new XSSFWorkbook();
                //xlsx最大行数
                MAX_ROW = XLSX_MAX_ROW;
            }

            Sheet sheet = workbook.createSheet(title + "1");
            sheet.setDefaultColumnWidth((short) 25);
            //标题栏单元格
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            for (int i = 0; i < 2; i++) {
                Row tRow = i == 0 ? titleRow : sheet.createRow(i);
                for (int j = 0; j < header.length; j++) {
                    if (i == 0 && j == 0) continue;
                    Cell tCell = tRow.createCell(j);
                    tCell.setCellValue("");
                }
            }
            CellStyle titleStyle = getTilteStyle(workbook);
            CellStyle headStyle = getHeadStyle(workbook);
            CellStyle bodyStyle = getBodyStyle(workbook);
            //合并2行n列
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, header.length - 1));
            titleCell.setCellStyle(titleStyle);
            titleCell.setCellValue(title);

            //表头栏字段
            Row titleHead = sheet.createRow(2);
            for (int i = 0; i < header.length; i++) {
                Cell titleHeadCell = titleHead.createCell(i);
                titleHeadCell.setCellStyle(headStyle);
                RichTextString text = null;
                if (workbook instanceof HSSFWorkbook) {
                    text = new HSSFRichTextString(header[i]);
                } else if (workbook instanceof XSSFWorkbook) {
                    text = new XSSFRichTextString(header[i]);
                }
                titleHeadCell.setCellValue(text);
            }

            int a = (datalist.size() + HEAD_COUNT) / MAX_ROW;
            int b = (datalist.size() + HEAD_COUNT) % MAX_ROW;

            //sheet页个数
            int m = 0;
            if (a == 0) {//第一页
                m = 1;
            } else if (a != 0 && b == 0) {//是整数页
                m = a;
            } else if (a != 0 && b != 0) {//非整数页
                m = a + 1;
            }

            //循环创建sheet页
            for (int i = 0; i < m; i++) {

                //计算当前页数据条数[每页数据的起始位置]
                int start = 0;
                int end = 0;
                if (m == 1 && (b == 0)) {//只有一页,整数
                    start = 0;
                    end = MAX_ROW - HEAD_COUNT;
                } else if (m == 1 && (b != 0)) {//只有一页,非整数
                    start = 0;
                    end = b - 3;
                } else if (i == 0 && m != 1) {//多页中，第一页
                    start = 0;
                    end = MAX_ROW - HEAD_COUNT;
                } else if ((i == m - 1) && (a != 0 && b == 0)) {//最后一页，是整数页
                    start = MAX_ROW * i - HEAD_COUNT;
                    end = MAX_ROW;
                } else if ((i == m - 1) && (a != 0 && b != 0)) {//最后一页，非整数页
                    start = MAX_ROW * i - HEAD_COUNT;
                    end = b;
                } else { //既不是第一页，也不是最后一页
                    start = MAX_ROW * i - HEAD_COUNT;
                    end = MAX_ROW;
                }

                Sheet sheet_new = null;
                if (i != 0) {
                    sheet_new = workbook.createSheet(title + (i + 1));
                    sheet_new.setDefaultColumnWidth((short) 25);
                }
                //当前sheet表体数据
                for (int j = start; j < start + end; j++) {
                    Object[] obj = datalist.get(j);
                    //创建行:每页必须从0开始
                    Row row = null;
                    if (i == 0) {
                        row = sheet.createRow(j + HEAD_COUNT);//从表头后开始
                    } else {
                        row = sheet_new.createRow(j - start);//相当于从0-655365(最大行数)
                    }
                    //每列写入数据
                    for (int k = 0; k < obj.length; k++) {
                        Cell cell = row.createCell(k);
                        if (obj[k] instanceof Date) {
                            String time = simpleDateFormat.format((Date) obj[k]);
                            cell.setCellValue(time);
                        } else if (obj[k] instanceof Calendar) {
                            Calendar calendar = (Calendar) obj[k];
                            String time = simpleDateFormat.format(calendar.getTime());
                            cell.setCellValue(time);
                        } else if (obj[k] instanceof Boolean) {
                            cell.setCellValue((Boolean) obj[k]);
                        } else {
                            if (!"".equals(obj[k]) && obj[k] != null) {
                                cell.setCellValue(obj[k].toString());
                            }
                        }
                        cell.setCellStyle(bodyStyle);
                    }
                }
            }


            //写入文件
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            workbook.write(out);
            out.flush();

        } catch (Exception e) {
            log.info("写入出现异常：", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.info("流关闭异常：", e);
                }
            }
        }


    }

    /**
     * 表格标题样式
     *
     * @param workbook
     * @return
     */
    public static CellStyle getTilteStyle(Workbook workbook) {

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 15);
        font.setBold(true);
        font.setFontName("黑体");

        CellStyle style = workbook.createCellStyle();
        //设置边框属性
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);


        return style;
    }

    /**
     * 表格头样式
     *
     * @param workbook
     * @return
     */
    public static CellStyle getHeadStyle(Workbook workbook) {

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        font.setFontName("黑体");

        CellStyle style = workbook.createCellStyle();
        //设置边框属性
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /**
     * 表格数据样式
     *
     * @param workbook
     * @return
     */
    public static CellStyle getBodyStyle(Workbook workbook) {

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        font.setFontName("宋体");

        CellStyle style = workbook.createCellStyle();
        //设置边框属性
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);


        return style;
    }


    /**
     * 读取Excel所有sheet页到list
     *
     * @param path
     * @return
     */
    public static List<ArrayList<Object>> readExcel(String path) {
        List<ArrayList<Object>> datalist = new ArrayList<>();
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(new File(path));
            wb = WorkbookFactory.create(is);
            //获取sheet页数
            int sheetCount = wb.getNumberOfSheets();
            //循环读取每页
            for (int i = 0; i < sheetCount; i++) {
                Sheet sheet = wb.getSheetAt(i);
                //获取当前页的总记录数
                int totalRow = sheet.getLastRowNum();
                //读取每行
                for (int j = 0; j <= totalRow; j++) {
                    Row row = sheet.getRow(j);
                    if (row != null) {
                        //获取每行的总列数
                        int totalCol = row.getLastCellNum();
                        //循环读取每列
                        ArrayList<Object> rowData = new ArrayList<>();
                        for (int k = 0; k < totalCol; k++) {
                            Cell cell = row.getCell(k);
                            if (cell != null) {
                                if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                                    rowData.add(k, cell.getBooleanCellValue());
                                } else if (cell.getCellTypeEnum() == CellType.STRING) {
                                    rowData.add(k, cell.getStringCellValue());
                                } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                    rowData.add(k, cell.getNumericCellValue());
                                } else {//未知类型，默认NULL
                                    rowData.add(k, "NULL");
                                }
                            }
                        }
                        datalist.add(rowData);
                    }

                }
            }


        } catch (Exception e) {
            log.info("读取异常：", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return datalist;
    }


    public static void main(String[] args) {


    }
}
