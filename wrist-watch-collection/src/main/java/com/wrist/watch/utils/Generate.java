package com.wrist.watch.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName :         //类名
 * @Description :  四位随机 //描述
 * @Author Administrator -Earl
 * @Date 2020/10/22 10:43
 * @Version 1.0
 */
public class Generate {

    public static String generateSord()
    {
        String[] beforeShuffle =
                new String[] {"0","1", "2", "3", "4", "5", "6", "7","8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
                              "I", "J","K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W", "X", "Y", "Z" };
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(5, 9);
        return result;
    }
}
