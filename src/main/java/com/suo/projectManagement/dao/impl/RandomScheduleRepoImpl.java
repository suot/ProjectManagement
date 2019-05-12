package com.suo.projectManagement.dao.impl;

import com.suo.projectManagement.dao.IRandomScheduleRepo;
import com.suo.projectManagement.po.RandomSchedule;
import com.suo.projectManagement.vo.RandomCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by wuxu on 2018/5/30.
 */
@Repository
public class RandomScheduleRepoImpl implements IRandomScheduleRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int[] addSchedules(List<RandomSchedule> randomSchedules) {
        String sql = "insert into randomschedule(projectid, randomnumber, randomresult, groupnumber, orderingroup, grouptype, stratificationfactor) values(?,?,?,?,?,?,?)";

        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(0, randomSchedules.get(i).getProjectId());
                preparedStatement.setInt(1, randomSchedules.get(i).getRandomNumber());
                preparedStatement.setString(2, randomSchedules.get(i).getRandomResult());
                preparedStatement.setInt(3, randomSchedules.get(i).getGroupNumber());
                preparedStatement.setInt(4, randomSchedules.get(i).getOrderInGroup());
                preparedStatement.setString(5, randomSchedules.get(i).getGroupType());
                preparedStatement.setString(6, randomSchedules.get(i).getStratificationFactor());
            }

            @Override
            public int getBatchSize() {
                return randomSchedules.size();
            }
        });
    }


    @Override
    public int[] add(List<RandomCell> randomCells) {
        String sql = "insert into randomschedule(projectid, randomnumber, randomresult, groupnumber, orderingroup, grouptype, stratificationfactor) values(?,?,?,?,?,?,?)";

        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, randomCells.get(i).getProjectId());
                preparedStatement.setInt(2, randomCells.get(i).getRandomNumber());
                preparedStatement.setString(3, Double.toString(randomCells.get(i).getRandomResult()));
                preparedStatement.setInt(4, randomCells.get(i).getGroupNumber());
                preparedStatement.setInt(5, randomCells.get(i).getOrderInGroup());
                preparedStatement.setString(6, randomCells.get(i).getGroupType());
                preparedStatement.setString(7, randomCells.get(i).getStratificationFactor());
            }

            @Override
            public int getBatchSize() {
                return randomCells.size();
            }
        });
    }


    @Override
    public List<RandomSchedule> getAll(int projectID) {
        String sql = "select * from randomschedule where projectid = ?";
        RowMapper<RandomSchedule> rm = BeanPropertyRowMapper.newInstance(RandomSchedule.class);
        return jdbcTemplate.query(sql,rm, projectID);
    }

    @Override
    public int delete(int projectID) {
        String sql = "delete from randomschedule where projectid=?";
        return jdbcTemplate.update(sql, projectID);
    }

    @Override
    public int total(int projectid){
        String sql = "select count(*) from randomschedule where projectid = ?";
        return jdbcTemplate.queryForObject(sql, int.class, projectid);
    }


    @Override
    public RandomSchedule find(int projectID, int randomNumber, String stratificationFactor) {
        String sql = "select * from randomschedule where projectid=? and randomnumber=? and stratificationfactor=?";
        List<RandomSchedule> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(RandomSchedule.class), projectID, randomNumber, stratificationFactor);
        return list.size()>0?list.get(0):null;
    }

    @Override
    public RandomSchedule find(int projectID, int groupNumber, int randomNumber, String stratificationFactor) { //2018.07.25，将参数orderInGroup改为randomNumber, by xuhang
        String sql = "select * from randomschedule where projectid=? and groupnumber=? and randomnumber=? and stratificationfactor=?"; // 2018.07.25，orderingroup 改为 randomnumber，按randomnumber顺序取号
        List<RandomSchedule> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(RandomSchedule.class), projectID, groupNumber, randomNumber, stratificationFactor);
        return list.size()>0?list.get(0):null;
    }

    @Override
    public RandomSchedule getFirstUnUsed(int projectID, String stratificationFactor) {
        String sql = "select * from randomschedule where projectid=? and stratificationfactor=? and isused !=?";
        List<RandomSchedule> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(RandomSchedule.class), projectID, stratificationFactor, "true");
        return list.size()>0?list.get(0):null;
    }

    @Override
    public int setToUsed(RandomSchedule randomSchedule) {
        String sql = "update randomschedule set isused=? where id=?";
        return jdbcTemplate.update(sql, "true", randomSchedule.getId());
    }
}
