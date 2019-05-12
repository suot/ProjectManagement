package com.suo.projectManagement.controller;

import com.suo.projectManagement.aop.PrintServiceName;
import com.suo.projectManagement.dao.IInvestiGroupRepo;
import com.suo.projectManagement.dao.IRandomScheduleRepo;
import com.suo.projectManagement.po.*;
import com.suo.projectManagement.service.impl.UnfoldblindnessDlView;
import com.suo.projectManagement.service.impl.UserService;
import com.suo.projectManagement.vo.*;
import com.suo.projectManagement.service.IGenerateRandomService;
import com.suo.projectManagement.service.IOfferNumberService;
import com.suo.projectManagement.service.impl.CurrentProjectService;
import com.suo.projectManagement.service.impl.ProjectService;
import com.suo.projectManagement.utils.GenerateRandomUtil;
import com.suo.projectManagement.utils.StraifiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by wuxu on 2018/5/30.
 */

@Controller
public class RandomController {
    private static final Logger logger = LoggerFactory.getLogger(RandomController.class);

    @Autowired
    IGenerateRandomService generateRandomService;

    @Autowired
    IOfferNumberService offerNumberService;

    @Autowired
    CurrentProjectService currentProjectService;

    @Autowired
    ProjectService projectService;

    @Autowired
    IInvestiGroupRepo invesiGroupRepo;

    @Autowired
    UserService userService;

    @Autowired
    IRandomScheduleRepo randomScheduleRepo;



    @RequestMapping("/random/randomGrouping")
    public String randomGrouping(Model model){
        String investigatorId = SecurityContextHolder.getContext().getAuthentication().getName();

        int projectId = currentProjectService.getCurrentProjectByUsername(investigatorId).getProjectId();
        int total = randomScheduleRepo.total(projectId);

        if(total > 0){
            Project project = projectService.getProjectById(projectId);
            model.addAttribute("project", project);

            //randomSettings基本参数
            RandomSettings randomSettings = generateRandomService.findRandomSettings(projectId);
            model.addAttribute("randomSettings", randomSettings);
            String investigatorName = userService.getUserByUsername(investigatorId).getName();
            model.addAttribute("investigatorName", investigatorName);

            //investiGroup分组信息：T or R
            List<InvestiGroup> investiGroupList = invesiGroupRepo.get(projectId);
            model.addAttribute("investiGroupList", investiGroupList);

            //investiOrganization中心设置
            List<InvestiOrganizationInfo> investiOrganizationInfoList = generateRandomService.getInvestiOrganizationInfo(projectId);
            model.addAttribute("investiOrganizationInfoList", investiOrganizationInfoList);

            //stratification分层信息
//            Map<String, List<Stratification>> stratificationMap = generateRandomService.getStratificationMap(projectId);
//            model.addAttribute("stratificationMap", stratificationMap);
            List<Stratification> stratificationList = generateRandomService.getAllStratifications(projectId);
            model.addAttribute("stratificationList", stratificationList);

            return "/randomGrouping :: randomGroupingResult";
        }else {
            model.addAttribute("investigatorId", investigatorId);

            //中心设置页面的机构列表，取自investiOrganization，即该项目的临床研究单位（也叫参试机构），在新建project页面设置的。
            List<InvestiOrganizationInfo> investiOrganizationInfoList = generateRandomService.getInvestiOrganizationInfo(projectId);
            model.addAttribute("investiOrganizationInfoList", investiOrganizationInfoList);
            return "/randomGrouping :: randomGrouping";
        }
    }


