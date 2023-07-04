package com.zxm.toolbox.controller;

import com.zxm.toolbox.pojo.rpt.DailyData;
import com.zxm.toolbox.resources.PropertiesFactory;
import com.zxm.toolbox.service.DataWriter;
import com.zxm.toolbox.util.FxUtil;
import com.zxm.toolbox.util.MyOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BatLoadDataController implements Initializable {
    @FXML private TextArea logArea;
    @FXML private BorderPane root;
    @FXML private TextField pathText;
    private File selectedDir;
    private List<DailyData> list;
    private String message;

    public void choose() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择目录");
        String lastDirStr = PropertiesFactory.getInstance().getUiProps().getValue("LastLoadDir");
        if (lastDirStr == null) {
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        } else if (new File(lastDirStr).exists()) {
            directoryChooser.setInitialDirectory(new File(lastDirStr));
        }
        selectedDir = directoryChooser.showDialog(root.getScene().getWindow());
        if (selectedDir != null) {
            pathText.setText(selectedDir.getPath());
            PropertiesFactory.getInstance().getUiProps().setValue("LastLoadDir", selectedDir.getAbsolutePath());
            list = null;
        }
    }

    public void load() {
        if (selectedDir == null) {
            message = "为选取读取目录！";
            Platform.runLater(() -> FxUtil.showWarningAlert(message));
            return;
        }
        File[] files = selectedDir.listFiles(pathname -> pathname.isDirectory() || pathname.getPath().toLowerCase().endsWith(".xlsx"));
        if (files == null) {
            message = "所选目录下没有XLSX文件！";
            Platform.runLater(() -> FxUtil.showWarningAlert(message));
            return;
        }
        list = new ArrayList<>();
        new Thread(() -> {
            double departShip = 0;
            double departPax = 0;
            double arriveShip = 0;
            double arrivePax = 0;
            try {
                for (File file : files) {
                    Platform.runLater(() ->System.out.println(file.getName()));
                    DailyData data = new DailyData(file);
                    Platform.runLater(() ->System.out.println(data));
                    departShip = departShip + data.getDepartShip();
                    departPax = departPax + data.getDepartPax();
                    arriveShip = arriveShip + data.getArriveShip();
                    arrivePax = arrivePax + data.getArrivePax();
                    list.add(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            System.out.println("到港班次：" + arriveShip);
            System.out.println("到港人数：" + arrivePax);
            System.out.println("出港班次：" + departShip);
            System.out.println("出港人数：" + departPax);
        }).start();

    }

    public void export() {
        if(list == null || list.isEmpty()) {
            Platform.runLater(() -> FxUtil.showWarningAlert("无可导出数据"));
            return;
        }
        File resultFile = FxUtil.saveXlsxFile(root, "ResultFileDir");
        try {
            DataWriter.export(resultFile, list);
        } catch (IOException e) {
            e.printStackTrace();
            message = "导出文件时发生输入输出异常，任务已终止";
            Platform.runLater(() -> FxUtil.showErrorAlert(message));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //将System.out输出重定向到logArea
        MyOutputStream os = new MyOutputStream(logArea);
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        System.setErr(ps);
    }
}
