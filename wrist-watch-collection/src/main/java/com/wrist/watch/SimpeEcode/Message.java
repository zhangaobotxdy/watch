package com.wrist.watch.SimpeEcode;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
public class Message {

    //	帧头
    private byte head;

    //	帧类型
    private byte type;

    //	帧长度
    private byte[] length;

    //	数据内容
    private byte[] data;

    //	CRC16校验和
    private byte[] crc16;

    //	帧尾
    private byte tail;

    public Message(byte head, byte type, byte[] length, byte[] data, byte[] crc16, byte tail) {
        this.head = head;
        this.type = type;
        this.length = length;
        this.data = data;
        this.crc16 = crc16;
        this.tail = tail;
    }

    public Message()
    {
        super();
    }

    public Message(byte[] data) {
        this.data = data;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public byte[] getLength() {
        return length;
    }
    public void setLength(byte[] length) {
        this.length = length;
    }

    public byte getHead() {
        return head;
    }

    public void setHead(byte head) {
        this.head = head;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte[] getCrc16() {
        return crc16;
    }

    public void setCrc16(byte[] crc16) {
        this.crc16 = crc16;
    }

    public byte getTail() {
        return tail;
    }

    public void setTail(byte tail) {
        this.tail = tail;
    }


}
