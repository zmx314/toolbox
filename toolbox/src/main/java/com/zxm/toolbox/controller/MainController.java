package com.zxm.toolbox.controller;

import com.zxm.toolbox.MainApplication;
import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.util.ExceptionUtils;
import com.zxm.toolbox.util.FxUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.EventHandler;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(MainApplication.class);
    @FXML private TabPane tabPane;
    @FXML private Label versionLabel;

    protected void addTab(URL url, String name) {
        LOGGER.info("addTab");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Node pane = fxmlLoader.getRoot();
        for(Tab t : tabPane.getTabs()){
            if (t.getText().equals(name))
                return;
        }
        Tab tab = new Tab(name);
        tab.setClosable(true);
        tab.setId(name);
        tab.setContent(pane);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML protected void basicDataItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/basic-data-view.fxml");
        addTab(url, "基础资料");
    }
    @FXML protected void attendanceGroupItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/attn-group-view.fxml");
        addTab(url, "考勤组");
    }
    @FXML protected void employeeItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/employee-view.fxml");
        addTab(url, "员工信息");

    }

    @FXML protected void settingsItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/settings-view.fxml");
        addTab(url, "参数设置");
    }
    @FXML protected void shippingGroupItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/shipping-group-view.fxml");
        addTab(url, "航线组设置");
    }
    @FXML protected void shippingCompanyItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/shipping-company-view.fxml");
        addTab(url, "船属公司设置");
    }
    @FXML protected void roundTripPriceItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/round-trip-price-view.fxml");
        addTab(url, "来回套票票价设置");
    }
    @FXML protected void keywordItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/keyword-view.fxml");
        addTab(url, "旅行社名称关键词");
    }
    @FXML protected void attendanceReportItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/attn-rpt-view.fxml");
        addTab(url, "上报考勤");
    }
    @FXML protected void groupTicketReportItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/group-ticket-rpt-view.fxml");
        addTab(url, "团体票报表");
    }
    @FXML protected void groupTicketPDFItemOnclick() {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/group-ticket-pdf-view.fxml");
        addTab(url, "乘船券生成");
    }

    @FXML public void batLoadDataItemOnclick(ActionEvent actionEvent) {
        URL url = MainApplication.class.getResource(
                "/com/zxm/toolbox/bat-load-data-view.fxml");
        addTab(url, "批量读取文件数据");
    }
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        Properties properties = new Properties();
        try {
            InputStreamReader inStream = new InputStreamReader(
                    Objects.requireNonNull(MainApplication.class.getResourceAsStream(
                            "version.properties")), "UTF-8");
            properties.load(inStream);
            inStream.close();
            versionLabel.setText("Powered By Java   Version:"
                    + properties.getProperty("version")
                    + "  Copyright (c)2018-2023 雪丝敏捷");
        } catch (IOException e) {
            ExceptionUtils.logStackTrace(e, LOGGER);
            FxUtil.showErrorAlert("加载版本信息时发生异常！");
        }

    }

}