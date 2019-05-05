package com.abreaking.office.word.export;

import com.abreaking.office.word.WordExporter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @{USER}
 * @{DATE}
 */
public class WordStreamExporter implements WordExporter {

    private static final String regex = ".*\\$\\{.+\\}.*";

    InputStream inputStream;

    public WordStreamExporter(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public void export(Map<String,String> dataMap, Writer writer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));

        String line = "";
        while((line = reader.readLine())!=null){
            if (line.indexOf("${")!=-1){
                if (line.matches(regex)){
                    List<String> list = new ArrayList<>();
                    keys(line,list);
                    for (String s : list){
                        if (dataMap.containsKey(s)){
                            line = line.replaceAll("\\$\\{"+s+"\\}",dataMap.get(s));
                        }else{
                            System.out.println("原来的excel中没得 "+s+" 这个key");
                        }
                    }
                }
            }
            writer.write(line);
        }
    }




    public static void keys(String state,List<String> list){
        if (state.indexOf("${")==-1){
            return ;
        }
        state = state.substring(state.indexOf("${")+2);
        int i = state.indexOf("}");
        list.add(state.substring(0,i));
        keys(state.substring(i)+1,list);
    }
}
