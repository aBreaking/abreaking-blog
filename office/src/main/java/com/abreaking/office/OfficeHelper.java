package com.abreaking.office;

import com.abreaking.office.excel.ExcelReader;
import com.abreaking.office.excel.ExcelResult;
import com.abreaking.office.word.export.WordDomExporter;
import com.abreaking.office.word.export.WordStreamExporter;
import com.abreaking.office.word.XmlParser;
import com.jfinal.kit.PathKit;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @{USER}
 * @{DATE}
 */
public class OfficeHelper {

    //模板文件上传在这里
    public static String TEMPLATE_DIR;

    //生成后的jiamei的合同
    public static String JiaMei_HETONG;

    static {
        String webRoot = PathKit.getWebRootPath();
        TEMPLATE_DIR = new StringBuilder(webRoot).append(File.separator).append("template").append(File.separator).toString();
        JiaMei_HETONG = TEMPLATE_DIR+"jiamei"+File.separator;
        File tdFile = new File(TEMPLATE_DIR);
        File jhFile = new File(JiaMei_HETONG);
        if (!tdFile.exists()){
            tdFile.mkdirs();
        }
        if (!jhFile.exists()){
            jhFile.mkdirs();
        }
    }

    /**
     * 对表格进行处理
     * @return
     * @throws Exception
     */
    public InputStream buildTable(InputStream templateFileInput,List<Map<String, String>> tableDataList){
        //处理后的结构且用StringBuffer来封装
        StringWriter writer = new StringWriter();
        try{
            Document document = new SAXReader().read(templateFileInput);
            WordDomExporter wordDomExporter = new WordDomExporter(document);
            wordDomExporter.export(tableDataList,writer);
            String retString = writer.toString();
            writer.flush();
            return new ByteArrayInputStream(retString.getBytes("utf-8"));
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 整个合同的处理，输出到文件
     * @param templateFileInput
     * @param map
     * @param outFile
     * @return
     */
    public void buildHetong(InputStream templateFileInput,Map<String,String> map,File outFile){
        OutputStreamWriter writer = null;
        try{
            writer = new OutputStreamWriter(new FileOutputStream(outFile), "utf-8");
            WordStreamExporter exporter = new WordStreamExporter(templateFileInput);
            exporter.export(map,writer);
            writer.flush();
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if (writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String build(String wordTemplateName, File excelFile) throws Exception {
        //将源excel生成数据
        Workbook workbook = new XSSFWorkbook(excelFile);
        ExcelReader reader = new ExcelReader();
        ExcelResult excelResult = reader.parse(workbook);
        //指定的模板
        InputStream templateFileInput = new FileInputStream(TEMPLATE_DIR + wordTemplateName);

        //模板的表格处理
        InputStream afterTableTemplate = buildTable(templateFileInput, excelResult.getTableDataList());

        //指定生成后的合同位置及名字
        String name = excelFile.getName();
        String retFileName = name.substring(0,name.indexOf("."))+".doc";
        File outFile = new File(JiaMei_HETONG + retFileName);
        buildHetong(afterTableTemplate,excelResult.getBaseDataMap(),outFile);

        return retFileName;
    }


    /**
     * 添加模板到template目录中去，会对该模板的占位符进行一些处理.
     * @param name
     * @param originWordFile
     * @throws Exception
     */
    public void addWordTemplate(String name, File originWordFile) throws Exception {
        //解析源文件
        XmlParser parser = new XmlParser();
        Document document = parser.parse(new FileInputStream(originWordFile));

        //保存解析后的文档
        File newFile = new File(TEMPLATE_DIR+name);

        //如果这个文件存在，就删除掉
        if (!newFile.exists()){
            newFile.createNewFile();
        }

        //文件保存在这个目录
        save(document,newFile);
        System.out.println("增加了模板"+newFile.getAbsolutePath());
    }

    public void save(Document document,File file) throws IOException {
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
        writer.write(document);
        writer.close();
    }


    public List<String> getAllTemplate(){
        List<String> list = new ArrayList<>();
        File file = new File(TEMPLATE_DIR);
        File[] files = file.listFiles();
        for (File f : files){
            //只要文件
            if (f.isFile()){
                list.add(f.getName());
            }
        }
        return list;
    }

    public void deleteTemplate(String template){
        File file = new File(TEMPLATE_DIR+template);
        if (file.exists()){
            file.delete();
        }
    }

}
