package com.zxm.toolbox.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.attn.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AttnCheckService {
    private static final Logger LOGGER = LogManager.getLogger(AttnCheckService.class);
    /**
     * 查找表格中指定列
     * @param sheet   sheet对象
     * @param rowNum  行号
     * @param colName 列名
     * @return 指定列的序号，序号从0开始，如果找不到指定列则返回-1
     */
    public static int findSpecialCol(Sheet sheet, int rowNum, String colName) {
        int specialColIndex = -1;
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            System.out.print("指定的行为空，找不到指定的列名");
            return specialColIndex;
        }
        int lastColNum = row.getLastCellNum();
        System.out.println("该行后一个单元格列号：" + lastColNum);
        for (int i = 0; i < lastColNum; i++) {
            Cell tempCell = row.getCell(i);
            if (tempCell == null)
                continue;
            if (tempCell.getStringCellValue().replaceAll(" ", "").equals(colName)) {
                specialColIndex = i;
                break;
            }
        }
        if (specialColIndex != -1)
            System.out.println("找到指定列名，列号为：" + specialColIndex);
        else
            System.out.println("未找到指定列名！");
        return specialColIndex;
    }

    /**
     * 从打卡记录数据中获取打卡记录的最后日期
     *
     * @param punchRecord 从打卡记录文件中读取的打卡记录数据
     * @return 打卡记录的最后日期
     */
    public static int getLastDay4PunchRecord(Map<Employee, List<LocalDateTime>> punchRecord) {
        LocalDateTime lastDate = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        for (Entry<Employee, List<LocalDateTime>> entry : punchRecord.entrySet()) {
            List<LocalDateTime> tempList = entry.getValue();
            for (LocalDateTime date : tempList) {
                if (date.isAfter(lastDate))
                    lastDate = date;
            }
        }
        return lastDate.getDayOfMonth();
    }

    /**
     * 核对考勤
     *
     * @param dutyRoster         考勤表对象
     * @param aaMap              打卡记录数据
     * @param faultToleranceTime 容错时间
     * @return 考勤核对结果列表
     */
    public static List<AttendanceResult> check(DutyRoster dutyRoster, Map<Employee, List<LocalDateTime>> aaMap,
                                               int faultToleranceTime) {
        System.out.println("开始核对考勤");
        List<Post> postList = dutyRoster.getWithinPosts();
        Map<String, Post> postMap;
        //将postList转换为postMap，Key为postName，Value为Post
        try {
            postMap = postList.stream().collect(Collectors.toMap(Post::getPostName, post -> post));
        } catch (IllegalStateException exception) {
            exception.printStackTrace();
            return null;
        }

        int year = dutyRoster.getYear();
        int month = dutyRoster.getMonth();
        int day;
        int hour;
        int minute;
        List<AttendanceResult> resultList = new ArrayList<>(dutyRoster.getEsList().size());
        String empName;
        Employee employee;
        List<String> schedule;
        AttendanceResult ar;
        Post post;
        String postName;
        LocalDateTime clockInTime;
        LocalDateTime clockOutTime;
        EmpDailyResult edr;
        List<EmpDailyResult> edrList;
        List<EmpSchedule> esList = dutyRoster.getEsList();
        for (EmpSchedule es : esList) {
            empName = es.getEmployee().getName();
            schedule = es.getSchedule();
//			System.out.println(name + "\t" + schedule);
            int days = schedule.size();
            // 初始化AttendanceResult存放被考勤人的考勤结果
            employee = DaoFactory.getInstance().getEmployeeDao().findByName(empName);
            //从配置文件查找员工信息。如果没有配置该员工信息，则核对结果的员工属性只填写名字。
            if(employee == null)
                ar = new AttendanceResult(new Employee(empName));
            else
                ar = new AttendanceResult(employee);

            edrList = new ArrayList<>();
            ar.setPppdResults(edrList);
            // 初始化核对结果默认初始化为忘记打卡
            // 休息日期的考勤结果为不需要打卡
            for (int index = 0; index < days; index++) {
                postName = schedule.get(index);
                day = index + 1;
                edr = new EmpDailyResult();
                edr.setPost(postName);
                post = postMap.get(postName);
                if (post.getClockInTime() == null) {
                    edr.setClockInType(EmpDailyResult.UNNEED_CLOCK);
                    edr.setClockOutType(EmpDailyResult.UNNEED_CLOCK);
                    edr.setClockInTime(null);
                    edr.setClockOutTime(null);
                    edr.setBusinessHour(null);
                } else {
                    edr.setClockInType(EmpDailyResult.NEED_CLOCK);
                    edr.setClockOutType(EmpDailyResult.NEED_CLOCK);
                    hour = Integer.parseInt(post.getClockInTime().substring(0, 2));
                    minute = Integer.parseInt(post.getClockInTime().substring(3, 5));
                    clockInTime = LocalDateTime.of(year, month, day, hour, minute, 0);
                    hour = Integer.parseInt(post.getClockOutTime().substring(0, 2));
                    minute = Integer.parseInt(post.getClockOutTime().substring(3, 5));
                    clockOutTime = LocalDateTime.of(year, month, day, hour, minute, 0);
                    edr.setClockInTime(clockInTime);
                    edr.setClockOutTime(clockOutTime);
                    edr.setBusinessHour(post.getClockInTime() + "-" + post.getClockOutTime());
                }
                edr.setPunchingCardRecord(new ArrayList<>());
                edrList.add(edr);
            }
//            for(EmpDailyResult e : edrList){
//                System.out.println(e.getPost() + "\t" + e.getClockInTime() + "\t" + e.getClockOutTime() + "\t" + e.getClockInType()  + "\t" + e.getClockOutType());
//            }
            List<LocalDateTime> acList = aaMap.get(new Employee(empName));
            // 打卡记录为空时将打卡记录全部设为无效打卡记录
            if (acList == null) {
                for (EmpDailyResult pr : edrList) {
                    pr.setClockInType(EmpDailyResult.INVALID);
                    pr.setClockOutType(EmpDailyResult.INVALID);
                }
                resultList.add(ar);
                continue;
            }
            // 打卡记录不为空地处理逻辑
            for (LocalDateTime actualTime : acList) {
                // 实际打卡时间
                day = actualTime.getDayOfMonth();
                // 获得对应日期的结果
                edr = ar.getPppdResults().get(day - 1);
                edr.getPunchingCardRecord().add(actualTime);
                // 对应日期的岗位的考勤时间表为空
                // 即当天为休息或者其他不用打卡的班
                // 有可能被考勤人上错班或者加班或者和别人换班但是考勤表没有更新
                if (edr.getBusinessHour() == null) {
                    edr.setClockInType(EmpDailyResult.UNNEED_BUT_HAVE);
                    continue;
                }
                // 初始化上班时间的极限值
                clockInTime = edr.getClockInTime();
                LocalDateTime inUpperLim = clockInTime.plusMinutes(faultToleranceTime);
                // 初始化下班时间的极限值
                clockOutTime = edr.getClockOutTime();
                LocalDateTime outLowerLim = clockOutTime.minusMinutes(faultToleranceTime);
                LocalDateTime midTime = clockInTime
                        .plusSeconds(Duration.between(clockInTime, clockOutTime).getSeconds() / 2);
//				System.out.println(post + "\t" + actualTime + "\t" + clockInTime + "\t" + inUpperLim + "\t" + clockOutTime + "\t" + outLowerLim + "\t" + midTime);
                int minutes;
                if (actualTime.isBefore(inUpperLim)) {
                    if (edr.getClockInType() != EmpDailyResult.NORMAL)
                        edr.setClockInType(EmpDailyResult.NORMAL);
                } else if (actualTime.isBefore(midTime)) {
                    if (edr.getClockInType() != EmpDailyResult.NORMAL) {
                        edr.setClockInType(EmpDailyResult.DELAYED);
                        minutes = (int) (Duration.between(clockInTime, actualTime).getSeconds() / 60);
                        if (edr.getClockInDuration() == 0 || minutes < edr.getClockInDuration()) {
                            edr.setClockInDuration(minutes);
                        }
                    }
                } else if (actualTime.isBefore(outLowerLim)) {
                    if (edr.getClockOutType() != EmpDailyResult.NORMAL) {
                        edr.setClockOutType(EmpDailyResult.ADVANCED);
                    }
                    minutes = (int) (Duration.between(actualTime, clockOutTime).getSeconds() / 60);
                    if (edr.getClockOutDuration() == 0 || minutes < edr.getClockOutDuration()) {
                        edr.setClockOutDuration(minutes);
                    }
                } else {
                    if (edr.getClockOutType() != EmpDailyResult.NORMAL)
                        edr.setClockOutType(EmpDailyResult.NORMAL);
                }
//				System.out.println(edr);
            }

            resultList.add(ar);
        }
        System.out.println("核对考勤完成");
        return resultList;
    }

    /**
     * 将考勤核对结果导出到Excel文件
     *
     * @param file       导出到此文件
     * @param resultList 考勤核对结果
     * @param year       考勤表年份
     * @param month      考勤表月份
     * @param lastDay    打卡记录的最后一天
     * @param dates      考勤抽查日期，如果参数为null，则全部日期都为抽查日期
     * @throws IOException 输入输出异常
     */
    public static void exportAttnChkResult(File file, List<AttendanceResult> resultList, int year, int month,
                                           int lastDay, List<Integer> dates) throws IOException {
        System.out.println("正在导出核对结果文件");
        LOGGER.debug("正在导出核对结果文件");
        XSSFWorkbook wb = new XSSFWorkbook();
        file.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(file);
        XSSFSheet sheet = wb.createSheet("Sheet1");

        int column = 0;
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(column++);
        cell.setCellValue("姓名");
        cell = row.createCell(column++);
        cell.setCellValue("日期");
        cell = row.createCell(column++);
        cell.setCellValue("上班异常情况");
        cell = row.createCell(column++);
        cell.setCellValue("下班异常情况");
        cell = row.createCell(column++);
        cell.setCellValue("考勤记录");
        cell = row.createCell(column++);
        cell.setCellValue("上班时间");
        cell = row.createCell(column++);
        cell.setCellValue("下班时间");
        cell = row.createCell(column);
        cell.setCellValue("打卡记录");
        Calendar c;
        CellStyle cellStyle;
        XSSFDataFormat df;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (resultList != null) {
            int rowNum = 1;
            for (AttendanceResult ar : resultList) {
                List<EmpDailyResult> result = ar.getPppdResults();
                if (dates == null) {
                    dates = new ArrayList<>();
                    for (int i = 1; i <= lastDay; i++) {
                        dates.add(i);
                    }
                }
                for (int date : dates) {
                    EmpDailyResult pr = result.get(date - 1);
                    column = 0;
                    if (pr.getClockInType() != EmpDailyResult.UNNEED_CLOCK
                            && (pr.getClockInType() != EmpDailyResult.NORMAL
                            || pr.getClockOutType() != EmpDailyResult.NORMAL)) {
                        row = sheet.createRow(rowNum);
                    } else {
                        continue;
                    }

                    cell = row.createCell(column++);
                    cell.setCellValue(ar.getEmployee().getName());

                    cell = row.createCell(column++);
                    c = Calendar.getInstance();
                    c.set(year, month - 1, date);
                    cellStyle = wb.createCellStyle();
                    df = wb.createDataFormat();
                    cellStyle.setDataFormat(df.getFormat("yyyy-MM-dd"));
                    cell.setCellValue(c);
                    cell.setCellStyle(cellStyle);

                    cell = row.createCell(column++);
                    if (pr.getClockInType() != EmpDailyResult.NORMAL) {
                        if (pr.getClockInType() == EmpDailyResult.DELAYED) {
                            cell.setCellValue("上班推迟" + pr.getClockInDuration() + "分钟打卡");
                        } else if (pr.getClockInType() == EmpDailyResult.NEED_CLOCK) {
                            cell.setCellValue("上班未打卡");
                        } else if (pr.getClockInType() == EmpDailyResult.UNNEED_BUT_HAVE) {
                            cell.setCellValue("无须打卡但有记录");
                        } else if (pr.getClockInType() == EmpDailyResult.INVALID) {
                            cell.setCellValue("无此员工的打卡记录");
                            rowNum++;
                            break;
                        }
                    }
                    cell = row.createCell(column++);
                    if (pr.getClockOutType() != EmpDailyResult.NORMAL) {
                        if (pr.getClockOutType() == EmpDailyResult.NEED_CLOCK) {
                            cell.setCellValue("下班未打卡");
                        } else if (pr.getClockOutType() == EmpDailyResult.ADVANCED) {
                            cell.setCellValue("下班提前" + pr.getClockOutDuration() + "分钟打卡");
                        }
                    }
                    // 导出考勤记录到单元格
                    cell = row.createCell(column++);
                    cell.setCellValue(pr.getPost());

                    // 导出上班时间到单元格
                    cell = row.createCell(column++);
                    if (pr.getClockInTime() != null) {
                        cell.setCellValue(pr.getClockInTime().toLocalTime().format(timeFormatter));
                    }
                    // 导出下班时间到单元格
                    cell = row.createCell(column++);
                    if (pr.getClockOutTime() != null) {
                        cell.setCellValue(pr.getClockOutTime().toLocalTime().format(timeFormatter));
                    }
                    if (pr.getPunchingCardRecord() != null) {
                        for (LocalDateTime record : pr.getPunchingCardRecord()) {
                            cell = row.createCell(column++);
                            cell.setCellValue(record.format(dateTimeFormatter));
                        }
                    }
                    rowNum++;
                }
            }
        }
        wb.write(fileOut);
        fileOut.close();
        wb.close();
        System.out.println("考勤核对结果文件已导出成功！");
    }

}
