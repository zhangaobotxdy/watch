package com.wrist.watch.server;

import com.wrist.watch.SimpeEcode.Message;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
@Slf4j
public class HEncoder extends MessageToMessageEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        out.add(msg.getData());
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("OK OK OK OK"
                .getBytes()));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        ctx.write(response);
        ctx.flush();
    }

}