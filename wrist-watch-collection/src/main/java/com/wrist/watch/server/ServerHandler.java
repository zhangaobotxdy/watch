package com.wrist.watch.server;

import com.wrist.watch.SimpeEcode.Message;
import com.wrist.watch.constant.Const;
import com.wrist.watch.model.DataModel;
import com.wrist.watch.model.KeyvrandvsVo;

import com.wrist.watch.utils.AEC256CBC;
import com.wrist.watch.utils.CRC16Util;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ServerHandler {

    private AtomicInteger nConnection = new AtomicInteger();

    private static int sint3 = 0; //通道建立

    private static int sint2 = 0; //密钥协商

    private Codeutil codeutil;

    private String sVRAND1 = null;//呼吸机随机数1—— 4个字节

    private String sVRAND2 = null;//呼吸机随机数2—— 4个字节

    private String sSRAND1 = null;//服务器随机数1—— 4个字节

    private String sSRAND2 = null; //服务器随机数2—— 4个字节

    private MessageService messageService;

    private KeyvrandvsVo keyvrandvsVo = new KeyvrandvsVo();

    public ServerHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    public static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * @param ctx
     * @author zhangaobo on 2021/06/04 16:10
     * @DESCRIPTION: 有客户端发消息会触发此函数
     * @return: void
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg, int type) throws Exception {
        keyvrandvsVo.setChannelId(ctx.channel().id());
        keyvrandvsVo.setCtxIP(ctx);
        log.info("加载客户端报文......");
        log.info("【" + ctx.channel().id() + "】" + "msg :" + msg);

        Message mes = null;
        try {
            mes = (Message) msg;
        } catch (Exception e) {
        }
        if (mes == null) {
            return;
        }
        if (mes.getType() == 0x00) {
            return;
        }
        log.info("~~~~~包认证协议: " + mes.getType());
        switch (mes.getType()) {
            case Const.Req01://认证请求错误
                handlerReq01(mes.getData(), ctx.channel().id(), ctx, type);
                break;
            case Const.Req02: //密钥协商
                break;
            case Const.Req03://通道建立应答
                handlerReq03(mes.getData());
                break;
            case Const.Req04://通信正常 应用层
                log.info(Codeutil.DateandString() + "\n ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~04 H  start~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                try {
                    DataModel dataModel = messageService.startDataModelService(mes.getData(), keyvrandvsVo);
                    log.info("\n dataModel:" + dataModel.getCommand());
                    if (dataModel.getCommand() != null && dataModel.getCommand().length != 0) {
                        this.ResponseMesage(ctx.channel().id(), dataModel, type);
                    }
                    log.info(Codeutil.DateandString() + "\n ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~04 H  end~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getMessage();
                }
                break;
            case Const.Req05:
                break;
            case Const.Req06:
                break;
            default:
                break;

        }
        if (type == 0) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(HDecoder.byteBufMap.get(ctx.channel().id()).getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.write(response);
            ctx.flush();
        }
    }

    /**
     * @param msg       需要发送的消息内容
     * @param channelId 连接通道唯一id
     * @author earl 2019/4/28 16:10
     * @DESCRIPTION: 服务端给客户端发送消息
     * @return: void
     */
    public void channelWriteHttp(ChannelId channelId, Message msg) throws Exception {
        String info = ByteBufUtil.hexDump(new byte[]{msg.getHead()}) + ByteBufUtil.hexDump(new byte[]{msg.getType()}) + ByteBufUtil.hexDump(msg.getLength()) + ByteBufUtil.hexDump(msg.getData()) + ByteBufUtil.hexDump(msg.getCrc16()) + ByteBufUtil.hexDump(new byte[]{msg.getTail()});
        HDecoder.byteBufMap.put(channelId, info);
    }

    /**
     * @param msg       需要发送的消息内容
     * @param channelId 连接通道唯一id
     * @author earl 2019/4/28 16:10
     * @DESCRIPTION: 服务端给客户端发送消息
     * @return: void
     */
    public void channelWrite(ChannelId channelId, Object msg) throws Exception {
        ChannelHandlerContext ctx = CHANNEL_MAP.get(channelId);

        if (ctx == null) {
            log.info("通道【" + channelId + "】不存在");
            return;
        }
        if (msg == null || msg == "") {
            log.info("服务端响应空的消息");
            return;
        }
        //将客户端的信息直接返回写入ctx
        /* log.info("___写入___MSG: "+msg); */
        ctx.write(msg);
        //刷新缓存区
        ctx.flush();
    }

    @SneakyThrows
    public void handlerReq01(byte[] dtmsg, ChannelId ctx, ChannelHandlerContext cctx, int type) {
        // TODO 调用注册接口
        try {
            log.info(Codeutil.DateandString() + "\n ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~认证请求指令: 01H  start~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            // TODO 调用认证逻辑


        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            log.info(e.getMessage());
            log.info("~~~~~~~~~~~~~~~~~认证失败~~~~~~~~~~~~~~~~~");
        }
    }

    @SneakyThrows
    private void handlerReq03(byte[] dtmsg) {
        log.info(Codeutil.DateandString() + "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~通道建立应答指令: 03H start~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        int channowIndex = 0;
        byte[] a01data = dtmsg;
        byte[] bchanProtocolVersion = Arrays.copyOfRange(a01data, channowIndex, channowIndex + 2); //偏移2
        String sbversion = Codeutil.byte2Hex(bchanProtocolVersion); //receiveHexToString
        channowIndex += 2;
        byte[] bResponseResults = Arrays.copyOfRange(a01data, channowIndex, channowIndex + 2); //偏移2
        String strbResponseResults = Codeutil.byte2Hex(bResponseResults);
        channowIndex += 2;
        byte[] bVR1_vR2_sR2_sR3 = Arrays.copyOfRange(a01data, channowIndex, channowIndex + 32); //偏移2
        String sbVR1_vR2_sR2_sR3 = Codeutil.byte2Hex(bVR1_vR2_sR2_sR3);
        log.info("\n 协议版本：" + sbversion + "\n 应答结果：" + strbResponseResults + " \n 呼吸~服务器 1~4随机数:" + sbVR1_vR2_sR2_sR3);
        log.info("\n ^异或交互密钥:" + keyvrandvsVo.getSsxkey());
       /* log.info("\n 通道建立应答_呼吸机随机数1:"+sVRAND1); //呼吸机随机数1—— 4个字节
        log.info("\n 通道建立应答_呼吸机随机数2:"+sVRAND2); //呼吸机随机数2—— 4个字节
        log.info("\n 通道建立应答_服务器随机数1:"+sSRAND1); //服务器随机数1—— 4个字节
        log.info("\n 通道建立应答_服务器随机数2:"+sSRAND2); //服务器随机数2—— 4个字节*/
        //解析
        //AEC256CBC aec256cbc= new AEC256CBC();
        byte[] decbt = AEC256CBC.AES_cbc_decrypt(bVR1_vR2_sR2_sR3, Codeutil.hex2Bytes(keyvrandvsVo.getSsxkey()), Codeutil.hex2Bytes(keyvrandvsVo.getSvr1_vr2_vs1_vs2()));
        int aescbcnowIndex = 0;
        byte[] basscbc_sVRAND1 = Arrays.copyOfRange(decbt, aescbcnowIndex, aescbcnowIndex + 4); //偏移4
        String sbasscbc_sVRAND1 = Codeutil.byte2Hex(basscbc_sVRAND1); //receiveHexToString
        aescbcnowIndex += 4;
        byte[] basscbc_sVRAND2 = Arrays.copyOfRange(decbt, aescbcnowIndex, aescbcnowIndex + 4); //偏移4
        String sbasscbc_sVRAND2 = Codeutil.byte2Hex(basscbc_sVRAND2); //receiveHexToString
        aescbcnowIndex += 4;
        byte[] basscbc_sSRAND1 = Arrays.copyOfRange(decbt, aescbcnowIndex, aescbcnowIndex + 4); //偏移4
        String sbasscbc_sSRAND1 = Codeutil.byte2Hex(basscbc_sSRAND1); //receiveHexToString
        aescbcnowIndex += 4;
        byte[] basscbc_sSRAND2 = Arrays.copyOfRange(decbt, aescbcnowIndex, aescbcnowIndex + 4); //偏移4
        String sbasscbc_sSRAND2 = Codeutil.byte2Hex(basscbc_sSRAND2); //receiveHexToString
        log.info("\n decbt:" + Codeutil.byte2Hex(decbt) +
                "\n 呼吸机随机数1:" + sbasscbc_sVRAND1 +
                "\n 呼吸机随机数2:" + sbasscbc_sVRAND2 +
                "\n 服务器随机数1:" + sbasscbc_sSRAND1 +
                "\n 服务器随机数2:" + sbasscbc_sSRAND2);
        log.info(Codeutil.DateandString() + "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~通道建立应答指令: 03H end~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @SneakyThrows
    public void ResponseMesage(ChannelId ctx, DataModel dataModel, int type) throws Exception {
        String str = null;
        log.info(Codeutil.DateandString() + "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ResponseMesage 05H start~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        byte[] strcom = dataModel.getCommand();
        Message mssg = new Message();
        try {
              /* if(!Arrays.equals(strcom,new byte[]{(byte)0x36})) //
                {*/
            log.info("&&&&&&&&&&&&&&&&&&&&返回呼吸机相关指令参数:" + Codeutil.byte2Hex(dataModel.getCommand()));
            if (!Arrays.equals(strcom, new byte[]{(byte) 0x31}) && !Arrays.equals(strcom, new byte[]{(byte) 0x32}) && !Arrays.equals(strcom, new byte[]{(byte) 0x32})) {
                str = "A5" + Codeutil.byte2Hex(strcom) + String.format("%4d", strcom.length + 2).replace(" ", "0") + "FFFF";
            } else {
                str = "A5" + Codeutil.byte2Hex(strcom) + String.format("%4d", strcom.length + 1).replace(" ", "0") + "FF";
            }
            int ifcrc8 = CRC16Util.crc8_table(Codeutil.HexString2Bytes(str));
            log.info(" \n ~~~~~CRC8十进制:" + ifcrc8 + " CRC8 长度十六进制:" + Codeutil.byte2Hex(Codeutil.intcrc8ToByteArray(ifcrc8)));
            String strcrc8 = str + Codeutil.byte2Hex(Codeutil.intcrc8ToByteArray(ifcrc8));
            log.info("\n ~~~~~~~~~~~数据段内容:" + strcrc8);
            byte[] bmesdecbt = AEC256CBC.AES_cbc_encrypt(Codeutil.HexString2Bytes(strcrc8), Codeutil.hex2Bytes(keyvrandvsVo.getSsxkey()), Codeutil.hex2Bytes(keyvrandvsVo.getSvr1_vr2_vs1_vs2()));
            //String str16=Codeutil.lowtostringhigh(String.format("%4d",Codeutil.intToByteArray(bmesdecbt.length)).replace(" ", "0") );
            String str16 = Codeutil.lowtostringhigh(Codeutil.byte2Hex(Codeutil.intToByteArray(bmesdecbt.length)));

            log.info("AES加密转换 长度:" + str16 + "  ~~ AES加密转换" + Codeutil.byte2Hex(bmesdecbt));
            String strdata = "5005" + str16 + Codeutil.byte2Hex(bmesdecbt);
            log.info("\n 数据内容:" + strdata);
            int stcrc16 = CRC16Util.CRC16(Codeutil.HexString2Bytes(strdata));
            log.info(" \n CRC16 长度十六进制:" + Codeutil.byte2Hex(Codeutil.intToByteArray(stcrc16)));
            String strmes = strdata + Codeutil.byte2Hex(Codeutil.intToByteArray(stcrc16));
            log.info("\n 打印呼吸机信息:" + strmes);

            mssg.setHead(Const.HeadDefault);//帧头
            mssg.setType(Const.Req05);//服务器发送
            mssg.setLength(Codeutil.HexString2Bytes(str16));
            mssg.setData(bmesdecbt);
            mssg.setCrc16(Codeutil.intToByteArray(stcrc16));
            mssg.setTail((byte) 0x16);//帧尾
            if (type == 0) {
                this.channelWriteHttp(ctx, mssg);
            } else {
                this.channelWrite(ctx, mssg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        log.info(Codeutil.DateandString() + "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ResponseMesage 05H end~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

}
