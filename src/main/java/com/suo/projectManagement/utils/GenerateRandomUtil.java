package com.suo.projectManagement.utils;

import com.suo.projectManagement.vo.InvestiGroupInfo;
import com.suo.projectManagement.vo.RandomCell;
import com.suo.projectManagement.po.RandomSettings;

import java.util.*;

/**
 * Created by wuxu on 2018/5/30.
 */
public class GenerateRandomUtil {
    private ArrayList<InvestiGroupInfo> groupInformations;
    private int numOfBlock; //区组数量
    private int lengthOfBlock; //区组长度
    private int randomSeed; //随机种子
    private int simpleSize;//样本量
    private String stratifName;
    private int projectId;
    private ArrayList<String> stratifNames;

    public GenerateRandomUtil() {
    }

    public ArrayList<RandomCell> generateRandomGroup(RandomSettings randomSettings, ArrayList<InvestiGroupInfo> investiGroupInfos, String stratifName) {
        this.numOfBlock = randomSettings.getNumOfBlock();
        this.lengthOfBlock = randomSettings.getLengthOfBlock();
        this.simpleSize = randomSettings.getSimpleSize();
        this.randomSeed = Integer.parseInt(randomSettings.getRandomSeed());
        this.stratifName = stratifName;
        this.groupInformations= investiGroupInfos;
        this.projectId = randomSettings.getProjectId();
        return generate();
    }

    public ArrayList<RandomCell> generateRandomGroup(RandomSettings randomSettings, ArrayList<InvestiGroupInfo> investiGroupInfos, ArrayList<String> stratifNames) {
        this.numOfBlock = randomSettings.getNumOfBlock();
        this.lengthOfBlock = randomSettings.getLengthOfBlock();
        this.simpleSize = randomSettings.getSimpleSize();
        this.randomSeed = Integer.parseInt(randomSettings.getRandomSeed());
        this.stratifNames = stratifNames;
        this.groupInformations= investiGroupInfos;
        this.projectId = randomSettings.getProjectId();
        return generate2();
    }

    //为了有分层的随机号生成正确
    private ArrayList<RandomCell> generate2() {
        ArrayList<RandomCell> cells = new ArrayList<>();
        //根据分层生成所有随机序列
        ArrayList<Double> randomList = new ArrayList<>();
        Random random = new Random(this.randomSeed);
        int total = stratifNames.size() * simpleSize;//需要的随机序列总长度是分层的组合数量 * 样本数
        for (int i = 0; i < total; i++) {
            randomList.add(random.nextDouble());
        }

        for (int i = 0; i < stratifNames.size(); i++) {
            ArrayList<RandomCell> rtn = new ArrayList<>();
            List<Double> sub = randomList.subList(i*simpleSize,i*simpleSize+simpleSize);
            generateRandomGroup2(rtn,sub);
            //step2 order by group
            orderRandomGroup(rtn);
            //step3 arrange group tags
            //count greatest common divisor
            int totaltypes = processGroupInformations();
            //generate typelist
            String[] generateTypeList = generateTypeList(totaltypes);
            setTypeToCells(rtn,totaltypes,generateTypeList,stratifNames.get(i));
            cells.addAll(rtn);
        }
        return  cells;


    }

    private void generateRandomGroup2(ArrayList<RandomCell> rtn, List<Double> sub) {
        for (int i = 0; i <this.simpleSize ; i++) {
            RandomCell cell = new RandomCell();
            cell.setRandomNumber(i+1);//begin from 1
            cell.setRandomResult(sub.get(i));
            rtn.add(cell);
        }
    }


    private ArrayList<RandomCell> generate() {
        ArrayList<RandomCell> rtn=new ArrayList<>();
        //step1 generate random group
        generateRandomGroup(rtn);
        //step2 order by group
        orderRandomGroup(rtn);
        //step3 arrange group tags
        //count greatest common divisor
        int totaltypes = processGroupInformations();
        //generate typelist
        String[] generateTypeList = generateTypeList(totaltypes);
        //finish cell group
        if (this.stratifName.equals("")){
            setTypeToCells(rtn,totaltypes,generateTypeList);
        }
        else {
            setTypeToCells(rtn,totaltypes,generateTypeList,this.stratifName);
        }
        return rtn;
    }


    private void setTypeToCells(ArrayList<RandomCell> cellLists, int totaltypes, String[] generateTypeList, String sliceName) {
        for (RandomCell cell : cellLists){
            int order = cell.getOrderInGroup();
            cell.setGroupType(generateTypeList[order%totaltypes]);
            cell.setStratificationFactor(sliceName);
            cell.setProjectId( projectId);
        }
    }

    /**
     * @param cellLists 结果列表 in/out
     * @param totaltypes 试验组类型约分后的总数
     * @param generateTypeList 组装后的试验组类型数组
     */
    private void setTypeToCells(ArrayList<RandomCell> cellLists, int totaltypes, String[] generateTypeList) {
        for (RandomCell cell : cellLists){
            int order = cell.getOrderInGroup();
            cell.setGroupType(generateTypeList[order%totaltypes]);
            cell.setProjectId( projectId);
        }
    }


    /**
     * @param total 试验组类型约分后的总数
     * @return 组装后的试验组类型数组
     */
    private String[] generateTypeList(int total) {
        ArrayList<String> rtn = new ArrayList<String>();
        for(InvestiGroupInfo m : groupInformations){
            String type = m.getGroupType();
            int oc=m.getProcessd_occupy();
            for (int i = 0; i < oc; i++) {
                rtn.add(type);
            }
        }
        return rtn.toArray(new String[total]);
    }

    /**
     * @return 试验组类型约分后的总数
     */
    private int processGroupInformations() {
        InvestiGroupInfo min = Collections.min(groupInformations);
        int count=0;
        int max=0;
        for (int i=min.getOccupy();i>0;i--){
            for(InvestiGroupInfo m : groupInformations){
                if(m.getOccupy()%i ==0){
                    count++;
                }
                else{
                    count=0;
                    break;
                }
            }
            if (count==groupInformations.size()) {
                max= i;
                break;
            }
        }
        int rtn=0;
        for (InvestiGroupInfo m:groupInformations){
            if (max!=0){
                m.setProcessd_occupy(m.getOccupy()/max);
            }
            else m.setProcessd_occupy(m.getOccupy());
            rtn+=m.getProcessd_occupy();
        }
        return rtn;


    }

    /**
     * @param cellArrayList 试验数据 in/out
     */
    private void orderRandomGroup(ArrayList<RandomCell> cellArrayList) {

        int groupNumber = 1;
        for (int i = 0; i < this.simpleSize; i+=this.lengthOfBlock) {
            List<RandomCell> cells = cellArrayList.subList(i, i+this.lengthOfBlock);
            RandomCell[] sortCell = new RandomCell[cells.size()];
            cells.toArray(sortCell);
            Arrays.sort(sortCell);
            for (int j = 0; j < cells.size(); j++) {
                sortCell[j].setOrderInGroup(j + 1);
                sortCell[j].setGroupNumber(groupNumber);

            }
            groupNumber++;
        }
    }

    /**
     * @param cells 试验数据 in/out
     */
    private void generateRandomGroup(ArrayList<RandomCell> cells) {
        Random random = new Random(this.randomSeed);
        for (int i = 0; i <this.simpleSize ; i++) {
            RandomCell cell = new RandomCell();
            cell.setRandomNumber(i+1);//begin from 1
            cell.setRandomResult(random.nextDouble());
            cells.add(cell);
        }
        return;
    }
}
