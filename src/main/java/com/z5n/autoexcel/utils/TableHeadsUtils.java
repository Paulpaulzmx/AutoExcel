package com.z5n.autoexcel.utils;

public class TableHeadsUtils {
    public static StringBuffer beautifyHeads(String headContent){
        StringBuilder headStringBuffer = new StringBuilder(headContent);
        String[] heads = headStringBuffer.substring(1, headStringBuffer.length() - 1).split(",");
        StringBuffer newHeadContent = new StringBuffer();
        for (String head : heads) {
            newHeadContent.append("「"+head.substring(head.lastIndexOf(':') + 2, head.length() - 1)+"」");
        }
        return newHeadContent;
    }
}
