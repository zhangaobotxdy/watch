package com.wrist.watch.utils;

import com.wrist.watch.server.Codeutil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @ClassName :     //类名
 * @Description : 测试证书 //描述
 * @Author Administrator -Earl
 * @Date 2020/10/22 10:43
 * @Version 1.0
 */
@Slf4j
public class RSAEncrypt {


    private static final int MAX_DECRYPT_BLOCK = 128;
    //测试
     /* private static final String DEFAULT_PUBLIC_KEY=
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCheDbpNNrmPljzbvVZZT7NXu7G\n" +
                        "PJmteWMpDgZpvB8cz3PA1bXuxh9w65pGtsC5sux1hEsHT+ZQ7hsYhzrsuWX6NbHf\n" +
                        "qTI+hGrY08gxbVficDtLWKPFuzMqDdnRelkG0FxvG+6ArJYOZ9PWwid5cGCE8vC7\n" +
                        "fCkvTAxSM3OqCQRzmwIDAQAB";*/
        //正式环境证书
    /*"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVYf51Si61vMJCPcRBDJhHsF8T\n" +
            "nNGi5PViacKpa/BxKj5pSsYbrsS+vLQfFcCWRLUgfMDyl4+ZMxjdwovue5XGM/t1\n" +
            "hUv8pvJsoZ5XUqEybcWZHNW/m295MokbrpC5e/c7FcO2m9Mb/ecIkK6n6ffup8sj\n" +
            "GCF21gjhuETxOHIEJQIDAQAB";*/

    public static final String DEFAULT_PRIVATE_KEY="";
            //正式证书
        /*  "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANVh/nVKLrW8wkI9\n" +
                    "xEEMmEewXxOc0aLk9WJpwqlr8HEqPmlKxhuuxL68tB8VwJZEtSB8wPKXj5kzGN3C\n" +
                    "i+57lcYz+3WFS/ym8myhnldSoTJtxZkc1b+bb3kyiRuukLl79zsVw7ab0xv95wiQ\n" +
                    "rqfp9+6nyyMYIXbWCOG4RPE4cgQlAgMBAAECgYEAjRunlVLAPuibogiMnuPAVDAb\n" +
                    "P7IFo+47I6CC3V0G/R8NdL3A8o+JhgTuLFxLJlQ7X0eApSx7BrmWqRe9R7EsYlfL\n" +
                    "yPW63BJMqg1SIVt5mw+v8QnwHoE5h0tiv1G0lhZbGcz/D+a9FNUe1RfTTZzqHyM2\n" +
                    "RbOA/IMhWl0t5y/WbQECQQDvLsikcEKyTot/fQTdZ3Z9TK3yUzNuXzXqMgLMHdMa\n" +
                    "VL6r1SJeAioOsIqNgWN1d4F5CSG3DS/XsYHzC9WUDD1lAkEA5GLRn0xocG7bWbyg\n" +
                    "9RG3rcDv+ZmHXaLMcpqe9otzR4xzxv2JNuS0y+7MdtgCnwVwLm+6eU+tXZvhMBhs\n" +
                    "Pl6fwQJADlQJJQCsni3iYXLF1dWa3Yq4aOnfDN+bRTpVvJCU0uD8wyJzepo6lsKw\n" +
                    "qiV0JddQ5EVNxb9+fDdgB4VP+Bx7mQJBANW/BqEoSbawiep6d8nlQxsDL5VS8zXX\n" +
                    "X7ECtTPZX5LfCJx8PSd69RrH6+RuVkWuOUe8mbFfuMmbUi5JLUVSGgECQQDXjjz2\n" +
                    "cyUdzC4+gJY1imzTwTsraMLuRqbBBftYusqb0QzmQZTM4oi3SAgA7CroPY3WcGf2\n" +
                    "5JjzALVgT8yr4XGJ";*/
       //测试证书
        /* "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKF4Nuk02uY+WPNu\n" +
                    "9VllPs1e7sY8ma15YykOBmm8HxzPc8DVte7GH3Drmka2wLmy7HWESwdP5lDuGxiH\n" +
                    "Ouy5Zfo1sd+pMj6EatjTyDFtV+JwO0tYo8W7MyoN2dF6WQbQXG8b7oCslg5n09bC\n" +
                    "J3lwYITy8Lt8KS9MDFIzc6oJBHObAgMBAAECgYAA0CQmpHd9tU6pWyOtIY9hZVr6\n" +
                    "cI+tifDv4fwySBo4AOtqSykxXJoH7YfHV2A9DyOVq+bc0JAuVCSayQOI8Z4L1HE2\n" +
                    "GGsEQoQWD9vXtENvRUIagHFj3ugHE7yaAXMehN7yAyYPbwtYwvnD7AvMx7uxF9aF\n" +
                    "kr+F/R0f1LOQw00pUQJBAMx03PDFQl3K7eR5xKAc3rB8Eab00sawk9FMFKozbZ9J\n" +
                    "K6ppTZwhRzX2altI1zEWWWnpoHAo4duySqtPP5I7fy0CQQDKLRQTGjqS9lqvxK3i\n" +
                    "EXw6zWAO27Mc7ad8akCm71HUZlwx2hS6hHGcgr06F2YZr+3W5vsIDCSa7j34UdNL\n" +
                    "ZrrnAkAg41N+71wjljb6H5Q/ZSV1Ih/8yTj2eQTuD9zrG+awTqbVfKbdTIUV6xNk\n" +
                    "p6zl2oB7hvcl042xN1bHg7HoJeXpAkBg409+hBBUA4mcOEYYU8bUKz4AgNsKXv3n\n" +
                    "ysem+IOfpVpDbb6HUBdeKO1kHHiKkNwLI/Bjf+Y+9TNvgE8hEF87AkAJq4rZ+TcE\n" +
                    "Vnoett45SkCbPjrvnYa3yZOUrBzvv7oUpKDpj35piApGPM+Jcf9awwCrZNHU1bu1\n" +
                    "W9gUDGJSmG6V";*/

