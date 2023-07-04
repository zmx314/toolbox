package com.zxm.toolbox.util;

import com.zxm.toolbox.resources.PropertiesFactory;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FxUtil {

    public static FileChooser.ExtensionFilter getExcelFilter() {
        List<String> list = new ArrayList<>();
        list.add("*.xls");
        list.add("*.xlsx");
        return new FileChooser.ExtensionFilter("*.xls,*.xlsx", list);
    }

    public static FileChooser.ExtensionFilter getXlsFilter() {
        return new FileChooser.ExtensionFilter("*.xls", ".xls");
    }

    public static FileChooser.ExtensionFilter getXlsxFilter() {
        return new FileChooser.ExtensionFilter("*.xlsx", ".xlsx");
    }

    public static FileChooser.ExtensionFilter getPdfFilter() {
        return new FileChooser.ExtensionFilter("*.pdf", ".pdf");
    }

    public static File chooseFile(Parent root, TextField path, String propKey, FileChooser.ExtensionFilter filter) {
        String lastDir = PropertiesFactory.getInstance().getUiProps().getValue(propKey);
        FileChooser fc = new FileChooser();
        fc.setTitle("打开");
        //设置初始化的目录
        File file = new File(lastDir);
        if (file.exists())
            fc.setInitialDirectory(file);
        else
            fc.setInitialDirectory(new File(System.getProperty("usr.home")));
        fc.getExtensionFilters().add(filter);
        file = fc.showOpenDialog(root.getScene().getWindow());
        path.setText(file == null ? "" : file.getAbsolutePath());
        if (file != null) {
            PropertiesFactory.getInstance().getUiProps().setValue(propKey, file.getParent());
            System.out.println("选择的文件:" + file);
        }
        return file;
    }

    public static File chooseExcelFile(Parent root, TextField path, String propKey) {
        return chooseFile(root, path, propKey, getExcelFilter());
    }

    public static File chooseXlsFile(Parent root, TextField path, String propKey) {
        return chooseFile(root, path, propKey, getXlsFilter());
    }

    public static File chooseXlsxFile(Parent root, TextField path, String propKey) {
        return chooseFile(root, path, propKey, getXlsxFilter());
    }

    public static File choosePdfFile(Parent root, TextField path, String propKey) {
        return chooseFile(root, path, propKey, getPdfFilter());
    }

    public static File saveFile(Parent root, String propKey, FileChooser.ExtensionFilter filter) {
        String lastDir = PropertiesFactory.getInstance().getUiProps().getValue("ResultFileDir");
        FileChooser fc = new FileChooser();
        fc.setTitle("保存");
        if (new File(lastDir).exists())
            fc.setInitialDirectory(new File(lastDir));
        fc.getExtensionFilters().clear();
        fc.getExtensionFilters().add(filter);
        File file = fc.showSaveDialog(root.getScene().getWindow());
        if (file != null) {
            PropertiesFactory.getInstance().getUiProps().setValue(propKey, file.getParent());
            System.out.println("保存的文件:" + file);
        }
        return file;
    }

    public static File saveXlsxFile(Parent root, String propKey) {
        return saveFile(root, propKey, getXlsxFilter());
    }

    public static File savePdfFile(Parent root, String propKey) {
        return saveFile(root, propKey, getPdfFilter());
    }

    public static void showInfoAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.setTitle("信息");
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    public static void showWarningAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, message);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    public static void showErrorAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }
}
