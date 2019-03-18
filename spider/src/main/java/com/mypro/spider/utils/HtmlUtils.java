package com.mypro.spider.utils;

public class HtmlUtils {

    public static String parseOrigionToJson(String origionData) {
        int start = origionData.indexOf("(");
        int end = origionData.lastIndexOf(")");
        String json = origionData.substring(start + 1, end);
        return json;
    }
}
