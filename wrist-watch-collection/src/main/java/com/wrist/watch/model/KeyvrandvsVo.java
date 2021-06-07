package com.wrist.watch.model;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
@Getter
@Setter
public class KeyvrandvsVo {

    private  String ssxkey;//交互密钥

    private  String svr1_vr2_vs1_vs2;

    private  String sbMachineSerialNumbere;

    private  String stpublickey;//交互密钥

    private  ChannelId channelId;

    private ChannelHandlerContext ctxIP;

    public KeyvrandvsVo(String ssxkey, String svr1_vr2_vs1_vs2, String sbMachineSerialNumbere,
                        String stpublickey, ChannelId channelId, ChannelHandlerContext ctxIP)
    {
        this.ssxkey=ssxkey;
        this.svr1_vr2_vs1_vs2=svr1_vr2_vs1_vs2;
        this.sbMachineSerialNumbere=sbMachineSerialNumbere;
        this.stpublickey=stpublickey;
        this.channelId=channelId;
        this.ctxIP=ctxIP;
    }
    public KeyvrandvsVo()
    {
        super();
    }

    public String getSsxkey() {
        return ssxkey;
    }

    public void setSsxkey(String ssxkey) {
        this.ssxkey = ssxkey;
    }

    public String getSvr1_vr2_vs1_vs2() {
        return svr1_vr2_vs1_vs2;
    }

    public void setSvr1_vr2_vs1_vs2(String svr1_vr2_vs1_vs2) {
        this.svr1_vr2_vs1_vs2 = svr1_vr2_vs1_vs2;
    }

    public String getSbMachineSerialNumbere() {
        return sbMachineSerialNumbere;
    }

    public void setSbMachineSerialNumbere(String sbMachineSerialNumbere) {
        this.sbMachineSerialNumbere = sbMachineSerialNumbere;
    }

    public String getStpublickey() {
        return stpublickey;
    }

    public void setStpublickey(String stpublickey) {
        this.stpublickey = stpublickey;
    }

    public ChannelId getChannelId() {
        return channelId;
    }

    public void setChannelId(ChannelId channelId) {
        this.channelId = channelId;
    }

    public ChannelHandlerContext getCtxIP() {
        return ctxIP;
    }

    public void setCtxIP(ChannelHandlerContext ctxIP) {
        this.ctxIP = ctxIP;
    }
}