    /**
     * 私钥
     */
    private RSAPrivateKey privateKey;

    /**
     * 公钥
     */
    private RSAPublicKey publicKey;

    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 获取私钥
     * @return 当前的私钥对象
     */
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 获取公钥
     * @return 当前的公钥对象
     */
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 随机生成密钥对
     */
    public void genKeyPair(){
        KeyPairGenerator keyPairGen= null;
        try {
            keyPairGen= KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair= keyPairGen.generateKeyPair();
        this.privateKey= (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey= (RSAPublicKey) keyPair.getPublic();
    }

    /**
     * 从文件中输入流中加载公钥
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(InputStream in) throws Exception{
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

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
        for (int i = 0; i < bytes.length; i++) {
            // 每个byte转换成16进制字符时，bytes[i] & 0xff如果高位是0，输出将会去掉，所以+0x100(在更高位加1)，再截取后两位字符
            builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1).toUpperCase());
            builder.append(" ");
        }
        return builder.toString();
    }

    /**
     * 从字符串中加载公钥
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     * @return
     */
    public void loadPublicKey(String publicKeyStr) throws Exception{
        try {

           /* BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr); 2021-1-28*/
            byte[] buffer = Base64.decodeBase64(publicKeyStr);
           /* log.info("\n ~~~~~~~~ buffer :"+byte2Hex(buffer));*/
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
            /*  log.info("\n 加载公秘钥转换十六进制:"+byte2Hex(keySpec.getEncoded())); */
            this.publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } /*catch (IOException e) {
            throw new Exception("公钥数据内容读取错误");
        } */
        catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }

    }

    // private static String str="30819F300D06092A864886F70D010101050003818D0030818902818100846A95F0AA4D0A325C6A0C56AAA018B9B5664C1336E135657DCCD3103EFCE2C7E18D2ACBA675EC47608ECB9A86042218DC4A75DFEADB738436A2BD0120996FD9F19AED3A90F182D9E6F8B72E482A3C8DB4A1E7E3A32F887DC4C3A5389B13B6A90788A7F9E15DD27FAAD2DD7ABE868D54C7D369089C9A56C4BB682D6A109860E10203010001";

    /**
     * 针对呼吸机秘钥加密
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public void bloadPublicKey(String publicKeyStr) throws Exception{
        try {
            //BASE64Decoder base64Decoder= new BASE64Decoder();
            //byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);
            byte[] buffer= Codeutil.hexString2Bytes(publicKeyStr);
            /*log.info("\n ~~~~~~~~~~~~~~~~~~~~~初始化秘钥:"+ Codeutil.byte2Hex(buffer));*/
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
           /* log.info("\n 加载公秘钥转换十六进制:"+byte2Hex(keySpec.getEncoded()));*/
            // log.info("~~~!!!!!!!!!!!!!!!!!!!!!!1~~~~~初始化秘钥:"+ this.publicKey.getModulus()+" \n"+this.publicKey.getPublicExponent());
            this.publicKey=(RSAPublicKey) keyFactory.generatePublic(keySpec);

        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        /*} catch (IOException e) {
            throw new Exception("公钥数据内容读取错误");*/
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     * @param_keyFileName 私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    public void loadPrivateKey(InputStream in) throws Exception{
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());

        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public boolean loadPrivateKey(String privateKeyStr) throws Exception{
        try {
            /*BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);*/
            byte[] buffer = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
            /*log.info("\n 加载私秘钥转换十六进制:"+byte2Hex(keySpec.getEncoded()));*/
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            this.privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            return true;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        }/* catch (IOException e) {
            throw new Exception("私钥数据内容读取错误");
        }*/ catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
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
            builder.append(" ");
        }
        return builder.toString();
    }

