package com.wrist.watch.server;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @ClassName :     //类名
 * @Description :  编码格式转换//描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
@Slf4j
public class Codeutil {


    public final static byte[] getBytes(short s, boolean asc) {
        byte[] buf = new byte[2];
        if (asc) {
            for (int i = buf.length - 1; i >= 0; i--) {
                buf[i] = (byte) (s & 0x00ff);
                s >>= 8;
            }
        }
        else {
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) (s & 0x00ff);
                s >>= 8;
            }
        }
        return buf;
    }

    /**
     * @param b 字节数组
     * @return 16进制字符串
     * @throws
     * @Title:bytes2HexString
     * @Description:字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        //log.info("长度："+b.length);
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase()); //update +“ ”
        }
        return result.toString();
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * @param strPart 字符串
     * @return 16进制字符串
     * @Title:string2HexString
     * @Description:字符串转16进制字符串
     */
    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexString2String
     * @Description:16进制字符串转字符串
     */
    public static String hexString2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            //System.out.println(Integer.valueOf(src.substring(i * 2, i * 2 + 2),16).byteValue());
            temp = temp + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return temp;
    }

    /**
     * @param src
     * @return
     * @throws
     * @Title:char2Byte
     * @Description:字符转成字节数据char-->integer-->byte
     */
    public static Byte char2Byte(Character src) {
        return Integer.valueOf((int) src).byteValue();
    }

    /**
     * @param a   转化数据
     * @param len 占用字节数
     * @return
     * @throws
     * @Title:intToHexString
     * @Description:10进制数字转成16进制
     */
    public static String intToHexString(int a, int len) {
        len <<= 1;
        String hexString = Integer.toHexString(a);
        int b = len - hexString.length();
        if (b > 0) {
            for (int i = 0; i < b; i++) {
                hexString = "0" + hexString;
            }
        }
        return hexString;
    }


    /**
     * 将16进制的2个字符串进行异或运算
     * http://blog.csdn.net/acrambler/article/details/45743157
     *
     * @param strHex_X
     * @param strHex_Y 注意：此方法是针对一个十六进制字符串一字节之间的异或运算，如对十五字节的十六进制字符串异或运算：1312f70f900168d900007df57b4884
     *                 先进行拆分：13 12 f7 0f 90 01 68 d9 00 00 7d f5 7b 48 84
     *                 13 xor 12-->1
     *                 1 xor f7-->f6
     *                 f6 xor 0f-->f9
     *                 ....
     *                 62 xor 84-->e6
     *                 即，得到的一字节校验码为：e6
     * @return
     */
    public static String xor(String strHex_X, String strHex_Y) {
        //将x、y转成二进制形式
        log.info("strHex_X :" + Integer.valueOf(strHex_X, 16));
        String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));
        String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));
        String result = "";
        //判断是否为8位二进制，否则左补零
        if (anotherBinary.length() != 8) {
            for (int i = anotherBinary.length(); i < 8; i++) {
                anotherBinary = "0" + anotherBinary;
            }
        }
        if (thisBinary.length() != 8) {
            for (int i = thisBinary.length(); i < 8; i++) {
                thisBinary = "0" + thisBinary;
            }
        }
        //异或运算
        for (int i = 0; i < anotherBinary.length(); i++) {
            //如果相同位置数相同，则补0，否则补1
            if (thisBinary.charAt(i) == anotherBinary.charAt(i)) {
                result += "0";
            }else {
                result += "1";
            }
        }
        return Integer.toHexString(Integer.parseInt(result, 2));
    }


    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytes2Str(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

   /* public static byte[] bytes2byte(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return hexString2Bytes(stringBuilder.toString());
    }*/

    /**
     * @return 接收字节数据并转为16进制字符串
     * @param_msg
     */
    public static String receiveHexToString(byte[] by) {
        try {
 			/*io.netty.buffer.WrappedByteBuf buf = (WrappedByteBuf)msg;
 			ByteBufInputStream is = new ByteBufInputStream(buf);
 			byte[] by = input2byte(is);*/
            String str = bytes2Str(by);
            //log.info("长度:"+by.length);
            str = str.toLowerCase();
            return str;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("接收字节数据并转为16进制字符串异常");
        }
        return null;
    }

    /**
     * "7dd",4,'0'==>"07dd"
     *
     * @param input  需要补位的字符串
     * @param size   补位后的最终长度
     * @param symbol 按symol补充 如'0'
     * @return N_TimeCheck中用到了
     */
    public static String fill(String input, int size, char symbol) {
        while (input.length() < size) {
            input = symbol + input;
        }
        return input;
    }

    /**
     * 方式二
     *
     * @param bArray
     * @return
     */
    public String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 方式二
     *
     * @param b
     * @return
     */
    public static String toHexString1(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(toHexString1(b[i]));

        }
        return buffer.toString();
    }

    /**
     * 方式二
     *
     * @param b
     * @return
     */
    public static String toHexString1(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }


    public static String demoChangeStringToHex(final String inputString) {
        int changeLine = 1;
        String s = "Convert a string to HEX";
        if (inputString != null) {
            s = inputString;
        }

        for (int i = 0; i < s.length(); i++) {
            byte[] ba = s.substring(i, i + 1).getBytes();
            // & 0xFF for preventing minus
            String tmpHex = Integer.toHexString(ba[0] & 0xFF);
            System.out.print("0x" + tmpHex.toUpperCase());
            System.out.print(" ");
           /* if (changeLine++ % 8 == 0) {
                System.out.println("");
            }*/
            // Multiply byte according
            if (ba.length == 2) {
                tmpHex = Integer.toHexString(ba[1] & 0xff);
                System.out.print("0x" + tmpHex.toUpperCase());
                System.out.print(" ");
               /* if (changeLine++ % 8 == 0) {
                    System.out.println("");
                }*/
            }
        }
/*
        System.out.println(""); // change line
        System.out.println(""); // change line*/
        return s;
    }


    public static byte uniteBytes(byte src0, byte src1) {
        byte B_b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        B_b0 = (byte) (B_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (B_b0 ^ _b1);
        return ret;
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
     *
     * @param src String
     * @return byte[]
     */
    public static byte[] HexStringtOBytes(String src) {
        byte[] ret = new byte[8];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < 8; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    //
   /* uint32_t a = 0xAABBCCDD;
    a = ((a & 0x000000ff) << 24 ) | ((a & 0x0000ff00) << 8) | ((a & 0x00ff0000) >> 8) | ((a & 0xff000000 ) >> 24);*/

    /**
     * 转换byte数组为int（小端）
     *
     * @return
     * @note 数组长度至少为4，按小端方式转换,即传入的bytes是小端的，按这个规律组织成int
     */
    public static int BytestoInt(byte[] bytes) {
        if (bytes.length == 0) {
            return -1;
        }
        int iRst = (bytes[0] & 0xFF);
        if (bytes.length > 1)
        {
            iRst |= (bytes[1] & 0xFF) << 8;
        }
        if (bytes.length > 2)
        {
            iRst |= (bytes[2] & 0xFF) << 16;
        }
        if (bytes.length > 3)
        {
            iRst |= (bytes[3] & 0xFF) << 24;
        }
        if (bytes.length > 4)
        {
            iRst |= (bytes[4] & 0xFF) << 32;
        }
        return iRst;
    }

    public static int BytestotoInt(byte[] bytes) {
        if (bytes.length == 0)
        {
            return -1;
        }
        int iRst = (bytes[0] & 0xFF)<< 8 ;
        if (bytes.length > 1)
        {
            iRst |= (bytes[1] & 0xFF);
        }
        return iRst;
    }

    public static byte[] inttoBytes(int value) {
        byte[] src = new byte[2];
        src[0] = (byte) ((value >> 8) & 0xFF);
        src[1] = (byte) (value & 0xFF);
        return src;
    }



    public static byte[] inttotoBytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 32) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }
    /**
     * 转换byte数组为Char（小端）
     *
     * @return
     * @note 数组长度至少为2，按小端方式转换
     */
    public static String Bytes2String(byte[] bytes) {
        byte[] bbuf={};
        if (bytes.length <= 1)
        {
            return null;
        }
           bbuf = inttoBytes(BytestoInt(bytes));
          //log.info("1111："+byte2Hex(bbuf));
        /*byte数组转十六进制*/
        StringBuilder buf = new StringBuilder(bbuf.length * 2);
        for (byte b : bbuf) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf.toString().toUpperCase();
    }
  public  static boolean datecheck (String str) {
      //括号内为日期格式，y代表年份，M代表年份中的月份（为避免与小时中的分钟数m冲突，此处用M），d代表月份中的天数
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sd.setLenient(false);//此处指定日期/时间解析是否不严格，在true是不严格，false时为严格
            sd.parse(str);//从给定字符串的开始解析文本，以生成一个日期
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public  static boolean datemonthcheck (String str) {
        //括号内为日期格式，y代表年份，M代表年份中的月份（为避免与小时中的分钟数m冲突，此处用M），d代表月份中的天数
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
        try {
            sd.setLenient(false);//此处指定日期/时间解析是否不严格，在true是不严格，false时为严格
            sd.parse(str);//从给定字符串的开始解析文本，以生成一个日期
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String tostrto(byte[] btr,int sint)
    {
        if (btr.length == 0)
        {
            return null;
        }
        StringBuffer str=new StringBuffer();
        byte[] bproductcrc={(byte)0x00};
        for(int i=0;i< btr.length;i=i+2)  //i=i+2
        {
            bproductcrc = Arrays.copyOfRange(btr, i, i + 2);
            long ilong=Integer.parseInt(byte2Hex(bproductcrc),16);
            if( sint == 1)
            {
                DecimalFormat format = new DecimalFormat("#.0");
                str.append(format.format(ilong*0.1)).append(",");
                sint=1;
            }
            if( sint == 2)
            {

                str.append(ilong-500).append(",");
                sint=2;
            }
            if( sint == 0)
            {
                str.append(ilong).append(",");
                sint=0;
            }
        }
        return str.toString();
    }

    public static String Stryymmdd(byte[] bytes)
    {
        if (bytes.length == 0)
        {
            return null;
        }
        StringBuffer strbuf=new StringBuffer();
        int isum=0;
        for(int i=0;i<bytes.length;i++)
        {
            byte[] bbyte = Arrays.copyOfRange(bytes, i, i + 1);
            int ib= BytestoInt(bbyte);
            isum++;
            if(isum <= 3)
            {
                strbuf.append(ib+"-");
                if(isum == 3)
                {
                    strbuf.deleteCharAt(strbuf.length() - 1).append(" ");
                }
            }
            if(isum >= 4)
            {
                strbuf.append(ib+":");
            }
        }
        return Timeandtime(String.valueOf(strbuf.deleteCharAt(strbuf.length() - 1)));
    }

    public static String Timeandtime(String timeStr) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yy-M-d H:m:s");
        LocalDateTime date = LocalDateTime.parse(timeStr, df);
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        String sTime = f2.format(date);
        return sTime;
    }

    public static void main(String args[]) throws UnsupportedEncodingException {


        //log.info("sss:"+datecheck("5328-20-7 0:255:255"));// 2020-12-25 21:36:12
       /* Codeutil demoChange = new Codeutil();
        log.info(Codeutil.demoChangeStringToHex("50 05 06 00 A5 31 00 02 00 B7 BB 48 16"));

       // log.info(Codeutil.HexString2Bytes("50 05 06 00 A5 31 00 02 00 B7 BB 48 16") );
        String productNo = "3030303032383838";
        System.out.println(hexString2String(productNo));

        productNo = "04050103000001070302050304";
        System.out.println(hexString2String(productNo));*/

        // log.info(" lowtostringhigh:"+ String.valueOf (Codeutil.byte2Hex(Codeutil.intToByteArray(16))));
        /*byte[] bytes4 = {(byte) 0xFF, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] bytes2 = {(byte) 0x01, (byte) 0x02};
        byte[] bytes2s = {(byte) 0x80, (byte) 0x28};
        String str = null;
        log.info(" 1111大小端转换:" + BytestoInt(bytes2s)); //BytestotoInt*/

       // log.info(" 大小端转换:" + Bytes2String(bytes4));

        // byte[] byyyyy={(byte)0x07,(byte)0xE4};
        //String syyyyy= Codeutil.BytestoInt(byyyyy);
        // log.info(" syyyyy:"+Codeutil.Bytes2String(byyyyy));
       /* byte[] xhap = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D, (byte) 0x00,
                (byte) 0x0F, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x15, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x17,
                (byte) 0x00, (byte) 0x19, (byte) 0x00, (byte) 0x19, (byte) 0x00, (byte) 0x1B, (byte) 0x00, (byte) 0x1D, (byte) 0x00, (byte) 0x00, (byte) 0xF1,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        byte[] xxhap = {(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06};


        log.info("Xhap时的漏气量:" + xxhap.length / 2);
        int iRst = 0;
        for (int i = 0; i < xhap.length; i++) {
            iRst = (xhap[i] & 0xFF);
            iRst |= (xhap[i + 1] & 0xFF) << 8;
            i++;
            log.info("转换:" + byte2Hex(inttoBytes(iRst)));
        }
        log.info("Xhap时的漏气量:" + String.valueOf(iRst));

         byte[] strcom={(byte)0x04};
        log.info("ji："+String.format("%4d", strcom.length+2).replace(" ", "0"));*/
       // log.info("日期:"+Dateanddate());
 /*       byte[] strcom = {(byte) 0x31};
      String  str = "A5" + Codeutil.byte2Hex(strcom) + String.format("%4d", strcom.length+1).replace(" ", "0") + "FF";
        log.info("str"+str);

        byte[] bmonth = {(byte) 0x07,(byte) 0xE4};
        String smonth= String.valueOf(Codeutil.h2d(Codeutil.byte2Hex(bmonth)));

        log.info("smonth"+smonth+"111："+String.valueOf(Codeutil.BytestotoInt(bmonth)));
        byte[] binitialPressure={(byte)0x01,(byte)0x32};
        log.info("binitialPressure："+String.valueOf(BytestotoInt(binitialPressure)*0.1));

        log.info("日期"+Codeutil.dateTostring("2020-12-31 10:26:27"));
        byte[] mke={
                (byte)0x41,(byte)0x53,(byte)0x31,(byte)0x30,(byte)0x30,(byte)0x41,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x41,(byte)0x53,(byte)0x32,(byte)0x31,(byte)0x39,(byte)0x30,(byte)0x37,(byte)0x33,(byte)0x31,(byte)0x31,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x31,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x1D,(byte)0x01,(byte)0x01,(byte)0x00,(byte)0xE4,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0xE3,(byte)0x07,(byte)0x0B,(byte)0x1A,(byte)0x24,(byte)0x06,(byte)0xF2,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0xF2,(byte)0x08,(byte)0x24,(byte)0x06,(byte)0xF2,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0x8B,(byte)0x08,(byte)0xE9,(byte)0x05,(byte)0x1C,(byte)0x01,(byte)0x18,(byte)0x00,(byte)0x30,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x18,(byte)0x00,(byte)0x1B,(byte)0x00,(byte)0x1D,(byte)0x00,(byte)0x1E,(byte)0x00,(byte)0x21,(byte)0x00,(byte)0x23,(byte)0x00,(byte)0x26,(byte)0x00,(byte)0x29,(byte)0x00,(byte)0x2B,(byte)0x00,(byte)0x2D,(byte)0x00,(byte)0x30,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x7E,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x6D
        };
        byte[] bcrcMark = Arrays.copyOfRange(mke, mke.length-2, mke.length);
        log.info("bcrcMark:"+Codeutil.byte2Hex(bcrcMark));*/
       /* byte[] ss={(byte)0x1D,(byte)0x01,(byte)0x01,(byte)0x00};
        String iss= String.valueOf(ss[3] & 0xFF)+"."+String.valueOf(ss[2] & 0xFF)+"."+String.valueOf(ss[1] & 0xFF)+"."+String.valueOf(ss[0] & 0xFF);

        log.info("ssss1:"+iss);
        log.info("ssss2:"+Bytes2String(ss)); //0001 1101 0000 00001 0000 0001 0000 0000

        String    str1="AS100A AS219073110001";
        log.info("ssss1:"+str1.substring(str1.indexOf(" ")));*/

/*       byte[] byte52={
                (byte)0x50,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,
                (byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,
                (byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,
                (byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,
                (byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,
                (byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,
                (byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,
                (byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,
                (byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,
                (byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42,(byte)0x00,(byte)0x42
        };
        StringBuffer str=new StringBuffer();
        for(int i=0;i< byte52.length;i=i+2)  //i=i+2
        {
            byte[] bproductcrc = Arrays.copyOfRange(byte52, i, i + 2);
            DecimalFormat format = new DecimalFormat("#.0");
            str.append(format.format(Integer.parseInt(byte2Hex(bproductcrc),16)*0.1)).append(",");
        }
         log.info("__int2:"+str);*/

        byte[] bytewave={(byte)0x15,(byte)0x01,(byte)0x1e,(byte)0x02,(byte)0x08,(byte)0x09};
        StringBuffer strbuf=new StringBuffer();
        int isum=0;
        for(int i=0;i<bytewave.length;i++)
        {
            byte[] bbyte = Arrays.copyOfRange(bytewave, i, i + 1);
            int ib= BytestoInt(bbyte);
            isum++;
            if(isum <= 3)
            {
                strbuf.append(ib+"-");
                if(isum == 3)
                {
                    strbuf.deleteCharAt(strbuf.length()-1).append(" ");
                }
            }
             if(isum >= 4)
             {
                 strbuf.append(ib+":");
             }
        }
         String str=String.valueOf(strbuf.deleteCharAt(strbuf.length()-1));
        String transTime = Timeandtime(str);
        log.info("transTime:"+transTime);

       /*  String strjkj=str.substring(strtrbuf.length());
         log.info("str:"+str+"strtrbuf:"+strtrbuf);*/

       /*byte[] bytet={(byte)0x00,(byte)0x07};
        log.info("dddd:"+byte2Hex(bytet));
        if(byte2Hex(bytet).equals("0007"))
        {
            log.info("待机状态");
        }else
        {
            log.info("工作状态-10秒波形数据");
        }*/

       // Codeutil.BytestoInt();
    }

/*
    public static  String convertStringToHex(String str){
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }
        return hex.toString();
    }

    public static String convertHexToString(String hex){
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){
            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }*/


    /*用Java语言实现对十六进制字符串异或运算 http://blog.csdn.net/acrambler/article/details/45743157*/

    /**
     * 对象转字节数组
     */
    public static byte[] objectToBytes(Object obj) throws IOException {

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        ObjectOutputStream objout = new ObjectOutputStream(baout);
        objout.writeObject(obj);
        objout.flush();
        byte[] bytes = baout.toByteArray();
        return bytes;
    }

    /**
     * 字节数组转对象
     */
    public static Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn = new ObjectInputStream(in);
        return sIn.readObject();
    }


    private static int parse(char c) {
        if (c >= 'a')
        {
            return (c - 'a' + 10) & 0x0f;
        }
        if (c >= 'A')
        {
            return (c - 'A' + 10) & 0x0f;
        }
        return (c - '0') & 0x0f;
    }

    private final static byte[] Hex = "0123456789ABCDEF".getBytes();

    // 从字节数组到十六进制字符串转换
    public static String Bytes2HexString(byte[] b) {
        byte[] buff = new byte[2 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[2 * i] = Hex[(b[i] >> 4) & 0x0f];
            buff[2 * i + 1] = Hex[b[i] & 0x0f];
        }
        return new String(buff);
    }

    // 从十六进制字符串到字节数组转换
    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    /**
     * 高低转换
     *
     * @param temp
     * @return
     */
    public static String hightolow(String temp) {
        List<String> list = Arrays.asList(temp.split(" "));
        Collections.reverse(list);
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : list) {
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }


    public static String lowtohigh(String temp) {
        List<String> list = Arrays.asList(temp.split(""));
        Collections.reverse(list);
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : list) {
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }

    public static String lowtostringhigh(String temp) {
        if (temp == null) {
            return null;
        }
        String str1 = temp.substring(0, 2);  //str2从合并的字符串中截取str1
        String str2 = temp.substring(2, 4);    //str1从合并的字符串中截取从str1长度起后面的字符串，即为str2
        return str2 + str1;
    }

    /**
     * 十进制数据转换为十六进制字符串数
     *
     * @param dec
     * @return
     */
    public static String decToHex(String dec) {
        BigInteger data = new BigInteger(dec, 10);
        return data.toString(16);
    }

    /**
     * 十六进制数据转换为十进制字符串数
     *
     * @param hex
     * @return
     */
    public static String hexToDec(String hex) {
        BigInteger data = new BigInteger(hex, 16);
        return data.toString(10);
    }


    /**
     * object转换成map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public Map<String, Object> Obj2Map(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    /**
     * map转换为object
     *
     * @param map
     * @param clz
     * @return
     * @throws Exception
     */
    public Object map2Obj(Map<String, Object> map, Class<?> clz) throws Exception {
        Object obj = clz.newInstance();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }

    /**
     * Object转换为List类型
     *
     * @param obj
     * @param cla
     * @param <T>
     * @return
     */

    public static <T> List<T> objToList(Object obj, Class<T> cla) {
        List<T> list = new ArrayList<T>();
        if (obj instanceof ArrayList<?>) {
            for (Object o : (List<?>) obj) {
                list.add(cla.cast(o));
            }
            return list;
        }
        return null;
    }

    /**
     * //获取高四位
     *
     * @param data
     * @return
     */
    public static int getHeight4(byte data) {
        int height;
        height = ((data & 0xf0) >> 4);
        return height;
    }

    /**
     * //获取低四位
     *
     * @param data
     * @return
     */
    public static int getLow4(byte data) {
        int low;
        low = (data & 0x0f);
        return low;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        /*byte[] src = new byte[4];
        src[3] =  (byte) ((value>>24) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[0] =  (byte) (value & 0xFF);*/
        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8));
        //| ((src[offset+2] & 0xFF)<<16)
        //| ((src[offset+3] & 0xFF)<<24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
      /*  value = (int) ( ((src[offset] & 0xFF)<<24)
                |((src[offset+1] & 0xFF)<<16)
                |((src[offset+2] & 0xFF)<<8)
                |(src[offset+3] & 0xFF));*/

        value = (int) ((src[offset] & 0xFF) | (src[offset + 1] & 0xFF) << 8);

        return value;
    }


    /**
     * int到byte[] 由高位到低位
     *
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        /* byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);*/

        byte[] result = new byte[2];
        result[0] = (byte) ((i >> 8) & 0xFF);
        result[1] = (byte) (i & 0xFF);
        return result;
    }


    /**
     * int到byte[] 由高位到低位
     *
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intcrc8ToByteArray(int i) {

        byte[] result = new byte[1];
        result[0] = (byte) ((i) & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     *
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * 16进制字符串转字符或数字。
     *
     * @author 老紫竹 JAVA世纪网(java2000.net)
     */
    public class Test {

        String s = "0x40";
        int b = Integer.parseInt(s.replaceAll("^0[x|X]", ""), 16);
        /* log. (char)b;*/

    }


    /**
     * 将整数转换为byte数组并指定长度
     */
    private static byte[] intToBytes(int a, int length) {
        byte[] bs = new byte[length];
        for (int i = bs.length - 1; i >= 0; i--) {
            bs[i] = (byte) (a % 0xFF);
            a = a / 0xFF;
        }
        return bs;
    }

    /**
     * 将byte数组转换为整数
     */
    public static int bytesToInt(byte[] bs) {
        int a = 0;
        for (int i = bs.length - 1; i >= 0; i--) {
            a += bs[i] * Math.pow(0xFF, bs.length - i - 1);
        }
        return a;
    }

    /**
     * 字节数组转16进制编码字符串
     * <p>Title: toHexString</p>
     * <p>Description: </p>
     *
     * @param byteArray
     * @return
     */
    public static String toHexString(byte[] byteArray) {
        String str = null;
        if (byteArray != null && byteArray.length > 0) {
            StringBuffer stringBuffer = new StringBuffer(byteArray.length);
            for (byte byteChar : byteArray) {
                stringBuffer.append(String.format("%02X", byteChar));
            }
            str = stringBuffer.toString();
        }
        return str;
    }

    /**
     * @param bytes，输入byte[]数组
     * @return 16进制字符
     * @Description: 将byte[]数组转换成16进制字符。一个byte生成两个字符，长度对应1:2
     * @Author: mastermind
     * @Date: 2020-04-06 12:16
     */
    public static String byte2Hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        // 遍历byte[]数组，将每个byte数字转换成16进制字符，再拼接起来成字符串
        for (int i = 0; i < bytes.length; i++) {
            // 每个byte转换成16进制字符时，bytes[i] & 0xff如果高位是0，输出将会去掉，所以+0x100(在更高位加1)，再截取后两位字符
            builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1).toUpperCase());
            //builder.append(" ");
        }
        return builder.toString();
    }

    public static String bytetoHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        // 遍历byte[]数组，将每个byte数字转换成16进制字符，再拼接起来成字符串
        for (int i = 0; i < bytes.length; i++) {
            // 每个byte转换成16进制字符时，bytes[i] & 0xff如果高位是0，输出将会去掉，所以+0x100(在更高位加1)，再截取后两位字符
            builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1).toUpperCase());
            builder.append(" ");
        }
        return builder.toString();
    }

    /**
     * hexString2Bytes
     * byte2Hex
     *
     * @param bytes
     * @return
     */
    public static String hexString2BytesANDbyte2Hex(String bytes) {
        if (bytes == null) {
            return null;
        }
        byte[] byd = hexString2Bytes(bytes);
        log.info(" 长度;" + byd.length);
        return byte2Hex(byd);
    }

    /**
     * @param bytes，输入byte[]数组
     * @return 16进制字符
     * @Description: 将byte[]数组转换成16进制字符。一个byte生成两个字符，长度对应1:2
     * @Author: mastermind
     * @Date: 2020-04-06 12:16
     */
    public static String byte26Hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        // 遍历byte[]数组，将每个byte数字转换成16进制字符，再拼接起来成字符串
        for (int i = 0; i < bytes.length; i++) {
            // 每个byte转换成16进制字符时，bytes[i] & 0xff如果高位是0，输出将会去掉，所以+0x100(在更高位加1)，再截取后两位字符
            builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            builder.append(" ");
        }
        return builder.toString();
    }

    /**
     * 字节数据转十六进制字符串
     *
     * @param data 输入数据
     * @return 十六进制内容
     */
    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 将16进制字符串转为转换成字符串
     */
    public static byte[] hex2Bytes(String source) {
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        return sourceBytes;
    }


    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 16进制字符串转为16进制
     *
     * @param hex 16进制的字符串
     * @return
     */
    public static byte[] hexString_2Bytes(String hex) {
        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }
    }

    /**
     * YYYYMMDDHHMM
     *
     * @param stryyy
     * @return
     */
    public static Date dateTostring(String stryyy) {
        //String time = "2020-12-5";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime =null;
        try {
            dateTime = simpleDateFormat.parse(stryyy);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info("dateTime:"+dateTime);
        return dateTime;
    }

    /**
     * YYYYMMDDHHMM
     *
     * @param stryyy
     * @return
     */
    public static Date dateToYYYYMMDDstring(String stryyy) {
        //String time = "2020-12-5";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime = null;
        try {
            dateTime = simpleDateFormat.parse(stryyy);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static int h2d(String s) {
        int i = 0, l = s.length(), n = 0;
        while (i < l) {
            int x = s.codePointAt(i);
            n = n << 4 | (x > '9' ? x - ('A' - 10) : x - '0');
            i += 1;
        }
        return n;
    }

    public static String DateandString()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String Dateanddate()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}