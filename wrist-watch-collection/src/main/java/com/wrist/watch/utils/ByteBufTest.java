package com.wrist.watch.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

/**
 * @ClassName :     //类名
 * @Description :  //描述
 * @Author Administrator -Earl
 * @Date 2020/10/22 10:43
 * @Version 1.0
 */
@Slf4j
public class ByteBufTest {


    public static void main(String[] args) throws UnsupportedEncodingException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);

        log.info("&&&&&&&&&&&&&&&&&&&bytes:"+ByteBufUtil.hexDump(byteBuf).toUpperCase());
        byteBuf.writeInt(126);
        byteBuf.writeInt(127);
        byteBuf.writeBytes(new byte[]{(byte)0x07,(byte)0x03,(byte)0x09 });
        System.out.println("byteBuf readInt="+byteBuf.readInt());
        System.out.println("byteBuf readerIndex=" + byteBuf.readerIndex() + "| writeIndex=" + byteBuf.writerIndex() + "|capacity=" + byteBuf.capacity());
        //返回ByteBuf的可读字节的拷贝。修改返回的ByteBuf内容与当前ByteBuf完全不会相互影响。
        //此方法不会修改当前ByteBuf的readerIndex或writerIndex
        System.out.println("====================byteBuf.copy()==================");
        ByteBuf cByteBuf = byteBuf.copy();
        System.out.println("cByteBuf readerIndex=" + cByteBuf.readerIndex() + "| writeIndex=" + cByteBuf.writerIndex() + "|capacity=" + cByteBuf.capacity());
        System.out.println("byteBuf readerIndex=" + byteBuf.readerIndex() + "| writeIndex=" + byteBuf.writerIndex() + "|capacity=" + byteBuf.capacity());
        System.out.println("向cByteBuf中写入数据" + cByteBuf.setInt(0, 128));
        System.out.println("cByteBuf=" + cByteBuf.getInt(0) + "|cByteBuf=" + cByteBuf.getByte(4));
        System.out.println("byteBuf=" + byteBuf.getInt(0) + "|byteBuf=" + byteBuf.getInt(4) + "|byteBuf=" + byteBuf.getByte(8));
        //返回ByteBuf可读字节的一部分。 修改返回的ByteBuf或当前ByteBuf会影响彼此的内容，
        // 同时它们维护单独的索引和标记,此方法不会修改当前ByteBuf的readerIndex或writerIndex
        //另请注意，此方法不会调用{@link #retain（）}，因此不会增加引用计数。
        System.out.println("====================byteBuf.slice()==================");
        ByteBuf sByteBuf = byteBuf.slice();
        System.out.println("sByteBuf readerIndex=" + sByteBuf.readerIndex() + "| writeIndex=" + sByteBuf.writerIndex() + "|capacity=" + sByteBuf.capacity());
        System.out.println("byteBuf readerIndex=" + byteBuf.readerIndex() + "| writeIndex=" + byteBuf.writerIndex() + "|capacity=" + byteBuf.capacity());
        System.out.println("向sByteBuf中写入数据" + sByteBuf.setInt(0, 128));
        System.out.println("sByteBuf=" + sByteBuf.getInt(0) + "|sByteBuf=" + sByteBuf.getByte(4));
        System.out.println("byteBuf=" + byteBuf.getInt(0) + "|byteBuf=" + byteBuf.getInt(4) + "|byteBuf=" + byteBuf.getByte(8));
        //返回共享当前ByteBuf信息的新ByteBuf,他们使用独立的readIndex writeIndex markIndex
        //修改返回的ByteBuf或当前ByteBuf会影响彼此的内容，同时它们维护单独的索引和标记,
        // 此方法不会修改当前ByteBuf的readerIndex或writerIndex，
        //另请注意，此方法不会调用{@link #retain（）}，因此不会增加引用计数。
        System.out.println("====================byteBuf.duplicate()==================");
        ByteBuf dByteBuf = byteBuf.duplicate();
        System.out.println("dByteBuf readerIndex=" + dByteBuf.readerIndex() + "| writeIndex=" + dByteBuf.writerIndex() + "|capacity=" + dByteBuf.capacity());
        System.out.println("byteBuf readerIndex=" + byteBuf.readerIndex() + "| writeIndex=" + byteBuf.writerIndex() + "|capacity=" + byteBuf.capacity());
        System.out.println("向dByteBuf中写入数据" + dByteBuf.setInt(0, 100));
        System.out.println("dByteBuf=" + dByteBuf.getInt(0) + "|dByteBuf=" + dByteBuf.getInt(4) + "|dByteBuf=" + dByteBuf.getByte(8));
        System.out.println("byteBuf=" + byteBuf.getInt(0) + "|byteBuf=" + byteBuf.getInt(4) + "|byteBuf=" + byteBuf.getByte(8));
        dByteBuf.writeBytes(new byte[]{2});
        System.out.println("dByteBuf readerIndex=" + dByteBuf.readerIndex() + "| writeIndex=" + dByteBuf.writerIndex() + "|capacity=" + dByteBuf.capacity());
        System.out.println("byteBuf readerIndex=" + byteBuf.readerIndex() + "| writeIndex=" + byteBuf.writerIndex() + "|capacity=" + byteBuf.capacity());

        System.out.println("dByteBuf=" + dByteBuf.getInt(0) + "|dByteBuf=" + dByteBuf.getInt(4) + "|dByteBuf=" + dByteBuf.getByte(8) + "|dByteBuf=" + dByteBuf.getByte(9));
        System.out.println("byteBuf=" + byteBuf.getInt(0) + "|byteBuf=" + byteBuf.getInt(4) + "|byteBuf=" + byteBuf.getByte(8) + "|byteBuf=" + dByteBuf.getByte(9));
    }

}
