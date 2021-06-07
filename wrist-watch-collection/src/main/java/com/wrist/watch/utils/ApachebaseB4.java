package com.wrist.watch.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -Earl
 * @Date 2020/10/22 10:43
 * @Version 1.0
 */
@Slf4j
public class ApachebaseB4 {

    /**
     * 加密
     * @param srccommons
     * @return
     */
    public static byte[] Commonsencodebase64(String srccommons)
    {
        byte[] encodeBytes = Base64.encodeBase64(srccommons.getBytes());
        log.info("encodeBytes:"+new String(encodeBytes));
        byte[] decodeBytes = Base64.decodeBase64(encodeBytes);
        log.info("decodeBytes："+new String(decodeBytes));
        return decodeBytes;
    }
    /**
     * bouncycastle
     * @param srcbouncy
     * @return
     */
    public static byte[] Bouncybase64(String srcbouncy)
    {
        byte[] encodeBytes = org.bouncycastle.util.encoders.Base64.encode(srcbouncy.getBytes());
        log.info("Bouncybase64:"+new String(encodeBytes));
        byte[] decodeBytes = org.bouncycastle.util.encoders.Base64.decode(encodeBytes);
        log.info("Bouncybase64:"+new String(decodeBytes));
        return decodeBytes;
    }

    public static void main(String[] args){
        String str=" RSAEncryptTest! ";
        log.info("encode_RSAEncryptTest:"+Commonsencodebase64(str));
        log.info("one_RSAEncryptTest:"+Bouncybase64(str));
         /*
        CommonsBase64();
        BouncyBase64();*/
    }
}
