package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.Stratification;

import java.util.List;

public interface IStratificationRepo {
    int add(Stratification fcxxes);
    List<Stratification> getAll(int projectID);
    int delete(int projectID);//为了重新生成随机序列
}
