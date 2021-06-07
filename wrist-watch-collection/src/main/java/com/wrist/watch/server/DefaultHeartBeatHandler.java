package com.wrist.watch.server;

import com.wrist.watch.constant.Connection;
import com.wrist.watch.constant.Const;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultHeartBeatHandler extends ChannelHandlerAdapter implements DetHeartBeatHandler {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		Connection conn = ctx.channel().attr(Const.Connection).get();
		if(conn == null){
			return ;
		}
		try {
			if (evt instanceof IdleStateEvent) {
				IdleStateEvent e = (IdleStateEvent) evt;
				switch (e.state()) {
				case WRITER_IDLE:
					break;
				case READER_IDLE:
					//dto注销操作 TODO 调接口，传入设备编号
					ctx.channel().close();
					log.debug("编号为"+conn.getIdentityId()+"的设备,心跳超时。");
					break;
				default:
					break;
				}
				super.getClass();
				//this.userEventTriggered(ctx, evt);
			}
		} catch (Exception e) {
			throw e;
		}

    }

}
