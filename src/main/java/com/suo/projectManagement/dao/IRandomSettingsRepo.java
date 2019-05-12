package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.RandomSettings;

public interface IRandomSettingsRepo {
    public int add(RandomSettings randomSettings);
    public RandomSettings find(int projectID);
    public int update(RandomSettings randomSettings);
}
