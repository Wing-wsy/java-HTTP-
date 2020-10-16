package com.annet.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: jh
 * @Date: 2019/11/4 18:01
 * @Version: 1.0
 * @Description: 字符串工具类
 */
public class StrUtils {

    public static boolean isEmpty(String str) {
       if(str == null || "".equals(str.trim())){
           return true;
       }
       return false;
    }

    public static boolean isNullOrEmpty(String s) {
        if (null == s)
            return true;
        return s.length() == 0;
    }

    public static boolean isNotNullOrEmpty(String s) {
        if (null != s)
            return true;
        return s.length() != 0;
    }

    public static boolean notEmpty(String... array) {
        for (String s : array) {
            if (null == s || s.length() == 0)
                return false;
        }
        return true;
    }

    public static boolean isNullOrEmpty(String... array) {
        for (String s : array) {
            if (null == s || s.length() == 0)
                return true;
        }
        return false;
    }

    /**
     * @param string 字符串
     * @param symbol 切割字符标志位
     * @return 字符串集合
     */
    public static List<String> stringSplit(String string, String symbol) {

        return Arrays.asList(string.split(symbol));

    }

    /**
     * @param string 字符串
     * @param lastChar 字符串需要去除的字符
     * @return
     */
    public  static String deleteLastChar(String string,String lastChar){
        // 将所有空格去除掉
        String replace = string.replace(" ", "");
        // 获取字符串最后一位字符
        String lastStr = string.substring(replace.length() - 1, replace.length());
        if(lastChar.equals(lastStr)){
            return replace.substring(0, replace.length() - 1);
        }
        return replace;
    }

}
