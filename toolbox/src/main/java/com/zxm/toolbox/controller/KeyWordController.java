package com.zxm.toolbox.controller;

import com.zxm.toolbox.dao.DaoFactory;
import com.zxm.toolbox.pojo.gt.Keyword;
import com.zxm.toolbox.vo.ui.IndexTableCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class KeyWordController implements Initializable {
    @FXML private TextField idText;
    @FXML private TableView<Keyword> keyWordTable;
    @FXML private TextField keyText;
    @FXML private TextField fullNameText;
    @FXML private TableColumn<Keyword, String> idxCol;
    @FXML private TableColumn<Keyword, Integer> idCol;
    @FXML private TableColumn<Keyword, String> keyCol;
    @FXML private TableColumn<Keyword, String> fullNameCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        keyWordTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //给表格添加序号列
        idxCol.setCellFactory((tableColumn) -> new IndexTableCell<>());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        List<Keyword> list = DaoFactory.getInstance().getKeywordDao().findAll();
        keyWordTable.setItems(FXCollections.observableList(list));
    }

    @FXML private void add() {
        keyText.setDisable(false);
        fullNameText.setDisable(false);
    }

    @FXML private void edit() {
        keyText.setDisable(false);
        fullNameText.setDisable(false);
        Keyword keyword = keyWordTable.getSelectionModel().getSelectedItem();
        idText.setText(String.valueOf(keyword.getId()));
        keyText.setText(keyword.getKey());
        fullNameText.setText(keyword.getFullName());
    }

    @FXML private void del() {
        int selectedIndex = keyWordTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex != -1){
            DaoFactory.getInstance().getKeywordDao().deleteById(
              keyWordTable.getSelectionModel().getSelectedItem().getId()
            );
            keyWordTable.getItems().remove(selectedIndex);
        }
    }

    @FXML private void cancel() {
        keyText.setText("");
        fullNameText.setText("");
        keyText.setDisable(true);
        fullNameText.setDisable(true);
    }

    public void save() {
        int id = idText.getText().equals("") ? 0 : Integer.parseInt(idText.getText());
        Keyword keyword = new Keyword(id, keyText.getText(), fullNameText.getText());
        if (id != 0)
            DaoFactory.getInstance().getKeywordDao().update(keyword);
        else
            DaoFactory.getInstance().getKeywordDao().save(keyword);
        keyWordTable.setItems(FXCollections.observableList(
                DaoFactory.getInstance().getKeywordDao().findAll()
        ));
        cancel();
    }
}
