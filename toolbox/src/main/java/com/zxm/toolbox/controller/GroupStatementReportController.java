package com.zxm.toolbox.controller;

import com.zxm.toolbox.dao.impl.ColumnConfigDaoXmlImpl;
import com.zxm.toolbox.pojo.gt.ColumnProperties;
import com.zxm.toolbox.pojo.gt.GroupStatement;
import com.zxm.toolbox.service.GroupStatementLoadService;
import com.zxm.toolbox.service.GroupStatementReportService;
import com.zxm.toolbox.service.task.GroupStatementExportTask;
import com.zxm.toolbox.util.FxUtil;
import com.zxm.toolbox.util.MyOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GroupStatementReportController implements Initializable {
    @FXML private BorderPane root;
    @FXML private ProgressBar progress;
    @FXML private TextArea logArea;
    @FXML private TextField groupStatementPath;

    private File statementFile;

    public void openGroupTicketFile() {
        statementFile = FxUtil.chooseExcelFile(root, groupStatementPath, "GroupStatementFileDir");
        System.out.println(statementFile == null ? "未选取团体明细文件" : "团体明细文件:" + statementFile.getAbsolutePath());
    }

    public void export() {
        File resultFile = FxUtil.saveXlsxFile(root, "ResultFileDir");
        if (resultFile == null)
            return;
        // 原始团体记录List
        List<GroupStatement> records = GroupStatementLoadService.loadGroupStatementFile(statementFile);
        String message;
        if (records == null || records.isEmpty()) {
            message = "未能从团体记录文件读取到数据，任务已终止！";
            FxUtil.showWarningAlert(message);
            return;
        }
        List<ColumnProperties> colPropList = ColumnConfigDaoXmlImpl.findAll();
        if (colPropList.isEmpty()) {
            message = "无法找到团体报表文件表头配置信息，任务已终止！";
            FxUtil.showWarningAlert(message);
            return;
        }
        GroupStatementReportService.HEADERS = colPropList;
        progress.setVisible(true);
        GroupStatementExportTask<Void> task = new GroupStatementExportTask<>();
        task.setResultFile(resultFile);
        task.setRecords(records);
        progress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //将进度条隐藏
        progress.setVisible(false);
        //将System.out输出重定向到logArea
        MyOutputStream os = new MyOutputStream(logArea);
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        System.setErr(ps);
    }
}
