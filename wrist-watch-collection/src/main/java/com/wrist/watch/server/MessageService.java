package com.wrist.watch.server;


import com.wrist.watch.model.DataModel;
import com.wrist.watch.model.KeyvrandvsVo;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
public interface MessageService {

    public DataModel startDataModelService(byte[] bytes, KeyvrandvsVo keyvrandvsVo)
            throws NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            NoSuchProviderException, InvalidKeyException;

 }
