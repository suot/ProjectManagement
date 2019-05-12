package com.suo.projectManagement.vo.echarts;

/**
 * Created by TianSuo on 2018-07-27.
 */
public class Normal {
    private String data;

    public Normal(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Normal{" +
                "data='" + data + '\'' +
                '}';
    }
}
