package com.wrist.watch.SimpeEcode;


/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
public class MessageData {

      private String head;

      private String deal;

      private Integer length;

      private String data;

      private String crc8;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCrc8() {
        return crc8;
    }

    public void setCrc8(String crc8) {
        this.crc8 = crc8;
    }
}
