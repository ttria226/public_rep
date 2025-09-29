package com.xsrw.common.core.print;

/**
 * @author wxr
 * @date 2023/10/915:54
 */
public class PrintDataBackDTO {

    public StringBuilder getBegin() {
        return begin;
    }

    public void setBegin(StringBuilder begin) {
        this.begin = begin;
    }

    public StringBuilder getContent() {
        return content;
    }

    public void setContent(StringBuilder content) {
        this.content = content;
    }

    private StringBuilder begin;
    private StringBuilder content;


}
