package com.zxm.toolbox.controller;

import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.attn.Group;
import com.zxm.toolbox.util.FxUtil;
import com.zxm.toolbox.vo.ui.IndexTableCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.util.CellAddress;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AttnGroupController implements Initializable {
    @FXML private TextField idText;
    @FXML private TextField nameText;
    @FXML private TextField dateCellText;
    @FXML private TextField startCellText;
    @FXML private TextField dutyCellText;
    @FXML private TextField timeCellText;
    @FXML private TextField manHourText;
    @FXML private TextField offCellText;
    @FXML private TableView<Group> table;
    @FXML private TableColumn<Group, String> idxCol;
    @FXML private TableColumn<Group, Integer> idCol;
    @FXML private TableColumn<Group, String> nameCol;
    @FXML private TableColumn<Group, String> dateCellCol;
    @FXML private TableColumn<Group, String> startCellCol;
    @FXML private TableColumn<Group, String> dutyCellCol;
    @FXML private TableColumn<Group, String> timeCellCol;
    @FXML private TableColumn<Group, String> manHourCellCol;
    @FXML private TableColumn<Group, String> offCellCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //设置表格选择模式（单行、多行）
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //给表格添加序号列
        idxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCellCol.setCellValueFactory(new PropertyValueFactory<>("dateCell"));
        startCellCol.setCellValueFactory(new PropertyValueFactory<>("startCell"));
        dutyCellCol.setCellValueFactory(new PropertyValueFactory<>("dutyCell"));
        timeCellCol.setCellValueFactory(new PropertyValueFactory<>("timeCell"));
        manHourCellCol.setCellValueFactory(new PropertyValueFactory<>("manHourCell"));
        offCellCol.setCellValueFactory(new PropertyValueFactory<>("offCell"));
        List<Group> list = DaoFactory.getInstance().getGroupDao().findAll();
        table.setItems(FXCollections.observableList(list));
    }

    private void editMode(boolean b) {
        nameText.setDisable(!b);
        dateCellText.setDisable(!b);
        startCellText.setDisable(!b);
        dutyCellText.setDisable(!b);
        timeCellText.setDisable(!b);
        manHourText.setDisable(!b);
        offCellText.setDisable(!b);
    }

    public void add() {
        editMode(true);
    }

    public void edit() {
        int i = table.getSelectionModel().getSelectedIndex();
        if (i != -1) {
            editMode(true);
            Group group = table.getSelectionModel().getSelectedItem();
            idText.setText(String.valueOf(group.getId()));
            nameText.setText(group.getName());
            dateCellText.setText(group.getDateCell().toString());
            startCellText.setText(group.getStartCell().toString());
            dutyCellText.setText(group.getDutyCell().toString());
            timeCellText.setText(group.getTimeCell().toString());
            manHourText.setText(group.getManHourCell().toString());
            offCellText.setText(group.getOffCell().toString());
        }

    }

    public void del() {
        int i = table.getSelectionModel().getSelectedIndex();
        if (i != -1)
            DaoFactory.getInstance().getGroupDao().delete(table.getSelectionModel().getSelectedItem());
        table.getItems().remove(i);
    }

    public void cancel() {
        idText.setText("");
        nameText.setText("");
        dateCellText.setText("");
        startCellText.setText("");
        dutyCellText.setText("");
        timeCellText.setText("");
        manHourText.setText("");
        offCellText.setText("");
        editMode(false);
    }

    public void save() {
        if (nameText.getText().equals("")) {
            Platform.runLater(() -> FxUtil.showWarningAlert("考勤组名不能为空！"));
            return;
        }
        if (dateCellText.getText().equals("")) {
            Platform.runLater(() -> FxUtil.showWarningAlert("考勤表年月单元格地址不能为空！"));
            return;
        }
        if (startCellText.getText().equals("")) {
            Platform.runLater(() -> FxUtil.showWarningAlert("考勤起始单元格地址不能为空！"));
            return;
        }
        Group group;
        try {
            group = new Group(idText.getText().equals("") ? 0 : Integer.parseInt(idText.getText()),
                    nameText.getText(),
                    new CellAddress(dateCellText.getText()),
                    new CellAddress(startCellText.getText()),
                    new CellAddress(dutyCellText.getText()),
                    new CellAddress(timeCellText.getText()),
                    new CellAddress(manHourText.getText()),
                    new CellAddress(offCellText.getText())
            );
            System.out.println(group);
        } catch (Exception e) {
            e.printStackTrace();
            FxUtil.showWarningAlert("格式错误，请重新输入！");
            return;
        }
        if (idText.getText().equals(""))
            //添加逻辑
            DaoFactory.getInstance().getGroupDao().save(group);
        else
            //修改逻辑
            DaoFactory.getInstance().getGroupDao().update(group);
        table.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getGroupDao().findAll()
        ));
        cancel();

    }
}
