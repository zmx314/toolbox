package com.zxm.toolbox.controller;

import com.zxm.toolbox.MainApplication;
import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.gt.ShippingCompany;
import com.zxm.toolbox.pojo.gt.ShippingGroup;
import com.zxm.toolbox.util.FxUtil;
import com.zxm.toolbox.vo.ui.IndexTableCell;
import com.zxm.toolbox.vo.ui.MyStringConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShippingCompanyController implements Initializable {

    @FXML private TextField idText;
    @FXML private TextField nameText;
    @FXML private ComboBox<ShippingGroup> groupComb;
    @FXML private ComboBox<String> typeComb;
    @FXML private TableView<ShippingCompany> table;
    @FXML private TableColumn<ShippingCompany, String> idxCol;
    @FXML private TableColumn<ShippingCompany, Integer> idCol;
    @FXML private TableColumn<ShippingCompany, String> nameCol;
    @FXML private TableColumn<ShippingCompany, String> groupCol;
    @FXML private TableColumn<ShippingCompany, String> typeCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //设置表格选择模式（单行、多行）
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //给表格添加序号列
        idxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        //给表格绑定bean的属性
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        groupCol.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        //初始化表格数据
        table.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getShippingCompanyDao().findAll()));
        //初始华groupComb选项数据
        groupComb.setConverter(new MyStringConverter<>());
        groupComb.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getShippingGroupDao().findAll()));
        groupComb.getSelectionModel().select(0);
        List<String> typeList = new ArrayList<>();
        typeList.add("去程");
        typeList.add("回程");
        typeComb.setItems(FXCollections.observableList(typeList));
        typeComb.getSelectionModel().select(0);

    }

    private void editMode() {
        nameText.setDisable(false);
        groupComb.setDisable(false);
        typeComb.setDisable(false);

    }
    @FXML private void add() {
        editMode();
    }

    @FXML private void edit() {
        if (table.getSelectionModel().getSelectedIndex() == -1)
            return;
        ShippingCompany sc = table.getSelectionModel().getSelectedItem();
        idText.setText(String.valueOf(sc.getId()));
        nameText.setText(sc.getName());
        groupComb.getSelectionModel().select(
                DaoFactory.getInstance().getShippingGroupDao().findById(sc.getGroupId()));
        typeComb.getSelectionModel().select(sc.getType());
        editMode();
    }

    @FXML private void del() {
        int i = table.getSelectionModel().getSelectedIndex();
        if(i != -1) {
            DaoFactory.getInstance().getShippingCompanyDao().deleteById(
                    table.getSelectionModel().getSelectedItem().getId());
            table.getItems().remove(i);
        }

    }

    @FXML private void cancel() {
        idText.setText("");
        idText.setDisable(true);
        nameText.setText("");
        nameText.setDisable(true);
        groupComb.getSelectionModel().select(0);
        groupComb.setDisable(true);
        typeComb.getSelectionModel().select(0);
        typeComb.setDisable(true);

    }

    @FXML private void save() {
        if (nameText.getText().equals("")) {
            FxUtil.showWarningAlert("船属公司名不能为空！");
            return;
        }
        if(DaoFactory.getInstance().getShippingCompanyDao().findByName(nameText.getText()) != null){
            FxUtil.showWarningAlert("已存在同名船属公司！");
            return;
        }
        ShippingCompany sc = new ShippingCompany(
                idText.getText().equals("") ? 0 : Integer.parseInt(idText.getText()),
                nameText.getText(),
                groupComb.getSelectionModel().getSelectedItem(),
                typeComb.getSelectionModel().getSelectedItem());
        if(idText.getText().equals(""))
            //添加逻辑
            DaoFactory.getInstance().getShippingCompanyDao().save(sc);
        else
            //修改逻辑
            DaoFactory.getInstance().getShippingCompanyDao().update(sc);
        table.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getShippingCompanyDao().findAll()));
        cancel();
    }

}
