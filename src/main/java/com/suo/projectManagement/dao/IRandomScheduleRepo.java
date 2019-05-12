package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.RandomSchedule;
import com.suo.projectManagement.vo.RandomCell;

import java.util.List;

public interface IRandomScheduleRepo {
    int[] addSchedules(List<RandomSchedule> randomSchedules);
    int[] add(List<RandomCell> randomCells);
    List<RandomSchedule> getAll(int projectID);
    int delete(int projectID);//为了重新生成随机结果
    RandomSchedule find(int projectID, int randomNumber, String stratificationFactor);
    RandomSchedule find(int projectID, int groupNumber, int randomNumber, String stratificationFactor); // 2018.07.25，将orderInGroup修改为randomnumnber，by xuhang
    RandomSchedule getFirstUnUsed(int projectID, String stratificationFactor);
    int setToUsed(RandomSchedule randomSchedule);//设置使用情况
    int total(int projectid);
}
