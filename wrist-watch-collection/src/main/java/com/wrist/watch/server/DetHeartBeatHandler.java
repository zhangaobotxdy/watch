package com.wrist.watch.server;

import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
public interface DetHeartBeatHandler {
    void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception;
}
