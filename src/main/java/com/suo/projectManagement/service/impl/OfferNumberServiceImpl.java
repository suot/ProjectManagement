package com.suo.projectManagement.service.impl;

import com.suo.projectManagement.dao.*;
import com.suo.projectManagement.vo.ReturnEntity;
import com.suo.projectManagement.po.*;
import com.suo.projectManagement.service.IOfferNumberService;

import com.suo.projectManagement.vo.echarts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wuxu on 2018/5/30.
 */
@Service
public class OfferNumberServiceImpl implements IOfferNumberService {

    @Autowired
    IRandomSettingsRepo randomSettingsRepo; //随机信息表
    @Autowired
    IOfferNumberResultRepo offerNumberResultRepo; //取号结果表
    @Autowired
    IOfferNumberStatusRepo offerNumberStatusRepo;   //取号情况表
    @Autowired
    IRandomScheduleRepo randomScheduleRepo; //随机结果表
    @Autowired
    IGroupUsageRepo groupUsageRepo; //取号信息表
    @Autowired
    IInvestiOrganizationRepo investiOrganizationRepo;  //分组信息
    @Autowired
    IOrganizationRepo organizationRepo;

    /**
     * 不要求平衡
     * @param projectId
     * @param stratificationFactor
     * @param investigatorId
     * @param subjectId
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public ReturnEntity competitiveRan(int projectId, String stratificationFactor, String investigatorId, String subjectId, int organizationId) {
        RandomSettings randomSettings = randomSettingsRepo.find(projectId);
        int currentOrgEnrollNum =investiOrganizationRepo.getEnrollNumber(projectId,organizationId);
        int currentOrgTotal = offerNumberResultRepo.countByOrg(projectId, organizationId);
        ReturnEntity returnEntity=new ReturnEntity();
        //控制样本量 且已经超出了
        if (randomSettings.getIsCtrlSubEnroll().equals("yes") &&  offerNumberResultRepo.countAll(projectId)>=randomSettings.getSimpleSize()){
            returnEntity.setStatus("ERROR");
            returnEntity.setMessage("已经超出样本量,不能取号");
            return returnEntity;
        }
        else if(currentOrgEnrollNum!=0 &&  currentOrgTotal+1>currentOrgEnrollNum){
            returnEntity.setStatus("ERROR");
            returnEntity.setMessage("本组织已超过限制参与者数量,不能取号");
            return returnEntity;
        }
        else{
            /**
             * 有无分层 取号条件类似 其他步骤一样
             * 1 从 取号情况表 取得当前的随机号码
             * 2 随机号码+1 从 随机结果表 取得信息
             * 3 插入 取号结果表
             * 4 插入 取号情况表
             * 5 更新随机结果表
             */

