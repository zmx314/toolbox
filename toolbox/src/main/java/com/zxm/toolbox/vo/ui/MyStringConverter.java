package com.zxm.toolbox.vo.ui;

import com.zxm.toolbox.pojo.attn.BasicBean;
import javafx.util.StringConverter;

public class MyStringConverter<T extends BasicBean> extends StringConverter<T> {
    @Override
    public String toString(T t) {
        if (t == null)
            return "";
            return t.getId() + " - " + t.getName();
    }

    @Override
    public T fromString(String string) {
        return null;
    }
}
