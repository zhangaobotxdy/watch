package com.wrist.watch.server;

import com.wrist.watch.SimpeEcode.Message;
import com.wrist.watch.constant.Const;
import com.wrist.watch.utils.CRC16Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.HttpRequestDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
@Slf4j
public class HDecoder extends HttpRequestDecoder {

    /**
     * 剩余长度
     */
    private int remainLength = 0;
    /**
     * 已读长度
     */
    private int hasReadLength = 0;

    public static Map<ChannelId, String> byteBufMap = new ConcurrentHashMap<>();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        super.decode(ctx, in, out);
        log.info("-----------------------------" + Codeutil.DateandString() + "--------------------------------监控收到数据----start----------------------------");
        // 显示十六进制的接收码
        log.info("\n 手环接收协议收到数据为:{}", ByteBufUtil.hexDump(in).toUpperCase());
        byte[] bytes = Codeutil.hex2Bytes(ByteBufUtil.hexDump(in).toUpperCase());
        int readableLength = in.readableBytes();
        if (readableLength < Const.AcceptMinMsgLength) {
            return;
        }
        remainLength = readableLength;
        readableLength = in.readableBytes();
        remainLength = readableLength;
        int markLength = in.readerIndex();
        readableLength = readableLength + markLength;
        hasReadLength = markLength;
        while (checkByteBuf(in, readableLength)) {
            byte head = getHeadField(in);
            byte type = getTypeField(in);
            int msgLength = getMsgLengthField(in);
            byte[] data = {};
            byte[] crc16 = {};
            if (msgLength != Const.AcceptMinMsgLength) {
                byte[] bbata = new byte[msgLength];
                in.readBytes(bbata);
                data = Arrays.copyOf(bbata, msgLength);
                byte[] lcrc = new byte[2];
                in.readBytes(lcrc);
                crc16 = Arrays.copyOf(lcrc, 2);
                /*log.info("~~~~~~~~~~~~~~~~crc16:"+crc16);*/
                if (getCrc16(crc16, Arrays.copyOfRange(bytes, 0, bytes.length - 3)) != true) {
                    //byte end=getTailField(in);
                    log.info("~~~ 手环与本地校验CRC错误! ~~~");
                    continue;
                }
            }
            byte end = getTailField(in);
            Message mssg = new Message(head, type, Codeutil.intToByteArray(msgLength), data, crc16, end);
            out.add(mssg);
        }
        in.clear();
        out.remove(in);
        log.info("\n 手环接收协议收到数据-----end:{}", ByteBufUtil.hexDump(in).toUpperCase());
        log.info("--------------------------------" + Codeutil.DateandString() + "--------------------------------呼吸接收协议收到数据----end----------------------------");
    }

    /**
     * @param in
     * @param readableLength
     * @return boolean
     * @Title. checkByteBuf
     * @Description. 处理半包和粘包
     * @exception.
     */
    private boolean checkByteBuf(ByteBuf in, int readableLength) {
        int headIndex = in.indexOf(hasReadLength, readableLength, Const.HeadDefault);
        if (headIndex < 0) {
            return false;
        }
        in.readerIndex(headIndex);
        byte head = getHeadField(in);
        byte type = getTypeField(in);

        if (!ArrayUtils.contains(Const.Reqs, type)) {
            log.error("类型不正确");
            hasReadLength = headIndex + 1;
            return checkByteBuf(in, readableLength);
        }
        /** 数据段内容长度 **/
        int imsgLength = getMsgLengthField(in);
        log.info("~~~~~~~~数据段内容长度imsgLength:" + imsgLength);
        int needLength = imsgLength + headIndex;
        if (needLength > readableLength) {
            return false;
        }
        byte end = in.getByte(readableLength - 1);
        if (end != Const.TailDefault) {
            log.error("~~~~~~~~~~~~~~呼吸请求帧尾不正确!");
            hasReadLength = headIndex + 1;
            return checkByteBuf(in, readableLength);
        }
        /* 总长度-数据端长度 */
        in.readerIndex(headIndex);
        remainLength = readableLength - needLength;
        hasReadLength = needLength; /*数据段内容*/
        return true;

    }

    private byte getHeadField(ByteBuf in) {
        ByteBuf headbuf = in.readBytes(Const.HeadFieldLength);
        return headbuf.getByte(0);
    }

    private byte getTypeField(ByteBuf in) {
        ByteBuf buf = in.readBytes(Const.TypeFieldLength);
        return buf.getByte(0);
    }

    private int getMsgLengthField(ByteBuf in) {
        ByteBuf buf = in.readBytes(Const.MsgFieldLength);
        //设置网络字节序
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        return Codeutil.bytesToInt(req, 0);
    }

    private boolean getCrc16(byte[] crc16in, byte[] datacrc16) {
        boolean bo = false;
        log.info("_手环获取:" + Codeutil.byte2Hex(crc16in) + "_手环本地计算:" + Codeutil.byte2Hex(Codeutil.intToByteArray(CRC16Util.CRC16(datacrc16))));
        if (crc16in.length != 0) {
            if (Arrays.equals(crc16in, Codeutil.intToByteArray(CRC16Util.CRC16(datacrc16)))) {
                bo = true;
            }
        }
        return bo;
    }

    private byte getTailField(ByteBuf in) {
        ByteBuf buf = in.readBytes(Const.TailFieldLength);
        return buf.getByte(0);
    }

}