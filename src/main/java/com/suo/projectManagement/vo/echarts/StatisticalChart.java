package com.suo.projectManagement.vo.echarts;

/**
 * Created by TianSuo on 2018-07-27.
 */
public class StatisticalChart {
    private String name;
    private String type;
    private AreaStyle areaStyle;
    private Integer[] data;

    public StatisticalChart(String name, String type, AreaStyle areaStyle, Integer[] data) {
        this.name = name;
        this.type = type;
        this.areaStyle = areaStyle;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AreaStyle getAreaStyle() {
        return areaStyle;
    }

    public void setAreaStyle(AreaStyle areaStyle) {
        this.areaStyle = areaStyle;
    }

    public Integer[] getData() {
        return data;
    }

    public void setData(Integer[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StatisticalChart{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", areaStyle=" + areaStyle +
                ", data=" + data +
                '}';
    }
}

