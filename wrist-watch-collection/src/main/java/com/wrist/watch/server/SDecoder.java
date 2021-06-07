package com.wrist.watch.server;

import com.wrist.watch.SimpeEcode.Message;
import com.wrist.watch.constant.Const;
import com.wrist.watch.utils.CRC16Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
@Slf4j
public class SDecoder extends ByteToMessageDecoder {


    @Autowired
    private static int fint=0;
    /**
     * 剩余长度
     */
    private int remainLength = 0;
    /**
     * 已读长度
     */
    private int hasReadLength = 0;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //in.capacity(); //内存
        log.info("-----------------------------"+Codeutil.DateandString()+"--------------------------------呼吸接收协议收到数据----start----------------------------");

        // 显示十六进制的接收码
        log.info("\n 呼吸接收协议收到数据为:{}",ByteBufUtil.hexDump(in).toUpperCase());
           byte[] bytes=Codeutil.hex2Bytes(ByteBufUtil.hexDump(in).toUpperCase());
       // byte[] bbydata = Arrays.copyOfRange(bytes, 0,bytes.length-3);//数据段数据内容

        int readableLength = in.readableBytes();
        if (readableLength < Const.AcceptMinMsgLength) {
            return;
        }
        remainLength = readableLength;
       readableLength = in.readableBytes();
      remainLength = readableLength;
      int markLength =in.readerIndex();
      readableLength = readableLength+markLength;
      hasReadLength=markLength;
      while (checkByteBuf(in, readableLength)) {
          byte head = getHeadField(in);
          byte type = getTypeField(in);
          int msgLength = getMsgLengthField(in);
          byte[] data ={};
          byte[] crc16={};
          if(msgLength != Const.AcceptMinMsgLength)
          {
              /*byte[] crc8={};
              data = getDataFile(in, msgLength);*/

              byte[] bbata=new byte[msgLength];
              in.readBytes(bbata);
              data = Arrays.copyOf(bbata, msgLength);
              byte[] lcrc=new byte[2];
              in.readBytes(lcrc);
              crc16 = Arrays.copyOf(lcrc, 2);
              /*log.info("~~~~~~~~~~~~~~~~crc16:"+crc16);*/
              if(getCrc16(crc16,Arrays.copyOfRange(bytes, 0,bytes.length-3)) != true)
              {
                  //byte end=getTailField(in);
                  log.info("~~~ 呼吸机与本地校验CRC错误! ~~~");
                  continue;
               }
          }
         /*byte[] lcrc=new byte[msgLength - data.length];
          in.readBytes(lcrc);
          byte[] crc16= crc16 = Arrays.copyOf(lcrc, 2);//getCrc16(in);
          log.info("@@@@@@@@@@crc16@@@@@@@@@@@@:"+Codeutil.byte2Hex(crc16));
          */
          byte end=getTailField(in);
          /*byte end=in.getByte(readableLength-1);*/
          Message mssg = new Message(head,type,Codeutil.intToByteArray(msgLength),data,crc16,end);
          /*Message message = new Message(new byte[]{(byte) 0xFF});*/
          out.add(mssg);
        }
        in.clear();
        out.remove(in);
      log.info("\n 呼吸接收协议收到数据-呼吸接收协议收到数据----end:{}",ByteBufUtil.hexDump(in).toUpperCase());
      log.info("--------------------------------"+Codeutil.DateandString()+"--------------------------------呼吸接收协议收到数据----end----------------------------");

    }

    /**
     *
     * @Title. checkByteBuf
     * @Description. 处理半包和粘包
     * @param in
     * @param readableLength
     * @return boolean
     * @exception.
     */
    private  boolean checkByteBuf(ByteBuf in,int readableLength){

       /* if(remainLength < Const.acceptMinMsgLength){
            return false;
        }*/
        int headIndex = in.indexOf(hasReadLength, readableLength, Const.HeadDefault);
        if(headIndex < 0){
            return false;
        }
        in.readerIndex(headIndex);
        byte head = getHeadField(in);
        byte type =getTypeField(in);

      if (!ArrayUtils.contains(Const.Reqs, type)){
            log.error("类型不正确");
            hasReadLength =headIndex+1;
          return checkByteBuf(in,readableLength);
      }
      /** 数据段内容长度 **/
        int imsgLength = getMsgLengthField(in);
        log.info("~~~~~~~~数据段内容长度imsgLength:"+imsgLength);
        int needLength = imsgLength+headIndex;
        if(needLength > readableLength){
            return false;
        }
        /*
        byte bcrc6=in.getByte(imsgLength+1);
        int bcrc16=in.getByte(readableLength-3);
        byte[] bcrc16=getCrc16Legth(in,headIndex+1);
        log.info("~~~~~bcrc16：bcrc16"+bcrc16);
        */
        byte end=in.getByte(readableLength-1);
        if(end != Const.TailDefault){
            log.error("~~~~~~~~~~~~~~呼吸请求帧尾不正确!");
            hasReadLength =headIndex+1;
            return checkByteBuf(in,readableLength);
        }
        /* 总长度-数据端长度 */
        in.readerIndex(headIndex);
        remainLength = readableLength - needLength;
        hasReadLength =needLength ; /*数据段内容*/
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
        byte[] req=new byte[buf.readableBytes()];
        buf.readBytes(req);
       return Codeutil.bytesToInt(req,0);
    }

    private byte getcrc16l(ByteBuf in) {
        ByteBuf buf = in.readBytes(Const.MsgFieldLength);
        return buf.getByte(1);
    }

    /**
     * 需优化
     * @param in
     * @param msgLengthTotal
     * @return
     */
    private byte[] getDataFile(ByteBuf in, int msgLengthTotal) {
        //消息实际长度
        /*int dataLength = msgLengthTotal - Const.headFieldLength - Const.typeFieldLength - Const.msgFieldLength -Const.crcFieldLength - Const.tailFieldLength;
        ByteBuf buf = in.readBytes(msgLengthTotal);
        byte[] res=new byte[buf.readableBytes()];
        log.info("消息实际长度: "+Codeutil.byte2Hex(res));
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~end~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return res;*/
        ByteBuf buf=in.duplicate();
        byte[] dst=new byte[msgLengthTotal];/*buf.readableBytes()*/
        buf.readBytes(dst);
        log.info("~~~~~~~~~~~~~~~~~~数据段实际内容:"+Codeutil.byte2Hex(dst));
        return dst;
    }

      private boolean getCrc8(ByteBuf in,byte[] data) {
          boolean bo=false;
          ByteBuf buf = in.duplicate();
          byte[] lcrc=new byte[Const.CrcFieldLength];
          log.info("~~~~~~~~~~~lcrc:"+Codeutil.byte2Hex(lcrc) +" \n data:"+Codeutil.byte2Hex(data) );
          buf.readBytes(lcrc);
          byte[] crc16in = Arrays.copyOf(lcrc, 2);
          log.info("~~~~~~~~~~~~~~~~~~`crc校验:"+Codeutil.byte2Hex(crc16in));
          log.info("_呼吸机获取:"+Codeutil.byte2Hex(crc16in) +"_呼吸机本地计算:"+Codeutil.byte2Hex(Codeutil.intToByteArray( CRC16Util.CRC16(data))));
          if(data.length !=0 )
          {
              if(Arrays.equals(crc16in, Codeutil.intToByteArray(CRC16Util.CRC16(data))))
              {
                  bo=true;
              }
          }

        return bo;
    }

    /* private byte[] getCrc16(ByteBuf in) {
         ByteBuf buf = in.readBytes(in.readableBytes());
         byte[] lcrc16=new byte[Const.crcFieldLength];
         buf.readBytes(lcrc16);
         byte[] crc16in = Arrays.copyOf(lcrc16, 2);
         log.info("~~~~~lcrc16~~~~:"+Codeutil.byte2Hex(crc16in));
        //byte[] bbydata = Arrays.copyOfRange(datacrc16, 0,datacrc16.length-3);

        return null;
    }*/
     private boolean getCrc16(byte[] crc16in,byte[] datacrc16) {
        boolean bo=false;
         /*byte[] bbydata = Arrays.copyOfRange(datacrc16, 0,datacrc16.length-3);*/
        log.info("_呼吸机获取:"+Codeutil.byte2Hex(crc16in) +"_呼吸机本地计算:"+Codeutil.byte2Hex(Codeutil.intToByteArray( CRC16Util.CRC16(datacrc16))));
        if(crc16in.length !=0 )
        {
            if(Arrays.equals(crc16in, Codeutil.intToByteArray(CRC16Util.CRC16(datacrc16))))
            {
                bo=true;
            }
        }
        return bo;
    }
  private byte[] getCrc16Legth(ByteBuf in,int ll) {
      /* in.readBytes(Const.crcFieldLength); */
      ByteBuf buf =in.slice(in.readableBytes(),ll);
      byte[] req=new byte[2];
      log.info("req###################："+Codeutil.byte2Hex(req));
      buf.readBytes(req);
      return new byte[]{buf.getByte(1)};
  }

    private byte getTailField(ByteBuf in) {
        ByteBuf buf = in.readBytes(Const.TailFieldLength);
        return buf.getByte(0);
    }

}