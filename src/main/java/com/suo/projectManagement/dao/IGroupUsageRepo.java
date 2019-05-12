package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.GroupUsage;

/**
 * Created by wuxu on 2018/5/30.
 */
public interface IGroupUsageRepo {
    int add(GroupUsage groupUsage);
    int update(GroupUsage groupUsage);
    GroupUsage find(int projectID, String stratificationFactor);
}
