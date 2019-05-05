package com.abreaking.office;

/**
 * @{USER}
 * @{DATE}
 */
public class StringUtils {

    public static boolean isBlank(String s){
        if (s==null){
            return true;
        }
        if (s.trim().length()==0){
            return true;
        }
        return false;
    }
}
