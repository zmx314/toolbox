package com.zxm.toolbox.controller;

import com.zxm.toolbox.resources.PropertiesFactory;
import com.zxm.toolbox.util.FxUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.apache.poi.ss.util.CellAddress;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML private TextField dutyTime;
    @FXML private TextField extraTime;
    @FXML private TextField compensatoryTime;
    @FXML private TextField compensatoryLeft;
    @FXML private TextField annualLeaveLeft;
    @FXML private TextField parentLeaveLeft;
    @FXML private TextField nursingLeaveLeft;
    @FXML private TextField timeHeader;
    @FXML private TextField manHourHeader;
    @FXML private TextField dutyHeader;
    @FXML private TextField offHeader;
    @FXML private TextField headerCell;
    @FXML private TextField departmentCell;
    @FXML private TextField reportDateCell;
    @FXML private TextField dateHeaderCell;
    @FXML private TextField dutyRecordCell;
    @FXML private TextField clockTimeKeyWord;
    @FXML private TextField attnOfficerKeyWord;
    @FXML private TextField departmentName;
    @FXML private TextField faultToleranceTime;

    @FXML
    private void dutyTime() {
        try {
            PropertiesFactory.getInstance()
                    .getDutyRosterProps().setValue("DutyTime", dutyTime.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误！");
            dutyTime.setText(PropertiesFactory.getInstance()
                    .getDutyRosterProps().getValue("DutyTime"));
        }
    }
    @FXML
    private void extraTime() {
        try {
            PropertiesFactory.getInstance().getDutyRosterProps().setValue("ExtraTime", extraTime.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误，请重新输入！");
            extraTime.setText(PropertiesFactory.getInstance()
                    .getDutyRosterProps().getValue("ExtraTime"));
        }
    }
    @FXML
    private void compensatoryTime() {
        try {
            new CellAddress(compensatoryTime.getText() + 1);
            PropertiesFactory.getInstance().getDutyRosterProps().setValue("CompensatoryTime", compensatoryTime.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            compensatoryTime.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("CompensatoryTime"));
        }
    }
    @FXML
    private void compensatoryLeft() {
        try {
            new CellAddress(compensatoryLeft.getText() + 1);
            PropertiesFactory.getInstance().getDutyRosterProps().setValue("CompensatoryLeft", compensatoryLeft.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            compensatoryLeft.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("CompensatoryLeft"));
        }
    }
    @FXML
    private void annualLeaveLeft() {
        try {
            new CellAddress(annualLeaveLeft.getText() + 1);
            PropertiesFactory.getInstance().getDutyRosterProps().setValue("AnnualLeaveLeft", annualLeaveLeft.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            annualLeaveLeft.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("AnnualLeaveLeft"));
        }
    }
    @FXML
    private void parentLeaveLeft() {
        try {
            new CellAddress(parentLeaveLeft.getText() + 1);
            PropertiesFactory.getInstance().getDutyRosterProps().setValue("ParentLeaveLeft", parentLeaveLeft.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            parentLeaveLeft.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("ParentLeaveLeft"));
        }
    }
    @FXML
    private void nursingLeaveLeft() {
        try {
            new CellAddress(nursingLeaveLeft.getText() + 1);
            PropertiesFactory.getInstance().getDutyRosterProps().setValue("NursingLeaveLeft", nursingLeaveLeft.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            nursingLeaveLeft.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("NursingLeaveLeft"));
        }
    }
    @FXML
    private void dutyHeader() {
        PropertiesFactory.getInstance().getDutyRosterProps().setValue("Header_DutyPostName", parentLeaveLeft.getText());
    }
    @FXML
    private void timeHeader() {
        PropertiesFactory.getInstance().getDutyRosterProps().setValue("Header_DutyPostClockTime", parentLeaveLeft.getText());
    }
    @FXML
    private void manHourHeader() {
        PropertiesFactory.getInstance().getDutyRosterProps().setValue("Header_DutyPostManHour", parentLeaveLeft.getText());
    }
    @FXML
    private void offHeader() {
        PropertiesFactory.getInstance().getDutyRosterProps().setValue("Header_OffPostName", parentLeaveLeft.getText());
    }
    @FXML
    private void headerCell() {
        try {
            PropertiesFactory.getInstance().getTemplateProps().setValue("HeaderCell", headerCell.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            headerCell.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("HeaderCell"));
        }
    }
    @FXML
    private void departmentCell() {
        try {
            PropertiesFactory.getInstance().getTemplateProps().setValue("DepartmentCell", departmentCell.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            departmentCell.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("DepartmentCell"));
        }
    }
    @FXML
    private void reportDateCell() {
        try {
            PropertiesFactory.getInstance().getTemplateProps().setValue("ReportDateCell", reportDateCell.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            reportDateCell.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("ReportDateCell"));
        }
    }
    @FXML
    private void dateHeaderCell() {
        try {
            PropertiesFactory.getInstance().getTemplateProps().setValue("DateHeaderCell", dateHeaderCell.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            dateHeaderCell.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("DateHeaderCell"));
        }
    }
    @FXML
    private void dutyRecordCell() {
        try {
            PropertiesFactory.getInstance().getTemplateProps().setValue("DutyRecordCell", dutyRecordCell.getText());
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            dutyRecordCell.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("DutyRecordCell"));
        }
    }
    @FXML
    private void clockTimeKeyWord() {
        PropertiesFactory.getInstance().getTemplateProps().setValue("ClockTimeKeyWord", clockTimeKeyWord.getText());
    }
    @FXML
    private void attnOfficerKeyWord() {
        PropertiesFactory.getInstance().getTemplateProps().setValue("AttnOfficerKeyWord", attnOfficerKeyWord.getText());
    }
    @FXML
    private void departmentName() {
        PropertiesFactory.getInstance().getTemplateProps().setValue("DepartmentName", departmentName.getText());
    }
    @FXML
    private void faultToleranceTime() {
        try {
            int i = Integer.parseInt(faultToleranceTime.getText());
            PropertiesFactory.getInstance().getSettingProps().setValue("FaultToleranceTime", Integer.toString(i));
        } catch (Exception e) {
            FxUtil.showWarningAlert("格式错误,请重新输入！");
            faultToleranceTime.setText(PropertiesFactory.getInstance().getSettingProps().getValue("FaultToleranceTime"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dutyTime.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("DutyTime"));
        extraTime.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("ExtraTime"));
        compensatoryTime.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("CompensatoryTime"));
        compensatoryLeft.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("CompensatoryLeft"));
        annualLeaveLeft.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("AnnualLeaveLeft"));
        parentLeaveLeft.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("ParentLeaveLeft"));
        nursingLeaveLeft.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("NursingLeaveLeft"));
        dutyHeader.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("Header_DutyPostName"));
        timeHeader.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("Header_DutyPostClockTime"));
        manHourHeader.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("Header_DutyPostManHour"));
        offHeader.setText(PropertiesFactory.getInstance().getDutyRosterProps().getValue("Header_OffPostName"));

        headerCell.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("HeaderCell"));
        departmentCell.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("DepartmentCell"));
        reportDateCell.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("ReportDateCell"));
        dateHeaderCell.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("DateHeaderCell"));
        dutyRecordCell.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("DutyRecordCell"));
        clockTimeKeyWord.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("ClockTimeKeyWord"));
        attnOfficerKeyWord.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("AttnOfficerKeyWord"));
        departmentName.setText(PropertiesFactory.getInstance().getTemplateProps().getValue("DepartmentName"));

        faultToleranceTime.setText(PropertiesFactory.getInstance().getSettingProps().getValue("FaultToleranceTime"));
    }

}
