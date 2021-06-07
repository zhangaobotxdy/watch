package com.wrist.watch.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
@Slf4j
@Component
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        /**
         * 统一编码格式
         */

          //、IdleStateHandler心跳机制,如果超时触发Handle中userEventTrigger()方法
         //字符串编解码器 update 20201028-earl
        //先硬编码，待得到确认数据后改为配置的
        /*        channel.pipeline().addLast(new IdleStateHandler(Const.maxIdleTime,
                    Const.maxIdleTime, Const.maxIdleTime, TimeUnit.SECONDS));
        */
       // channel.pipeline().addLast(new DefaultHeartBeatHandler());
        channel.pipeline()
                //数据传输大小限制
                .addLast(new LengthFieldBasedFrameDecoder(65536, 3, 2, 5, 0))
                //编码解码功能
                .addLast("decoder", new SDecoder())
                .addLast("encoder", new SEncoder())
                .addLast(new NettyServerHandler())
                .addLast(new DefaultHeartBeatHandler());
    }
}
