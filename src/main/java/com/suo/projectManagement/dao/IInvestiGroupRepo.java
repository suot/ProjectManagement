package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.InvestiGroup;
import com.suo.projectManagement.vo.InvestiGroupInfo;

import java.util.List;

public interface IInvestiGroupRepo {
    int add(InvestiGroupInfo invesiGroupInfo);
    List<InvestiGroup> get(int projectId);
    String getGroupName(int projectId,String typeName);
    InvestiGroup get(int projectId, String groupType);
}
