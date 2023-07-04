package com.zxm.toolbox.controller;

import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.attn.*;
import com.zxm.toolbox.vo.ui.IndexTableCell;
import com.zxm.toolbox.vo.ui.MyStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BasicDataController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(BasicDataController.class);
    @FXML private TitledPane titledPane1;
    @FXML private TitledPane titledPane2;
    @FXML private TitledPane titledPane3;
    @FXML private TitledPane titledPane4;
    @FXML private TableView<Company> companyTable;
    @FXML private TableColumn<Company, String> companyIdxCol;
    @FXML private TableColumn<Company, Integer> companyIdCol;
    @FXML private TableColumn<Company, String> companyNameCol;
    @FXML private TableView<Department> departmentTable;
    @FXML private TableColumn<Department, String> departmentIdxCol;
    @FXML private TableColumn<Department, Integer> departmentIdCol;
    @FXML private TableColumn<Department, String> departmentNameCol;
    @FXML private TableView<Section> sectionTable;
    @FXML private TableColumn<Section, String> sectionIdxCol;
    @FXML private TableColumn<Section, Integer> sectionIdCol;
    @FXML private TableColumn<Section, String> sectionNameCol;
    @FXML private TableView<Position> positionTable;
    @FXML private TableColumn<Position, String> positionIdxCol;
    @FXML private TableColumn<Position, Integer> positionIdCol;
    @FXML private TableColumn<Position, String> positionNameCol;
    @FXML private ChoiceBox<BasicBean> choiceBox;
    @FXML private TextField nameText;
    @FXML private TextField idText;
    @FXML Button addBtn;
    @FXML Button delBtn;
    @FXML Button editBtn;
    @FXML Button cnlBtn;
    @FXML Button saveBtn;
    @FXML
    protected void addAction(ActionEvent event) {
        LOGGER.info(event.getSource());
        if (titledPane1.isExpanded())
            choiceBox.getSelectionModel().select(0);
        if (titledPane2.isExpanded())
            choiceBox.getSelectionModel().select(1);
        if (titledPane3.isExpanded())
            choiceBox.getSelectionModel().select(2);
        if (titledPane4.isExpanded())
            choiceBox.getSelectionModel().select(3);
        else
            choiceBox.getSelectionModel().select(0);
        idText.setText("");
        nameText.setText("");
        nameText.setDisable(false);
    }

    @FXML
    protected void editAction(ActionEvent event) {
        LOGGER.info(event.getSource());
        if (titledPane1.isExpanded() && companyTable.getSelectionModel().getSelectedIndex() != -1){
            choiceBox.getSelectionModel().select(0);
            idText.setText(String.valueOf(companyTable.getSelectionModel().getSelectedItem().getId()));
            nameText.setText(companyTable.getSelectionModel().getSelectedItem().getName());
            nameText.setDisable(false);
        }
        if (titledPane2.isExpanded() && departmentTable.getSelectionModel().getSelectedIndex() != -1){
            choiceBox.getSelectionModel().select(1);
            idText.setText(String.valueOf(departmentTable.getSelectionModel().getSelectedItem().getId()));
            nameText.setText(departmentTable.getSelectionModel().getSelectedItem().getName());
            nameText.setDisable(false);
        }
        if (titledPane3.isExpanded() && sectionTable.getSelectionModel().getSelectedIndex() != -1){
            choiceBox.getSelectionModel().select(2);
            idText.setText(String.valueOf(sectionTable.getSelectionModel().getSelectedItem().getId()));
            nameText.setText(sectionTable.getSelectionModel().getSelectedItem().getName());
            nameText.setDisable(false);
        }
        if (titledPane4.isExpanded() && positionTable.getSelectionModel().getSelectedIndex() != -1){
            choiceBox.getSelectionModel().select(3);
            idText.setText(String.valueOf(positionTable.getSelectionModel().getSelectedItem().getId()));
            nameText.setText(positionTable.getSelectionModel().getSelectedItem().getName());
            nameText.setDisable(false);
        }
    }
    @FXML
    protected void delAction(ActionEvent event) {
        LOGGER.info(event.getSource());
        if (titledPane1.isExpanded() && companyTable.getSelectionModel().getSelectedIndex() != -1){
            DaoFactory.getInstance().getCompanyDao().deleteById(
                    companyTable.getSelectionModel().getSelectedItem().getId());
            companyTable.getItems().remove(companyTable.getSelectionModel().getSelectedIndex());
        }
        if (titledPane2.isExpanded() && departmentTable.getSelectionModel().getSelectedIndex() != -1){
            DaoFactory.getInstance().getDepartmentDao().delete(
                    departmentTable.getSelectionModel().getSelectedItem());
            departmentTable.getItems().remove(departmentTable.getSelectionModel().getSelectedIndex());
        }
        if (titledPane3.isExpanded() && sectionTable.getSelectionModel().getSelectedIndex() != -1){
            DaoFactory.getInstance().getSectionDao().delete(
                    sectionTable.getSelectionModel().getSelectedItem());
            sectionTable.getItems().remove(sectionTable.getSelectionModel().getSelectedIndex());
        }
        if (titledPane4.isExpanded() && positionTable.getSelectionModel().getSelectedIndex() != -1){
            DaoFactory.getInstance().getPositionDao().delete(
                    positionTable.getSelectionModel().getSelectedItem());
            positionTable.getItems().remove(positionTable.getSelectionModel().getSelectedIndex());
        }
    }
    @FXML
    public void cancelAction(ActionEvent event) {
        LOGGER.info(event.getSource());
        idText.setText("");
        nameText.setText("");
        nameText.setDisable(true);
    }

    @FXML
    public void saveAction(ActionEvent event) {
        LOGGER.info(event.getSource());
        int i = choiceBox.getSelectionModel().getSelectedItem().getId();
        if(idText.getText().equals("")){
            //添加逻辑
            if (i == 1){
                DaoFactory.getInstance().getCompanyDao().save(
                        new Company(nameText.getText()));
                companyTable.setItems(FXCollections.observableList(
                        DaoFactory.getInstance().getCompanyDao().findAll()
                ));
            }
            if (i == 2){
                DaoFactory.getInstance().getDepartmentDao().add(
                        new Department(nameText.getText()));
                departmentTable.setItems(FXCollections.observableList(
                        DaoFactory.getInstance().getDepartmentDao().findAll()
                ));
            }
            if (i == 3){
                DaoFactory.getInstance().getSectionDao().add(
                        new Section(nameText.getText()));
                sectionTable.setItems(FXCollections.observableList(
                        DaoFactory.getInstance().getSectionDao().findAll()
                ));
            }
            if (i == 4){
                DaoFactory.getInstance().getPositionDao().add(
                        new Position(nameText.getText()));
                positionTable.setItems(FXCollections.observableList(
                        DaoFactory.getInstance().getPositionDao().findAll()
                ));
            }
        } else {
            //修改逻辑
            if (i == 1){
                DaoFactory.getInstance().getCompanyDao().update(
                        new Company(Integer.parseInt(idText.getText()), nameText.getText()));
                companyTable.setItems(FXCollections.observableList(
                        DaoFactory.getInstance().getCompanyDao().findAll()
                ));
            }
            if (i == 2){
                DaoFactory.getInstance().getDepartmentDao().update(
                        new Department(Integer.parseInt(idText.getText()), nameText.getText()));
                departmentTable.setItems(FXCollections.observableList(
                        DaoFactory.getInstance().getDepartmentDao().findAll()
                ));
            }
            if (i == 3){
                DaoFactory.getInstance().getSectionDao().update(
                        new Section(Integer.parseInt(idText.getText()), nameText.getText()));
                sectionTable.setItems(FXCollections.observableList(
                        DaoFactory.getInstance().getSectionDao().findAll()
                ));
            }
            if (i == 4){
                DaoFactory.getInstance().getPositionDao().update(
                        new Position(Integer.parseInt(idText.getText()), nameText.getText()));
                positionTable.setItems(FXCollections.observableList(
                        DaoFactory.getInstance().getPositionDao().findAll()
                ));
            }
          idText.setText("");
        }
        nameText.setText("");
        nameText.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOGGER.info("BasicDataController Init");
        //设置表格选择模式（单行、多行）
        companyTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //给表格添加序号列
        companyIdxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        companyIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        companyNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        List<Company> companyList = DaoFactory.getInstance().getCompanyDao().findAll();
        companyTable.setItems(FXCollections.observableList(companyList));

        //设置表格选择模式（单行、多行）
        departmentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //给表格添加序号列
        departmentIdxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        departmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        departmentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        List<Department> departmentList = DaoFactory.getInstance().getDepartmentDao().findAll();
        departmentTable.setItems(FXCollections.observableList(departmentList));

        //设置表格选择模式（单行、多行）
        sectionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //给表格添加序号列
        sectionIdxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        sectionIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        sectionNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        List<Section> sectionList = DaoFactory.getInstance().getSectionDao().findAll();
        sectionTable.setItems(FXCollections.observableList(sectionList));

        //设置表格选择模式（单行、多行）
        positionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //给表格添加序号列
        positionIdxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        positionIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        positionNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        List<Position> positionList = DaoFactory.getInstance().getPositionDao().findAll();
        positionTable.setItems(FXCollections.observableList(positionList));

        ObservableList<BasicBean> choiceList = FXCollections.observableArrayList();
        choiceList.add(new BasicBean(1, "公司"));
        choiceList.add(new BasicBean(2, "部门"));
        choiceList.add(new BasicBean(3, "子部门"));
        choiceList.add(new BasicBean(4, "职位"));
        choiceBox.setConverter(new MyStringConverter<>());
        choiceBox.setItems(choiceList);
        choiceBox.getSelectionModel().select(0);
        idText.setText("");
        nameText.setText("");
        idText.setDisable(true);
        nameText.setDisable(true);

    }

}
