package com.wrist.watch.SimpeEcode;

import java.util.Date;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
public class MessageHeader {

    private int headData;

    private int client;//客户端发起

    private int length; //两位字节长度

    private Message message;//数据段

    private String token;//认证

    private  Date createdDate;

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public int getHeadData() {
        return headData;
    }

    public void setHeadData(int headData) {
        this.headData = headData;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
