package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IRandomSettingsRepo;
import com.suo.projectManagement.po.RandomSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wuxu on 2018/5/30.
 */
@Repository
public class RandomSettingsRepoImpl implements IRandomSettingsRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int add(RandomSettings randomSettings) {
        String sql = "insert into randomsettings(projectid, blindtype, simplesize, isctrlsubenroll, randomseed, numofgroup, balanceType, isStratifi, lengthofblock, numofblock, unfoldmgr) values(?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, randomSettings.getProjectId(), randomSettings.getBlindType(), randomSettings.getSimpleSize(), randomSettings.getIsCtrlSubEnroll(), randomSettings.getRandomSeed(), randomSettings.getNumOfGroup(), randomSettings.getBalanceType(), randomSettings.getIsStratifi(), randomSettings.getLengthOfBlock(), randomSettings.getNumOfBlock(), randomSettings.getUnFoldMgr());
    }

    @Override
    public RandomSettings find(int projectID) {
        String sql = "select * from randomsettings where projectid=?";
        RowMapper<RandomSettings> rm = BeanPropertyRowMapper.newInstance(RandomSettings.class);
        List<RandomSettings> randomSettingsList = jdbcTemplate.query(sql, rm, projectID);
        if(randomSettingsList.size()>0)
            return randomSettingsList.get(0);
        else
            return null;
    }

    @Override
    public int update(RandomSettings randomSettings) {
        String sql = "update randomsettings set projectid=?, blindtype=?, simplesize=?, isctrlsubenroll=?, randomseed=?, numofgroup=?, balanceType=?, isStratifi=?, lengthofblock=?, numofblock=?, unfoldmgr=? where id=?";
        return jdbcTemplate.update(sql, randomSettings.getProjectId(), randomSettings.getBlindType(), randomSettings.getSimpleSize(), randomSettings.getIsCtrlSubEnroll(), randomSettings.getRandomSeed(), randomSettings.getNumOfGroup(), randomSettings.getBalanceType(), randomSettings.getIsStratifi(), randomSettings.getLengthOfBlock(), randomSettings.getNumOfBlock(), randomSettings.getUnFoldMgr(), randomSettings.getId());
    }
}
