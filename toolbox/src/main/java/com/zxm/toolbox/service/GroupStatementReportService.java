package com.zxm.toolbox.service;

import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.gt.*;
import com.zxm.toolbox.util.PoiUtils;
import com.zxm.toolbox.util.ReflectionUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GroupStatementReportService {
    //报表中列的参数，包括：列名、列值来源、是否需要求和、列中单元格样式、列宽
    public static List<ColumnProperties> HEADERS;
    private static int CURRENT_ROW = 0;
    private static int FIRST_ROW = 0;
    private static int LAST_ROW = 0;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 创建表头单元格样式
     *
     * @param wb XSSFWorkbook对象
     * @return CellStyle对象
     */

    public static CellStyle createHeaderStyle(XSSFWorkbook wb) {
        CellStyle cellStyle;
        cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor((short) 22);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
        cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
        return cellStyle;
    }

    /**
     * 创建内容单元格通用样式
     *
     * @param wb XSSFWorkbook对象
     * @return CellStyle对象
     */
    public static CellStyle createContentGenericStyle(XSSFWorkbook wb) {
        CellStyle cellStyle;
        cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
        cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
        return cellStyle;
    }

    /**
     * 创建内容单元格中价格单元格样式
     *
     * @param wb XSSFWorkbook对象
     * @return CellStyle对象
     */
    public static CellStyle createContentPriceStyle(XSSFWorkbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        XSSFDataFormat df = wb.createDataFormat();
        cellStyle.setDataFormat(df.getFormat("0.00")); // 两位小数
        cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
        cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
        return cellStyle;
    }



    /**
     * 设置列宽
     * @param sheet 工作表对象
     */
    public static void setColumnsSize(Sheet sheet) {
        int siz = HEADERS.size();
        for (int i = 0; i < siz; i++) {
            sheet.setColumnWidth(i, HEADERS.get(i).getColumnSize() * 256);
        }
    }

    /**
     * 生成表头
     *
     * @param wb          工作本对象
     * @param sheet       工作表对象
     * @param title       表头标题
     */
    public static void createHeader(XSSFWorkbook wb, Sheet sheet, String title) {
        // 开始生成标题（表头上面的航线名称）
        Row row;
        Cell cell;
        CellStyle cellStyle;
        XSSFFont font;
        row = sheet.createRow(CURRENT_ROW);
        cell = row.createCell(0);
        cell.setCellValue(title);
        font = wb.createFont();
        font.setFontName("仿宋_GB2312");
        font.setFontHeight(12);
        font.setBold(true);// 粗体显示
        cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        // 行号自动+1，准备生成下一行
        CURRENT_ROW++;
        row = sheet.createRow(CURRENT_ROW);
        int size = HEADERS.size();
        ColumnProperties colProp;
        for (int i = 0; i < size; i++) {
            colProp = HEADERS.get(i);
            cell = row.createCell(i);
            cell.setCellValue(colProp.getName());
            cellStyle = createHeaderStyle(wb);
            cell.setCellStyle(cellStyle);
        }
        // 行号自动+1，以便生成content使用
        CURRENT_ROW++;
    }

    public static void createOneRecord(XSSFWorkbook wb, Row row, GroupStatement record, int index){
        Cell cell;
        CellStyle cellStyle;
        Map<String, TicketDetail> tdMap = record.getTicketDetail();
        LocalDateTime time;
        TicketDetail td;
        int size = HEADERS.size();
        ColumnProperties colProp;
        for (int i = 0; i < size; i++) {
            colProp = HEADERS.get(i);
            cell = row.createCell(i);
            // 生成单元格样式
            if (colProp.getStyle().equals("PriceStyle")) {
                cellStyle = createContentPriceStyle(wb);
            } else {
                cellStyle = createContentGenericStyle(wb);
            }
            cell.setCellStyle(cellStyle);
            // 生成单元格内容
            if (colProp.getResource().equals(""))
                // 序号单元格
                cell.setCellValue(index);
            if (colProp.getResource().equals("Field")) {
                if (colProp.getReturnType().equals("LocalDateTime")) {
                    time = (LocalDateTime) ReflectionUtil.getFieldValueByName(colProp.getMethod(), record);
                    cell.setCellValue(Optional.ofNullable(time).map(t -> t.format(FORMATTER)).orElse(""));
                } else if (colProp.getReturnType().equals("String")) {
                    cell.setCellValue((String) ReflectionUtil.getFieldValueByName(colProp.getMethod(), record));
                }
            }
            if (colProp.getResource().equals("SubField")){
                td = tdMap.get(colProp.getPaxType());
                if (colProp.getTicketDetailAttr().equals("价格")) {
                    cell.setCellValue(td != null ? td.getPrice().doubleValue() : 0);
                } else {
                    cell.setCellValue(td != null ? td.getNum() : 0);
                }
            }
        }
    }
    /**
     * 生成单程团体报表内容
     *
     * @param wb 工作本对象
     * @param sheet 工作表对象
     * @param records 团体记录
     */
    public static void createOnewayContent(XSSFWorkbook wb, Sheet sheet, List<GroupStatement> records) {
        Row row;
        int rowNum = CURRENT_ROW;
        GroupStatementReportService.FIRST_ROW = CURRENT_ROW;
        int index = 1;
        for (GroupStatement record : records) {
            row = sheet.createRow(rowNum);
            createOneRecord(wb, row, record, index);
            rowNum++;
            index++;
        }
        CURRENT_ROW = rowNum;
        LAST_ROW = rowNum - 1;
    }

    /**
     * 生成来回团体报表内容
     *
     * @param wb          工作本对象
     * @param sheet       工作表对象
     * @param records     团体记录
     */
    public static void createRoundTripContent(XSSFWorkbook wb, Sheet sheet, List<RoundTripGroup> records) {
        Row row;
        int index = 1;
        int rowNum = CURRENT_ROW;
        FIRST_ROW = CURRENT_ROW;
        for (RoundTripGroup record : records) {
            // 去程团体记录
            row = sheet.createRow(rowNum);
            createOneRecord(wb, row, record.getDepartureGroup(), index);
            rowNum++;
            // 回程团体记录
            row = sheet.createRow(rowNum);
            createOneRecord(wb, row, record.getDepartureGroup(), index);
            rowNum++;
            index++;
        }
        CURRENT_ROW = rowNum;
        LAST_ROW = rowNum - 1;
    }

    /**
     * 生成报表底部统计行
     *
     * @param wb          工作本对象
     * @param sheet       工作表对象
     * @param recordsNum  团体记录的数量
     */
    public static void createFooter(XSSFWorkbook wb, Sheet sheet, int recordsNum) {
        Row row;
        Cell cell;
        String sumFormula;
        CellAddress ca1, ca2;
        row = sheet.createRow(GroupStatementReportService.CURRENT_ROW);
        int size = HEADERS.size();
        ColumnProperties colProp;
        for (int i = 0; i < size; i++) {
            colProp = HEADERS.get(i);
            cell = row.createCell(i);
            // 生成单元格样式
            cell.setCellStyle(colProp.getStyle().equals("PriceStyle") ?
                    createContentPriceStyle(wb) : createContentGenericStyle(wb));
            // 生成单元格内容
            if (colProp.getResource().equals("")) {
                // 序号单元格
                cell.setCellValue(recordsNum);
            } else if (!colProp.getNeedSum()) {
                cell.setCellValue("");
            } else {
                ca1 = new CellAddress(FIRST_ROW, i);
                ca2 = new CellAddress(LAST_ROW, i);
                sumFormula = "SUM(" + ca1 + ":" + ca2 + ")";
                cell.setCellFormula(sumFormula);
            }
        }
        FIRST_ROW = 0;
        LAST_ROW = 0;
        CURRENT_ROW = CURRENT_ROW + 3;
    }

    /**
     * 此方式适用单去类型的航线组 DOG(Depart Only Group)
     *
     * @param file    要导出的团体报表文件
     * @param sheetName 工作表名
     * @param records 团体记录
     * @param group   团体记录所属的航线组
     * @throws Exception 从数据配置文件读取数据时发生异常
     */
    public static void exportDepartOnly(File file, String sheetName, List<GroupStatement> records, ShippingGroup group) throws Exception {
        System.out.println("开始生成团体报表：" + group.getName());
        XSSFWorkbook wb = PoiUtils.loadXSSFWorkbook(file);
        if (wb == null)
            return;
        XSSFSheet sheet = wb.createSheet(sheetName);
        CURRENT_ROW = 0;
        if (records.size() != 0) {
            // 生成表头
            createHeader(wb, sheet, group.getName());
            // 开始生成表格内容
            createOnewayContent(wb, sheet, records);
            // 生成统计行
            createFooter(wb, sheet, records.size());
        }
        // 判断是否需要分船东
        if (group.getIsCompanySeparate().equals("是")) {
            List<GroupStatement> tempRecords;
            List<ShippingCompany> companies =
                    DaoFactory.getInstance().getShippingCompanyDao().findByGroupName(group.getName());
            for (ShippingCompany company : companies) {
                tempRecords = GroupStatementService.selectOneShippingCompanyRecords(records, company.getName());
                if (tempRecords.size() != 0) {
                    // 生成表头
                    createHeader(wb, sheet, company.getName());
                    // 开始生成表格内容
                    createOnewayContent(wb, sheet, tempRecords);
                    // 生成统计行
                    createFooter(wb, sheet, tempRecords.size());
                }
            }
        }
        setColumnsSize(sheet);
        // 公式自动计算值
        wb.setForceFormulaRecalculation(true);
        // 把数据写入文件
		FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
        wb.close();
        System.out.println("团体报表已生成：" + group.getName());
        CURRENT_ROW = 0;
    }

    /**
     * 此方式适用单去单回类型的航线组(Depart And Return Group)
     *
     * @param file    要导出的团体报表文件
     * @param sheetName 工作表名
     * @param records 团体记录
     * @param group   团体记录所属的航线组
     * @throws Exception 从数据配置文件读取数据时发生异常
     */
    public static void exportDepartAndReturn(File file, String sheetName, List<GroupStatement> records,
                                             ShippingGroup group) throws Exception {
        System.out.println("开始生成团体报表：" + group.getName());
        XSSFWorkbook wb = PoiUtils.loadXSSFWorkbook(file);
        if (wb == null)
            return;
        XSSFSheet sheet = wb.createSheet(sheetName);
        List<GroupStatement> tempRecords;
        CURRENT_ROW = 0;
        // 生成去程团体表
        tempRecords = GroupStatementService.selectDepartRecord(records);
        if (tempRecords.size() != 0) {
            // 生成表头
            createHeader(wb, sheet, group.getName());
            // 开始生成表格内容
            createOnewayContent(wb, sheet, tempRecords);
            // 生成统计行
            createFooter(wb, sheet, tempRecords.size());
        }

        // 生成回程团体表
        tempRecords = GroupStatementService.selectReturnRecord(records);
        if (tempRecords.size() != 0) {
            // 生成表头
            createHeader(wb, sheet, group.getName());
            // 开始生成表格内容
            createOnewayContent(wb, sheet, tempRecords);
            // 生成统计行
            createFooter(wb, sheet, tempRecords.size());
        }
        // 判断是否需要分船东
        if (group.getIsCompanySeparate().equals("是")) {
            List<ShippingCompany> companies = DaoFactory.getInstance().getShippingCompanyDao().findByGroupName(group.getName());
            for (ShippingCompany company : companies) {
                tempRecords = GroupStatementService.selectOneShippingCompanyRecords(records, company.getName());
                if (tempRecords.size() != 0) {
                    // 生成表头
                    createHeader(wb, sheet, company.getName());
                    // 开始生成表格内容
                    createOnewayContent(wb, sheet, tempRecords);
                    // 生成统计行
                    createFooter(wb, sheet, tempRecords.size());
                }
            }

        }
        setColumnsSize(sheet);
        // 公式自动计算值
        wb.setForceFormulaRecalculation(true);
        // 把数据写入文件
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
        wb.close();
        System.out.println("团体报表已生成：" + group.getName());
        CURRENT_ROW = 0;
    }

    /**
     * 此方式适用单来回类型的航线组(Round Only Group)
     * @param file    要导出的团体报表文件
     * @param sheetName 工作表名
     * @param records 团体记录
     * @param group   团体记录所属的航线组
     * @throws IOException 输入输出异常
     */
    public static void exportRoundOnly(File file,String sheetName, List<RoundTripGroup> records,
                                       ShippingGroup group) throws IOException {
        System.out.println("开始生成团体报表：" + group.getName());
        XSSFWorkbook wb = PoiUtils.loadXSSFWorkbook(file);
        if (wb == null)
            return;
        XSSFSheet sheet = wb.createSheet(sheetName);
        CURRENT_ROW = 0;
        if (records.size() != 0) {
            // 生成表头
            createHeader(wb, sheet, group.getName());
            // 开始生成表格内容
            createRoundTripContent(wb, sheet, records);
            // 生成统计行
            createFooter(wb, sheet, records.size());
        }
        setColumnsSize(sheet);

        wb.setForceFormulaRecalculation(true);
		FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
        wb.close();
        System.out.println("团体表表已生成：" + group.getName());
        CURRENT_ROW = 0;
    }

    /**
     * 此方式适用单去单回来回类型的航线组(Depart Return And Round Group)
     *
     * @param file 要导出的团体报表文件
     * @param roundRecords 团体记录
     * @param departRecords 去程团体记录数据
     * @param returnRecords 回程团体记录数据
     * @param sheetName 团体报表月份
     * @param group 团体记录所属的航线组
     * @throws IOException 输入输出异常
     */
    public static void exportDepartReturnRound(
            File file,
            List<RoundTripGroup> roundRecords,
            List<GroupStatement> departRecords,
            List<GroupStatement> returnRecords,
            String sheetName,
            ShippingGroup group) throws IOException {
        System.out.println("开始生成团体报表：" + group.getName());
        XSSFWorkbook wb = PoiUtils.loadXSSFWorkbook(file);
        if (wb == null)
            return;

        XSSFSheet sheet = wb.createSheet(sheetName);
        CURRENT_ROW = 0;
        // 开始生成去程团体记录
        if (departRecords.size() != 0) {
            // 生成表头
            createHeader(wb, sheet, group.getName() + " - 去程");
            // 开始生成表格内容
            createOnewayContent(wb, sheet, departRecords);
            // 生成统计行
            createFooter(wb, sheet, departRecords.size());
        }

        // 开始生成来回表
        if (roundRecords.size() != 0) {
            // 生成表头
            createHeader(wb, sheet, group.getName() + " - 来回");
            // 开始生成表格内容
            createRoundTripContent(wb, sheet, roundRecords);
            // 生成统计行
            createFooter(wb, sheet, roundRecords.size());
        }

        // 开始生成回程团体记录
        if (returnRecords.size() != 0) {
            // 生成表头
            createHeader(wb, sheet, group.getName() + " - 回程");
            // 开始生成表格内容
            createOnewayContent(wb, sheet, returnRecords);
            // 生成统计行
            createFooter(wb, sheet, returnRecords.size());
        }
        setColumnsSize(sheet);
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.setForceFormulaRecalculation(true);
        wb.write(fileOut);
        fileOut.close();
        wb.close();
        System.out.println("团体表表已生成：" + group.getName());
        CURRENT_ROW = 0;
    }

    /**
     * 此方法用来生成异常团体记录报表 EG(Excepted Group)
     *
     * @param file    要导出的团体报表文件
     * @param records 异常团体记录
     * @throws IOException 输入输出异常
     */
    public static void exportReportEG(File file, List<GroupStatement> records) throws IOException {
        System.out.println("开始生成异常团体记录报表");
        XSSFWorkbook wb = PoiUtils.loadXSSFWorkbook(file);
        if (wb == null)
            return;

        XSSFSheet sheet = wb.createSheet("异常团体记录");
        CURRENT_ROW = 0;
        if (records.size() != 0) {
            // 生成表头
            createHeader(wb, sheet, "异常团体记录");
            // 开始生成表格内容
            createOnewayContent(wb, sheet, records);
            // 生成统计行
            createFooter(wb, sheet, records.size());
        }
        setColumnsSize(sheet);
        wb.setForceFormulaRecalculation(true);

        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
        wb.close();
        System.out.println("异常团体记录报表已生成");
        CURRENT_ROW = 0;
    }
}
