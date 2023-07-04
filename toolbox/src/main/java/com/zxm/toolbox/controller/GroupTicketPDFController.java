package com.zxm.toolbox.controller;

import com.google.zxing.WriterException;
import com.zxm.toolbox.pojo.gt.Ticket;
import com.zxm.toolbox.resources.PropertiesFactory;
import com.zxm.toolbox.resources.Resources;
import com.zxm.toolbox.service.DataWriter;
import com.zxm.toolbox.service.TicketRecordLoader;
import com.zxm.toolbox.util.FxUtil;
import com.zxm.toolbox.util.QRCodeUtil;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GroupTicketPDFController implements Initializable {
    @FXML private BorderPane root;
    @FXML private Slider scaling;
    @FXML private Label scalingValue;
    //@FXML private Button zoomInBtn;
    //@FXML private Button zoomOutBtn;
    @FXML private VBox vBox;
    @FXML private TextField pathText;
    @FXML private File ticketRecordFile;
    private final DecimalFormat df = new DecimalFormat("0%");
    private final int cols = Integer.parseInt(
            PropertiesFactory.getInstance().getSettingProps().getValue("BoardingCardsColsInPdf"));
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scalingValue.setText(df.format(scaling.getValue()));
        scaling.setLabelFormatter(new StringConverter<>() {

            @Override
            public String toString(Double object) {
                return df.format(object);
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        });
        //缩放比例Slider组件交互事件监听器对象的添加
        scaling.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val,
                                             Number new_val) -> {
            //监听对象的事件监听回调函数的实现
            //设置图片在X轴上的缩放比例
            vBox.setScaleX(new_val.doubleValue());
            //设置图片在Y轴上的缩放比例
            vBox.setScaleY(new_val.doubleValue());
            //设置缩放标签文本label显示的文字
            scalingValue.setText(df.format(new_val));
        });
        //通过btn来设置缩放比例
//        zoomInBtn.setOnMouseClicked(event -> {
//            vBox.setScaleX(vBox.getScaleX() + 0.1);
//            vBox.setScaleY(vBox.getScaleY() + 0.1);
//        });
//        zoomOutBtn.setOnMouseClicked(event -> {
//            vBox.setScaleX(vBox.getScaleX() - 0.1);
//            vBox.setScaleY(vBox.getScaleY() - 0.1);
//        });
    }

    public void open() {
        ticketRecordFile = FxUtil.chooseExcelFile(root, pathText, "TicketRecordFileDir");
    }

    public void preview() {
        if (ticketRecordFile == null) {
            FxUtil.showWarningAlert("请先选择团体售票记录文件！");
            return;
        }
        //先把原来生成的内容全部清除
        ObservableList<Node> nodes = vBox.getChildren();
        vBox.getChildren().remove(0, nodes.size());
        //读取团体船票记录文件
        List<Ticket> tickets = TicketRecordLoader.loadTktRecords(ticketRecordFile);
        //船票上面的日期格式化输出时候使用
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (tickets == null || tickets.isEmpty())
            return;
        //开始生成预览图
        HBox rowHBox;
        VBox pageVBox;
        VBox tktVBox;
        ImageView imageView;
        BufferedImage bufferedImage;
        WritableImage writableImage;
        Label label;
        try {
            Ticket tkt;
            rowHBox = new HBox();
            pageVBox = new VBox();
            //预览图是把每张乘船券拼起来，cols为每行的数量，pageSize为每页数量，为了保持比例，pageSize为cols的平方
            //生成预览图的时候，rowBox用来存放一行乘船券，pageBox用来存放一页的乘船券
            //后续导出图片的时候，也是以页为单位导出，方面整页添加到pdf文件里面
            for (int i = 0; i < tickets.size(); i++) {
                tkt = tickets.get(i);
                tktVBox = new VBox();
                tktVBox.getStyleClass().add("ticket");
                bufferedImage = QRCodeUtil.getBufferedImage(tkt.getTktNo());
                writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView = new ImageView(writableImage);
                imageView.setFitHeight(250);
                imageView.setFitWidth(250);
                tktVBox.getChildren().add(imageView);
                label = new Label(tkt.getTktNo());
                label.getStyleClass().add("ticket-label");
                tktVBox.getChildren().add(label);
                label = new Label(tkt.getDepartPortName() + " - " + tkt.getArrivePortName());
                label.getStyleClass().add("ticket-label");
                tktVBox.getChildren().add(label);
                label = new Label(df.format(tkt.getDepartDate()) + "   " + tkt.getDepartTime());
                label.getStyleClass().add("ticket-label");
                tktVBox.getChildren().add(label);
                label = new Label(tkt.getPaxName() + " " + tkt.getClassName() + " - " + tkt.getSeatNo());
                label.getStyleClass().add("ticket-label");
                tktVBox.getChildren().add(label);

                int pageSize = cols * cols;
                if (i % pageSize == 0) {
                    pageVBox = new VBox();
                    vBox.getChildren().add(pageVBox);
                }
                if (i % cols == 0) {
                    rowHBox = new HBox();
                    pageVBox.getChildren().add(rowHBox);
                }
                rowHBox.getChildren().add(tktVBox);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //设置图片在X轴上的缩放比例
        vBox.setScaleX(scaling.getValue());
        //设置图片在Y轴上的缩放比例
        vBox.setScaleY(scaling.getValue());
    }

    public void export() {
        //创建缓存图片的目录，用来存放node导出的图片文件
        File dir = new File(Resources.HOME_DIR + "\\Temp\\Ticket\\");
        Path path = Paths.get(dir.getAbsolutePath());
        try {
            if (!dir.exists())
                Files.createDirectories(path);
            //将要导出到pdf文件里面的node，按页面大小导出为图片，放到缓存图片的目录里面，后面用来添加到pdf文件里。
            File imgFile;
            WritableImage writableImage;
            int i = 1;
            ObservableList<Node> nodes = vBox.getChildren();
            if (nodes.isEmpty()){
                FxUtil.showWarningAlert("无可导出数据！");
                return;
            }
            List<File> images = new ArrayList<>();
            for (Node node : nodes) {
                writableImage = node.snapshot(new SnapshotParameters(), null);
                imgFile = new File(dir.getAbsolutePath() + "\\temp-page-" + String.format("%03d", i++) + ".png");
                images.add(imgFile);
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", imgFile);
            }
            File pdfFile = FxUtil.savePdfFile(root, "ResultFileDir");
            if (pdfFile == null)
                return;
            DataWriter.createPdfForBoardingCard(pdfFile, images);
        } catch (IOException e) {
            e.printStackTrace();
            FxUtil.showWarningAlert("创建PDF文件时发生输入输出异常，任务已终止！");
        }
    }

}
