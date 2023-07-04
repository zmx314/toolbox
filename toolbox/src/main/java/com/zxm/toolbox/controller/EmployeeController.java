package com.zxm.toolbox.controller;

import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.attn.*;
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

public class EmployeeController implements Initializable {

    @FXML private Button saveBtn;
    @FXML private TableColumn<Employee, String> idxCol;
    @FXML private TableColumn<Employee, Integer> idCol;
    @FXML private TableColumn<Employee, String> nameCol;
    @FXML private TableColumn<Employee, String> companyCol;
    @FXML private TableColumn<Employee, String> departmentCol;
    @FXML private TableColumn<Employee, String> positionCol;
    @FXML private TableColumn<Employee, String> groupCol;
    @FXML private TableColumn<Employee, String> officerCol;
    @FXML private TableView<Employee> empTable;
    @FXML private ComboBox<Company> companyComb;
    @FXML private ComboBox<Department> departmentComb;
    @FXML private ComboBox<Position> positionComb;
    @FXML private ComboBox<Group> groupComb;
    @FXML private ComboBox<String> isOfficer;
    @FXML private TextField idText;
    @FXML private TextField nameText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        companyComb.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getCompanyDao().findAll()));
        companyComb.setConverter(new MyStringConverter<>());
        companyComb.getSelectionModel().select(0);

        departmentComb.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getDepartmentDao().findAll()));
        departmentComb.setConverter(new MyStringConverter<>());
        departmentComb.getSelectionModel().select(0);

        positionComb.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getPositionDao().findAll()));
        positionComb.setConverter(new MyStringConverter<>());
        positionComb.getSelectionModel().select(0);

        groupComb.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getGroupDao().findAll()));
        groupComb.setConverter(new MyStringConverter<>());
        groupComb.getSelectionModel().select(0);
        //设置表格选择模式（单行、多行）
        empTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //给表格添加序号列
        idxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        companyCol.setCellValueFactory(new PropertyValueFactory<>("company"));
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        groupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        officerCol.setCellValueFactory(new PropertyValueFactory<>("isOfficer"));
        List<Employee> empList = DaoFactory.getInstance().getEmployeeDao().findAll();
        empTable.setItems(FXCollections.observableList(empList));
        List<String> list = new ArrayList<>();
        list.add("N");
        list.add("Y");
        isOfficer.setItems(FXCollections.observableList(list));
        isOfficer.getSelectionModel().select(0);
    }

    private void editMode() {
        nameText.setDisable(false);
        companyComb.setDisable(false);
        departmentComb.setDisable(false);
        positionComb.setDisable(false);
        groupComb.setDisable(false);
        isOfficer.setDisable(false);
    }

    @FXML private void add() {
        idText.setText("");
        nameText.setText("");
        idText.setDisable(false);
        editMode();
        saveBtn.setId("save");
    }
    @FXML
    private void edit() {
        Employee emp = empTable.getSelectionModel().getSelectedItem();
        idText.setText(emp.getId());
        nameText.setText(emp.getName());
        companyComb.getSelectionModel().select(new Company(emp.getCompanyId(),emp.getCompany()));
        departmentComb.getSelectionModel().select(new Department(emp.getDepartmentId(),emp.getDepartment()));
        positionComb.getSelectionModel().select(new Position(emp.getPositionId(),emp.getPosition()));
        groupComb.getSelectionModel().select(new Group(emp.getGroupId(), emp.getGroup()));
        isOfficer.getSelectionModel().select(emp.getIsOfficer());
        editMode();
        saveBtn.setId("edit");
    }
    @FXML
    private void del() {
        DaoFactory.getInstance().getEmployeeDao().deleteById(empTable.getSelectionModel().getSelectedItem().getId());
        empTable.getItems().remove(empTable.getSelectionModel().getSelectedIndex());
    }
    @FXML
    private void cancel() {
        idText.setText("");
        nameText.setText("");
        idText.setDisable(true);
        nameText.setDisable(true);
        companyComb.setDisable(true);
        departmentComb.setDisable(true);
        positionComb.setDisable(true);
        groupComb.setDisable(true);
        isOfficer.setDisable(true);
    }
    @FXML
    public void save() {
        if (idText.getText().equals("") || nameText.getText().equals("")) {
            FxUtil.showWarningAlert("ID和姓名不能为空！");
            return;
        }
        Employee employee = DaoFactory.getInstance().getEmployeeDao().findById(idText.getText());
        if (employee != null){
            FxUtil.showWarningAlert("已存在同名ID！");
            return;
        }
        employee = new Employee(idText.getText(), nameText.getText(),
                companyComb.getSelectionModel().getSelectedItem(),
                departmentComb.getSelectionModel().getSelectedItem(),
                positionComb.getSelectionModel().getSelectedItem(),
                groupComb.getSelectionModel().getSelectedItem(),
                isOfficer.getSelectionModel().getSelectedItem());
        if (saveBtn.getId().equals("save")) {
            //添加逻辑
            DaoFactory.getInstance().getEmployeeDao().save(employee);
        } else if(saveBtn.getId().equals("edit")) {
            //修改逻辑
            DaoFactory.getInstance().getEmployeeDao().update(employee);
        }
        empTable.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getEmployeeDao().findAll()
        ));
        saveBtn.setId("");
        cancel();
    }

}
