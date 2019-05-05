package com.abreaking.office.word;

import java.io.Writer;
import java.util.Map;

/**
 * @{USER}
 * @{DATE}
 */
public interface WordExporter {

    /**
     * 将word 输出
     * @param dataMap
     * @param writer
     */
    void export(Map<String,String> dataMap, Writer writer) throws Exception;
}
