package com.abreaking.office.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析完excel 后的数据
 * @author liwei_paas
 * @date 2019/4/30
 */
public class ExcelResult {

    //基本的数据：key:value
    private Map<String,String> baseDataMap = new HashMap<>();

    //表格里面的数据
    private List<Map<String,String>> tableDataList = new ArrayList<>();

    public void pullAll(Map<String,String> baseDataMap){
        this.baseDataMap.putAll(baseDataMap);
    }

    public void addAll(List<Map<String,String>> tableDataMap){
        this.tableDataList.addAll(tableDataMap);
    }


    public Map<String, String> getBaseDataMap() {
        return baseDataMap;
    }

    public void setBaseDataMap(Map<String, String> baseDataMap) {
        this.baseDataMap = baseDataMap;
    }

    public List<Map<String, String>> getTableDataList() {
        return tableDataList;
    }

    public void setTableDataList(List<Map<String, String>> tableDataList) {
        this.tableDataList = tableDataList;
    }

    public void prettyPrint(){
        System.out.println("baseDataMap----------");
        for (String key : baseDataMap.keySet()){
            System.out.println(key+"->"+baseDataMap.get(key));
        }
        System.out.println("tableDataMap---------");
        for (Map<String,String> map: tableDataList){
            for (String key : map.keySet()){
                System.out.println(key+"->"+map.get(key));
            }
        }
    }
}
