package com.zxm.toolbox;

import com.zxm.toolbox.resources.Resources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MainApplication extends Application {
    public static final Logger LOGGER = LogManager.getLogger(MainApplication.class);
    @Override
    public void start(Stage stage) throws IOException {

        initResource();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("工具箱");
        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("/img/main.gif")));
        stage.setMaximized(true);
        stage.setScene(scene);
//        stage.getScene().getStylesheets().save("org/kordamp/bootstrapfx/bootstrapfx.css");
        //统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
//        String os = System.getProperty("os.name","generic").toLowerCase(Locale.US);
        scene.getRoot().setStyle("-fx-font-family: 'Arial'; -fx-font-size: 12px");
        stage.show();
    }

    public void initResource() {
        if (!Resources.init()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("警告");
            alert.setHeaderText("程序初始化过程中发生异常");
//            alert.setContentText("点击确定退出程序！");
            alert.showAndWait();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}