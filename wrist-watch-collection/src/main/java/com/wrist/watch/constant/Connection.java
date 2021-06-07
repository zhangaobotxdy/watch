package com.wrist.watch.constant;

import com.wrist.watch.server.NettyServer;
import io.netty.channel.Channel;

/**
 * 
 * @ClassName: Connection
 * @Description:
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
*
 */
public class Connection {
	
	public static Connection getConnection(String idenitity) {
		return NettyServer.connections.get(idenitity);
	}
	
	private Channel channel;
	
	private String identityId;

	public Connection(Channel channel) {
		super();
		this.channel = channel;

	}
	
	public void register(Connection conn) {
		conn.getChannel().attr(Const.Connection).set(conn);
		NettyServer.addConnection(conn);
	}

	public void logOut() {
		NettyServer.removeConnection(this);
		/*this.getChannel().attr(Const.connection).remove();*/
		this.channel.close();
	}
	
	public void write(byte[] rec) {
		this.channel.writeAndFlush(rec);
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	
	
}
