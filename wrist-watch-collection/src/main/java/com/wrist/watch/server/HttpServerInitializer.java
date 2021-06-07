package com.wrist.watch.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline()
                .addLast("decoder", new HDecoder())
                .addLast("encoder", new HttpResponseEncoder())
                .addLast(new HttpServerHandler());
    }

}
