package com.wrist.watch.constant;

import io.netty.util.AttributeKey;

/**
 * 
 * @ClassName: Const
 * @Description: 内容
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
*
 */
public class Const {
	
	/**
	 * 接收报文最短长度（无数据内容无校验）
	 */
	public static final int AcceptMinMsgLength=17;
	
	/**
	 * 接收报文最大长度
	 */
	public static final int AcceptMaxMsgLength=65536;
	/**
	 * 报文最短长度
	 */
	public static final int SendMinMsgLength=25;
	
	public static final int NextFieldLength = 1;
	
	/**
	 * 帧头字段长度
	 */
	public static final int HeadFieldLength = 1;
	
	/**
	 * 类型字段长度
	 */
	public static final int TypeFieldLength = 1;
		
	/**
	 * 消息长度字段的长度
	 * 整个帧的长度
	 */
	public static final int MsgFieldLength = 2;
	

	/**
	 * crc字段长度
	 */
	public static final int CrcFieldLength = 2;
	
	/**
	 * 帧尾字段长度
	 */
	public static final int TailFieldLength = 1;
	
	//最大能忍受多长时间未收到心跳
	public static final int MaxIdleTime = 1100;
	
	//channel对应的连接
	public static final AttributeKey<Connection> Connection = AttributeKey.valueOf("connection");

	public static final byte Req01 = (byte)0x01;//认证请求

	public static final byte Req02 = (byte)0x02;//密钥协商

	public static final byte Req03 = (byte)0x03;//通道建立应答
	//手环发服务器
	public static final byte Req04 = (byte)0x04 ;
	//服务器发手环
	public static final byte Req05 = (byte)0x05;

	/**
	 * 数据结构帧内
	 */
	public static final byte Req06=(byte)0x5A;
	//保留字节
	public static final byte ReserveCode = 0x00;

    //帧头
	public static final byte HeadDefault =0x50;
	//clien -server
	public static final byte TypeclientDefault =0x04;
	//server-clien
	public static final byte TypeserverDefault =0x05;
	//固定帧尾
	public static final byte TailDefault =0x16;
	//类型
	public static final byte Resp50 =0x50;

	//业务模块
	public static final byte Busines_01 =(byte)0x31;//治疗控制参数信息结构体数据传输
	public static final byte Busines_02 =(byte)0x32;//产品信息结构体数据传输
	public static final byte Busines_03 =(byte)0x33;//产品使用状态结构体数据传输

	public static final byte Busines_04 =(byte)0x36;//统计数据结构体传输
	public static final byte Busines_05 =(byte)0x35;//
	public static final byte Busines_06 =(byte)0x52; //10秒波形参数
	public static final byte Busines_07 =(byte)0x60; //呼吸机运行状态传输
	public static final byte Busines_08 =(byte)0x39; //呼吸机呼吸事件同步指令
	public static final byte Busines_09 =(byte)0x51; //设置治疗参数请求
	public static final byte Busines_10 =(byte)0x40; //呼吸机使用日志还同步指令
	public static final byte AuthenticationModel =0x01; //认证请求
	public static final byte KeyNegotiation =0x02; //密钥协商
	public static final byte ChannelesponseModel =0x03; //通道建立应答

	//msg请求类型
	public static final byte[] Reqs = {Req01,Req02,Req03,Req04,Req05,Busines_01, Busines_03, Busines_02, Busines_04, Busines_05, Busines_06, Busines_07, Busines_08, Busines_09, Busines_10};

	public static final  byte[] Bevent={(byte)0xa0,(byte)0xA1,(byte)0xA2,(byte)0xA3,(byte)0xA4,(byte)0xA5,
										(byte)0xA6,(byte)0xA7,(byte)0xA8,(byte)0xAE,(byte)0xAF};

	public static final int[] Iarray={0,1,2,3};


}
