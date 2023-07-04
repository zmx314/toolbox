package com.zxm.toolbox.pojo.attn;

import java.util.Objects;

public class BasicBean {
    private int id;
    private String name;

    public BasicBean() {
    }

    public BasicBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicBean basicBean = (BasicBean) o;

        if (id != basicBean.id) return false;
        return Objects.equals(name, basicBean.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BasicBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
