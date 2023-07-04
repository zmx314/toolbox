package com.zxm.toolbox.vo.ui;

import javafx.scene.control.TableCell;

public class IndexTableCell<S, T> extends TableCell<S, T> {
    @Override
    protected void updateItem(T var, boolean empty) {
        super.updateItem(var, empty);
        this.setText(null);
        this.setGraphic(null);
        if (!empty) {
            this.setText(java.lang.String.valueOf(this.getIndex() + 1));
        }
    }
}
