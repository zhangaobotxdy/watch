package com.wrist.watch;

import com.wrist.watch.server.NettyServer;
import com.wrist.watch.server.NettyServerHttp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


/**
 * @ClassName :     //类名
 * @Description :  设备启动服务
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
@SpringBootApplication
@Slf4j
@Component
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class WatchApplication implements CommandLineRunner {

    @Autowired
    private NettyServer nettyServer;

    @Autowired
    private NettyServerHttp nettyServerHttp;

    @Value("${netty.port}")
    private Integer port;

    @Value("${netty.host}")
    private String host;

    public static void main(String[] args) {
        SpringApplication.run(WatchApplication.class, args);

    }

    @Async
    @Override
    public void run(String... args) throws Exception
    {
        InetSocketAddress address=new InetSocketAddress(host,port);
        log.info("WristWatchApplication_Netty Server 启动");
        nettyServer.start(address);
        nettyServerHttp.start(address);
    }

}