            OfferNumberStatus offerNumberStatus = offerNumberStatusRepo.find(projectId, stratificationFactor);//不要求平衡取号 investigatorId 不作为查询条件为空
            if (offerNumberStatus ==null){//没有数据,说明第一次取号
                int next_number = 1;//next_number 实际上就是 随机号ID.
                RandomSchedule randomSchedule = randomScheduleRepo.find(projectId, next_number,stratificationFactor);
                //组装取号结果
                OfferNumberResult offerNumberResult = new OfferNumberResult();
                offerNumberResult.setProjectId(projectId);
                offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                offerNumberResult.setGroupType(randomSchedule.getGroupType());
                offerNumberResult.setStratificationFactor(stratificationFactor);
                offerNumberResult.setSubjectId(subjectId);
                offerNumberResult.setInvestigatorId(investigatorId);//插不插没什么用
                offerNumberResult.setOrganizationId(organizationId);
                offerNumberResultRepo.add(offerNumberResult);

                //组装 qhqk 相关信息
                offerNumberStatus = new OfferNumberStatus();
                offerNumberStatus.setProjectId(projectId);
                offerNumberStatus.setStratificationFactor(stratificationFactor);
                offerNumberStatus.setCurrentNumber(randomSchedule.getRandomNumber());//最主要是update 当前编号
                offerNumberStatus.setRandomNumberId(randomSchedule.getId());
                offerNumberStatus.setOrganizationId(organizationId);
                offerNumberStatus.setInvestigatorId(investigatorId);
                offerNumberStatusRepo.add(offerNumberStatus);
                //更新随机结果表
                randomSchedule.setIsUsed("T");
                randomScheduleRepo.setToUsed(randomSchedule);
                returnEntity.setStatus("SUCCESS");
                returnEntity.setMessage("成功");
                returnEntity.setO(offerNumberResult);
                return returnEntity;
            }
            else{
                //qhqk表已经有数据了,那么就继续取号
                int next_number = offerNumberStatus.getCurrentNumber()+1;
                //由于之前判断过总取号数小于样本量 因此总取号+1 最多也是等于样本量
                //同时因为 分层的数量一定大于样本量
                // 所以这里不需要判断

                //这里有一种情况需要处理
                //在不需要分层的时候 随机数==样本量 需要进行判断 否则会取不到
                if (randomSettings.getIsStratifi().equals("no")){
                    if (next_number>randomSettings.getSimpleSize()){
                        returnEntity.setStatus("ERROR");
                        returnEntity.setMessage("本次试验已无号可取了");
                        return returnEntity;
                    }
                }

                RandomSchedule randomSchedule = randomScheduleRepo.find(projectId, next_number,stratificationFactor);
                //组装取号结果
                OfferNumberResult offerNumberResult = new OfferNumberResult();
                offerNumberResult.setProjectId(projectId);
                offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                offerNumberResult.setGroupType(randomSchedule.getGroupType());
                offerNumberResult.setStratificationFactor(stratificationFactor);
                offerNumberResult.setSubjectId(subjectId);
                offerNumberResult.setInvestigatorId(investigatorId);
                offerNumberResult.setOrganizationId(organizationId);
                offerNumberResultRepo.add(offerNumberResult);
                //更新 qhqk 相关信息
                offerNumberStatus.setCurrentNumber(randomSchedule.getRandomNumber());//最主要是update 当前编号
                offerNumberStatus.setRandomNumberId(randomSchedule.getId());
                offerNumberStatusRepo.update(offerNumberStatus);
                //更新随机结果表
                randomSchedule.setIsUsed("T");
                randomScheduleRepo.setToUsed(randomSchedule);

                returnEntity.setStatus("SUCCESS");
                returnEntity.setMessage("成功");
                returnEntity.setO(offerNumberResult);
                return returnEntity;
            }
        }
    }

    /**
     * 绝对平衡
     * @param projectId
     * @param stratificationFactor
     * @param investigatorId
     * @param subjectId
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public ReturnEntity balancedRan(int projectId, String stratificationFactor, String investigatorId, String subjectId,int organizationId) {
        RandomSettings randomSettings = randomSettingsRepo.find(projectId);
        int currentOrgEnrollNum =investiOrganizationRepo.getEnrollNumber(projectId,organizationId);
        int currentOrgTotal = offerNumberResultRepo.countByOrg(projectId, organizationId);
        ReturnEntity returnEntity = new ReturnEntity();
        //控制样本量 且已经超出了
        if (randomSettings.getIsCtrlSubEnroll().equals("yes") &&  offerNumberResultRepo.countAll(projectId)>=randomSettings.getSimpleSize()){
            returnEntity.setStatus("ERROR");
            returnEntity.setMessage("已经超出样本量,不能取号");
            return returnEntity;
        }
        else if(currentOrgEnrollNum!=0 && currentOrgTotal+1>currentOrgEnrollNum){
            returnEntity.setStatus("ERROR");
            returnEntity.setMessage("本组织已超过限制参与者数量,不能取号");
            return returnEntity;
        }
        else{
            OfferNumberStatus offerNumberStatus = offerNumberStatusRepo.find(projectId, stratificationFactor,organizationId);
            if (offerNumberStatus==null){
                GroupUsage groupUsage = groupUsageRepo.find(projectId, stratificationFactor);
                //如果在信息表里也没有,说明这是在该分层的第一次取号,那么就取第一组第一个号码
                /**
                 *  1 根据 组号 组内号码 分层情况 取随机信息
                 *  2 插入 取号结果表
                 *  3 插入 取号情况表
                 *  4 插入 取号信息表
                 *  5 更新 随机结果表
                 *  6 返回 试验组号
                 */
                if (groupUsage==null){
                    RandomSchedule randomSchedule = randomScheduleRepo.find(projectId, 1, 1, stratificationFactor);
                    //组装取号结果
                    OfferNumberResult offerNumberResult = new OfferNumberResult();
                    offerNumberResult.setProjectId(projectId);
                    offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                    offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                    offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                    offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                    offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                    offerNumberResult.setGroupType(randomSchedule.getGroupType());
                    offerNumberResult.setStratificationFactor(stratificationFactor);
                    offerNumberResult.setSubjectId(subjectId);
                    offerNumberResult.setInvestigatorId(investigatorId);
                    offerNumberResult.setOrganizationId(organizationId);
                    offerNumberResultRepo.add(offerNumberResult);

                    //组装 qhqk 相关信息
                    offerNumberStatus = new OfferNumberStatus();
                    offerNumberStatus.setProjectId(projectId);
                    offerNumberStatus.setStratificationFactor(stratificationFactor);
                    offerNumberStatus.setGroupNumber(1);
                    offerNumberStatus.setRandomNumber(1);
                    offerNumberStatus.setOrderInGroup(randomSchedule.getOrderInGroup());
                    offerNumberStatus.setCurrentNumber(randomSchedule.getRandomNumber());
                    offerNumberStatus.setInvestigatorId(investigatorId);
                    offerNumberStatus.setRandomNumberId(randomSchedule.getId());
                    offerNumberStatus.setOrganizationId(organizationId);
                    offerNumberStatusRepo.add(offerNumberStatus);
                    //组装取号信息表
                    groupUsage = new GroupUsage();
                    groupUsage.setProjectId(projectId);
                    groupUsage.setStratificationFactor(stratificationFactor);
                    groupUsage.setGroupNumber(1);
                    groupUsageRepo.add(groupUsage);
                    //更新随机结果
                    randomSchedule.setIsUsed("T");
                    randomScheduleRepo.setToUsed(randomSchedule);
                    returnEntity.setStatus("SUCCESS");
                    returnEntity.setMessage("成功");
                    returnEntity.setO(offerNumberResult);
                    return returnEntity;
                }
                /**
                 * 在取号信息中有内容,说明有别的研究者已经取过号了,那么顺延取下一个分组,如果分组没有,就不取了

                 */
                else{
                    int groupNumber =groupUsage.getGroupNumber();
                    if(groupNumber+1<=randomSettings.getNumOfBlock()){
                        /**
                         *  1 根据 组号 组内号码 分层情况 取随机信息
                         *  2 插入 取号结果表
                         *  3 插入 取号情况表
                         *  4 更新 取号信息表
                         *  5 更新 随机结果表
                         *  6 返回 试验组号
                         */
                        int randomnumber = groupNumber * randomSettings.getLengthOfBlock() + 1; //get first randomnumber of new group
                        //有新的组 一定有新的号可取
                        RandomSchedule randomSchedule = randomScheduleRepo.find(projectId, groupNumber+1, randomnumber, stratificationFactor);
                        //组装取号结果
                        OfferNumberResult offerNumberResult = new OfferNumberResult();
                        offerNumberResult.setProjectId(projectId);
                        offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                        offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                        offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                        offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                        offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                        offerNumberResult.setGroupType(randomSchedule.getGroupType());
                        offerNumberResult.setStratificationFactor(stratificationFactor);
                        offerNumberResult.setSubjectId(subjectId);
                        offerNumberResult.setOrganizationId(organizationId);
                        offerNumberResult.setInvestigatorId(investigatorId);
                        offerNumberResultRepo.add(offerNumberResult);
                        //组装QHQK
                        offerNumberStatus = new OfferNumberStatus();
                        offerNumberStatus.setProjectId(projectId);
                        offerNumberStatus.setStratificationFactor(stratificationFactor);
                        offerNumberStatus.setGroupNumber(groupNumber+1);
                        offerNumberStatus.setRandomNumber(randomnumber);
                        offerNumberStatus.setOrderInGroup(randomSchedule.getOrderInGroup());
                        offerNumberStatus.setCurrentNumber(randomSchedule.getRandomNumber());
                        offerNumberStatus.setInvestigatorId(investigatorId);
                        offerNumberStatus.setRandomNumberId(randomSchedule.getId());
                        offerNumberStatus.setOrganizationId(organizationId);
                        offerNumberStatusRepo.add(offerNumberStatus);
                        //update QHXX
                        groupUsage.setGroupNumber(groupNumber+1);
                        groupUsageRepo.update(groupUsage);

                        randomSchedule.setIsUsed("T");
                        randomScheduleRepo.setToUsed(randomSchedule);
                        returnEntity.setStatus("SUCCESS");
                        returnEntity.setMessage("成功");
                        returnEntity.setO(offerNumberResult);
                        return returnEntity;
                    }
                    else{
                        returnEntity.setStatus("ERROR");
                        returnEntity.setMessage("已没有剩余的随机块,不能取号!");
                        return returnEntity;
                    }
                }
            }
            //如果取号情况里面有,那么读一下组内编号
            else{
                int orderInGroup = offerNumberStatus.getOrderInGroup();
                int randomnumber = offerNumberStatus.getRandomNumber();
                int maxRandomnumberInGroup = offerNumberStatus.getGroupNumber() * randomSettings.getLengthOfBlock();
                //如果区段内还有号码可取
                /**
                 * 1 获得 随机结果信息
                 * 2 插入 取号结果表
                 * 3 更新 取号情况表
                 * 4 更新 随机结果信息
                 * 4 返回 试验组号
                 */
                if (randomnumber+1<=maxRandomnumberInGroup){
                    //组内有号 一定有号可取
                    RandomSchedule randomSchedule = randomScheduleRepo.find(projectId, offerNumberStatus.getGroupNumber(), randomnumber + 1, stratificationFactor);
                    //组装取号结果
                    OfferNumberResult offerNumberResult = new OfferNumberResult();
                    offerNumberResult.setProjectId(projectId);
                    offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                    offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                    offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                    offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                    offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                    offerNumberResult.setGroupType(randomSchedule.getGroupType());
                    offerNumberResult.setStratificationFactor(stratificationFactor);
                    offerNumberResult.setSubjectId(subjectId);
                    offerNumberResult.setInvestigatorId(investigatorId);
                    offerNumberResult.setOrganizationId(organizationId);
                    offerNumberResultRepo.add(offerNumberResult);
                    //更新qhqk表
                    offerNumberStatus.setRandomNumber(randomnumber + 1);
                    offerNumberStatus.setOrderInGroup(randomSchedule.getOrderInGroup());
                    offerNumberStatus.setRandomNumberId(randomSchedule.getId());
                    offerNumberStatus.setCurrentNumber(randomSchedule.getRandomNumber());
                    offerNumberStatusRepo.update(offerNumberStatus);

                    //更新随机结果信息
                    randomSchedule.setIsUsed("T");
                    randomScheduleRepo.setToUsed(randomSchedule);

                    returnEntity.setStatus("SUCCESS");
                    returnEntity.setMessage("成功");
                    returnEntity.setO(offerNumberResult);
                    return returnEntity;
                }
                //如果组内取完没有可取的号码了,就去取号信息里面去取组号,因为取号qhqk表中有数据说明qhxx一定结果不是null
                else{
                    GroupUsage groupUsage = groupUsageRepo.find(projectId, stratificationFactor);
                    int zh =groupUsage.getGroupNumber();
                    //如果还有其他的组可以取,那么就取下一组
                    /**
                     *  1 根据 组号 组内号码 分层情况 去随机信息
                     *  2 插入 取号结果表
                     *  3 更新 取号情况表
                     *  4 更新 区号信息表
                     *  5 返回 试验组号
                     */
                    if(zh+1<=randomSettings.getNumOfBlock()){
                        randomnumber = zh * randomSettings.getLengthOfBlock() + 1; //caculate first randomnumber of next group
                        RandomSchedule randomSchedule = randomScheduleRepo.find(projectId, zh+1, randomnumber, stratificationFactor);
                        //组装取号结果
                        OfferNumberResult offerNumberResult = new OfferNumberResult();
                        offerNumberResult.setProjectId(projectId);
                        offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                        offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                        offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                        offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                        offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                        offerNumberResult.setGroupType(randomSchedule.getGroupType());
                        offerNumberResult.setStratificationFactor(stratificationFactor);
                        offerNumberResult.setSubjectId(subjectId);
                        offerNumberResult.setInvestigatorId(investigatorId);
                        offerNumberResult.setOrganizationId(organizationId);
                        offerNumberResultRepo.add(offerNumberResult);
                        //update QHQK
                        offerNumberStatus.setGroupNumber(zh+1);
                        offerNumberStatus.setRandomNumber(randomnumber);
                        offerNumberStatus.setOrderInGroup(randomSchedule.getOrderInGroup());
                        offerNumberStatus.setCurrentNumber(randomSchedule.getRandomNumber());
                        offerNumberStatus.setRandomNumberId(randomSchedule.getId());
                        offerNumberStatusRepo.update(offerNumberStatus);
                        //UPDATE qhxx
                        groupUsage.setGroupNumber(zh+1);
                        groupUsageRepo.update(groupUsage);

                        randomSchedule.setIsUsed("T");
                        randomScheduleRepo.setToUsed(randomSchedule);
                        returnEntity.setStatus("SUCCESS");
                        returnEntity.setMessage("成功");
                        returnEntity.setO(offerNumberResult);
                        return returnEntity;
                    }
                    else{
                        returnEntity.setStatus("ERROR");
                        returnEntity.setMessage("已没有剩余的随机块,不能取号!");
                        return returnEntity;
                    }
                }
            }
        }
    }

    /**
     * 相对平衡
     * @param projectId
     * @param stratificationFactor
     * @param investigatorId
     * @param subjectId
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public ReturnEntity mixedRan(int projectId, String stratificationFactor, String investigatorId, String subjectId,int organizationId) {
        RandomSettings randomSettings = randomSettingsRepo.find(projectId);
        int currentOrgEnrollNum =investiOrganizationRepo.getEnrollNumber(projectId,organizationId);
        int currentOrgTotal = offerNumberResultRepo.countByOrg(projectId, organizationId);
        ReturnEntity returnEntity = new ReturnEntity();
        //控制样本量 且已经超出了
        if (randomSettings.getIsCtrlSubEnroll().equals("yes") &&  offerNumberResultRepo.countAll(projectId)>=randomSettings.getSimpleSize()){
            returnEntity.setStatus("ERROR");
            returnEntity.setMessage("已经超出样本量,不能取号");
            return returnEntity;
        }
        //TODO:限制受试者数 超过取号也不能取了
        else if(currentOrgEnrollNum!=0 && currentOrgTotal+1 >currentOrgEnrollNum ){
            returnEntity.setStatus("ERROR");
            returnEntity.setMessage("本组织已超过限制参与者数量,不能取号");
            return returnEntity;
        }
        else{
            OfferNumberStatus offerNumberStatus = offerNumberStatusRepo.find(projectId, stratificationFactor,organizationId);
            //如果在取号情况里没有
            if (offerNumberStatus==null){
                GroupUsage groupUsage = groupUsageRepo.find( projectId, stratificationFactor);
                //如果在信息表里也没有,说明这是该分层第一次取号,那么就取第一组第一个号码
                /**
                 *  1 根据 组号 组内随机号码 分层情况 取随机信息
                 *  2 插入 取号结果表
                 *  3 插入 取号情况表
                 *  4 插入 区号信息表
                 *  5 更新 随机结果表
                 *  6 返回 试验组号
                 */
                if (groupUsage==null){
                    RandomSchedule randomSchedule = randomScheduleRepo.find(projectId, 1, 1, stratificationFactor);
                    //组装取号结果
                    OfferNumberResult offerNumberResult = new OfferNumberResult();
                    offerNumberResult.setProjectId(projectId);
                    offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                    offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                    offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                    offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                    offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                    offerNumberResult.setGroupType(randomSchedule.getGroupType());
                    offerNumberResult.setStratificationFactor(stratificationFactor);
                    offerNumberResult.setSubjectId(subjectId);
                    offerNumberResult.setInvestigatorId(investigatorId);
                    offerNumberResult.setOrganizationId(organizationId);
                    offerNumberResultRepo.add(offerNumberResult);
                    //生成取号情况
                    offerNumberStatus = new OfferNumberStatus();
                    offerNumberStatus.setProjectId(projectId);
                    offerNumberStatus.setStratificationFactor(stratificationFactor);
                    offerNumberStatus.setGroupNumber(1);
                    offerNumberStatus.setRandomNumber(1);
                    offerNumberStatus.setOrderInGroup(randomSchedule.getOrderInGroup());
                    offerNumberStatus.setCurrentNumber(randomSchedule.getRandomNumber());
                    offerNumberStatus.setInvestigatorId(investigatorId);
                    offerNumberStatus.setRandomNumberId(randomSchedule.getId());
                    offerNumberStatus.setOrganizationId(organizationId);
                    offerNumberStatusRepo.add(offerNumberStatus);
                    //生成取号信息
                    groupUsage = new GroupUsage();
                    groupUsage.setProjectId(projectId);
                    groupUsage.setStratificationFactor(stratificationFactor);
                    groupUsage.setGroupNumber(1);
                    groupUsageRepo.add(groupUsage);
                    //更新随机结果表
                    randomSchedule.setIsUsed("T");
                    randomScheduleRepo.setToUsed(randomSchedule);

                    returnEntity.setStatus("SUCCESS");
                    returnEntity.setMessage("成功");
                    returnEntity.setO(offerNumberResult);
                    return returnEntity;
                }
                /**
                 * 在取号信息中有内容,说明有别的研究者已经取过号了,那么顺延取下一个分组,
                 * 如果分组没有,就从已有的组里面取号
                 */
                else{
                    int zh =groupUsage.getGroupNumber();
                    //取下一组
                    /**
                     *  1 根据 组号 组内随机号码 分层情况 取随机信息
                     *  2 插入 取号结果表
                     *  3 插入 取号情况表
                     *  4 更新 取号信息表
                     *  5 更新 随机数表
                     *  6 返回 试验组号
                     */
                    if(zh+1<=randomSettings.getNumOfBlock()){
                        int randomnumberNext = randomSettings.getLengthOfBlock() * zh + 1; // 计算出新组的起始randomnumber，已用组*区段长度，组=区段
                        RandomSchedule randomSchedule = randomScheduleRepo.find(projectId,zh+1, randomnumberNext, stratificationFactor);
                        //组装QHJG
                        OfferNumberResult offerNumberResult = new OfferNumberResult();
                        offerNumberResult.setProjectId(projectId);
                        offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                        offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                        offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                        offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                        offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                        offerNumberResult.setGroupType(randomSchedule.getGroupType());
                        offerNumberResult.setStratificationFactor(stratificationFactor);
                        offerNumberResult.setSubjectId(subjectId);
                        offerNumberResult.setInvestigatorId(investigatorId);
                        offerNumberResult.setOrganizationId(organizationId);
                        offerNumberResultRepo.add(offerNumberResult);

                        //组装QHQK

                        offerNumberStatus = new OfferNumberStatus();
                        offerNumberStatus.setProjectId(projectId);
                        offerNumberStatus.setStratificationFactor(stratificationFactor);
                        offerNumberStatus.setGroupNumber(zh+1);
                        offerNumberStatus.setRandomNumber(randomnumberNext);
                        offerNumberStatus.setOrderInGroup(randomSchedule.getOrderInGroup());
                        offerNumberStatus.setCurrentNumber(randomSchedule.getRandomNumber());
                        offerNumberStatus.setInvestigatorId(investigatorId);
                        offerNumberStatus.setRandomNumberId(randomSchedule.getId());
                        offerNumberStatus.setOrganizationId(organizationId);
                        offerNumberStatusRepo.add(offerNumberStatus);
                        //更新取号信息
                        groupUsage.setGroupNumber(zh+1);
                        groupUsageRepo.update(groupUsage);

                        //更新随机表
                        randomSchedule.setIsUsed("T");
                        randomScheduleRepo.setToUsed(randomSchedule);
                        returnEntity.setStatus("SUCCESS");
                        returnEntity.setMessage("成功");
                        returnEntity.setO(offerNumberResult);
                        return returnEntity;
                    }
                    //如果没有多余的组了,那就在当前的所有没有用的里面取下一个号,如果没有号 就提示取完了
                    else{
                        RandomSchedule randomSchedule = randomScheduleRepo.getFirstUnUsed(projectId, stratificationFactor);
                        /**
                         *  1 根据 组号 组内号码 分层情况 去随机信息
                         *  2 更新 随机数表
                         *  3 返回 试验组号
                         */
                        if (randomSchedule!=null){

                            OfferNumberResult offerNumberResult = new OfferNumberResult();
                            offerNumberResult.setProjectId(projectId);
                            offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                            offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                            offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                            offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                            offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                            offerNumberResult.setGroupType(randomSchedule.getGroupType());
                            offerNumberResult.setStratificationFactor(stratificationFactor);
                            offerNumberResult.setSubjectId(subjectId);
                            offerNumberResult.setInvestigatorId(investigatorId);
                            offerNumberResult.setOrganizationId(organizationId);
                            offerNumberResultRepo.add(offerNumberResult);
                            //查询offerNumberStatus表  更新号码字段;
                            //检查是否通过projectid stratification和 randomnubeerid 可以定位到唯一组 如果可以的话 这里应该是 update orderingroup and randomnumber
                            OfferNumberStatus byGroupNumber = offerNumberStatusRepo.findByGroupNumber(projectId, stratificationFactor, randomSchedule.getGroupNumber());
                            byGroupNumber.setRandomNumber(randomSchedule.getRandomNumber());
                            byGroupNumber.setOrderInGroup(randomSchedule.getOrderInGroup());
                            byGroupNumber.setCurrentNumber(randomSchedule.getRandomNumber());
                            byGroupNumber.setRandomNumberId(randomSchedule.getId());
                            offerNumberStatusRepo.update(byGroupNumber);

                            //update sjjg
                            randomSchedule.setIsUsed("T");
                            randomScheduleRepo.setToUsed(randomSchedule);
                            returnEntity.setStatus("SUCCESS");
                            returnEntity.setMessage("成功");
                            returnEntity.setO(offerNumberResult);
                            return returnEntity;
                        }
                        else{
                            returnEntity.setStatus("ERROR");
                            returnEntity.setMessage("所有号码已取完,不能取号");
                            return returnEntity;
                        }
                    }
                }
            }
            //qhqk里面存在信息
            else{
                int znhm = offerNumberStatus.getOrderInGroup();
                int randomNumber = offerNumberStatus.getRandomNumber(); //当前randomnumber
                int maxRandomNumberInGroup = offerNumberStatus.getGroupNumber() * randomSettings.getLengthOfBlock(); //计算当前组内最大的randomnumber，组号*区段长度，组=区段
                //如果区段内还有号码可取
                /**
                 * 1 获得结果信息
                 * 2 插入 取号结果表
                 * 3 更新 取号情况表
                 * 4 更新 随机结果表
                 * 4 返回 试验组号
                 */
                if (randomNumber+1<=maxRandomNumberInGroup){ // 当前randomnumber + 1 小于等于当前组最大randomnumber，说明该组还有号取
                    RandomSchedule randomSchedule = randomScheduleRepo.find(projectId, offerNumberStatus.getGroupNumber(), randomNumber+1, stratificationFactor);
                    //组装取号结果
                    OfferNumberResult offerNumberResult = new OfferNumberResult();
                    offerNumberResult.setProjectId(projectId);
                    offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                    offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                    offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                    offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                    offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                    offerNumberResult.setGroupType(randomSchedule.getGroupType());
                    offerNumberResult.setStratificationFactor(stratificationFactor);
                    offerNumberResult.setSubjectId(subjectId);
                    offerNumberResult.setInvestigatorId(investigatorId);
                    offerNumberResult.setOrganizationId(organizationId);
                    offerNumberResultRepo.add(offerNumberResult);
                    // 更新QHQK
                  //TODO:同上述确认
                    offerNumberStatus.setRandomNumber(randomNumber + 1);
                    offerNumberStatus.setOrderInGroup(znhm+1);
                    offerNumberStatus.setCurrentNumber(randomSchedule.getRandomNumber());
                    offerNumberStatus.setRandomNumberId(randomSchedule.getId());
                    offerNumberStatusRepo.update(offerNumberStatus);
                    //更新随机结果表
                    randomSchedule.setIsUsed("T");
                    randomScheduleRepo.setToUsed(randomSchedule);
                    returnEntity.setStatus("SUCCESS");
                    returnEntity.setMessage("成功");
                    returnEntity.setO(offerNumberResult);
                    return returnEntity;
                }
                //如果组内没有可取的号码了,去取新组
                else{
                    GroupUsage qhxx = groupUsageRepo.find(projectId, stratificationFactor);
                    int zh =qhxx.getGroupNumber();
                    //如果还有其他的组可以取,那么就取下一组
                    /**
                     *  1 根据 组号 组内号码 分层情况 取随机信息
                     *  2 插入 取号结果表
                     *  3 更新 取号情况表
                     *  4 更新 区号信息表
                     *  5 更新 随机结果表
                     *  6 返回 试验组号
                     */
                    if(zh+1<=randomSettings.getNumOfBlock()){
                        int randomnumber = zh * randomSettings.getLengthOfBlock() + 1; // 计算下一新组第一个randomnumber，当期组*区段长度+1，组=区段
                        RandomSchedule randomSchedule = randomScheduleRepo.find(projectId, zh+1, randomnumber, stratificationFactor);
                        //完善QHJG
                        OfferNumberResult offerNumberResult = new OfferNumberResult();
                        offerNumberResult.setProjectId(projectId);
                        offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                        offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                        offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                        offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                        offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                        offerNumberResult.setGroupType(randomSchedule.getGroupType());
                        offerNumberResult.setStratificationFactor(stratificationFactor);
                        offerNumberResult.setSubjectId(subjectId);
                        offerNumberResult.setInvestigatorId(investigatorId);
                        offerNumberResult.setOrganizationId(organizationId);
                        offerNumberResultRepo.add(offerNumberResult);
                        //update qhqk
                      //TODO:同上述确认
                        offerNumberStatus.setGroupNumber(zh+1);
                        offerNumberStatus.setRandomNumber(randomnumber);
                        offerNumberStatus.setOrderInGroup(randomSchedule.getOrderInGroup());
                        offerNumberStatus.setRandomNumberId(randomSchedule.getId());
                        offerNumberStatus.setCurrentNumber(randomSchedule.getRandomNumber());
                        offerNumberStatusRepo.update(offerNumberStatus);
                        //update qhxx
                        qhxx.setGroupNumber(zh+1);
                        groupUsageRepo.update(qhxx);

                        //update sjjg
                        randomSchedule.setIsUsed("T");
                        randomScheduleRepo.setToUsed(randomSchedule);

                        returnEntity.setStatus("SUCCESS");
                        returnEntity.setMessage("成功");
                        returnEntity.setO(offerNumberResult);
                        return returnEntity;
                    }
                    /**
                     * 如果没有多余的组了,那就在当前的组里面取下一个号,如果没有号 就提示取完了
                     */
                    else{
                        //直接查第一个没有用过的
                        RandomSchedule randomSchedule = randomScheduleRepo.getFirstUnUsed(projectId, stratificationFactor);
                        /**
                         *  1 根据 组号 组内号码 分层情况 去随机信息
                         *  2 更新 随机数表
                         *  3 返回 试验组号
                         */
                        if (randomSchedule!=null){
                            //完善QHJG
                            OfferNumberResult offerNumberResult = new OfferNumberResult();
                            offerNumberResult.setProjectId(projectId);
                            offerNumberResult.setRnid(this.getRnid(projectId, organizationId));
                            offerNumberResult.setRandomNumber(randomSchedule.getRandomNumber());
                            offerNumberResult.setRandomResult(randomSchedule.getRandomResult());
                            offerNumberResult.setGroupNumber(randomSchedule.getGroupNumber());
                            offerNumberResult.setOrderInGroup(randomSchedule.getOrderInGroup());
                            offerNumberResult.setGroupType(randomSchedule.getGroupType());
                            offerNumberResult.setStratificationFactor(stratificationFactor);
                            offerNumberResult.setSubjectId(subjectId);
                            offerNumberResult.setInvestigatorId(investigatorId);
                            offerNumberResult.setOrganizationId(organizationId);
                            offerNumberResultRepo.add(offerNumberResult);

                          
                            //查询offerNumberStatus表  更新号码字段;
                          //TODO:同上述确认
                            OfferNumberStatus byGroupNumber = offerNumberStatusRepo.findByGroupNumber(projectId, stratificationFactor, randomSchedule.getGroupNumber());
                            byGroupNumber.setRandomNumber(randomSchedule.getRandomNumber());
                            byGroupNumber.setOrderInGroup(randomSchedule.getOrderInGroup());
                            byGroupNumber.setCurrentNumber(randomSchedule.getRandomNumber());
                            byGroupNumber.setRandomNumberId(randomSchedule.getId());
                            offerNumberStatusRepo.update(byGroupNumber);

                            //update sjjg
                            randomSchedule.setIsUsed("T");
                            randomScheduleRepo.setToUsed(randomSchedule);
                            returnEntity.setStatus("SUCCESS");
                            returnEntity.setMessage("成功");
                            returnEntity.setO(offerNumberResult);
                            return returnEntity;
                        }
                        else{
                            returnEntity.setStatus("ERROR");
                            returnEntity.setMessage("所有号码已取完,不能取号");
                            return returnEntity;
                        }
                    }
                }
            }
        }
    }

    public String getRnid(int projectId, int orgId){
        // 0 代表前面补充0
        // 3 代表长度为3
        // d 代表参数为正数型

        String formatedStr = String.format("%03d", offerNumberResultRepo.countAll(projectId)+1);
        return orgId + "-" + formatedStr;
    }

    @Override
    public int getCountOfRandomNumberUsed(int projectId){
        return offerNumberResultRepo.countAll(projectId);
    }

    @Override
    public int getTrailDuration(int projectId){
        List<OfferNumberResult> offerNumberResultList = offerNumberResultRepo.getAll(projectId);
        if(offerNumberResultList != null){
            int size = offerNumberResultList.size();
            if(size == 0){
                return 0;
            }else if(size == 1){
                return 1;
            }else {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date last =  offerNumberResultList.get(size-1).getOfferDate();
                Date first = offerNumberResultList.get(0).getOfferDate();

                int interval = 1+(int)((last.getTime()-first.getTime())/(1000*60*60*24));
                return interval;
            }
        }else
            return 0;
    }


    /**
     * 根据projectId取offerNumberResult表里的数据，再根据参试机构组织成曲线图所需要的series option.
     * @param projectId
     * @return
     */
    @Override
    public List<StatisticalChart> getOfferNumberChart(int projectId){
        //不同的参试机构id列表：{2,3,66}
        List<Integer> orgIdList = offerNumberResultRepo.getOrgIdList(projectId);
        //从第一个号开始的，不同的日期列表：{2018-07-25, 2018-07-26, 2018-07-28}
        List<String> dateList = offerNumberResultRepo.getDateList(projectId);

        if(orgIdList == null || dateList==null){
            return null;
        }else{
            int dataListSize = dateList.size();

            List<StatisticalChart> statisticalChartList = new ArrayList<>();
            StatisticalChart statisticalChart;
            List<StatisticalLine> statisticalLineList;
            String sj;//日期
            int index;

            //ECharts的series规定的属性
            String orgName;
            String type;
            AreaStyle areaStyle;
            Normal normal;
            Integer[] data;

            for (int orgId : orgIdList){
                statisticalLineList = offerNumberResultRepo.countForEcharts(projectId,orgId);
                orgName = organizationRepo.getOrganizationById(orgId).getName();
                type = "line";
                normal = new Normal("");
                areaStyle = new AreaStyle(normal);

                //定义初始数组，用来盛放每一天这个orgId的取号数number: [0, 0, 0...]
                data = new Integer[dataListSize];
                for(int i=0; i<data.length; i++){
                    data[i] = 0;
                }
                //遍历数据库里读出来的每一行（orgId, sj, number）:(2, 2018-07-25, 11)赋值给EChart规定的数组data
                for(StatisticalLine statisticalLine: statisticalLineList){
                    sj = statisticalLine.getSj();
                    index = dateList.indexOf(sj);
                    data[index] = statisticalLine.getNumber(); //第一次循环后data=[11,0,0...]
                }

                statisticalChart = new StatisticalChart(orgName, type, areaStyle, data);
                statisticalChartList.add(statisticalChart);
            }
            return statisticalChartList;
        }
    }

    @Override
    public EChartsMap getOptionsForEChart(int projectId){
        //不同的参试机构id列表：{2,3,66}
        List<Integer> orgIdList = offerNumberResultRepo.getOrgIdList(projectId);
        //从第一个号开始的，不同的日期列表：{2018-07-25, 2018-07-26, 2018-07-28}
        List<String> dateList = offerNumberResultRepo.getDateList(projectId);

        if(orgIdList == null || dateList==null){
            return null;
        }else{
            int orgIdListSize = orgIdList.size();
            int dateListSize = dateList.size();
            List<String> orgNameList = new ArrayList<>();

            for(int orgId:orgIdList){
                orgNameList.add(organizationRepo.getOrganizationById(orgId).getName());
            }

            EChartsMap map = new EChartsMap(this.getColor(orgIdListSize), orgNameList.toArray(), dateList.toArray());
            return map;
        }
    }

    private Object[] getColor(int count){
        //ECharts中使用到的color从这个colorList里面取
        List<String> colorList = Arrays.asList("#26b99a", "#31b0d5", "#d9534f", "#A330C9", "#FF7D0A", "#ABD473", "#00FF96", "#f0ad4e", "#C41F3B", "#ccc");
        return colorList.subList(0, count).toArray();
    }
}
