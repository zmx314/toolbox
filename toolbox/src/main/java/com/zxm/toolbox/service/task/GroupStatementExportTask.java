package com.zxm.toolbox.service.task;

import com.zxm.toolbox.MainApplication;
import com.zxm.toolbox.pojo.gt.*;
import com.zxm.toolbox.service.GroupStatementReportService;
import com.zxm.toolbox.service.GroupStatementService;
import com.zxm.toolbox.util.ExceptionUtils;
import com.zxm.toolbox.util.FxUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GroupStatementExportTask<Void> extends Task<Void> {
    private static final Logger logger = LogManager.getLogger(GroupStatementExportTask.class);
    private File resultFile;
    private List<GroupStatement> records;
    private String message;

    public void setResultFile(File resultFile) {
        this.resultFile = resultFile;
    }

    public void setRecords(List<GroupStatement> records) {
        this.records = records;
    }

    @Override
    public Void call() {
        System.out.println("正在导出核对结果文件");
        logger.debug("正在导出核对结果文件");
        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            resultFile.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(resultFile);
            wb.write(fileOut);
            fileOut.close();
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        int progress = 0;
        int max = 11;
        updateProgress(progress, max);
        //Task处理逻辑开始
        try {
            // 团体记录按团体号排序
            records.sort(new GroupStatementIdComparator());
            updateProgress(++progress, max);
            // 团体记录合并，因为一个团体分为几条记录（成人、领队、小童），团体号一样的即为一个团体
            GroupStatementService.mergeStatementRecord(records);
            updateProgress(++progress, max);
            // 根据关键词，修改团体记录里面的旅行社名字
            GroupStatementService.modifyGroupName(records);
            updateProgress(++progress, max);
            List<String> withoutCoNames = new ArrayList<>();
            // 统计包含的航线组，同时检测是否有未配置的航线
            List<ShippingGroup> withinGroups = GroupStatementService.countGroup(records, withoutCoNames);
            if (withoutCoNames.size() != 0) {
                message = "以下航线未在系统中进行相关信息的配置，\n"
                        + "未配置的航线:" + withoutCoNames;
                FxUtil.showWarningAlert(message);
                return null;
            }
            System.out.println("团体记录中包含的分组有：");
            for (ShippingGroup group : withinGroups) {
                System.out.println(group.getName());
            }
            // 校验检测到的分组的配置信息是否完整，如果不完整，则无法继续生成报表，会自动终止生成报表任务
            for (ShippingGroup group : withinGroups) {
                if (!GroupStatementService.isGroupCompleted(group)) {
                    message = "“" + group.getName() + "”分组的配置信息不完整，生成报表任务已终止！";
                    FxUtil.showWarningAlert(message);
                    return null;
                }
            }
            updateProgress(++progress, max);
            withinGroups.sort(new SGIdComparator());
            List<GroupStatement> selectedRecord,departRecords,returnRecords;
            List<RoundTripGroup> roundRecords;
            // 错误团体记录的List
            List<GroupStatement> wrongRecords = new ArrayList<>();
            String sheetName;
            // 从团体记录里面读取团体所属年份和月份，用来生成工作表名称
            LocalDateTime c = GroupStatementService.getStartDate(records);
            int year = c.getYear();
            int month = c.getMonthValue();
            for (ShippingGroup sg : withinGroups) {
                sheetName = year + String.format("%02d", month) + sg.getName();
                selectedRecord = GroupStatementService.selectOneShippingGroup(records, sg.getName());
                if (sg.getType().equals("单去"))
                    GroupStatementReportService.exportDepartOnly(resultFile, sheetName, selectedRecord, sg);
                if (sg.getType().equals("单去+单回"))
                    GroupStatementReportService.exportDepartAndReturn(resultFile, sheetName, selectedRecord, sg);
                if (sg.getType().equals("来回")) {
                    // 来回团体记录的List
                    roundRecords = new ArrayList<>();
                    GroupStatementService.separateRecords(selectedRecord, roundRecords, wrongRecords, sg.getName());
                    GroupStatementReportService.exportRoundOnly(resultFile, sheetName, roundRecords, sg);
                }
                if (sg.getType().equals("单去+单回+来回")) {
                    // 去程团体记录的List
                    departRecords = new ArrayList<>();
                    // 回程团体记录的List
                    returnRecords = new ArrayList<>();
                    // 来回程团体记录List
                    roundRecords = new ArrayList<>();
                    GroupStatementService.separateRecords(
                            selectedRecord, roundRecords, departRecords, returnRecords, sg.getName());
                    GroupStatementReportService.exportDepartReturnRound(
                            resultFile, roundRecords, departRecords, returnRecords, sheetName, sg);
                }
                updateProgress(++progress, max);
            }
            //生成异常团体记录工作表
            if (!wrongRecords.isEmpty())
                GroupStatementReportService.exportReportEG(resultFile, wrongRecords);
            message = "团体报表导出成功！";
            updateProgress(max, max);
            FxUtil.showWarningAlert(message);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionUtils.logStackTrace(e, logger);
            message = "生成报表过程中发生异常，任务已终止！";
            FxUtil.showWarningAlert(message);
        }
        return null;
    }
}
