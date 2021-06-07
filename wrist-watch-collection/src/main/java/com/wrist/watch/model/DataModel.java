package com.wrist.watch.model;

/**
 * @ClassName :     //类名
 * @Description : 手环运行状态传输  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
public class DataModel
{
    private String head;
    private String type;
    private String length;
    private byte[] command;
    private String crc;

    public DataModel(String head, String type, String length, byte[] command, String crc) {
        this.head = head;
        this.type=type;
        this.length = length;
        this.command = command;
        this.crc = crc;
    }
    public DataModel()
    {
        super();
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public byte[] getCommand() {
        return command;
    }

    public void setCommand(byte[] command) {
        this.command = command;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }
}
