package com.abreaking.util.controller;

import com.abreaking.core.JBaseController;
import com.abreaking.core.interceptor.ActionCacheClearInterceptor;
import com.abreaking.office.OfficeHelper;
import com.abreaking.router.RouterMapping;
import com.abreaking.router.RouterNotAllowConvert;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.upload.UploadFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 
 * @author liwei_paas 
 * @date 2019/4/29
 */
@RouterMapping(url = "/util/office", viewPath = "/WEB-INF/util/office")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class OfficeController extends JBaseController {

    static String filename;
    static File file;
    static OfficeHelper helper = new OfficeHelper();

    public void jiamei() {
        keepPara();
        setAttr("templates",helper.getAllTemplate());
    }

    public void uploadWord() {
        UploadFile uploadFile = getFile();

        if (null != uploadFile) {
            //文件的原始名字，作为key
            String originalFileName = uploadFile.getOriginalFileName();
            //而后，文件保存在 template/ 目录下面去
            File file = uploadFile.getFile();
            JSONObject json = new JSONObject();
            try {
                helper.addWordTemplate(originalFileName,file);
                json.put("success", true);
            } catch (Exception e) {
                e.printStackTrace();
                json.put("error",e.getMessage());
            }
            renderJson(json.toString());
        } else {
            renderJson("success", false);
        }
    }

    public void uploadExcel() {

        UploadFile uploadFile = getFile();

        if (null != uploadFile) {
            //文件的原始名字，作为key
            filename = uploadFile.getOriginalFileName();
            //而后，文件保存在 template/ 目录下面去
            file = uploadFile.getFile();

            JSONObject json = new JSONObject();
            renderJson(json.toString());
        } else {
            renderJson("success", false);
        }
    }

    /**
     * 删除模板
     */
    public void deleteTemplate(){
        String template = getPara("template");
        helper.deleteTemplate(template);
        renderAjaxResultForSuccess();
    }

    public void download() throws Exception {
        HttpServletResponse response = getResponse();
        HttpServletRequest request = getRequest();
        String templateName = request.getParameter("template");
        String retFile = helper.build(templateName, file);
        String filePath = OfficeHelper.JiaMei_HETONG+retFile;
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        String retName = setFileDownloadHeader(request,filename);
        response.setHeader("Content-Disposition", "attachment;fileName=" + retName);
        writeBytes(filePath,response.getOutputStream());
        render("jiamei.html");
    }


    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }

        return filename.substring(0,filename.indexOf("."))+".doc";
    }


}
