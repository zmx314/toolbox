package com.zxm.toolbox.controller;

import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.exception.attn.DuplicatePostException;
import com.zxm.toolbox.pojo.attn.*;
import com.zxm.toolbox.resources.MyProperties;
import com.zxm.toolbox.resources.PropertiesFactory;
import com.zxm.toolbox.service.AttnCheckService;
import com.zxm.toolbox.service.AttnLoadService;
import com.zxm.toolbox.service.AttnReportService;
import com.zxm.toolbox.util.ExceptionUtils;
import com.zxm.toolbox.util.FxUtil;
import com.zxm.toolbox.util.MyOutputStream;
import com.zxm.toolbox.util.PoiUtils;
import com.zxm.toolbox.vo.ui.IndexTableCell;
import com.zxm.toolbox.vo.ui.MyStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class AttnRptController implements Initializable {
    private static final Logger logger = LogManager.getLogger(AttnRptController.class);
    @FXML private GridPane root;
    @FXML private ComboBox<Group> groupComboBox;
    @FXML private CheckBox smartModeChkBox;
    @FXML private TextField punchRecordFilePath;
    @FXML private TextField dutyRosterFilePath;
    @FXML private ComboBox<String> dutyRosterSheet;
    @FXML private Button importAttnDataBtn;
    @FXML private Button checkBtn;
    @FXML private Button exportResultBtn;
    @FXML private TextField attnReportFilePath;
    @FXML private ComboBox<String> reportSheetComboBox;
    @FXML private ComboBox<String> companyComboBox;
    @FXML private Button attnRptBtn;
    @FXML private RadioButton newModeRad;
    @FXML private RadioButton additionalModeRad;
    @FXML private TextArea logArea;
    @FXML private TableView<Post> withinPostTable;
    @FXML private TableColumn<Post, String> withinPostIdxCol;
    @FXML private TableColumn<Post, String> withinPostNameCol;
    @FXML private TableColumn<Post, String> withinPostClockInCol;
    @FXML private TableColumn<Post, String> withinPostClockOutCol;
    @FXML private TableColumn<Post, Boolean> withinPostDutyCol;
    @FXML private TableColumn<Post, Double> withinPostManHourCol;
    @FXML private TableView<Post> withoutPostTable;
    @FXML private TableColumn<Post, String> withoutPostIdxCol;
    @FXML private TableColumn<Post, String> withoutPostNameCol;
    private File prFile;
    private File drFile;
    private Map<Employee, List<LocalDateTime>> punchRecord;
    private DutyRoster dutyRoster;
    private Workbook drWorkbook = null;
    private int lastDay;
    private List<AttendanceResult> resultList;
    private File reportFile;
    private String message;

    public AttnRptController() {
    }

    public void openPunchRecordFile() {
        prFile = FxUtil.chooseExcelFile(root, punchRecordFilePath, "PunchRecordFileDir");
        if (prFile == null) {
            checkBtn.setDisable(true);
            exportResultBtn.setDisable(true);
        } else {
            if(dutyRoster != null)
                checkBtn.setDisable(false);
        }
    }

    public void openDutyRosterFile() {
        dutyRoster = null;
        drFile = FxUtil.chooseExcelFile(root, dutyRosterFilePath, "DutyRosterFileDir");
        if (drFile == null) {
            //考勤表文件为空时，导入数据、核对考勤、上报考勤按钮设置为不可用
            importAttnDataBtn.setDisable(true);
            checkBtn.setDisable(true);
            exportResultBtn.setDisable(true);
            return;
        } else {
            ObservableList<Group> groups = groupComboBox.getItems();
            for (Group group : groups) {
                if (drFile.getName().contains(group.getName())) {
                    Platform.runLater(() -> groupComboBox.getSelectionModel().select(group));
                    break;
                }
            }
        }
        try {
            drWorkbook = PoiUtils.loadWorkbook(drFile);
            new Thread(() -> {
                List<String> names;
                names = PoiUtils.loadSheetNames(drWorkbook);
                Platform.runLater(() -> {
                    dutyRosterSheet.getItems().clear();
                    dutyRosterSheet.setItems(FXCollections.observableList(names));
                    //读取到考勤表的工作表名字列表后，导入数据按钮设置为可用
                    importAttnDataBtn.setDisable(false);
                });
            }).start();

        } catch (OLE2NotOfficeXmlFileException e1) {
            System.out.println("文件为加密文件，请先删除密码！");
            message = "读取文件失败，原因：\n文件为加密文件，请先去除密码！";
            FxUtil.showWarningAlert(message);
        }
    }

    public void importAttnData() {
        withinPostTable.getItems().clear();
        withoutPostTable.getItems().clear();
        dutyRoster = new DutyRoster();
        dutyRoster.setGroup(groupComboBox.getSelectionModel().getSelectedItem());
        Group group = dutyRoster.getGroup();
        Sheet drSheet = PoiUtils.loadSheet(drFile, dutyRosterSheet.getSelectionModel().getSelectedItem());
        if (drSheet == null) {
            System.out.println("所选工作表为空，导入数据失败");
            return;
        }
        // 从考勤表年元单元格读取年元参数
        LocalDate date = AttnLoadService.loadDate4DutyRoster(drSheet, group);
        if (date == null) {
            message = "导入数据失败，无法继续导入，原因：\n" +
                    "从考勤表文件“月份单元格”内读取数据时发生异常，“月份单元格”单元格内数据异常，“月份单元格”地址：" + group.getDateCell();
            FxUtil.showWarningAlert(message);
            return;
        }
        int year = date.getYear();
        int month = date.getMonthValue();
        dutyRoster.setYear(year);
        dutyRoster.setMonth(month);
        // 根据年月参数获取该年月的最大天数,用来校验和读取考勤记录区域
        int days = date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        System.out.println("考勤表年月：" + year + "年" + month + "月");
        // 读取考勤表文件数据
        List<EmpSchedule> esList = AttnLoadService.loadEsList(drSheet, group, days);
        if (esList.size() == 0) {
            message = "读取文件失败，原因：\n未能从考勤表文件中读取到考勤记录的数据，" +
                    "请检查读取考勤记录文件的相关参数是否设置正确，导入数据已终止！";
            FxUtil.showWarningAlert(message);
            return;
        }
        dutyRoster.setEsList(esList);
        System.out.println("读取到以下" + dutyRoster.getEsList().size() + "位员工的考勤记录:");
        for (EmpSchedule es : dutyRoster.getEsList()) {
            System.out.println(es.getEmployee().getName());
        }
        //统计考勤表里面包含的所有岗位
        dutyRoster.countPostsInSchedule();
        List<Integer> list;
        if (smartModeChkBox.isSelected()) {
            //自动获取偏移值
            list = AttnLoadService.getOffsets(drSheet);
            if (list.isEmpty()) {
                message = "读取文件失败，原因：\n智能模式下获取偏移值失败，关键词不匹配," +
                        "请修改配置文件中的关键词或者取消选择半智能模式,核对过程已终止！";
                FxUtil.showWarningAlert(message);
                return;
            }
            group.setDutyCell(new CellAddress(list.get(0) + 1, list.get(1)));
            group.setTimeCell(new CellAddress(list.get(0) + 1, list.get(2)));
            group.setManHourCell(new CellAddress(list.get(0) +1,list.get(3)));
            group.setOffCell(new CellAddress(list.get(0) + 1, list.get(4)));
        }

        List<Post> withinPosts = new ArrayList<>(64);
        List<Post> tempPosts;
        List<String> postsInSch = dutyRoster.getPostsInSchedule();
        tempPosts = AttnLoadService.loadPosts(drSheet, group);
        if (tempPosts.get(tempPosts.size() - 1).getPostName().equals("异常终止")) {
            message = "读取文件失败，原因：\n加载考勤表内岗位配置信息时发现异常，请在日志窗口中查看错误详细信息，核对过程已终止！";
            FxUtil.showWarningAlert(message);
            return;
        }
        //过滤读取的岗位信息，把考勤表里面不包含的岗位信息过滤掉
        for (Post p : tempPosts) {
            if (postsInSch.contains(p.getPostName())) {
                withinPosts.add(p);
            }
        }
        dutyRoster.setWithinPosts(withinPosts);
        withinPostTable.setItems(FXCollections.observableList(withinPosts));
        dutyRoster.countWithoutPost();
        List<Post> tempList = new ArrayList<>();
        for (String s : dutyRoster.getWithoutPosts()) {
            tempList.add(new Post(s));
        }
        withoutPostTable.setItems(FXCollections.observableList(tempList));
        if (prFile != null)
            checkBtn.setDisable(false);
    }

    public void check() {
        if (prFile == null) {
            message = "请选择打卡记录文件！";
            FxUtil.showWarningAlert(message);
            return;
        }
        if (withoutPostTable.getItems().size() != 0) {
            message = "考勤表中有未配置的岗位，请先配置好所有岗位再核对考勤！";
            FxUtil.showWarningAlert(message);
            return;
        }
        Sheet prSheet = PoiUtils.loadSheet(prFile);
        if (prSheet == null) {
            message = "读取打卡记录文件时发生异常，核对考勤任务已终止！";
            FxUtil.showWarningAlert(message);
            return;
        }
        new Thread(() -> {
            try {
                //打卡记录文件中姓名列号，Excel文件的列号从0开始
                int nameColIndex = AttnCheckService.findSpecialCol(prSheet, 2, "姓名");
                //打卡记录文件中打卡时间列号，Excel文件的列号从0开始
                int dateTimeColIndex = AttnCheckService.findSpecialCol(prSheet, 2, "打卡时间");

                Pram4LoadPunchRecord para = new Pram4LoadPunchRecord();
                para.setYear(dutyRoster.getYear());
                para.setMonth(dutyRoster.getMonth());
                para.setNameColIndex(nameColIndex);
                para.setDateTimeColIndex(dateTimeColIndex);
                // 读取打卡记录文件内的数据
                punchRecord = AttnLoadService.loadPunchRecord(prFile, para);
                lastDay = AttnCheckService.getLastDay4PunchRecord(punchRecord);
                System.out.println("打卡记录最后记录的日期：" + lastDay + "日");
                MyProperties settingsProps = PropertiesFactory.getInstance().getSettingProps();
                int faultToleranceTime = Integer.parseInt(settingsProps.getValue("FaultToleranceTime"));
                System.out.println("容错时间：" + faultToleranceTime + "分钟");
                // 核对考勤并把核对的结果存储到List对象内
                resultList = AttnCheckService.check(dutyRoster, punchRecord, faultToleranceTime);
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(()->FxUtil.showErrorAlert(message));
            }

        }).start();
        exportResultBtn.setDisable(false);
    }

    public void exportResult() {
        if (resultList == null || dutyRoster == null || resultList.isEmpty()) {
            message = "核对结果数据为空，无可导出数据！";
            FxUtil.showWarningAlert(message);
            return;
        }
        File resultFile = FxUtil.saveXlsxFile(root, "ResultFileDir");
        if (resultFile == null)
            return;
        new Thread(() -> {
            try {
                AttnCheckService.exportAttnChkResult(resultFile, resultList, dutyRoster.getYear(), dutyRoster.getMonth(), lastDay, null);
                FxUtil.showInfoAlert("考勤核对结果导出成功！");
            } catch (IOException writeIOException) {
                writeIOException.printStackTrace();
                FxUtil.showErrorAlert("导出文件时发生输入输出异常，核对结果导出失败！");
            }
        }).start();
    }

    public void openReportFile() {
        attnRptBtn.setDisable(true);
        reportFile = FxUtil.chooseExcelFile(root, attnReportFilePath, "ReportFileDir");
        if (reportFile == null)
            return;
        attnRptBtn.setDisable(false);
        if (reportFile.getName().contains("港发"))
            Platform.runLater(() -> companyComboBox.getSelectionModel().select("港发"));
        if (reportFile.getName().contains("客服"))
            Platform.runLater(() -> companyComboBox.getSelectionModel().select("客服"));
        new Thread(() -> {
            List<String> names;
            try {
                names = PoiUtils.loadSheetNames(reportFile);
            } catch (OLE2NotOfficeXmlFileException e) {
                message = "读取文件失败，原因：\n文件为加密文件，请先去除密码！";
                FxUtil.showWarningAlert(message);
                return;
            }
            Platform.runLater(() -> reportSheetComboBox.setItems(FXCollections.observableList(names)));
        }).start();

    }

    public void reportBtn() {
        if (reportFile == null) {
            FxUtil.showWarningAlert("请先选择上报考勤文件！");
            return;
        }
        if (dutyRoster.getEsList() == null) {
            FxUtil.showWarningAlert("请先导入考勤表数据！");
            return;
        }
        if (withoutPostTable.getItems().size() != 0) {
            FxUtil.showWarningAlert("考勤表中有未配置的岗位，请先配置好所有岗位再上报考勤！");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认信息");
        alert.setHeaderText(null);
        alert.setContentText("请确认上报的“工作表”和“上报模式”是否正确？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.CANCEL)
            return;
        // 数据统计模块
        try {
            for (EmpSchedule es : dutyRoster.getEsList()) {
                es.count(dutyRoster.getWithinPosts());
            }
        } catch (DuplicatePostException e) {
            FxUtil.showWarningAlert("岗位名发生冲突，任务已终止！\n" + e.getMessage());
        }
        String sheetName = reportSheetComboBox.getSelectionModel().getSelectedItem();
        String companyName = companyComboBox.getSelectionModel().getSelectedItem();
        try {
            if (additionalModeRad.isSelected())
                AttnReportService.additionalReport(dutyRoster, reportFile, sheetName, companyName);
            else
                AttnReportService.newReport(reportFile, sheetName, dutyRoster, companyName);
            FxUtil.showInfoAlert("上报考勤已完成！");
        } catch (FileNotFoundException e) {
            ExceptionUtils.logStackTrace(e, logger);
            message = "上报考勤过程发生异常，任务已终止！" + "\n" +
                    "异常信息：上报考勤文件已被一个程序打开，进程无法访问，请先关闭该文件！";
            FxUtil.showErrorAlert(message);
        } catch (IOException e) {
            ExceptionUtils.logStackTrace(e, logger);
            message = "上报考勤过程发生异常，任务已终止！" + "\n" + "异常信息：输入输出异常！";
            FxUtil.showErrorAlert(message);
        } catch (DuplicatePostException e) {
            ExceptionUtils.logStackTrace(e, logger);
            message = "上报考勤过程发生异常，任务已终止！" + "\n" + e.getMessage();
            FxUtil.showErrorAlert(message);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //设置logArea不能编辑
        logArea.setEditable(false);
        //设置logArea自动换行
        logArea.setWrapText(true);
        //将System.out输出重定向到logArea
        MyOutputStream os = new MyOutputStream(logArea);
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        System.setErr(ps);

        groupComboBox.setConverter(new MyStringConverter<>());
        List<Group> groups = DaoFactory.getInstance().getGroupDao().findAll();
        groupComboBox.setItems(FXCollections.observableList(groups));
        groupComboBox.getSelectionModel().select(0);
        companyComboBox.setItems(FXCollections.observableArrayList("客服", "港发"));
        companyComboBox.getSelectionModel().select(0);

        //初始化“未配置的岗位”表格
        //设置表格选择模式（单行、多行）
        withoutPostTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //设置表格不可编辑
        withoutPostTable.setEditable(false);
        //给表格添加序号列
        withoutPostIdxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        withoutPostNameCol.setCellValueFactory(new PropertyValueFactory<>("postName"));
        //初始化“已配置的岗位”表格
        //设置表格选择模式（单行、多行）
        withinPostTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        withinPostTable.setEditable(false);
        //给表格添加序号列
        withinPostIdxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        withinPostNameCol.setCellValueFactory(new PropertyValueFactory<>("postName"));
        withinPostClockInCol.setCellValueFactory(new PropertyValueFactory<>("clockInTime"));
        withinPostClockOutCol.setCellValueFactory(new PropertyValueFactory<>("clockOutTime"));
        withinPostDutyCol.setCellValueFactory(new PropertyValueFactory<>("isDuty"));
        withinPostManHourCol.setCellValueFactory(new PropertyValueFactory<>("manHour"));

        importAttnDataBtn.setDisable(true);
        checkBtn.setDisable(true);
        exportResultBtn.setDisable(true);
        newModeRad.setSelected(true);
        attnRptBtn.setDisable(true);
    }
}
