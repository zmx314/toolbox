package com.zxm.toolbox.service;

import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.attn.*;
import com.zxm.toolbox.resources.MyProperties;
import com.zxm.toolbox.resources.PropertiesFactory;
import com.zxm.toolbox.util.PoiUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AttnLoadService {
    private static final Logger LOGGER = LogManager.getLogger(AttnLoadService.class);

    /**
     * 从打卡记录文件读取打卡记录
     *
     * @param punchRecordFile 打卡记录文件
     * @param filter          过滤器
     * @return Map对象, Key考勤人姓名，Value考勤人的打卡记录
     */
    public static Map<Employee, List<LocalDateTime>> loadPunchRecord(File punchRecordFile,
                                                                     Pram4LoadPunchRecord filter) {
        System.out.println("开始读取打卡记录文件");
        // 用来存放读取到的数据
        Map<Employee, List<LocalDateTime>> resultMap;
        Sheet sheet = PoiUtils.loadSheet(punchRecordFile);
        // 没有成功读取到工作表
        if (sheet == null)
            return new HashMap<>();
        Row row;
        Cell cell;
        String name;
        Employee employee;
        int nameColIdx = filter.getNameColIndex();
        int dateTimeColIdx = filter.getDateTimeColIndex();
        LocalDateTime lowerLimDate = LocalDateTime.of(filter.getYear(), filter.getMonth(), 1, 0, 0, 0);
//		System.out.println("LowerLimDate:" + lowerLimDate);
        LocalDateTime upperLimDate = lowerLimDate.plusMonths(1);
//		System.out.println("UpperLimDate:" + upperLimDate);
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter df;
        LocalDateTime date;
        // 获取工作表最大行数
        int rowsCount = sheet.getLastRowNum();
//		System.out.println("LastRowNum:\t" + rowsCount+1);
        if (rowsCount == 0)
            return new HashMap<>();
        resultMap = new HashMap<>(64);
        df = DateTimeFormatter.ofPattern(pattern);
        // 读取数据
        for (int r = 3; r < rowsCount + 1; r++) {
            row = sheet.getRow(r);
            //行为空则终止循环
            if (row == null)
                break;
            cell = row.getCell(nameColIdx);
            //姓名单元格为空则终止循环
            if (cell == null)
                break;
            name = cell.getStringCellValue();
            employee = new Employee(name);
            cell = row.getCell(dateTimeColIdx);
            //打卡时间单元格为空则加载下一行
            if (cell == null)
                continue;
            String dateStr = cell.getStringCellValue();
            date = LocalDateTime.parse(dateStr, df);
            if (date.isBefore(lowerLimDate) || date.isAfter(upperLimDate)) {
                continue;
            }
            if (!resultMap.containsKey(employee)) {
                resultMap.put(employee, new ArrayList<>());
            }
//			System.out.println("row num:\t" + r + "\t" + employee.getName() + "\t" + date);
            resultMap.get(employee).add(date);
        }
        System.out.println("成功读取打卡记录文件");
        LOGGER.info("成功读取打卡记录文件");
        return resultMap;
    }

    /**
     * 从考勤表文件加载考勤年月
     *
     * @param sheet 工作表
     * @param group 分组对象
     * @return Calendar对象封装了年月数据
     */
    public static LocalDate loadDate4DutyRoster(Sheet sheet, Group group) {
        Cell cell = PoiUtils.loadSpecialCell(sheet, group.getDateCell());
        if (cell == null)
            return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            Date date = cell.getDateCellValue();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }

    /**
     * 从考勤表读取考勤员工记录
     *
     * @param drSheet 从考勤表文件读取获得的Sheet对象
     * @param group   分组类型
     * @param days    天数
     * @return 员工考勤计划List
     */
    public static List<EmpSchedule> loadEsList(Sheet drSheet, Group group, int days) {
        LOGGER.info("开始从考勤表读取考勤记录");
        MyProperties props = PropertiesFactory.getInstance().getDutyRosterProps();
        // 用来存放总的数据
        List<EmpSchedule> esList = new ArrayList<>();
        Row row;
        Cell cell;
        // 读取数据
        CellAddress cellAddress = new CellAddress(group.getStartCell());
        int startRowNum = cellAddress.getRow();
        int startColNum = cellAddress.getColumn();
        String name;
        String tempStr;
        Employee employee;
        // 先读取非合并单元格
        for (int r = startRowNum; r < drSheet.getLastRowNum(); r++) {
            row = drSheet.getRow(r);
            if (row == null)
                break;
            cell = row.getCell(startColNum);
            if (cell == null)
                break;
            if (cell.getCellType() != CellType.STRING)
                break;
            //去除名字里面的空格
            name = cell.getStringCellValue().replaceAll(" ", "");
            if (name.equals(""))
                break;
            employee = DaoFactory.getInstance().getEmployeeDao().findByName(name);
            //因为考勤表涉及到后面的考勤上报，所有员工必须配置相关信息，如果没有配置相关信息，则自动终读取任务
            if (employee == null) {
                System.out.println("读取考勤数据任务在" + name + "行终止");
                break;
            }
            EmpSchedule es = new EmpSchedule(employee);

            for (int c = startColNum + 1, length = startColNum + 1 + days; c < length; c++) {
                cell = row.getCell(c);
                if (cell == null)
                    es.getSchedule().add("空");
                else if (cell.getCellType() != CellType.STRING)
                    es.getSchedule().add("空");
                else {
                    tempStr = cell.getStringCellValue()
                            .replaceAll(" ", "")
                            .replace("\t", "")
                            .replace("\n", "")
                            .replace("\r", "");
                    if (tempStr.equals(""))
                        es.getSchedule().add("空");
                    else
                        es.getSchedule().add(tempStr);
                }

            }
            //读取出勤小时数
            tempStr = props.getValue("DutyTime") + 1;
            cell = row.getCell(new CellAddress(tempStr).getColumn());
            es.setDutyTime(cell.getNumericCellValue());
            //读取加班费小时数
            tempStr = props.getValue("ExtraTime") + 1;
            cell = row.getCell(new CellAddress(tempStr).getColumn());
            es.setExtraTime(cell.getNumericCellValue());
            //读取本月存假
            tempStr = props.getValue("CompensatoryTime") + 1;
            cell = row.getCell(new CellAddress(tempStr).getColumn());
            es.setCompensatoryTime(cell.getNumericCellValue());
            //读取本月累计存假
            tempStr = props.getValue("CompensatoryLeft") + 1;
            cell = row.getCell(new CellAddress(tempStr).getColumn());
            es.setCompensatoryLeft(cell.getNumericCellValue());
            //读取工龄假剩余天数
            tempStr = props.getValue("AnnualLeaveLeft") + 1;
            cell = row.getCell(new CellAddress(tempStr).getColumn());
            es.setAnnualLeaveLeft(cell.getNumericCellValue());
            //读取育儿假剩余天数
            tempStr = props.getValue("ParentLeaveLeft") + 1;
            cell = row.getCell(new CellAddress(tempStr).getColumn());
            es.setParentalLeaveLeft(cell.getNumericCellValue());
            //读取护理假剩余天数
            tempStr = props.getValue("NursingLeaveLeft") + 1;
            cell = row.getCell(new CellAddress(tempStr).getColumn());
            es.setNursingLeaveLeft(cell.getNumericCellValue());
            esList.add(es);
        }
        System.out.println("考勤记录已成功读取");
        return esList;
    }

    /**
     * 获取单一模式下的岗位信息子表的标志单元格的位置
     *
     * @param sheet sheet对象
     * @return list对象。行号，“出勤”列号，“上班时间”列号，“工时”列号，“休息”列号
     */
    public static List<Integer> getOffsets(Sheet sheet) {
        System.out.println("开始自动获取偏移值");
        MyProperties props = PropertiesFactory.getInstance().getDutyRosterProps();
        String tempStr;
        List<Integer> list = new ArrayList<>();
        int lastRowNum = sheet.getLastRowNum();
        Row tempRow;
        Cell cell;
        int lastColNum;
        for (int i = 0; i < lastRowNum; i++) {
            tempStr = props.getValue("Header_DutyPostName");
            tempRow = sheet.getRow(i);
            if (tempRow == null)
                continue;
            lastColNum = tempRow.getLastCellNum();
            for (int j = 0; j < lastColNum; j++) {
                cell = tempRow.getCell(j);
                if (cell != null && cell.getCellType() == CellType.STRING
                        && cell.getStringCellValue().equals(tempStr)) {
                    System.out.println("“出勤单元格定位成功”,地址：" + cell.getAddress());
                    LOGGER.info("“出勤单元格定位成功”,地址：" + cell.getAddress());
                    list.add(i);
                    list.add(j);
                    break;
                }
            }
            //没有找到标志的地址，重置后继续查找下一行
            if (list.size() != 2) {
                list.clear();
                continue;
            }

            tempStr = props.getValue("Header_DutyPostClockTime");
            for (int j = list.get(1); j < lastColNum; j++) {
                cell = tempRow.getCell(j);
                if (cell != null && cell.getCellType() == CellType.STRING
                        && cell.getStringCellValue().equals(tempStr)) {
                    System.out.println("“上班时间”单元格定位成功，地址：" + cell.getAddress());
                    LOGGER.info("“上班时间”单元格定位成功，地址：" + cell.getAddress());
                    list.add(j);
                    break;
                }
            }
            //没有找到标志的地址，重置后继续查找下一行
            if (list.size() != 3) {
                list.clear();
                continue;
            }

            tempStr = props.getValue("Header_DutyPostManHour");
            for (int j = list.get(2); j < lastColNum; j++) {
                cell = tempRow.getCell(j);
                if (cell != null && cell.getCellType() == CellType.STRING
                        && cell.getStringCellValue().equals(tempStr)) {
                    System.out.println("“工时”单元格定位成功，地址：" + cell.getAddress());
                    LOGGER.info("“工时”单元格定位成功，地址：" + cell.getAddress());
                    list.add(j);
                    break;
                }
            }
            //没有找到标志的地址，重置后继续查找下一行
            if (list.size() != 4) {
                list.clear();
                continue;
            }

            tempStr = props.getValue("Header_OffPostName");
            for (int j = list.get(3); j < lastColNum; j++) {
                cell = tempRow.getCell(j);
                if (cell != null && cell.getCellType() == CellType.STRING
                        && cell.getStringCellValue().equals(tempStr)) {
                    System.out.println("“休息”单元格定位成功，地址：" + cell.getAddress());
                    LOGGER.info("“休息”单元格定位成功，地址：" + cell.getAddress());
                    list.add(j);
                    break;
                }
            }
            //没有找到标志的地址,
            if (list.size() != 5) {
                System.out.println("自动获取偏移值失败");
                LOGGER.info("自动获取偏移值失败");
                list.clear();
            } else {
                System.out.println("自动获取偏移值成功");
                LOGGER.info("自动获取偏移值成功");
                break;
            }
        }
        return list;
    }

    /**
     * 从考勤表加载岗位信息
     *
     * @param sheet 工作表对象
     * @param group 分组对象
     * @return List<Post> 岗位列表
     */
    public static List<Post> loadPosts(Sheet sheet, Group group) {
        System.out.println("开始从考勤表文读取岗位数据");
        LOGGER.info("开始从考勤表文读取岗位数据");
        List<Post> result = new ArrayList<>();
        int rowIndex = group.getDutyCell().getRow();
        int colIndex = group.getDutyCell().getColumn();
        int clockColIndex = group.getTimeCell().getColumn();
        int manHourColIndex = group.getManHourCell().getColumn();
        Row row;
        Cell nameCell;
        String nameStr;
        Cell clockCell;
        String clockStr;
        Cell manHourCell;
        String clockInTime;
        String clockOutTime;
        Post tempPost;
        //读取出勤的岗位
        for (int i = rowIndex; i < sheet.getLastRowNum(); i++) {
            clockInTime = null;
            clockOutTime = null;
            // 岗位名称为空时终止循环
            row = sheet.getRow(i);
            //此行为null，则终止
            if (row == null)
                break;
            nameCell = row.getCell(colIndex);
            //岗位名单元格为null，则终止
            if (nameCell == null)
                break;
            //岗位名称单元格不是文本类型，则终止
            if (nameCell.getCellType() != CellType.STRING)
                break;
            //岗位名称单元格为空字符串或者为结束标志字符串时，读取结束，终止循环
            nameStr = nameCell.getStringCellValue();
            if (nameStr.equals(""))
                break;
            clockCell = row.getCell(clockColIndex);
            //上班时间单元格为null，或者不是字符串类型，则终止
            if (clockCell == null || clockCell.getCellType() != CellType.STRING) {
                System.out.println("单元格地址：" + new CellAddress(i, clockColIndex));
                System.out.println("格式要求：文本类型");
                result.add(new Post("异常终止"));
                return result;
            }
            clockStr = clockCell.getStringCellValue();
            if (!clockStr.equals("")) {
                //对上班时间字符串进行简单的替换处理，“--”替换为“-”，“：”替换为“:”
                if (clockStr.contains("--")) {
                    clockStr = clockStr.replaceFirst("--", "-");
                }
                if (clockStr.contains("：")) {
                    clockStr = clockStr.replaceAll("：", ":");
                }
                //上班时间字符串不满足格式要求，则终止
                if (!clockStr.matches("([0-1][0-9]|2[0-3]):([0-5][0-9])-([0-1][0-9]|2[0-3]):([0-5][0-9])")) {
                    System.out.println("上班时间字符串不满足格式要求，单元格地址：" + clockCell.getAddress());
                    System.out.println("1、上班时间和下班时间都使用长时间即4位数字时间");
                    System.out.println("2、上班时间和下班时间用“-”连接");
                    result.add(new Post("异常终止"));
                    return result;
                }
                clockInTime = clockStr.substring(0, 5);
                clockOutTime = clockStr.substring(clockStr.length() - 5);
            }
            manHourCell = row.getCell(manHourColIndex);
            //工时单元格为null，或者不是数值类型，则终止
            if (manHourCell == null || manHourCell.getCellType() != CellType.NUMERIC) {
                System.out.println("单元格地址：" + new CellAddress(i, manHourColIndex));
                System.out.println("格式要求：数值类型");
                result.add(new Post("异常终止"));
                return result;
            }
            String[] names = nameStr.split("/");
            for (String name : names) {
                tempPost = new Post();
                tempPost.setPostName(name);
                tempPost.setClockInTime(clockInTime);
                tempPost.setClockOutTime(clockOutTime);
                tempPost.setManHour(manHourCell.getNumericCellValue());
                tempPost.setIsDuty(true);
                result.add(tempPost);
            }
        }
        // 读取休息的岗位
        rowIndex = group.getOffCell().getRow();
        colIndex = group.getOffCell().getColumn();
        for (int i = rowIndex; i < sheet.getLastRowNum(); i++) {
            // 岗位名称为空时终止循环
            row = sheet.getRow(i);
            //此行为null，则终止
            if (row == null)
                break;
            nameCell = row.getCell(colIndex);
            //岗位名单元格为null，则终止
            if (nameCell == null)
                break;
            //岗位名称单元格不是文本类型，则终止
            if (nameCell.getCellType() != CellType.STRING) {
                break;
            }
            nameStr = nameCell.getStringCellValue();
            if (nameStr.equals(""))
                break;
            tempPost = new Post();
            tempPost.setPostName(nameStr);
            tempPost.setIsDuty(false);
            result.add(tempPost);
        }
        System.out.println("从考勤表读取岗位数据已完成");
        LOGGER.info("从考勤表读取岗位数据已完成");
        return result;
    }
}
