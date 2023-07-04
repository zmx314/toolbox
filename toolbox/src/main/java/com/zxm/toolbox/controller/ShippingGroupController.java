package com.zxm.toolbox.controller;

import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.gt.ShippingGroup;
import com.zxm.toolbox.util.FxUtil;
import com.zxm.toolbox.vo.ui.IndexTableCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShippingGroupController implements Initializable {
    @FXML private TextField idText;
    @FXML private TextField nameText;
    @FXML private ComboBox<String> typeComb;
    @FXML private ComboBox<String> separateComb;
    @FXML private TableView<ShippingGroup> table;
    @FXML private TableColumn<ShippingGroup, String> idxCol;
    @FXML private TableColumn<ShippingGroup, Integer> idCol;
    @FXML private TableColumn<ShippingGroup, String> nameCol;
    @FXML private TableColumn<ShippingGroup, String> typeCol;
    @FXML private TableColumn<ShippingGroup, String> separateCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //设置表格选择模式（单行、多行）
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //给表格添加序号列
        idxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        separateCol.setCellValueFactory(new PropertyValueFactory<>("isCompanySeparate"));
        List<ShippingGroup> list = DaoFactory.getInstance().getShippingGroupDao().findAll();
        table.setItems(FXCollections.observableList(list));

        List<String> typeList = new ArrayList<>();
        typeList.add("单去");
        typeList.add("单去+单回");
        typeList.add("来回");
        typeList.add("单去+单回+来回");
        typeComb.setItems(FXCollections.observableList(typeList));
        typeComb.getSelectionModel().select(0);
        List<String> booleanList = new ArrayList<>();
        booleanList.add("是");
        booleanList.add("否");
        separateComb.setItems(FXCollections.observableList(booleanList));
        separateComb.getSelectionModel().select(0);
    }

    private void editMode() {
        nameText.setDisable(false);
        typeComb.setDisable(false);
        separateComb.setDisable(false);
    }
    @FXML private void add() {
        editMode();
    }

    @FXML private void edit() {
        if(table.getSelectionModel().getSelectedIndex() < 0)
            return;
        ShippingGroup sg = table.getSelectionModel().getSelectedItem();
        idText.setText(String.valueOf(sg.getId()));
        nameText.setText(sg.getName());
        typeComb.getSelectionModel().select(sg.getType());
        separateComb.getSelectionModel().select(sg.getIsCompanySeparate());
        editMode();
    }

    @FXML private void del() {
        int i = table.getSelectionModel().getSelectedIndex();
        if(i != -1) {
            DaoFactory.getInstance().getShippingGroupDao().deleteById(
                    table.getSelectionModel().getSelectedItem().getId());
            table.getItems().remove(i);
        }

    }

    @FXML private void cancel() {
        idText.setText("");
        idText.setDisable(true);
        nameText.setText("");
        nameText.setDisable(true);
        typeComb.setDisable(true);
        typeComb.getSelectionModel().select(0);
        separateComb.setDisable(true);
        separateComb.getSelectionModel().select(0);
    }

    @FXML private void save() {
        if (nameText.getText().equals("")) {
            FxUtil.showWarningAlert("航线组名不能为空！");
            return;
        }
        if(DaoFactory.getInstance().getShippingGroupDao().findByName(nameText.getText()) != null){
            FxUtil.showWarningAlert("已存在同名航线组！");
            return;
        }
        ShippingGroup sg = new ShippingGroup(
                idText.getText().equals("") ? 0 : Integer.parseInt(idText.getText()),
                nameText.getText(),
                typeComb.getSelectionModel().getSelectedItem(),
                separateComb.getSelectionModel().getSelectedItem()
        );
        if(idText.getText().equals("")){
            //添加逻辑
            DaoFactory.getInstance().getShippingGroupDao().save(sg);
        } else {
            //修改逻辑
            DaoFactory.getInstance().getShippingGroupDao().update(sg);
        }
        table.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getShippingGroupDao().findAll()));
        cancel();
    }

}
