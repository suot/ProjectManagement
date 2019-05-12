package com.suo.projectManagement.service;

import com.suo.projectManagement.vo.InvestiGroupInfo;
import com.suo.projectManagement.vo.InvestiOrganizationInfo;
import com.suo.projectManagement.vo.RandomCell;
import com.suo.projectManagement.po.InvestiOrganization;
import com.suo.projectManagement.po.RandomSettings;
import com.suo.projectManagement.po.Stratification;
import com.suo.projectManagement.vo.OfferNumberResultInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IGenerateRandomService {
    int addRandomSchedule(ArrayList<RandomCell> cells);
    int addRandomSettings(RandomSettings randomSettings);

    int addInvestiGroup(ArrayList<InvestiGroupInfo> investiGroupInfos);

    int addInvestiOrganizations(List<InvestiOrganization> investiOrganizations);
    int updateEnrollNumberOfOrganizations(List<InvestiOrganization> investiOrganizations);
    int updateStatusOfOrganizations(List<InvestiOrganization> investiOrganizations);

    int addStratification(List<Stratification> stratification);
    int deleteStratification(int projectId);
    List<Stratification> getAllStratifications(int projectId);
    Map<String, List<Stratification>> getStratificationMap(int projectId);

    RandomSettings findRandomSettings(int projectId);
    List<OfferNumberResultInfo> getOfferNumberResultInfo(int projectId);
    List<InvestiOrganizationInfo> getInvestiOrganizationInfo(int projectId);
}
