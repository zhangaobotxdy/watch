package com.wrist.watch.server;

import com.wrist.watch.model.KeyvrandvsVo;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /*测试*/

    private AtomicInteger nConnection = new AtomicInteger();

    private static int sint3 = 0; //通道建立

    private static int sint2 = 0; //密钥协商

    private Codeutil codeutil;

    private String sVRAND1 = null;//呼吸机随机数1—— 4个字节

    private String sVRAND2 = null;//呼吸机随机数2—— 4个字节

    private String sSRAND1 = null;//服务器随机数1—— 4个字节

    private String sSRAND2 = null; //服务器随机数2—— 4个字节

    @Autowired
    private MessageService messageService;

    @Autowired
    protected static NettyServerHandler nettyServerHandler = new NettyServerHandler();

    private KeyvrandvsVo keyvrandvsVo = new KeyvrandvsVo();


    /*test*/

//    public NettyServerHandler() {
//        /**
//         *  每两秒统计一下连接数
//         */
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
//            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% connections: " + nConnection.get());}, 0, 2, TimeUnit.SECONDS);
//
//    }

    /**
     * 初始化调用
     */
    @PostConstruct
    public void init() {
        nettyServerHandler.messageService = this.messageService;
    }

    /**
     * @param ctx
     * @author earl on 2020/11/28 16:10
     * @DESCRIPTION: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        nConnection.incrementAndGet();/*test*/

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();
        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();
        System.out.println();
        //如果map中不包含此连接，就保存连接
        if (ServerHandler.CHANNEL_MAP.containsKey(channelId)) {
            log.info("客户端【" + channelId + "】是连接状态，连接通道数量: " + ServerHandler.CHANNEL_MAP.size());
        } else {
            //保存连接
            ServerHandler.CHANNEL_MAP.put(channelId, ctx);
            log.info("客户端【" + channelId + "】连接netty服务器[IP:" + clientIp + "--->PORT:" + clientPort + "]");
            log.info("连接通道数量: " + ServerHandler.CHANNEL_MAP.size());
        }
    }

    /**
     * @param ctx
     * @author zhangaobo 2021/5/28 16:10
     * @DESCRIPTION: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        nConnection.decrementAndGet();/*test*/

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        ChannelId channelId = ctx.channel().id();
        //包含此客户端才去删除
        if (ServerHandler.CHANNEL_MAP.containsKey(channelId)) {
            //删除连接
            ServerHandler.CHANNEL_MAP.remove(channelId);
            System.out.println();
            log.info("客户端【" + channelId + "】退出netty服务器[IP:" + clientIp + "--->PORT:" + insocket.getPort() + "]");
            log.info("连接通道数量: " + ServerHandler.CHANNEL_MAP.size());
        }
    }

    /**
     * @param ctx
     * @author zhangaobo 2021/5/28 16:10
     * @DESCRIPTION: 有客户端发消息会触发此函数
     * @return: void
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ServerHandler serverHandler = new ServerHandler(nettyServerHandler.messageService);
        serverHandler.channelRead(ctx, msg, 1);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String socketString = ctx.channel().remoteAddress().toString();
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("Client: " + socketString + " READER_IDLE 读超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("Client: " + socketString + " WRITER_IDLE 写超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("Client: " + socketString + " ALL_IDLE 总超时");
                ctx.disconnect();
            }
        }
    }

    /**
     * @param ctx
     * @author earl 2019/4/28 16:10
     * @DESCRIPTION: 发生异常会触发此函数
     * @return: void
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println();
        log.info(ctx.channel().id() + " 发生了错误,此连接被关闭" + "此时连通数量: " + ServerHandler.CHANNEL_MAP.size());
        cause.printStackTrace();
        // ctx.close(); //长连接
        /*test*/
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
