package com.wrist.watch.server.impl;

import com.wrist.watch.constant.Const;
import com.wrist.watch.model.DataModel;
import com.wrist.watch.model.KeyvrandvsVo;
import com.wrist.watch.server.MessageService;

import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName :     //消息服务接口
 * @Description :  //描述
 * @Author Administrator -zhangaobo
 * @Date 2021/06/03 17:33
 * @Version 1.0
 */
@Slf4j
//@Service
@Component
public class MessageServiceImpl implements MessageService {

    /*@Autowired
    private RedisService redisService;

    @Autowired
    private DevicecontrolService devicecontrolService;//治疗控制参数信息结构体数据传输

    @Autowired
    private DeviceBasisService deviceBasisService;// 产品信息结构体数据传输

    @Autowired
    private DeviceEventService deviceEventService; //呼吸机呼吸事件同步指令

    @Autowired
    private DeviceLogsService deviceLogsService; //呼吸机使用日志还同步指令

    @Autowired
    private DeviceUseStateService deviceUseStateService;//产品使用状态结构体数据传输

    @Autowired
    private DeviceStatisticsService deviceStatisticsService;//产品使用状态结构体数据传输

    @Autowired
    private DeviceOnlineService deviceOnlineService;//在线设备-呼吸机运行状态传输

    @Autowired
    private WaveParaService waveParaService;//10秒波形数据


    @Autowired
    private DeviceCustomerService deviceCustomerService;


    @Autowired
    private ScheduledtaskService scheduledtaskService;*/

