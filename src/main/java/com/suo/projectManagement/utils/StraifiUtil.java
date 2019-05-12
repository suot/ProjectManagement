package com.suo.projectManagement.utils;

import com.suo.projectManagement.vo.StratifiInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxu on 2018/5/30.
 */
public class StraifiUtil {

    private List<StratifiInfo> sliceSettings = new ArrayList<>();

    public StratifiInfo createOrGetStratifi(String key){
        for (StratifiInfo s:sliceSettings){
            if (s.getStratifiId().equals(key)){
                return s;
            }
        }
        StratifiInfo s = new StratifiInfo();
        s.setStratifiId(key);
        sliceSettings.add(s);
        return s;
    }

    public List<StratifiInfo> getSliceSettings() {
        return sliceSettings;
    }

    /**
     * 对分层信息进行正交组合
     * @return 正交组合结果
     */
    public ArrayList<String> getComb(){
        ArrayList<String> arrayList = new ArrayList<>();

        ArrayList<String> strings = sliceSettings.get(0).getFactorKeys(); //如果只有一组 直接返回


        if (sliceSettings.size()==1){
            for (String s : strings)
            {
                arrayList.add(s);
            }
            return arrayList;
        }
        for (String s : strings)
        {
            String[] split =  recurParse(s,1).split("__");
            for (int i = 0; i < split.length; i++) {
                arrayList.add(split[i]);
            }
        }
        return arrayList;
    }
    private String recurParse(String name,int next_index){
        String string ="";
        if (next_index==sliceSettings.size()-1){
            ArrayList<String> strings = sliceSettings.get(next_index).getFactorKeys();
            for (String s : strings){
                string +=name+ "$"+s+"__";
            }
            return string;
        }
        else{
            ArrayList<String> strings = sliceSettings.get(next_index).getFactorKeys();
            for (String s : strings){
                String s1 = recurParse(s, next_index + 1);
                String[] split = s1.split("__");
                for (int i = 0; i < split.length; i++) {
                    string += name+"$"+split[i]+"__";
                }
            }
            return string;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SliceSettings{");
        for (StratifiInfo s : sliceSettings){
            sb.append( s.toString());
        }
        sb.append("}");
        return sb.toString();
    }

}
