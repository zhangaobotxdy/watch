package com.wrist.watch.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -Earl
 * @Date 2020/10/22 10:43
 * @Version 1.0 https://my.oschina.net/u/4356872/blog/3467464
 */
@Slf4j
public class SX509 {

    /**
     * @param bytes，输入byte[]数组
     * @return 16进制字符
     * @Description: 将byte[]数组转换成16进制字符。一个byte生成两个字符，长度对应1:2
     * @Author: mastermind
     * @Date: 2020-04-06 12:16
     */
    public static String byte22Hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        // 遍历byte[]数组，将每个byte数字转换成16进制字符，再拼接起来成字符串
        for (int i = 22; i < bytes.length; i++) {
            // 每个byte转换成16进制字符时，bytes[i] & 0xff如果高位是0，输出将会去掉，所以+0x100(在更高位加1)，再截取后两位字符
            builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1).toUpperCase());
            builder.append(" ");
        }
        return builder.toString();
    }

    public static String byte2Hex(byte[] bytes) {
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
     * 将16进制字符转换成byte[]数组。与byte2Hex功能相反。
     *
     * @param string 16进制字符串
     * @return byte[]数组
     */
    public static byte[] hex2Byte(String string) {
        if (string == null || string.length() < 1) {
            return null;
        }
        // 因为一个byte生成两个字符，长度对应1:2，所以byte[]数组长度是字符串长度一半
        byte[] bytes = new byte[string.length() / 2];
        // 遍历byte[]数组，遍历次数是字符串长度一半
        for (int i = 0; i < string.length() / 2; i++) {
            // 截取没两个字符的前一个，将其转为int数值
            int high = Integer.parseInt(string.substring(i * 2, i * 2 + 1), 16);
            // 截取没两个字符的后一个，将其转为int数值
            int low = Integer.parseInt(string.substring(i * 2 + 1, i * 2 + 2), 16);
            // 高位字符对应的int值*16+低位的int值，强转成byte数值即可
            // 如dd，高位13*16+低位13=221(强转成byte二进制11011101，对应十进制-35)
            bytes[i] = (byte) (high * 16 + low);
        }
        return bytes;
    }

    public static void main(String[] args) throws Exception {
        byte[] msg = "test!中文".getBytes("UTF8"); // 待加解密的消息
      // 用证书的公钥加密
        CertificateFactory cff = CertificateFactory.getInstance("X.509");
        FileInputStream fis1 = new FileInputStream("D:\\vent_lic.cer"); // 证书文件
        Certificate cf = cff.generateCertificate(fis1);
        PublicKey pk1 = cf.getPublicKey(); // 得到证书文件携带的公钥
        log.info("~~~ 公秘钥加密: "+byte22Hex(pk1.getEncoded()));

        Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // 定义算法：RSA
        c1.init(Cipher.ENCRYPT_MODE, pk1);
        byte[] msg1 = c1.doFinal(msg); // 加密后的数据
        log.info("~~~ 公秘钥加密: "+byte2Hex(msg1));

//        // 用证书的私钥解密 - 该私钥存在生成该证书的密钥库中
//        FileInputStream fis2 = new FileInputStream("J:\\yianwork\\breathing_cloud\\src\\opensslx509\\pkcs8_rsa_private.key");
//        KeyStore ks = KeyStore.getInstance("JKS"); // 加载证书库
//        char[] kspwd = "yian".toCharArray(); // 证书库密码
//        char[] keypwd = "yian".toCharArray(); // 证书密码
//         ks.load(fis2, kspwd); // 加载证书
//        PrivateKey pk2 = (PrivateKey) ks.getKey("yian", keypwd); // 获取证书私钥

//        fis2.close();
//        Cipher c2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        c2.init(Cipher.DECRYPT_MODE, pk2);
//        byte[] msg2 = c2.doFinal(msg1); // 解密后的数据
//        log.info("~~~ 私秘钥加密: "+byte2Hex(msg2));
//
//
//        System.out.println(new String(msg2, "UTF8")); // 将解密数据转为字符串
//        String aaa = Base64.getEncoder().encodeToString(pk2.getEncoded());
//        System.out.println(aaa);
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(aaa));
//        PrivateKey priva = KeyFactory.getInstance("RSA").generatePrivate(spec);

//       Cipher cc2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        cc2.init(Cipher.DECRYPT_MODE, priva);
//        byte[] msg22 = cc2.doFinal(msg1); // 解密后的数据
//
//        System.out.println(new String(msg22, "UTF8")); // 将解密数据转为字符串*/

        //aaa();

       try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            FileInputStream fileInputStream = new FileInputStream("D:\\vent_lic.cer");
            X509Certificate cer = (X509Certificate)certificateFactory.generateCertificate(fileInputStream);
            fileInputStream.close();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            log.info("~~~~~~~~~~~~~~~~~~~~读取Cer-Crt证书信息~~~~~~~~~~~~~~~~~~~~");
            log.info("cer_版本号___:"+cer.getVersion());
            log.info("cer_序列号___:"+cer.getSerialNumber());
            log.info("cer_发布方标识名___:"+cer.getIssuerDN().getName());
            log.info("cer_主体标识___:"+cer.getSubjectDN());
            log.info("cer_证书算法OID字符串___:"+cer.getSigAlgOID());
            log.info("cer_证书有效期___:" + cer.getNotBefore() + "~" + cer.getNotAfter());
            log.info("cer_签名算法___:"+cer.getSigAlgName());
            log.info("cer_签名值___:"+cer.getSigAlgOID());
            Date beforedate  = cer.getNotBefore();
            Date afterdate  = cer.getNotAfter();
            log.info("cer_证书生效日期___:"+dateformat.format(beforedate)+" 证书失效日期 :"+dateformat.format(afterdate));
            log.info("cer_证书颁发者___:"+cer.getSubjectDN().getName());
            log.info("cer_公钥___:"+cer.getPublicKey());



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
