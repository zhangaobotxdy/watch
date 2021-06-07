package com.wrist.watch.server;

import com.wrist.watch.SimpeEcode.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SEncoder extends MessageToByteEncoder<Message> {


    @Autowired
    public Codeutil codeutil;

    @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, Message msg, ByteBuf out) throws Exception {

        out.writeByte(msg.getHead());
        out.writeByte(msg.getType());
        out.writeBytes(msg.getLength());
        out.writeBytes(msg.getData());
        out.writeBytes(msg.getCrc16());
        out.writeByte(msg.getTail());
        log.info(" Head：" + msg.getHead() + " Type：" + msg.getType() + " Length:" + msg.getLength() + " Data：" + msg.getData() + " Crc16:" + msg.getCrc16() + " Tail：" + msg.getTail());
        // out.writeBytes(new byte[]{'\r','\n'});


     /*   byte[] resmsg = (byte[]) msg;
        if(resmsg[0] != Const.req06){
            //包含crc校验的长度了吗
            int dtuMsgLength = resmsg.length + Const.sendMinMsgLength;
            //已建立链路限制
            Connection conn = channelHandlerContext.channel().attr(Const.connection).get();
            String identityId = conn.getIdentityId();

            byte[] crc16 = CRC16Util.crc16Bytes(resmsg);
            Message dtuMsg = new Message(Const.headDefault, Const.req05, dtuMsgLength, identityId, resmsg, crc16, Const.tailDefault);
            resmsg = buildMsg(dtuMsg);
        }else{
            resmsg =  ArrayUtils.remove(resmsg, 0);
        }
        out.writeBytes(resmsg);
    }

    private byte[] buildMsg(Message dtuMsg) {
        byte[] res = {};
        res = ArrayUtils.add(res, dtuMsg.getHead());
        res = ArrayUtils.add(res, dtuMsg.getType());
        //TODO length int to bytes
        //整个帧长度是否计算了
        res = ArrayUtils.addAll(res, ByteUtil.getBytes((short)dtuMsg.getLength(), true));
        res = ArrayUtils.addAll(res, dtuMsg.getIdentityId().getBytes());
        res = ArrayUtils.addAll(res, dtuMsg.getData());
        //crc16校验错误
        res = ArrayUtils.addAll(res, dtuMsg.getCrc16());
        res = ArrayUtils.add(res, dtuMsg.getTail());
        return res;
     }*/
    }
}
