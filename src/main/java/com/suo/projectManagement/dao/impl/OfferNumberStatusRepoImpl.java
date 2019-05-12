package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IOfferNumberStatusRepo;
import com.suo.projectManagement.po.OfferNumberStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wuxu on 2018/5/30.
 */
@Repository
public class OfferNumberStatusRepoImpl implements IOfferNumberStatusRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int add(OfferNumberStatus offerNumberStatus) {
        String sql = "insert into offernumberstatus(projectid, stratificationfactor, organizationId, groupnumber, randomnumber, orderIngroup, currentnumber, investigatorid, randomnumberid) values(?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, offerNumberStatus.getProjectId(), offerNumberStatus.getStratificationFactor(), offerNumberStatus.getOrganizationId(), offerNumberStatus.getGroupNumber(), offerNumberStatus.getRandomNumber(), offerNumberStatus.getOrderInGroup(), offerNumberStatus.getCurrentNumber(), offerNumberStatus.getInvestigatorId(),offerNumberStatus.getRandomNumberId());
    }
//TODO:分为两个方法 update组号和update组内序号
    @Override
    public int update(OfferNumberStatus offerNumberStatus) {
        String sql = "update offernumberstatus set projectid=?, stratificationfactor=?, organizationId=?, groupnumber=?, randomnumber=?, orderIngroup=?, currentnumber=?, investigatorid=?, randomnumberid=? where id=?";
        return jdbcTemplate.update(sql, offerNumberStatus.getProjectId(), offerNumberStatus.getStratificationFactor(), offerNumberStatus.getOrganizationId(), offerNumberStatus.getGroupNumber(), offerNumberStatus.getRandomNumber(), offerNumberStatus.getOrderInGroup(), offerNumberStatus.getCurrentNumber(), offerNumberStatus.getInvestigatorId(),offerNumberStatus.getRandomNumberId(),offerNumberStatus.getId());
    }

    @Override
    public OfferNumberStatus find(int projectID, String stratificationFactor, int organizationID) {
        String sql = "select * from offernumberstatus where projectid=? and stratificationfactor=? and organizationid=? order by randomnumber";

        List<OfferNumberStatus> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(OfferNumberStatus.class), projectID, stratificationFactor, organizationID);
        if(list.size()>0){
            return list.get(0);
        }else
            return null;
    }
//TODO:可能也需要order by
    @Override
    public OfferNumberStatus find(int projectID, String stratificationFactor) {
        String sql = "select * from offernumberstatus where projectid=? and stratificationfactor=? order by randomnumber";
        List<OfferNumberStatus> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(OfferNumberStatus.class), projectID, stratificationFactor);
        if(list.size()>0){
            return list.get(0);
        }else
            return null;
    }

    @Override
    public OfferNumberStatus findByGroupNumber(int projectID, String stratificationFactor, int groupNumber) {
        String sql = "select * from offernumberstatus where projectid=? and stratificationfactor=? and groupnumber=?";
        List<OfferNumberStatus> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(OfferNumberStatus.class), projectID, stratificationFactor, groupNumber);
        if(list.size()>0){
            return list.get(0);
        }else
            return null;
    }
}
