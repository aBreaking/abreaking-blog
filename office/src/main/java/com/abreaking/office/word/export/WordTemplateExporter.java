package com.abreaking.office.word.export;

import com.abreaking.office.OfficeHelper;
import com.abreaking.office.word.WordExporter;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

/**
 * @{USER}
 * @{DATE}
 */
public class WordTemplateExporter implements WordExporter {

    /**
     * 首先，需要一个模板
     */
    Template template;

    public WordTemplateExporter(Template template) {
        this.template = template;
    }

    public WordTemplateExporter(String templateName) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassicCompatible(true);
        configuration.setTemplateLoader(new FileTemplateLoader(new File(OfficeHelper.TEMPLATE_DIR)));
        this.template = configuration.getTemplate(templateName);
    }

    /**
     * 指定数据，通过freemark操作模板，生成对应word
     * @param dataMap
     * @param writer 输出
     */
    public void export(Map<String,String> dataMap, Writer writer) throws Exception {
        template.process(dataMap,writer);
    }

}
