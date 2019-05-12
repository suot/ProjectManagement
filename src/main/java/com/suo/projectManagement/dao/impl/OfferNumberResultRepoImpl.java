package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IOfferNumberResultRepo;
import com.suo.projectManagement.po.OfferNumberResult;
import com.suo.projectManagement.vo.echarts.StatisticalLine;
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
public class OfferNumberResultRepoImpl implements IOfferNumberResultRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int add(OfferNumberResult offerNumberResult) {
        String sql = "insert into offernumberresult(projectid, rnid, randomnumber, randomresult, groupnumber, orderIngroup, grouptype, stratificationFactor, organizationId, investigatorid,subjectid) values(?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, offerNumberResult.getProjectId(), offerNumberResult.getRnid(), offerNumberResult.getRandomNumber(), offerNumberResult.getRandomResult(), offerNumberResult.getGroupNumber(), offerNumberResult.getOrderInGroup(), offerNumberResult.getGroupType(),offerNumberResult.getStratificationFactor(), offerNumberResult.getOrganizationId(), offerNumberResult.getInvestigatorId(), offerNumberResult.getSubjectId());
    }

    @Override
    public List<OfferNumberResult> getAll(int projectID) {
        String sql = "select * from offernumberresult where projectid = ? order by offerdate";
        RowMapper<OfferNumberResult> rm = BeanPropertyRowMapper.newInstance(OfferNumberResult.class);
        return jdbcTemplate.query(sql, rm, projectID);
    }

    @Override
    public int countAll(int projectID) {
        String sql = "select count(*) from offernumberresult where projectid = ?";
        return jdbcTemplate.queryForObject(sql, int.class, projectID);
    }

    @Override
    public int countByOrg(int projectId, int organizationId) {
        String sql = "select count(*) from offernumberresult where projectid=? and organizationId=?";
        return jdbcTemplate.queryForObject(sql, int.class, projectId, organizationId);
    }

    @Override
    public List<StatisticalLine> countForEcharts(int projectId, int organizationId){
        String sql = "SELECT organizationid as orgId, DATE(offerdate) as sj, count(*) as number from offernumberresult where projectId=? and organizationid=? GROUP BY orgId, DATE(offerdate) ORDER BY orgId";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(StatisticalLine.class), projectId, organizationId);
    }

    @Override
    public List<Integer> getOrgIdList(int projectId){
        String sql = "select DISTINCT organizationid from offernumberresult where projectid=? ORDER BY organizationid";
        return jdbcTemplate.queryForList(sql, Integer.class, projectId);
    }

    @Override
    public List<String> getDateList(int projectId){
        String sql = "select DISTINCT DATE(offerdate) as sj from offernumberresult where projectid=? ORDER BY sj";
        return jdbcTemplate.queryForList(sql, String.class, projectId);
    }
}
