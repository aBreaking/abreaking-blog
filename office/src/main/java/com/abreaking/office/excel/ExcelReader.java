package com.abreaking.office.excel;

import com.abreaking.office.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @{USER}
 * @{DATE}
 */
public class ExcelReader {

    public static Map<String,String> PROPERTY_MAP = new HashMap<>();

    public ExcelResult parse(Workbook workbook){
        ExcelResult result = new ExcelResult();
        Sheet sheet = workbook.getSheetAt(0);
        for (int i=0;i<sheet.getPhysicalNumberOfRows();i++){
            Row row = sheet.getRow(i);
            String rowFirstText = row.getCell(0).toString().trim();
            //对表格的处理
            if ("序号".equals(rowFirstText)){
                List<Row> rows = new ArrayList<>();
                rows.add(row);
                for (int j =i+1 ;j<sheet.getPhysicalNumberOfRows();j++){
                    row = sheet.getRow(j);
                    String text = row.getCell(0).toString();
                    //如果是数字开头的
                    i = j;
                    if (text.matches("\\d+.*")){
                        //就加入到list中，作为表格处理
                        rows.add(row);
                    }else{
                        break;
                    }

                }
                //完事了对表格处理
                handTable(rows,result);
            }
            //表格完了继续正常的处理
            //正常行的处理
            handNormal(row,result);
        }
        return result;
    }


    /**
     * 表格行的处理思路
     *
     * @param rowList
     */
    private void handTable(List<Row> rowList,ExcelResult result){
        List<Map<String,String>> tableMapList = new ArrayList<>();
        //表格标题
        Row titleRow = rowList.get(0);
        for (int i=1;i<rowList.size();i++){
            Map<String,String> map = new HashMap<>();
            Row thisRow = rowList.get(i);
            for (int j=0;j<titleRow.getPhysicalNumberOfCells();j++){
                Cell cell = thisRow.getCell(j);
                String value = cell.toString();
                if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
                    try{
                        value = cell.getStringCellValue();
                    }catch (IllegalStateException  e){
                        value = String.valueOf(cell.getNumericCellValue());
                    }
                }
                map.put(titleRow.getCell(j).toString(),value);
            }
            tableMapList.add(map);
        }
        result.addAll(tableMapList);
    }

    private void handNormal(Row row,ExcelResult result){
        Map<String, String> map = new HashMap<>();
        for (int j=0;j<row.getPhysicalNumberOfCells();j++){
            Cell cell = row.getCell(j);
            if (!StringUtils.isBlank(cell.toString())){
                parse2Map(cell.toString(),map);
            }
        }
        result.pullAll(map);
    }


    private static void parse2Map(String s,Map<String,String> map){

        String separator = "：";

        if (s.indexOf(separator)==s.lastIndexOf(separator)){

            String[] split = s.split(separator);
            if (split.length<2){
                return;
            }
            String key = split[0].trim();
            String value = split[1].trim();
            if (key.indexOf("、")!=-1){
                key = key.substring(key.indexOf("、")+1);
            }
            map.put(key,value);
        }else{
            if (s.indexOf("技术规范要求")!=-1){
                //技术规范要求的解析
                int i = s.indexOf(separator);
                String pKey = s.substring(s.indexOf("、")+1,i);
                String value = s.substring(i+1);
                String[] split = value.split("；");
                for (String _s : split){
                    int i1 = _s.indexOf(".");
                    if (i1==-1){
                        continue;
                    }
                    String _k = pKey + "."+_s.substring(0,i1);
                    String _v = _s.substring(i1+1);
                    map.put(_k,_v);
                    map.putAll(parseMarkList(_k,_v));
                }
            }
            if(s.indexOf(" ")==-1){
                return;
            }
            String[] split = s.trim().split(" ");
            for (String _s :split){
                parse2Map(_s,map);
            }
        }

    }

    private static Map<String,String> parseMarkList(String key,String value){
        Map<String,String> map = new HashMap<>();
        String regex = "[a-z].";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        String[] split = value.split(regex);
        for (int i=1;matcher.find();i++){
            String tag = matcher.group();
            if (tag.endsWith(".")){
                tag = tag.substring(0,tag.length()-1);
            }
            map.put(key+"."+tag,split[i]);
        }
        return map;
    }



    public static void main(String[] args) throws IOException, InvalidFormatException {
        Workbook workbook = new XSSFWorkbook(new File("C:\\Users\\MI\\Desktop\\jiamei\\test\\original.xlsx"));
        ExcelReader reader = new ExcelReader();
        ExcelResult result = reader.parse(workbook);
        result.prettyPrint();

    }

}
