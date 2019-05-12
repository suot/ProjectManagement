package com.suo.projectManagement.service.impl;

import com.suo.projectManagement.po.*;
import com.suo.projectManagement.vo.InvestiOrganizationInfo;
import com.suo.projectManagement.vo.RandomCell;
import com.suo.projectManagement.vo.InvestiGroupInfo;
import com.suo.projectManagement.dao.*;
import com.suo.projectManagement.service.IGenerateRandomService;
import com.suo.projectManagement.vo.OfferNumberResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wuxu on 2018/5/30.
 */

@Service
public class GenerateRandomServiceImpl implements IGenerateRandomService {

    private  static  final Logger logger = LoggerFactory.getLogger(GenerateRandomServiceImpl.class);

    @Autowired
    IRandomScheduleRepo randomScheduleRepo;

    @Autowired
    IRandomSettingsRepo randomSettingsRepo;

    @Autowired
    IStratificationRepo stratificationRepo;

    @Autowired
    IInvestiGroupRepo invesiGroupRepo;

    @Autowired
    IInvestiOrganizationRepo investiOrganizationRepo;

    @Autowired
    IOfferNumberResultRepo offerNumberResultRepo;

    @Autowired
    IOrganizationRepo organizationRepo;


    @Override
    public int addRandomSchedule(ArrayList<RandomCell> cells) {
        logger.info("------添加随机结果:-----");
        ArrayList<RandomCell> c = cells;

        int[] result = randomScheduleRepo.add(cells);
        int affectedLineCount = 0;

        for(int resulti : result){
            affectedLineCount += resulti;
        }

        if(affectedLineCount == cells.size()) {
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int addRandomSettings(RandomSettings randomSettings) {
        logger.info("------添加随机设置:-----");

        int result = randomSettingsRepo.add(randomSettings);
        logger.info(randomSettings.toString());

        if(result == 1){
            return 0;
        }else
            return 1;
    }

    @Override
    public int addStratification(List<Stratification> stratification) {
        logger.info("------添加分层设置:-----");
        int affectedLineCount = 0;
        for (Stratification strati:stratification){
            logger.info(strati.toString());
            affectedLineCount += stratificationRepo.add(strati);
        }
        logger.info("数量是: "+stratification.size());

        return 0;

    }

    public int deleteStratification(int projectId){
        return stratificationRepo.delete(projectId);
    }
    public List<Stratification> getAllStratifications(int projectId){
        return stratificationRepo.getAll(projectId);
    }

    public Map<String, List<Stratification>> getStratificationMap(int projectId){
        List<Stratification> stratifications = this.getAllStratifications(projectId);

        if(stratifications.size()>0){
            Map<String, List<Stratification>> map = new HashMap<>();
            String stratifiid;

            for(Stratification stratification : stratifications){

                stratifiid = stratification.getStratifiId();
                if(map.containsKey(stratifiid)){
                    //stratifiid: group0在map里了
                    List<Stratification> list = map.get(stratifiid);
                    list.add(stratification);

                }else {
                    //stratifiid: group0不在map里
                    List<Stratification> list = new ArrayList<>();
                    list.add(stratification);
                    map.put(stratifiid, list);
                }
            }
            return map;
        }else{
            return null;
        }
    }



    @Override
    public int addInvestiGroup(ArrayList<InvestiGroupInfo> investiGroupInfos) {
        logger.info("------添加研究分组:-----");
        //检查一下 内容全不全
        int affectedLineCount = 0;

        for (InvestiGroupInfo investiGroupInfo:investiGroupInfos){
            logger.info(investiGroupInfo.toString());
            affectedLineCount += invesiGroupRepo.add(investiGroupInfo);
        }
        logger.info("数量是: "+investiGroupInfos.size());

        if(affectedLineCount == investiGroupInfos.size()) {
            return 0;
        }else {
            return 1;
        }
    }


    @Override
    public int addInvestiOrganizations(List<InvestiOrganization> investiOrganizations) {
        logger.info("------添加临床研究单位:-----");
        int affectedLineCount = 0;
        for (InvestiOrganization investiOrganization:investiOrganizations){
            logger.info(investiOrganization.toString());
            affectedLineCount += investiOrganizationRepo.add(investiOrganization);
        }
        logger.info("数量是: "+investiOrganizations.size());

        if(affectedLineCount == investiOrganizations.size()) {
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int updateEnrollNumberOfOrganizations(List<InvestiOrganization> investiOrganizations) {
        logger.info("------修改临床研究单位的受试者控制数量:-----");
        int affectedLineCount = 0;
        for (InvestiOrganization investiOrganization:investiOrganizations){
            logger.info(investiOrganization.toString());
            affectedLineCount += investiOrganizationRepo.updateEnrollNumber(investiOrganization);
        }
        logger.info("数量是: "+investiOrganizations.size());

        if(affectedLineCount == investiOrganizations.size()) {
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int updateStatusOfOrganizations(List<InvestiOrganization> investiOrganizations) {
        logger.info("------修改临床研究单位的状态:-----");
        int affectedLineCount = 0;
        for (InvestiOrganization investiOrganization:investiOrganizations){
            logger.info(investiOrganization.toString());
            affectedLineCount += investiOrganizationRepo.updateStatus(investiOrganization);
        }
        logger.info("数量是: "+investiOrganizations.size());

        if(affectedLineCount == investiOrganizations.size()) {
            return 0;
        }else {
            return 1;
        }
    }


    @Override
    public RandomSettings findRandomSettings(int projectId){
        return randomSettingsRepo.find(projectId);
    }


    @Override
    public List<OfferNumberResultInfo> getOfferNumberResultInfo(int projectId){
        RandomSettings randomSettings = randomSettingsRepo.find(projectId);
        if(randomSettings.getBlindType().equals("double_blind")){
            //双盲不能查看
            return null;
        }else {
            //单盲或者不盲
            List<OfferNumberResultInfo> offerNumberResultInfoList;
            OfferNumberResultInfo offerNumberResultInfo;
            InvestiGroup investiGroup;

            List<OfferNumberResult> offerNumberResultList = offerNumberResultRepo.getAll(projectId);

            if (offerNumberResultList.size() > 0) {
                offerNumberResultInfoList = new ArrayList<>();
                for (OfferNumberResult offerNumberResult : offerNumberResultList) {
                    investiGroup = invesiGroupRepo.get(projectId, offerNumberResult.getGroupType());
                    offerNumberResultInfo = new OfferNumberResultInfo(offerNumberResult, investiGroup);
                    offerNumberResultInfoList.add(offerNumberResultInfo);
                }
                return offerNumberResultInfoList;
            } else {
                return null;
            }
        }
    }

    @Override
    public List<InvestiOrganizationInfo> getInvestiOrganizationInfo(int projectId){
        //去investiOrganization表中的数据，他们是在新建项目的时候在 临床研究单位 中选择的。
        List<InvestiOrganization> investiOrganizationList = investiOrganizationRepo.get(projectId);
        if(investiOrganizationList != null && investiOrganizationList.size()>0) {
            List<InvestiOrganizationInfo> investiOrganizationInfoList = new ArrayList<>();
            int orgId;
            Organization organization;
            InvestiOrganizationInfo investiOrganizationInfo;

            for (InvestiOrganization investiOrganization : investiOrganizationList) {
                orgId = investiOrganization.getOrgId();
                organization = organizationRepo.getOrganizationById(orgId);
                investiOrganizationInfo = new InvestiOrganizationInfo(investiOrganization, organization);
                investiOrganizationInfoList.add(investiOrganizationInfo);
            }
            return investiOrganizationInfoList;
        }else
            return null;
    }

}
