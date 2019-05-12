package com.suo.projectManagement.vo.echarts;

/**
 * Created by TianSuo on 2018-07-27.
 */
public class AreaStyle{
    private Normal normal;

    public Normal getNormal() {
        return normal;
    }

    public void setNormal(Normal normal) {
        this.normal = normal;
    }

    public AreaStyle(Normal normal) {
        this.normal = normal;
    }

    @Override
    public String toString() {
        return "AreaStyle{" +
                "normal=" + normal +
                '}';
    }
}
