package com.zxm.toolbox.service;

import com.zxm.toolbox.util.PoiUtils;
import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.attn.*;
import com.zxm.toolbox.exception.attn.DuplicatePostException;
import com.zxm.toolbox.resources.MyProperties;
import com.zxm.toolbox.resources.PropertiesFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class AttnReportService {
    private static final Logger LOGGER = LogManager.getLogger(AttnReportService.class);

    /**
     * 查找追加考勤的起始单元格
     * @param sheet 工作表
     * @return 找到的单元格地址
     */
    public static CellAddress findDutyRecordStartCell(Sheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        Row row;
        Cell cell;
        for (int i = 5; i < lastRowNum; i++) {
            row = sheet.getRow(i);
            //如果此行为空，则从此行开始追加考勤
            if (row == null)
                return new CellAddress(i,1);
            cell = row.getCell(1);
            //如果此行的姓名列单元格为空，则从此行开始追加考勤
            if (cell == null)
                return new CellAddress(i,1);
            //如果此行姓名列的单元格等于空字符串，则从此行开始追加考勤
            if (cell.getStringCellValue().equals(""))
                return cell.getAddress();
        }
        return null;
    }
    /**
     * 此方法用在全新上报模式中,生成上报考勤表里面上下班时间单元格岗位信息字符串
     * @param postList 从考勤表里面读取的岗位列表
     * @return 上报考勤表里面上下班时间单元格所需的字符串，
     * 格式：岗位名+中文冒号+上班时间+中文分号，多个岗位名以”/“分隔，
     * 上班时间格式：上班时间-下班时间，时间里面的冒号为英文冒号。
     */
    public  static StringBuilder createPostStr(List<Post> postList) {
        LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
        //根据dutyRoster里面的岗位信息，生成上报考勤表里面岗位信息字符串
        List<String> clockTimeList = new ArrayList<>();
        Map<String, StringBuilder> map = new HashMap<>();
        String str;
        StringBuilder sb = new StringBuilder();
        for(Post p : postList) {
            if(p.getClockInTime() == null)
                continue;
            str = p.getClockInTime() + "-" + p.getClockOutTime();
            if(!clockTimeList.contains(str))
                clockTimeList.add(str);
            if(!map.containsKey(str)){
                map.put(str, new StringBuilder(p.getPostName()));
            } else {
                map.get(str).append("/").append(p.getPostName());
            }
        }
        Collections.sort(clockTimeList);
        for (String clockTime : clockTimeList) {
            sb.append(map.get(clockTime)).append("：").append(clockTime).append("；");
        }
        LOGGER.info(sb.toString());
        return sb;
    }

    /**
     * 分析字符串中的岗位信息
     *
     * @param str 要分析的字符串
     * @return 岗位列表，岗位为上报考勤专用的岗位，只包含岗位名和打卡时间信息
     */
    public static List<Post> analysis(String str) {
        LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
        System.out.println("分析考勤表备注中的岗位上下班时间");
        List<Post> postList = new ArrayList<>();
        Post tempPost;
        str = str.trim();

        //将岗位信息字符串用“；”分割开，获得岗位信息的数组
        String[] postStrArr = str.split("；");
        String[] nameAndTimeArr;
        String[] inAndOutArr;
        String[] namesArr;
        String postNames;
        String clockTime;
        for (String postStr : postStrArr) {
            nameAndTimeArr = postStr.split("：");
            postNames = nameAndTimeArr[0];
            clockTime = nameAndTimeArr[1];
            inAndOutArr = clockTime.split("-");
            namesArr = postNames.split("/");
            for (String name : namesArr) {
                tempPost = new Post(name, inAndOutArr[0],inAndOutArr[1]);
                postList.add(tempPost);
            }
        }
        System.out.println("分析完成");
        LOGGER.info(postList);
        return postList;
    }

    /**
     * 此方法用在追加模式中的岗位合并，合并岗位的同时按岗位名称排序,最后生成上报考勤表里面所需的字符串
     * @param orgPosts 从原来考勤表里面读取的岗位列表
     * @param toAddPosts 要追加的岗位列表
     * @return 上报考勤表里面所需的字符串，
     * 格式：岗位名+中文冒号+上班时间+中文分号，多个岗位名以”/“分隔，
     * 上班时间格式：上班时间-下班时间，时间里面的冒号为英文冒号。
     * @throws DuplicatePostException 岗位名冲突异常
     */
    public static String createPostStr(List<Post> orgPosts, List<Post> toAddPosts)
            throws DuplicatePostException{
        LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
        System.out.println("开始生成上报考勤所需岗位信息");
        //先把两个岗位列表合并成一个列表
        for(Post p: toAddPosts){
            if(p.getClockInTime() != null)
                orgPosts.add(p);
        }
        //Key:岗位名,Value:上班时间
        Map<String, String> map1 = new HashMap<>();
        StringBuilder s1;
        for (Post p : orgPosts){
            s1 = new StringBuilder().append(p.getClockInTime()).append("-").append(p.getClockOutTime());
            if(map1.get(p.getPostName()) == null)
                map1.put(p.getPostName(), s1.toString());
            else {
                if(!map1.get(p.getPostName()).equals(s1.toString())) {
                    Post p2 = new Post();
                    p2.setPostName(p.getPostName());
                    //07:00-14:00
                    p2.setClockInTime(map1.get(p.getPostName()).substring(0, 5));
                    p2.setClockOutTime(map1.get(p.getPostName()).substring(6, 11));
                    throw new DuplicatePostException(p, p2);
                }
            }
        }
        //map1-Key:岗位名,Value:上班时间
        //map2-Key:上班时间,Value:以“/”连接的岗位名字符串
        Map<String, StringBuilder> map2 = new HashMap<>();
        for(Map.Entry<String, String> e : map1.entrySet()){
            if(map2.get(e.getValue()) == null){
                map2.put(e.getValue(), new StringBuilder(e.getKey()));
            } else {
                map2.put(e.getValue(),map2.get(e.getValue()).append("/").append(e.getKey()));
            }
        }
        List<String> l1 = new ArrayList<>(map2.keySet());
        Collections.sort(l1);
        // 按上下班时间排序
        StringBuilder clockTimeSb = new StringBuilder();
        for (String tempStr : l1) {
            clockTimeSb.append(map2.get(tempStr)).append("：").append(tempStr).append("；");
        }
        System.out.println("生成上报考勤所需岗位信息");
        return clockTimeSb.toString();
    }

    private static Cell findCell(Sheet sheet, String keyWord) {
        System.out.println("根据关键词查找单元格");
        if (keyWord == null || keyWord.equals("")) {
            System.out.println("关键词为空，无法查找");
            return null;
        }
        System.out.println("关键词：" + keyWord);
        Row row;
        Cell cell = null;
        int lastRowNum = sheet.getLastRowNum();
        for (int i = lastRowNum; i > 0; i--) {
            row = sheet.getRow(i);
            if (row == null)
                continue;
            cell = row.getCell(0);
            if (cell == null)
                continue;
            if (cell.getStringCellValue().contains(keyWord)) {
                return cell;
            }
        }
        LOGGER.debug(cell == null ? "null" : cell.getAddress());
        return cell;
    }

    /**
     * 写入上报考勤表表头日期行和星期行数据
     * @param dr DutyRoster对象，需要从中获取年月来计算最后一天
     * @param wb 工作本对象
     * @param sheet 工作表对象
     */
    private static void writeDateHeader(DutyRoster dr, XSSFWorkbook wb, Sheet sheet) {
        CellAddress address = new CellAddress(
                PropertiesFactory.getInstance().getTemplateProps().getValue("DateHeaderCell"));
        //当月的最后一天
        int lastDay = LocalDate.of(dr.getYear(), dr.getMonth(), 1)
                .with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        Row row = sheet.getRow(address.getRow());
        Cell cell;
        CellStyle cellStyle;
        int column = address.getColumn();
        for (int i = column, j = 1, endNum = i + lastDay; i < endNum; i++, j++) {
            cell = row.getCell(i);
            cell.setCellValue(j);
            cellStyle = PoiUtils.createNumericStyle(wb, 0);
            cell.setCellStyle(cellStyle);
        }
        row = sheet.getRow(address.getRow() + 1);
        Calendar tempCalendar = Calendar.getInstance();
        Map<Integer, String> int2Weekday = new HashMap<>();
        int2Weekday.put(Calendar.MONDAY, "一");
        int2Weekday.put(Calendar.TUESDAY, "二");
        int2Weekday.put(Calendar.WEDNESDAY, "三");
        int2Weekday.put(Calendar.THURSDAY, "四");
        int2Weekday.put(Calendar.FRIDAY, "五");
        int2Weekday.put(Calendar.SATURDAY, "六");
        int2Weekday.put(Calendar.SUNDAY, "日");
        int weekday;
        for (int i = column, j = 1, endNum = i + lastDay; i < endNum; i++, j++) {
            cell = row.getCell(i);
            tempCalendar.set(Calendar.DATE, j);
            tempCalendar.set(Calendar.YEAR, dr.getYear());
            tempCalendar.set(Calendar.MONTH, dr.getMonth() - 1);
            weekday = tempCalendar.get(Calendar.DAY_OF_WEEK);
            cell.setCellValue(int2Weekday.get(weekday));
        }
    }

    /**
     * 写入考勤记录和统计数据
     * @param dr DutyRoster，需要从中获取考勤表的年月来计算最后一天
     * @param wb 工作本对象
     * @param sheet 工作表对象
     * @param address 考勤记录起始单元格地址
     * @param companyName 上报考勤所属公司名
     */
    private static void writeSchAndData(DutyRoster dr, XSSFWorkbook wb, Sheet sheet, CellAddress address, String companyName) {
        //当月的最后一天
        int lastDay = LocalDate.of(dr.getYear(), dr.getMonth(), 1)
                .with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        List<EmpSchedule> esList = dr.getEsList();
        int rowNum;
        int column;
        Row row;
        Cell cell;
        CellStyle cellStyle;
        rowNum = address.getRow();
        for (EmpSchedule es : esList) {
            String esCompanyName = es.getEmployee().getCompany();
            // 如果公司名字为空，则跳过
            if (esCompanyName == null)
                continue;
            //
            column = address.getColumn();
            if (esCompanyName.equals(companyName)) {
                row = sheet.getRow(rowNum);
                cell = row.createCell(column);
                cell.setCellValue(es.getEmployee().getName());
                cellStyle = PoiUtils.createTextStyle(wb);
                cell.setCellStyle(cellStyle);
                // 写入考勤记录
                for (int j = column + 1, end = column + lastDay + 1; j < end; j++) {
                    cell = sheet.getRow(rowNum).createCell(j);
                    cell.setCellValue(es.getSchedule().get(j - column - 1));
                    cellStyle = PoiUtils.createTextStyle(wb);
                    cellStyle.setWrapText(true);
                    cell.setCellStyle(cellStyle);
                }
                column = column + 32;
                cellStyle = PoiUtils.createNumericStyle(wb, 1);
                // 写入出勤天数
                cell = row.createCell(column++);
                cell.setCellValue(es.getDutyDays());
                cell.setCellStyle(cellStyle);
                // 写入出勤小时数
                cell = row.createCell(column++);
                cell.setCellValue(es.getDutyTime());
                cell.setCellStyle(cellStyle);
                // 写入加班费小时数
                cell = row.createCell(column++);
                cell.setCellValue(es.getExtraTime());
                cell.setCellStyle(cellStyle);
                // 写入补休小时数
                cell = row.createCell(column++);
                cell.setCellValue(es.getCompensatoryTime() < 0 ? 0 : es.getCompensatoryTime());
                cell.setCellStyle(cellStyle);
                // 写入休息天数
                cell = row.createCell(column++);
                cell.setCellValue(es.getOffDays());
                cell.setCellStyle(cellStyle);
                // 写入工龄假天数
                cell = row.createCell(column++);
                cell.setCellValue(es.getAnnualLeaveDays());
                cell.setCellStyle(cellStyle);
                // 写入育儿假天数
                cell = row.createCell(column++);
                cell.setCellValue(es.getParentalLeaveDays());
                cell.setCellStyle(cellStyle);
                // 写入护理假天数
                cell = row.createCell(column++);
                cell.setCellValue(es.getNursingLeaveDays());
                cell.setCellStyle(cellStyle);
                // 写入产假天数
                cell = row.createCell(column++);
                cell.setCellValue(es.getMaternityDays());
                cell.setCellStyle(cellStyle);
                // 写入哺乳假天数
                cell = row.createCell(column++);
                cell.setCellValue(es.getBreastfeedingDays());
                cell.setCellStyle(cellStyle);
                // 写入累计补休小时数
                cell = row.createCell(column++);
                cell.setCellValue(es.getCompensatoryLeft());
                cell.setCellStyle(cellStyle);
                // 写入工龄假接剩余天数
                cell = row.createCell(column++);
                cell.setCellValue(es.getAnnualLeaveLeft());
                cell.setCellStyle(cellStyle);
                // 写入育儿假剩余天数
                cell = row.createCell(column++);
                cell.setCellValue(es.getParentalLeaveLeft());
                cell.setCellStyle(cellStyle);
                // 写入护理假剩余天数
                cell = row.createCell(column);
                cell.setCellValue(es.getNursingLeaveLeft());
                cell.setCellStyle(cellStyle);
                rowNum++;
            }
        }
    }
    /**
     * 全新模式下的上报考勤
     *
     * @param reportFile  上报考勤文件
     * @param sheetName   上报考勤Excel文件中的工作表名
     * @param dr          考勤记录对象
     * @param companyName 公司名
     * @throws IOException 输入输出异常
     */
    public static void newReport(File reportFile, String sheetName, DutyRoster dr, String companyName) throws IOException {
        LOGGER.info("newReport");
        //加载上报考勤的XSSFWorkbook
        XSSFWorkbook wb = PoiUtils.loadXSSFWorkbook(reportFile);
        if (wb == null)
            return;
        //加载上报考勤的Sheet
        Sheet sheet = wb.getSheet(sheetName);
        //考勤表年份
        int year = dr.getYear();
        //考勤表月份
        int month = dr.getMonth();
        //考勤表月份的最后一天
        MyProperties templateProperties = PropertiesFactory.getInstance().getTemplateProps();
        CellAddress address;
        Cell cell;
        StringBuilder sb;
        // 写入表头，主要是修改月份
        System.out.println("开始写入表头月份");
        LOGGER.info("开始写入表头月份");
        //上报考勤模板里面的表头单元格地址
        cell = PoiUtils.getCell(sheet, templateProperties.getValue("HeaderCell"));
        sb = new StringBuilder();
        sb.append("考  勤  表 （").append(year).append("年").append(month).append("月）");
        cell.setCellValue(sb.toString());
        // 写入部门名称
        System.out.println("开始写入部门名");
        LOGGER.info("开始写入部门名");
        //上报考勤模板里面的部门单元格
        cell = PoiUtils.getCell(sheet, templateProperties.getValue("DepartmentCell"));
        sb = new StringBuilder("部门：");
        sb.append(companyName).append(templateProperties.getValue("DepartmentName"));
        cell.setCellValue(sb.toString());
        // 写入制表日期
        System.out.println("开始写入制表日期");
        LOGGER.info("开始写入制表日期");
        //上报考勤模板里面的制表时间单元格
        cell = PoiUtils.getCell(sheet, templateProperties.getValue("ReportDateCell"));
        sb = new StringBuilder("制表时间：");
        sb.append(year).append("年").append(month + 1).append("月").append(1).append("日");
        cell.setCellValue(sb.toString());
        // 写入表头日期信息和星期信息
        System.out.println("开始写入考勤记录表头日期行和星期行");
        LOGGER.info("开始写入考勤记录表头日期行和星期行");
        writeDateHeader(dr, wb, sheet);
        // 写入考勤记录和统计数据
        System.out.println("开始写入考勤记录和统计信息");
        LOGGER.info("开始写入考勤记录和统计信息");
        // 上报考勤模板里面的考勤记录起始单元格地址
        address = new CellAddress(
                PropertiesFactory.getInstance().getTemplateProps().getValue("DutyRecordCell"));
        AttnReportService.writeSchAndData(dr, wb, sheet, address, companyName);
        // 写入上下班时间单元格内容
        System.out.println("开始写入上下班时间");
        LOGGER.info("开始写入上下班时间");
        //查找上报考勤模板里面的上下班时间单元格
        cell = findCell(sheet, templateProperties.getValue("ClockTimeKeyWord"));
        if (cell == null) {
            System.out.println("上下班时间单元格不存在，上报考勤任务异常终止");
            LOGGER.info("上下班时间单元格不存在，上报考勤任务异常终止");
            return;
        }
        cell.setCellValue("注：1、上下班时间：" + AttnReportService.createPostStr(dr.getWithinPosts()));
        // 写入考勤人单元格内容
        System.out.println("开始写入考勤人");
        LOGGER.info("开始写入考勤人");
        //查找上报考勤模板里面的制表人单元格地址
        cell = findCell(sheet, templateProperties.getValue("AttnOfficerKeyWord"));
        if (cell == null) {
            System.out.println("考勤人单元格不存在，上报考勤任务异常终止");
            LOGGER.info("考勤人单元格不存在，上报考勤任务异常终止");
            return;
        }
        StringBuilder footerSb = new StringBuilder(cell.getStringCellValue());
        AttnOfficer officer = DaoFactory.getInstance().getAttnOfficerDao().
                find(companyName, dr.getGroup().getName());
        footerSb.append(officer == null ? "" : officer.getName());
        cell.setCellValue(footerSb.toString());
        // 把数据写入到文件
        System.out.println("开始把数据写入到文件");
        LOGGER.info("开始把数据写入到文件");
        FileOutputStream fileOut = new FileOutputStream(reportFile);
        wb.write(fileOut);
        fileOut.close();
        wb.close();
        System.out.println("上报考勤任务已完成");
        LOGGER.info("上报考勤任务已完成");
    }

    /**
     * 追加模式下的上报考勤
     * @param dr 考勤记录对象
     * @param reportFile 上报考勤文件
     * @param sheetName 工作表名
     * @param companyName 公司名
     * @throws IOException 输入输出异常
     * @throws DuplicatePostException 岗位常重复定义异，出现此异常说明在追加上报考勤时，考勤表中的岗位信息和追加的岗位信息存在冲突，岗位名相同但是岗位上班时间不一致。
     */
    public static void additionalReport(DutyRoster dr, File reportFile, String sheetName, String companyName)
            throws IOException, DuplicatePostException {
        XSSFWorkbook wb = PoiUtils.loadXSSFWorkbook(reportFile);
        if (wb == null)
            return;
        Sheet sheet = wb.getSheet(sheetName);
        if (sheet == null)
            return;
        MyProperties templateProperties = PropertiesFactory.getInstance().getTemplateProps();
        Cell cell;
        //工作表最后一行
        CellAddress address = findDutyRecordStartCell(sheet);
        if (address == null) {
            System.out.println("无法找到追加考勤的起始单元格，上报考勤任务异常终止！");
            LOGGER.info("无法找到追加考勤的起始单元格，上报考勤任务异常终止！");
            return;
        }
        // 写入考勤记录和统计数据
        AttnReportService.writeSchAndData(dr, wb, sheet, address, companyName);
        // 写入上下班时间单元格内容
        System.out.println("开始写入上下班时间");
        LOGGER.info("开始写入上下班时间");
        //查找上报考勤模板里面的上下班时间单元格
        cell = findCell(sheet, templateProperties.getValue("ClockTimeKeyWord"));
        if (cell == null) {
            System.out.println("上下班时间单元格不存在，上报考勤任务异常终止");
            LOGGER.info("上下班时间单元格不存在，上报考勤任务异常终止");
            return;
        }
        //截取岗位信息字符串
        String str = cell.getStringCellValue();
        String clockTimeStr = str.substring(str.indexOf("上下班时间：") + 6);
        List<Post> postList = AttnReportService.analysis(clockTimeStr);
        clockTimeStr = "注：1、上下班时间：" + AttnReportService.createPostStr(postList, dr.getWithinPosts());
        cell.setCellValue(clockTimeStr);
        // 写入考勤人单元格内容
        System.out.println("开始写入考勤人");
        LOGGER.info("开始写入考勤人");
        //查找上报考勤模板里面的制表人单元格
        cell = findCell(sheet, templateProperties.getValue("AttnOfficerKeyWord"));
        if (cell == null) {
            System.out.println("考勤人单元格不存在，上报考勤任务异常终止");
            LOGGER.info("考勤人单元格不存在，上报考勤任务异常终止");
            return;
        }
        String footerStr = cell.getStringCellValue();
        AttnOfficer officer = DaoFactory.getInstance().getAttnOfficerDao().find(companyName, dr.getGroup().getName());
        String attendanceOfficer = officer == null ? "" : officer.getName();
        if (!footerStr.contains(attendanceOfficer))
            footerStr = footerStr + "、" + attendanceOfficer;
        cell.setCellValue(footerStr);
        // 把数据写入到文件
        System.out.println("开始把数据写入到文件");
        LOGGER.info("开始把数据写入到文件");
        FileOutputStream fileOut = new FileOutputStream(reportFile);
        wb.write(fileOut);
        fileOut.close();
        wb.close();
        System.out.println("上报考勤任务已完成");
        LOGGER.info("上报考勤任务已完成");
    }
}
