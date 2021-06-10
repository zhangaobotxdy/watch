package com.wrist.watch.server;

import com.wrist.watch.constant.Connection;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
@Slf4j
@Component
public class NettyServerHttp
{

   @Value("${netty.httpport}")
   private Integer port;
   // private Integer port = 16608;

    @Value("${netty.host}")
    private String host;

/*    @Value("${jks.path}")
    private String jksPath;

    @Value("${jks.password}")
    private String jksPassword;*/

    public static ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<String, Connection>();

    public static void addConnection(Connection conn) {
        connections.put(conn.getIdentityId(), conn);
    }

    public static void removeConnection(Connection conn) {
        //判断channel
        if(conn.equals(getConnection(conn))){
            connections.remove(conn.getIdentityId());
        }
    }

    public static Connection getConnection(Connection conn){
        return connections.get(conn.getIdentityId());
    }

    public static void sendMsg(String identity, Object o) {
        //TODO
    }

    public void start(InetSocketAddress address) {
        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)  // 绑定线程池
                    .channel(NioServerSocketChannel.class)
                    .localAddress(address)
//                    .childHandler(new NettyServerChannelInitializer())//编码解码
                    .option(ChannelOption.SO_BACKLOG, 65536)  //服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝 缓冲区设置
                  /*  .option(ChannelOption.SO_SNDBUF,64*1024) //设置发送缓冲的大小
                    .option(ChannelOption.SO_RCVBUF,64*1024) //设置接收缓冲区大小
                  */
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  //保持长连接，2小时无数据激活心跳机制 保持连续
                    .childHandler(new HttpServerInitializer());
//                    .childHandler(new HttpsServerInitializer(jksPath, jksPassword));

         /*  优化绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(address).sync();
            log.info("netty服务器开始监听端口：" + address.getPort());
            //关闭channel和块，直到它被关闭
            future.channel().closeFuture().sync();*/
            bind(bootstrap, port);

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                log.info(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                log.error("端口[" + port + "]绑定失败!");
            }
        });
    }
}