    /**
     *
     *
     * @return*/
    @Override
    public DataModel startDataModelService(byte[] bytes, KeyvrandvsVo keyvrandvsVo)
            throws NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
      /*  if( keyvrandvsVo.getSsxkey() !=null || keyvrandvsVo.getSvr1_vr2_vs1_vs2() !=null) {
            try {
               // TODO  缓存存储数据
                log.info("\n ------------------ Service --------- MessageServiceImpl------startDataModelService-------start------------------: \n " + ByteBufUtil.hexDump(bytes).toUpperCase());
                DataModel dataModel = new DataModel();
                boolean bfalse = false;
                int iIndex = 0;
                byte[] hdata = Arrays.copyOfRange(bytes, iIndex, iIndex + 32);//数据段帧头
                String shdata = Codeutil.hexString2String(Codeutil.bytes2HexString(hdata)).trim();
                String MachineSerialNumbere = shdata.substring(shdata.indexOf(" ") + 1);
                log.info("\n $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$机器类型+序列号:" + shdata.trim() + "机器序号:" + MachineSerialNumbere);

                String reisture = redisService.getCacheObject("breathing:online:deviceId:" + MachineSerialNumbere);
                if (!MachineSerialNumbere.equals(reisture) && null == reisture) {
                    redisService.setCacheObject("breathing:online:deviceId:" + MachineSerialNumbere, MachineSerialNumbere);
                } else {
                    redisService.expire("breathing:online:deviceId:" + MachineSerialNumbere, 20, TimeUnit.SECONDS);
                    *//* boolean btrue=scheduledtaskService.NumbereRate(MachineSerialNumbere);*//*
                }

                log.info("\n MessageServiceImpl sxkey：" + keyvrandvsVo.getSsxkey() + " \n MessageServiceImpl vr1_vr2_vs1_vs2 " + keyvrandvsVo.getSvr1_vr2_vs1_vs2());
                iIndex += 32;
                byte[] hddata = Arrays.copyOfRange(bytes, iIndex, bytes.length);//数据段帧头

                //AEC256CBC aec256cbc = new AEC256CBC();
                byte[] bbuf = AEC256CBC.AES_cbc_decrypt(hddata, Codeutil.hex2Bytes(keyvrandvsVo.getSsxkey()), Codeutil.hex2Bytes(keyvrandvsVo.getSvr1_vr2_vs1_vs2()));

                String sbata = ByteBufUtil.hexDump(bbuf).toUpperCase();
                log.info("\n 解析AEC_CBC 数据格式:" + sbata);
                int inowIndex = 0;
                byte[] head = Arrays.copyOfRange(bbuf, inowIndex, inowIndex + 1);//数据段帧头
                String strhead = Codeutil.byte2Hex(head);
                log.info("\n 数段__运行状态:" + strhead);
                inowIndex += 1;
                byte[] btype = Arrays.copyOfRange(bbuf, inowIndex, inowIndex + 1);//数据段指令
                String sbytpe = Codeutil.byte2Hex(btype);
                log.info("\n 数段__数据段指令:" + sbytpe);
                inowIndex += 1;
                byte[] blegth = Arrays.copyOfRange(bbuf, inowIndex, inowIndex + 2);//数据段数据长度
                String sblegth = Codeutil.byte2Hex(blegth);
                log.info("\n 数据段数据长度:" + sblegth);
                inowIndex += 2;
                byte[] bbydata = Arrays.copyOfRange(bbuf, inowIndex, bbuf.length - 1);//数据段数据内容
                String sbbydata = Codeutil.byte2Hex(bbydata);
                log.info("\n 数据段数据内容:" + sbbydata);
               *//* if (sbbydata.equals("000000000000")) {*//*
                    byte[] bcrc8 = Arrays.copyOfRange(bbuf, bbuf.length - 1, bbuf.length);//数据段帧尾
                    String scrc8 = Codeutil.byte2Hex(bcrc8);
                    log.info("\n 数段_数据段帧尾_crc8:" + scrc8);

                    byte[] btycrc8 = Arrays.copyOfRange(bbuf, 0, bbuf.length - 1);//本地计算帧尾
                    String sbtycrc8 = Codeutil.byte2Hex(btycrc8);
                    int icrc8 = CRC16Util.crc8_table(btycrc8);
                    log.info("\n 本地换算__crc8:" + scrc8);
                    log.info(" \n ~~~~~CRC8十进制:" + icrc8 + " CRC8 长度十六进制:" + Codeutil.byte2Hex(Codeutil.intcrc8ToByteArray(icrc8)));
                    if (!Arrays.equals(bcrc8, Codeutil.intcrc8ToByteArray(icrc8))) {
                        log.info("\n 数据段CRC8 校验不正确!:" + scrc8);
                        log.info(" \n ~~~~~呼吸机_CRC8 十六进制 :" + scrc8);
                        log.info(" \n ~~~~~本地_CRC8十进制:" + sbtycrc8 + " CRC8_效验:" + Codeutil.byte2Hex(Codeutil.intcrc8ToByteArray(icrc8)));
                    }
                    switch (btype[0]) {
                        case Const.Busines_01:
                            log.info("~~~~~~~~~~~~~Const.busines_01: " + sblegth);
                            bfalse = devicecontrolService.startDevicecontrolService(bbydata, keyvrandvsVo);//治疗控制参数信息结构体数据传输
                            if (bfalse == true) {
                                //bfalse=false;
                                dataModel.setCommand(Codeutil.intcrc8ToByteArray((short) Const.Busines_01));
                            }
                            break;
                        case Const.Busines_02:
                            log.info("~~~~~~~~~~~~~Const.busines_02: " + sblegth);
                            bfalse = deviceBasisService.startDeviceBasisService(bbydata, keyvrandvsVo);//产品信息结构体数据传输
                            if (bfalse == true) {
                                //bfalse=false;
                                dataModel.setCommand(Codeutil.intcrc8ToByteArray((short) Const.Busines_02));
                            }
                            break;
                        case Const.Busines_03:
                            log.info("~~~~~~~~~~~~~Const.busines_03: " + sblegth);
                            bfalse = deviceUseStateService.startDeviceUseStateService(bbydata, keyvrandvsVo);//产品使用状态结构体数据传输
                            if (bfalse == true) {
                                // bfalse=false;
                                dataModel.setCommand(Codeutil.intcrc8ToByteArray((short) Const.Busines_02));
                            }
                            break;
                        case Const.Busines_04:
                            log.info("~~~~~~~~~~~~~Const.busines_04: " + sblegth);
                            bfalse = deviceStatisticsService.startDeviceStatisticsService(bbydata, keyvrandvsVo);//统计数据结构体传输
                            if (bfalse == true) {
                                log.info("______________________统计数据结构体传输________________________________");
                                dataModel.setCommand(Codeutil.intcrc8ToByteArray((short) Const.Busines_04));
                            }
                            break;
                        case Const.Busines_05:
                            log.info("~~~~~~~~~~~~~Const.busines_05: " + sblegth);
                            //nettyServerHandler.operatingstateService.StrstartOperatingstateService(msg);
                            *//*if (bfalse == true) {
                                dataModel.setCommand(Codeutil.intcrc8ToByteArray((short) Const.busines_05));
                            }*//*
                            break;
                        case Const.Busines_06:
                            log.info("~~~~~~~~~~~~~Const.busines_06: " + sblegth);//波形数据
                          *//*  if("0007".equals(sblegth))
                            {*//*
                                 waveParaService.startWaveParaService(bbydata, keyvrandvsVo);
                           *//*      log.info("~~~~~~待机状态~~~~~~~~~");
                            }else
                            {
                                log.info("~~~~~~工作状态状态【10秒波形数据】~~~~~~~~~");
                            }*//*
                            *//*if(bfalse == true) {
                                dataModel.setCommand(Codeutil.intcrc8ToByteArray((short) Const.busines_06));
                            }*//*
                            // if(!MachineSerialNumbere.equals(reisture))
                            //    redisService.expire("breathing:online:deviceId:"+MachineSerialNumbere,10, TimeUnit.SECONDS);
                            // redisService.deleteObject("breathing:online:deviceId:"+MachineSerialNumbere);
                            break;
                        case Const.Busines_07:
                            log.info("~~~~~~~~~~~~~Const.busines_07: " + sblegth);
                            bfalse = deviceCustomerService.startDeviceCustomerService(bbydata, keyvrandvsVo);//呼吸机运行状态传输
                            // bfalse = deviceOnlineService.startDeviceOnlineService(bbydata, keyvrandvsVo);//呼吸机运行状态传输
                            if (bfalse == true) {
                                dataModel.setCommand(Codeutil.intcrc8ToByteArray((short) Const.Busines_07));
                            }
                            break;
                        case Const.Busines_08:
                            log.info("~~~~~~~~~~~~~Const.busines_08: " + sblegth);
                            bfalse = deviceEventService.startDeviceEventService(bbydata, keyvrandvsVo);//呼吸机呼吸事件同步指令
                            log.info("______________________-呼吸机呼吸事件同步指令________________________________");
                            if (bfalse == true) {
                                dataModel.setCommand(Codeutil.intcrc8ToByteArray((short) Const.Busines_08));
                            }
                            break;
                        case Const.Busines_09:
                            log.info("~~~~~~~~~~~~~Const.busines_09: " + sblegth);
                            //deviceBasisService.startDeviceBasisService(bdata,strobject);//设置治疗参数请求
                           *//* if (bfalse == true) {
                                dataModel.setCommand(Codeutil.intcrc8ToByteArray((short) Const.busines_09));
                            }*//*
                            break;
                        case Const.Busines_10:
                            log.info("~~~~~~~~~~~~~Const.busines_10: " + sblegth);
                            bfalse = deviceLogsService.startDeviceLogsService(bbydata, keyvrandvsVo);//呼吸机使用日志还同步指令
                            if (bfalse == true) {
                                log.info("______________________-呼吸机使用日志还同步指令________________________________");
                                dataModel.setCommand(Codeutil.intcrc8ToByteArray((short) Const.Busines_10));
                            }
                            break;
                        default:
                            // dataModel.setCommand(Codeutil.intcrc8ToByteArray((short)0x03));
                            break;

                    }
                    log.info("\n -------------Service ------------- MessageServiceImpl-----startDataModelService--------end---------------");
                    iIndex = 0;
                    bbuf.clone();
                    bfalse = false;
                    return dataModel;
               *//* }*//*
            } catch(Exception e){
                e.printStackTrace();
                e.getMessage();
            }
        }*/
        return null;
    }
}
