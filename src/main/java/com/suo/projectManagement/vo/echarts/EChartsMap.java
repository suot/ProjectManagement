package com.suo.projectManagement.vo.echarts;

import java.util.Arrays;

/**
 * Created by TianSuo on 2018-07-27.
 */
public class EChartsMap {
    private Object[] color;
    private Object[] legendData;
    private Object[] xAxisData;

    public EChartsMap(Object[] color, Object[] legendData, Object[] xAxisData) {
        this.color = color;
        this.legendData = legendData;
        this.xAxisData = xAxisData;
    }

    public Object[] getColor() {
        return color;
    }

    public void setColor(Object[] color) {
        this.color = color;
    }

    public Object[] getLegendData() {
        return legendData;
    }

    public void setLegendData(Object[] legendData) {
        this.legendData = legendData;
    }

    public Object[] getxAxisData() {
        return xAxisData;
    }

    public void setxAxisData(Object[] xAxisData) {
        this.xAxisData = xAxisData;
    }

    @Override
    public String toString() {
        return "EChartsMap{" +
                "color=" + Arrays.toString(color) +
                ", legendData=" + Arrays.toString(legendData) +
                ", xAxisData=" + Arrays.toString(xAxisData) +
                '}';
    }
}
