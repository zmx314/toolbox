package com.zxm.toolbox.controller;

import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.gt.RoundTripPrice;
import com.zxm.toolbox.pojo.gt.ShippingCompany;
import com.zxm.toolbox.util.FxUtil;
import com.zxm.toolbox.vo.ui.IndexTableCell;
import com.zxm.toolbox.vo.ui.MyStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RoundTripPriceController implements Initializable {
    @FXML private TextField idText;
    @FXML private ComboBox<ShippingCompany> companyComb;
    @FXML private TextField typeText;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TextField priceText;

    @FXML private TableView<RoundTripPrice> table;
    @FXML private TableColumn<RoundTripPrice, String> idxCol;
    @FXML private TableColumn<RoundTripPrice, Integer> idCol;
    @FXML private TableColumn<RoundTripPrice, String> nameCol;
    @FXML private TableColumn<RoundTripPrice, String> typeCol;
    @FXML private TableColumn<RoundTripPrice, LocalDate> startCol;
    @FXML private TableColumn<RoundTripPrice, LocalDate> endCol;
    @FXML private TableColumn<RoundTripPrice, BigDecimal> priceCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //设置表格选择模式（单行、多行）
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //给表格添加序号列
        idxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("shippingCompany"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("ticketType"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        companyComb.setConverter(new MyStringConverter<>());
        companyComb.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getShippingCompanyDao().findAll()
        ));
        companyComb.getSelectionModel().select(0);
        List<RoundTripPrice> list = DaoFactory.getInstance().getRoundTripPriceDao().findAll();
        table.setItems(FXCollections.observableList(list));
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());
    }

    private void editMode() {
        companyComb.setDisable(false);
        typeText.setDisable(false);
        startDate.setDisable(false);
        endDate.setDisable(false);
        priceText.setDisable(false);
    }
    @FXML
    private void add() {
        editMode();
    }

    @FXML
    private void edit() {
        if(table.getSelectionModel().getSelectedIndex() < 0)
            return;
        RoundTripPrice rtp = table.getSelectionModel().getSelectedItem();
        idText.setText(String.valueOf(rtp.getId()));
        companyComb.getSelectionModel().select(
                DaoFactory.getInstance().getShippingCompanyDao()
                        .findByName(rtp.getShippingCompany()));
        typeText.setText(rtp.getTicketType());
        startDate.setValue(rtp.getStartDate());
        endDate.setValue(rtp.getEndDate());
        priceText.setText(rtp.getPrice().toString());
        editMode();
    }

    @FXML
    private void del() {
        int i = table.getSelectionModel().getSelectedIndex();
        if(i != -1) {
            DaoFactory.getInstance().getRoundTripPriceDao().deleteById(
                    table.getSelectionModel().getSelectedItem().getId());
            table.getItems().remove(i);
        }

    }

    @FXML private void cancel() {
        idText.setText("");
        idText.setDisable(true);
        companyComb.getSelectionModel().select(0);
        companyComb.setDisable(true);
        typeText.setText("");
        typeText.setDisable(true);
        startDate.setValue(LocalDate.now());
        startDate.setDisable(true);
        endDate.setValue(LocalDate.now());
        endDate.setDisable(true);
        priceText.setDisable(true);
        priceText.setText("");
    }

    @FXML private void save() {
        if (typeText.getText().equals("")) {
            Platform.runLater(()->FxUtil.showWarningAlert("票型不能为空！"));
            return;
        }
        if (priceText.getText().equals("")){
            Platform.runLater(()->FxUtil.showWarningAlert("票价不能为空！"));
            return;
        }
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(priceText.getText());
            RoundTripPrice rtp = new RoundTripPrice(
                    idText.getText().equals("") ? 0 : Integer.parseInt(idText.getText()),
                    companyComb.getSelectionModel().getSelectedItem().getName(),
                    typeText.getText(),
                    startDate.getValue(),
                    endDate.getValue(),
                    bigDecimal);
            if(idText.getText().equals("")){
                //添加逻辑
                DaoFactory.getInstance().getRoundTripPriceDao().save(rtp);
            } else {
                //修改逻辑
                DaoFactory.getInstance().getRoundTripPriceDao().update(rtp);
            }
            table.setItems(FXCollections.observableList(
                    DaoFactory.getInstance().getRoundTripPriceDao().findAll()));
            cancel();
        } catch (Exception e) {
            Platform.runLater(()->FxUtil.showWarningAlert("票价格式错误，请重新输入！"));
        }
    }
}