    /**
     * 加密过程
     * @param publicKey 公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception{
        if(publicKey== null){
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());//  RSA/ECB/PKCS1Padding
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output= cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            e.getMessage();
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.getMessage();
            e.printStackTrace();
            return null;
        }catch (InvalidKeyException e) {
            e.getMessage();
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            e.getMessage();
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            e.getMessage();
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 解密过程
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public  byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception{
        if (privateKey== null){
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding",new BouncyCastleProvider());// 本地测试不用添加格式 RSA/ECB/PKCS1Padding
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output= cipher.doFinal(cipherData);
            //log.info("\n 私秘钥解密: "+Codeutil.byte2Hex(output));
           return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }catch (InvalidKeyException e) {
            e.getMessage();
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            e.getMessage();
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            e.getMessage();
            throw new Exception("密文数据已损坏");
        }
    }


    /**
     * 字节数据转十六进制字符串
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data){
        StringBuilder stringBuilder= new StringBuilder();
        for (int i=0; i<data.length; i++){
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);
            //取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i<data.length-1){
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }


    /**
     * 字节数据转十六进制字符串
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayandToString(byte[] data){
        StringBuilder stringBuilder= new StringBuilder();
        for (int i=0; i<data.length; i++){
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);
            //取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);

        }
        return stringBuilder.toString();
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
     * 私钥加密
     * @paramdata
     * @paramprivateKeyrsa切割解码  , ENCRYPT_MODE,加密数据   ,DECRYPT_MODE,解密数据
     * @return
     */

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;  //最大块
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    //可以调用以下的doFinal（）方法完成加密或解密数据：
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();

        return resultDatas;
    }

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            //每个Cipher初始化方法使用一个模式参数opmod，并用此模式初始化Cipher对象。此外还有其他参数，包括密钥key、包含密钥的证书certificate、算法参数params和随机源random。
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes("UTF-8"), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    public static void main(String[] args){
        RSAEncrypt rsaEncrypt= new RSAEncrypt();
        //rsaEncrypt.genKeyPair();
        //加载公钥
       /* try {
            rsaEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY); // 测试
            System.out.println("加载公钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载公钥失败");
        }*/

        //加载私钥
        try {
            rsaEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY);
            System.out.println("加载私钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载私钥失败");
        }