    @PrintServiceName(description = "受试者管理&&生成了随机号")
    @RequestMapping("/random/generate")
    @ResponseBody
    public String generateRandom(HttpServletRequest request){
        RandomSettings randomSettings =new RandomSettings();
        String investigatorId = SecurityContextHolder.getContext().getAuthentication().getName();
        //User user = userRepo.getUserByUsername(investigatorId);
        int projectId = currentProjectService.getCurrentProjectByUsername(investigatorId).getProjectId();
        randomSettings.setProjectId(projectId);
        randomSettings.setBlindType(request.getParameter("blind_type"));//盲态设置
        randomSettings.setUnFoldMgr(request.getParameter("unblind_mgr"));//揭盲负责人
        randomSettings.setSimpleSize(Integer.parseInt(request.getParameter("simple_size")));//样本量
        randomSettings.setIsCtrlSubEnroll(request.getParameter("is_ctrl_subenroll"));//是否控制项目受试者
        randomSettings.setRandomSeed(request.getParameter("random_seed"));//随机种子
        randomSettings.setNumOfGroup(Integer.parseInt(request.getParameter("num_of_group")));//试验组数
        randomSettings.setBalanceType(request.getParameter("balance_type"));//平衡类型
        randomSettings.setNumOfBlock(Integer.parseInt(request.getParameter("num_of_block")));//区段数量
        randomSettings.setLengthOfBlock(Integer.parseInt(request.getParameter("num_of_length")));//区段长度
        randomSettings.setIsStratifi(request.getParameter("is_stratifi")); //是否设置分层

        String[] group_types = request.getParameterValues("group_type[]");//试验组类型
        String[] group_names = request.getParameterValues("group_name[]");//试验组名
        String[] occupies = request.getParameterValues("occupy[]");//分组比例
        String[] subject_numbers = request.getParameterValues("subject_number[]");//受试者数

        ArrayList<InvestiGroupInfo> investiGroupInfos = new ArrayList<>();
        int numOfGroup = randomSettings.getNumOfGroup();
        for (int i = 0; i < numOfGroup; i++) {
            InvestiGroupInfo investiGroupInfo = new InvestiGroupInfo();
            investiGroupInfo.setProjectId(randomSettings.getProjectId());
            investiGroupInfo.setGroupType(group_types[i]);
            investiGroupInfo.setGroupName(group_names[i]);
            investiGroupInfo.setOccupy(Integer.parseInt(occupies[i]));
            investiGroupInfo.setSubjectNumber(Integer.parseInt(subject_numbers[i]));
            investiGroupInfos.add(investiGroupInfo);
        }


        String[] org_ids = request.getParameterValues("org_id[]");//中心编号
        String[] enroll_nums = request.getParameterValues("enroll_num[]");//控制受试者人数

        ArrayList<InvestiOrganization> investiOrganizations = new ArrayList<>();

        for (int i = 0; i < org_ids.length; i++) {
            InvestiOrganization investiOrganization = new InvestiOrganization();
            investiOrganization.setProjectId(randomSettings.getProjectId());
            investiOrganization.setOrgId(Integer.parseInt(org_ids[i]));
            investiOrganization.setEnrollNumber(Integer.parseInt(enroll_nums[i]));
            investiOrganizations.add(investiOrganization);
        }

        GenerateRandomUtil generateRandomUtil = new GenerateRandomUtil();
        ArrayList<RandomCell> cells =new ArrayList<>();
        if (randomSettings.getIsStratifi().equals("yes")){
            StraifiUtil straifiUtil = new StraifiUtil();
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key=entry.getKey();
                if (key.contains("group") && !key.contains("_")){

                    StratifiInfo stratifiInfo= straifiUtil.createOrGetStratifi(key);
                    stratifiInfo.setStratifiName(request.getParameter(key));

                    Enumeration paramNames = request.getParameterNames();
                    while (paramNames.hasMoreElements()) {
                        String paramName = (String) paramNames.nextElement();
                        if (paramName.contains(key+"_")){
                            StratifiFactorInfo stratifiFactorInfo = new StratifiFactorInfo();
                            stratifiFactorInfo.setStratifiFactorId(paramName);
                            stratifiFactorInfo.setStratifiFactorName(request.getParameter(paramName));
                            stratifiInfo.getStratifiFactors().add(stratifiFactorInfo);
                        }
                    }
                }
            }
            logger.info(straifiUtil.toString());

            List<StratifiInfo> sliceSettings = straifiUtil.getSliceSettings();
            for (StratifiInfo stratifiInfo : sliceSettings){
                generateRandomService.addStratification(stratifiInfo.parseToStraification(projectId));
            }

            ArrayList<String> comb = straifiUtil.getComb();
            //
//            for (String s : comb){
//                cells.addAll(generateRandomUtil.generateRandomGroup(randomSettings, investiGroupInfos,s));
////                for (RandomCell c : cells) {
////                    logger.info(c.toString());
////                }
//            }
            cells.addAll(generateRandomUtil.generateRandomGroup(randomSettings,investiGroupInfos,comb));
        }
        else{
                cells = generateRandomUtil.generateRandomGroup(randomSettings, investiGroupInfos,"default");
//            for (RandomCell c : cells) {
//                logger.info(c.toString());
//            }
        }
        //保存随机设置
        int rtn = generateRandomService.addRandomSettings(randomSettings);
        //最后保存
//      随机结果  randomschedule
        rtn = rtn + generateRandomService.addRandomSchedule(cells);

//      研究组信息
        rtn = rtn + generateRandomService.addInvestiGroup(investiGroupInfos);

//      研究机构信息
        rtn = rtn + generateRandomService.updateEnrollNumberOfOrganizations(investiOrganizations);