/*

        byte[] bada={
                (byte)0x6C,(byte)0x9D,(byte)0xC2,(byte)0x9B,(byte)0xAA,(byte)0xBE,(byte)0x0A,(byte)0x28,(byte)0x9C,(byte)0x49,(byte)0xC5,(byte)0x6D,(byte)0xB3,(byte)0xEC,(byte)0x15,(byte)0x97,(byte)0x99,(byte)0x9B,(byte)0xB5,(byte)0x01,(byte)0x8E,(byte)0x0A,(byte)0x4C,(byte)0x79,(byte)0x61,(byte)0x66,(byte)0x12,(byte)0x8D,(byte)0x19,(byte)0x1E,(byte)0xDA,(byte)0xCE,(byte)0x15,(byte)0x0C,(byte)0xBF,(byte)0xF2,(byte)0x17,(byte)0x2B,(byte)0xF6,(byte)0x1C,(byte)0x23,(byte)0x25,(byte)0x56,(byte)0xC3,(byte)0x1A,(byte)0x8F,(byte)0x23,(byte)0x33,(byte)0xFF,(byte)0x9B,(byte)0x41,(byte)0xFF,(byte)0x15,(byte)0xCA,(byte)0xCA,(byte)0x69,(byte)0x9E,(byte)0x9F,(byte)0x1B,(byte)0x83,(byte)0x73,(byte)0xFE,(byte)0x6A,(byte)0x04,(byte)0xA7,(byte)0x6E,(byte)0x96,(byte)0xD5,(byte)0x77,(byte)0x8F,(byte)0x6B,(byte)0x73,(byte)0x96,(byte)0xC0,(byte)0xA0,(byte)0x4E,(byte)0x4E,(byte)0x46,(byte)0x72,(byte)0x90,(byte)0x07,(byte)0xF4,(byte)0xDD,(byte)0xB8,(byte)0xFB,(byte)0x7C,(byte)0x16,(byte)0xAD,(byte)0x2E,(byte)0x2D,(byte)0x95,(byte)0x9C,(byte)0xC3,(byte)0x8F,(byte)0xCE,(byte)0xEE,(byte)0x4A,(byte)0x41,(byte)0x2D,(byte)0x5B,(byte)0x9B,(byte)0x58,(byte)0x37,(byte)0x47,(byte)0xF2,(byte)0xF0,(byte)0x0C,(byte)0x27,(byte)0x14,(byte)0x01,(byte)0x24,(byte)0xD4,(byte)0xD8,(byte)0xDF,(byte)0x43,(byte)0x70,(byte)0xE0,(byte)0x07,(byte)0xDF,(byte)0x3C,(byte)0xAA,(byte)0x4B,(byte)0xBB,(byte)0x1D,(byte)0xFD,(byte)0x9A,(byte)0x30,(byte)0xC9
                 };
*/

       /* byte[] bada=
                {
               (byte)0x2B,(byte)0x16,(byte)0xD7,(byte)0x9B,(byte)0xCB,(byte)0xC0,(byte)0x25,(byte)0x99,(byte)0x1A,(byte)0x49,(byte)0x6C,(byte)0x2F,(byte)0xC3,(byte)0x9A,(byte)0x9C,(byte)0xF3,(byte)0x67,(byte)0xDE,(byte)0xDD,(byte)0x16,(byte)0x17,(byte)0xCD,(byte)0xD6,(byte)0x79,(byte)0x3B,(byte)0xEE,(byte)0x63,(byte)0x5A,(byte)0x57,(byte)0x08,(byte)0x20,(byte)0xAB,(byte)0x9E,(byte)0x39,(byte)0x0E,(byte)0x1E,(byte)0x8C,(byte)0x82,(byte)0xFF,(byte)0xEE,(byte)0xC6,(byte)0x64,(byte)0xB0,(byte)0xAC,(byte)0x36,(byte)0x64,(byte)0xDE,(byte)0x02,(byte)0x47,(byte)0x40,(byte)0xCE,(byte)0x8C,(byte)0xA2,(byte)0x40,(byte)0x45,(byte)0x25,(byte)0x7D,(byte)0x5D,(byte)0x3C,(byte)0x9F,(byte)0x2D,(byte)0x72,(byte)0xC4,(byte)0x09,(byte)0x7A,(byte)0xF5,(byte)0x4D,(byte)0xA9,(byte)0xD7,(byte)0x7F,(byte)0x8C,(byte)0x8C,(byte)0x92,(byte)0x1D,(byte)0x7E,(byte)0xAB,(byte)0xCA,(byte)0x94,(byte)0x22,(byte)0x89,(byte)0x0C,(byte)0xF4,(byte)0x90,(byte)0x27,(byte)0xC1,(byte)0x7A,(byte)0x77,(byte)0x96,(byte)0x64,(byte)0xD2,(byte)0x9C,(byte)0xDA,(byte)0xB0,(byte)0x34,(byte)0xA1,(byte)0x5A,(byte)0xDC,(byte)0xAF,(byte)0xB6,(byte)0x70,(byte)0x9C,(byte)0xC4,(byte)0xD4,(byte)0x12,(byte)0xF0,(byte)0x2A,(byte)0x1D,(byte)0x10,(byte)0x98,(byte)0xED,(byte)0xBF,(byte)0x88,(byte)0x67,(byte)0xD5,(byte)0xAF,(byte)0x8F,(byte)0x60,(byte)0x21,(byte)0x59,(byte)0x28,(byte)0x35,(byte)0xE6,(byte)0x95,(byte)0x89,(byte)0x93,(byte)0xDE,(byte)0x19,(byte)0xDB
                };
      */
      /*  byte[] bada=
                {
                (byte)0x98,(byte)0x16,(byte)0x7B,(byte)0x98,(byte)0xCF,(byte)0x7D,(byte)0xA6,(byte)0xAF,(byte)0x5C,(byte)0x94,(byte)0xD0,(byte)0x7B,(byte)0x54,(byte)0x4D,(byte)0xB1,(byte)0x07,(byte)0x49,(byte)0x38,(byte)0x44,(byte)0x33,(byte)0x75,(byte)0xEE,(byte)0x66,(byte)0x74,(byte)0xC7,(byte)0x0E,(byte)0xE8,(byte)0x58,(byte)0x43,(byte)0xBA,(byte)0x64,(byte)0xCF,(byte)0xA1,(byte)0x34,(byte)0x7D,(byte)0xFA,(byte)0x5B,(byte)0x6E,(byte)0xEE,(byte)0x9B,(byte)0xEB,(byte)0x2C,(byte)0x00,(byte)0x9E,(byte)0xDE,(byte)0xAE,(byte)0x8F,(byte)0xE5,(byte)0x64,(byte)0x96,(byte)0xCB,(byte)0x52,(byte)0x4F,(byte)0x32,(byte)0x88,(byte)0x5C,(byte)0xE7,(byte)0xDD,(byte)0xF9,(byte)0xEA,(byte)0x8E,(byte)0xC6,(byte)0x36,(byte)0x6B,(byte)0x65,(byte)0x89,(byte)0xC0,(byte)0x44,(byte)0x17,(byte)0x98,(byte)0xE5,(byte)0xAA,(byte)0x2F,(byte)0x30,(byte)0x0C,(byte)0x9F,(byte)0x80,(byte)0x1F,(byte)0xF7,(byte)0x75,(byte)0x1B,(byte)0x34,(byte)0xF9,(byte)0x48,(byte)0xC2,(byte)0xBB,(byte)0xC2,(byte)0xC5,(byte)0x40,(byte)0x97,(byte)0x1F,(byte)0x8F,(byte)0x40,(byte)0xD5,(byte)0x65,(byte)0x9E,(byte)0xB9,(byte)0x34,(byte)0x62,(byte)0x42,(byte)0x51,(byte)0x64,(byte)0x45,(byte)0xD5,(byte)0x38,(byte)0x16,(byte)0xA9,(byte)0x6D,(byte)0x4B,(byte)0x16,(byte)0x64,(byte)0x13,(byte)0x9D,(byte)0xA7,(byte)0xF7,(byte)0xEF,(byte)0x0B,(byte)0x24,(byte)0xEF,(byte)0xC8,(byte)0xBC,(byte)0xE8,(byte)0x3B,(byte)0x07,(byte)0xCE,(byte)0x5C,(byte)0x3B,(byte)0xB6
                };*/

        byte[] bada=
                {
                //版本 2
              // (byte) 0x01, (byte) 0x00,
              //  //机器序列号 32
              // (byte) 0x41, (byte) 0x53, (byte) 0x32, (byte) 0x30, (byte) 0x31, (byte) 0x35, (byte) 0x30, (byte) 0x33, (byte) 0x31, (byte) 0x35, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x31, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                //呼吸机随机数 1  4
            //   (byte)0x2C,(byte)0x0A,(byte)0x07,(byte)0xE0,(byte)0xA5,(byte)0xC0,(byte)0x2D,(byte)0xAC,(byte)0x73,(byte)0x38,(byte)0xD2,(byte)0xB5,(byte)0x5C,(byte)0xBF,(byte)0x2F,(byte)0xA9,(byte)0xD3,(byte)0x45,(byte)0xF3,(byte)0xDA,(byte)0x81,(byte)0xA2,(byte)0x9B,(byte)0x55,(byte)0x38,(byte)0x23,(byte)0xB3,(byte)0xE5,(byte)0x11,(byte)0x44,(byte)0x7E,(byte)0xF4,(byte)0x03,(byte)0x94,(byte)0xFE,(byte)0xEB,(byte)0xBB,(byte)0x82,(byte)0xA9,(byte)0xA0,(byte)0xD3,(byte)0xF5,(byte)0x43,(byte)0xDF,(byte)0x42,(byte)0xB5,(byte)0xF6,(byte)0x0E,(byte)0xEC,(byte)0x22,(byte)0xB8,(byte)0x33,(byte)0xEA,(byte)0xB3,(byte)0x09,(byte)0xDE,(byte)0xA4,(byte)0x89,(byte)0x88,(byte)0xDE,(byte)0x7A,(byte)0xA9,(byte)0x00,(byte)0xE6,(byte)0x03,(byte)0xD7,(byte)0x21,(byte)0xD1,(byte)0xE3,(byte)0x48,(byte)0x51,(byte)0x76,(byte)0x65,(byte)0xBA,(byte)0xA6,(byte)0x89,(byte)0xAD,(byte)0x4B,(byte)0x27,(byte)0x67,(byte)0xB9,(byte)0xAD,(byte)0x67,(byte)0x25,(byte)0x8B,(byte)0x0D,(byte)0x34,(byte)0x9E,(byte)0xCB,(byte)0x91,(byte)0x89,(byte)0x2F,(byte)0x53,(byte)0x69,(byte)0x2C,(byte)0x77,(byte)0x6E,(byte)0xCE,(byte)0xD1,(byte)0x7C,(byte)0xAE,(byte)0xD4,(byte)0xB6,(byte)0x2D,(byte)0x49,(byte)0x74,(byte)0x70,(byte)0x5F,(byte)0xA2,(byte)0x08,(byte)0x04,(byte)0x23,(byte)0x1C,(byte)0xDF,(byte)0x85,(byte)0xAF,(byte)0x39,(byte)0x65,(byte)0x13,(byte)0x7E,(byte)0xAB,(byte)0x47,(byte)0x03,(byte)0xA0,(byte)0xF4,(byte)0x61,(byte)0x81,(byte)0x22,
                //机器序列号  32
               (byte)0x6C,(byte)0x9D,(byte)0xC2,(byte)0x9B,(byte)0xAA,(byte)0xBE,(byte)0x0A,(byte)0x28,(byte)0x9C,(byte)0x49,(byte)0xC5,(byte)0x6D,(byte)0xB3,(byte)0xEC,(byte)0x15,(byte)0x97,(byte)0x99,(byte)0x9B,(byte)0xB5,(byte)0x01,(byte)0x8E,(byte)0x0A,(byte)0x4C,(byte)0x79,(byte)0x61,(byte)0x66,(byte)0x12,(byte)0x8D,(byte)0x19,(byte)0x1E,(byte)0xDA,(byte)0xCE,(byte)0x15,(byte)0x0C,(byte)0xBF,(byte)0xF2,(byte)0x17,(byte)0x2B,(byte)0xF6,(byte)0x1C,(byte)0x23,(byte)0x25,(byte)0x56,(byte)0xC3,(byte)0x1A,(byte)0x8F,(byte)0x23,(byte)0x33,(byte)0xFF,(byte)0x9B,(byte)0x41,(byte)0xFF,(byte)0x15,(byte)0xCA,(byte)0xCA,(byte)0x69,(byte)0x9E,(byte)0x9F,(byte)0x1B,(byte)0x83,(byte)0x73,(byte)0xFE,(byte)0x6A,(byte)0x04,(byte)0xA7,(byte)0x6E,(byte)0x96,(byte)0xD5,(byte)0x77,(byte)0x8F,(byte)0x6B,(byte)0x73,(byte)0x96,(byte)0xC0,(byte)0xA0,(byte)0x4E,(byte)0x4E,(byte)0x46,(byte)0x72,(byte)0x90,(byte)0x07,(byte)0xF4,(byte)0xDD,(byte)0xB8,(byte)0xFB,(byte)0x7C,(byte)0x16,(byte)0xAD,(byte)0x2E,(byte)0x2D,(byte)0x95,(byte)0x9C,(byte)0xC3,(byte)0x8F,(byte)0xCE,(byte)0xEE,(byte)0x4A,(byte)0x41,(byte)0x2D,(byte)0x5B,(byte)0x9B,(byte)0x58,(byte)0x37,(byte)0x47,(byte)0xF2,(byte)0xF0,(byte)0x0C,(byte)0x27,(byte)0x14,(byte)0x01,(byte)0x24,(byte)0xD4,(byte)0xD8,(byte)0xDF,(byte)0x43,(byte)0x70,(byte)0xE0,(byte)0x07,(byte)0xDF,(byte)0x3C,(byte)0xAA,(byte)0x4B,(byte)0xBB,(byte)0x1D,(byte)0xFD,(byte)0x9A,(byte)0x30,(byte)0xC9,
                //呼吸机公钥（低 64 字节）128
              //  (byte)0x98,(byte)0x16,(byte)0x7B,(byte)0x98,(byte)0xCF,(byte)0x7D,(byte)0xA6,(byte)0xAF,(byte)0x5C,(byte)0x94,(byte)0xD0,(byte)0x7B,(byte)0x54,(byte)0x4D,(byte)0xB1,(byte)0x07,(byte)0x49,(byte)0x38,(byte)0x44,(byte)0x33,(byte)0x75,(byte)0xEE,(byte)0x66,(byte)0x74,(byte)0xC7,(byte)0x0E,(byte)0xE8,(byte)0x58,(byte)0x43,(byte)0xBA,(byte)0x64,(byte)0xCF,(byte)0xA1,(byte)0x34,(byte)0x7D,(byte)0xFA,(byte)0x5B,(byte)0x6E,(byte)0xEE,(byte)0x9B,(byte)0xEB,(byte)0x2C,(byte)0x00,(byte)0x9E,(byte)0xDE,(byte)0xAE,(byte)0x8F,(byte)0xE5,(byte)0x64,(byte)0x96,(byte)0xCB,(byte)0x52,(byte)0x4F,(byte)0x32,(byte)0x88,(byte)0x5C,(byte)0xE7,(byte)0xDD,(byte)0xF9,(byte)0xEA,(byte)0x8E,(byte)0xC6,(byte)0x36,(byte)0x6B,(byte)0x65,(byte)0x89,(byte)0xC0,(byte)0x44,(byte)0x17,(byte)0x98,(byte)0xE5,(byte)0xAA,(byte)0x2F,(byte)0x30,(byte)0x0C,(byte)0x9F,(byte)0x80,(byte)0x1F,(byte)0xF7,(byte)0x75,(byte)0x1B,(byte)0x34,(byte)0xF9,(byte)0x48,(byte)0xC2,(byte)0xBB,(byte)0xC2,(byte)0xC5,(byte)0x40,(byte)0x97,(byte)0x1F,(byte)0x8F,(byte)0x40,(byte)0xD5,(byte)0x65,(byte)0x9E,(byte)0xB9,(byte)0x34,(byte)0x62,(byte)0x42,(byte)0x51,(byte)0x64,(byte)0x45,(byte)0xD5,(byte)0x38,(byte)0x16,(byte)0xA9,(byte)0x6D,(byte)0x4B,(byte)0x16,(byte)0x64,(byte)0x13,(byte)0x9D,(byte)0xA7,(byte)0xF7,(byte)0xEF,(byte)0x0B,(byte)0x24,(byte)0xEF,(byte)0xC8,(byte)0xBC,(byte)0xE8,(byte)0x3B,(byte)0x07,(byte)0xCE,(byte)0x5C,(byte)0x3B,(byte)0xB6,

                //呼吸机公钥（高 64 字节）128
               //  (byte)0x2B,(byte)0x16,(byte)0xD7,(byte)0x9B,(byte)0xCB,(byte)0xC0,(byte)0x25,(byte)0x99,(byte)0x1A,(byte)0x49,(byte)0x6C,(byte)0x2F,(byte)0xC3,(byte)0x9A,(byte)0x9C,(byte)0xF3,(byte)0x67,(byte)0xDE,(byte)0xDD,(byte)0x16,(byte)0x17,(byte)0xCD,(byte)0xD6,(byte)0x79,(byte)0x3B,(byte)0xEE,(byte)0x63,(byte)0x5A,(byte)0x57,(byte)0x08,(byte)0x20,(byte)0xAB,(byte)0x9E,(byte)0x39,(byte)0x0E,(byte)0x1E,(byte)0x8C,(byte)0x82,(byte)0xFF,(byte)0xEE,(byte)0xC6,(byte)0x64,(byte)0xB0,(byte)0xAC,(byte)0x36,(byte)0x64,(byte)0xDE,(byte)0x02,(byte)0x47,(byte)0x40,(byte)0xCE,(byte)0x8C,(byte)0xA2,(byte)0x40,(byte)0x45,(byte)0x25,(byte)0x7D,(byte)0x5D,(byte)0x3C,(byte)0x9F,(byte)0x2D,(byte)0x72,(byte)0xC4,(byte)0x09,(byte)0x7A,(byte)0xF5,(byte)0x4D,(byte)0xA9,(byte)0xD7,(byte)0x7F,(byte)0x8C,(byte)0x8C,(byte)0x92,(byte)0x1D,(byte)0x7E,(byte)0xAB,(byte)0xCA,(byte)0x94,(byte)0x22,(byte)0x89,(byte)0x0C,(byte)0xF4,(byte)0x90,(byte)0x27,(byte)0xC1,(byte)0x7A,(byte)0x77,(byte)0x96,(byte)0x64,(byte)0xD2,(byte)0x9C,(byte)0xDA,(byte)0xB0,(byte)0x34,(byte)0xA1,(byte)0x5A,(byte)0xDC,(byte)0xAF,(byte)0xB6,(byte)0x70,(byte)0x9C,(byte)0xC4,(byte)0xD4,(byte)0x12,(byte)0xF0,(byte)0x2A,(byte)0x1D,(byte)0x10,(byte)0x98,(byte)0xED,(byte)0xBF,(byte)0x88,(byte)0x67,(byte)0xD5,(byte)0xAF,(byte)0x8F,(byte)0x60,(byte)0x21,(byte)0x59,(byte)0x28,(byte)0x35,(byte)0xE6,(byte)0x95,(byte)0x89,(byte)0x93,(byte)0xDE,(byte)0x19,(byte)0xDB

                };
        //测试字符串
        String encryptStr= "Test String WELCOAME YIAN";
        try {
            //加密
           byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), encryptStr.getBytes());

            log.info("\n 公钥加密数据之后转换十六进制："+byte2Hex(cipher));
            //解密
             //String  rsaEncryptrsaEncrypt.getPrivateKey();
            // byte[] bytes1 = Base64.decodeBase64(cipher);
            // log.info("\n 公钥加密数据: "+Codeutil.byte2Hex(cipher));
            //String sprivate = byte26Hex(bytes1);
            byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), cipher);
           // byte[] bytes2 = Base64.decodeBase64(plainText);
           // System.out.println("密文长度:"+ cipher.length);
            System.out.println(RSAEncrypt.byteArrayToString(plainText));
            System.out.println("明文长度:"+ plainText.length);
            System.out.println(RSAEncrypt.byteArrayToString(plainText));
            System.out.println(new String(plainText));
            /*int nowIndex=0;
            byte[] bProtocolVersion = Arrays.copyOfRange(plainText, nowIndex, nowIndex + 8); //偏移0
            nowIndex+=8;
            byte[] bstr = Arrays.copyOfRange(plainText, nowIndex, nowIndex + 32); //偏移0

             String str =Codeutil.hexString2String(Codeutil.bytes2HexString(bstr));
             log.info("\n 为啥："+str);
*/
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


}