        //TODO:对结果进行判断 返回值
        if(rtn==0){
            return "生成成功";
        }else {
            return "生成失败";
        }

    }


    @RequestMapping("/random/fetchSequenceNumber")
    public String fetchSequenceNumber(Model model){
        logger.info("Entered page: /random/fetchSequenceNumber");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();

        Map<String, List<Stratification>> stratificationMap = generateRandomService.getStratificationMap(projectId);

        model.addAttribute("stratificationMap", stratificationMap);
        return "/randomGrouping :: stratification";
    }

    @PrintServiceName(description = "受试者管理&&取号成功")
    @PostMapping("/random/getnumber")
    @ResponseBody
    public ReturnMessage getNumber(HttpServletRequest request){
        //TODO:带入研究者名称 研究者所属的organization 获得projectID
        String investigatorId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(investigatorId);
        int projectId = currentProjectService.getCurrentProjectByUsername(investigatorId).getProjectId();
        int organizationId = user.getOrganizationId();


        //TODO:从 resultsetting 获得盲态类型 no_blind  single_blind double_blind
        RandomSettings randomSettings = generateRandomService.findRandomSettings(projectId);
        String blindtype= randomSettings.getBlindType();

        //TODO:判断平衡类型  balanced  competitive  mixed
        String balance_type = randomSettings.getBalanceType();

        //TODO:受试者 input框内容
        String subjectId = request.getParameter("subjectid");
        logger.info("测试者id : " + subjectId);


        // 组装分层标记  post过来 应该是  group0:group0_1  group1: group1_0
        ArrayList<String> values = new ArrayList<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key=entry.getKey();
            if (key.contains("group") && !key.contains("_")){
                values.add(request.getParameter(key));
            }
        }
        String stratificationFactor="default";
        if(values.size()>0) {
            stratificationFactor = "";
            for (String s : values) {
                stratificationFactor = stratificationFactor + s + "$";
            }
            stratificationFactor = stratificationFactor.substring(0, stratificationFactor.length() - 1);
        }
        logger.info("stratificationFactor is : " +stratificationFactor);


        ReturnEntity returnEntity;
        try {
            if (balance_type.equals("balanced")) {
                returnEntity = offerNumberService.balancedRan(projectId, stratificationFactor, investigatorId, subjectId, organizationId);
            } else if (balance_type.equals("competitive")) {
                returnEntity = offerNumberService.competitiveRan(projectId, stratificationFactor, investigatorId, subjectId, organizationId);
            } else {
                returnEntity = offerNumberService.mixedRan(projectId, stratificationFactor, investigatorId, subjectId, organizationId);
            }
        }
        catch (Exception ex){
            logger.error("取号功能异常:", ex);
            returnEntity = new ReturnEntity();
            returnEntity.setStatus("ERROR");
            returnEntity.setMessage("系统忙！请重新取号。");
        }

        ReturnMessage returnMessage;
        String status = returnEntity.getStatus();
        if(status.equals("ERROR")){
            returnMessage = new ReturnMessage("取号失败", returnEntity.getMessage(), null);
        }
        else{
            OfferNumberResult offerNumberResult = (OfferNumberResult) returnEntity.getO();
            if (blindtype.equals("no_blind")){
                String groupName = invesiGroupRepo.getGroupName(projectId, offerNumberResult.getGroupType());
                returnMessage = new ReturnMessage("取号成功", offerNumberResult.getRnid(), groupName);
            }
            else {
                returnMessage = new ReturnMessage("取号成功", offerNumberResult.getRnid(), null);
            }
        }
        return returnMessage;
    }


    @RequestMapping("/random/checkOfferNumberInfoList")
    public String checkOfferNumberInfoList(Model model){
        return "/randomGrouping :: sequenceNumberList";
    }

    @PrintServiceName(description = "受试者管理&&查看了取号结果列表")
    @RequestMapping("/random/queryOfferNumberInfoList")
    @ResponseBody
    public List<OfferNumberResultInfo> queryOfferNumberInfoList(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();
        return generateRandomService.getOfferNumberResultInfo(projectId);
    }


    @RequestMapping("/random/unfoldBlindness")
    public String unfoldBlindness(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();

        List<OfferNumberResultInfo> offerNumberResultInfoList = generateRandomService.getOfferNumberResultInfo(projectId);
        int lineCount;
        if(offerNumberResultInfoList == null || offerNumberResultInfoList.size()==0){
            lineCount = 0;
        }else {
            lineCount = offerNumberResultInfoList.size();
        }

        model.addAttribute("lineCount", lineCount);
        model.addAttribute("offerNumberResultInfoList", offerNumberResultInfoList);
        return "/randomGrouping :: unfoldBlindness";
    }


    @RequestMapping("/random/unfoldBlindness/download")
    public ModelAndView unfoldBlindnessDownload(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int projectId = currentProjectService.getCurrentProjectByUsername(username).getProjectId();
        List<OfferNumberResultInfo> offerNumberResultInfoList = generateRandomService.getOfferNumberResultInfo(projectId);

        List<UnfoldBlindnessResultDownload> unfoldBlindnessResultDownloadList = new ArrayList<>();
        if(offerNumberResultInfoList != null && offerNumberResultInfoList.size()>0) {
            UnfoldBlindnessResultDownload unfoldBlindnessResultDownload;
            OfferNumberResult offerNumberResult;

            for (OfferNumberResultInfo info : offerNumberResultInfoList) {
                offerNumberResult = info.getOfferNumberResult();
                unfoldBlindnessResultDownload = new UnfoldBlindnessResultDownload(offerNumberResult.getId(), offerNumberResult.getSubjectId(), offerNumberResult.getInvestigatorId(), offerNumberResult.getGroupType(), info.getInvestiGroup().getGroupName(), offerNumberResult.getGroupNumber(), offerNumberResult.getOrderInGroup(), offerNumberResult.getRnid(), offerNumberResult.getOfferDate());
                unfoldBlindnessResultDownloadList.add(unfoldBlindnessResultDownload);
            }
        }

        return new ModelAndView(new UnfoldblindnessDlView(),"unfoldBlindness",unfoldBlindnessResultDownloadList);
    }


}
