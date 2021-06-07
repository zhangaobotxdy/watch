package com.wrist.watch.utils;

import org.apache.commons.lang.ArrayUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class ByteUtil {
	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();
	}

	public static char[] getChars(byte[] bytes) {
		char[] res = new char[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			res[i] = (char) bytes[i];
		}
		return res;
	}

	public final static byte[] getBytes(short s, boolean asc) {
		byte[] buf = new byte[2];
		if (asc) {
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (s & 0x00ff);
				s >>= 8;
			}
		}else {
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (s & 0x00ff);
				s >>= 8;
			}
		}
		return buf;
	}

	public final static byte[] getBytes(int s, boolean asc) {
		byte[] buf = new byte[4];
		if (asc) {
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (s & 0x000000ff);
				s >>= 8;
			}
		}else {
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (s & 0x000000ff);
				s >>= 8;
			}
		}
		return buf;
	}

	public final static byte[] getBytes(long s, boolean asc) {
		byte[] buf = new byte[8];
		if (asc) {
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (s & 0x00000000000000ff);
				s >>= 8;
			}
		}
		else {
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (s & 0x00000000000000ff);
				s >>= 8;
			}
		}
		return buf;
	}

	public final static byte intToByte(int x) {  
	    return (byte) x;  
	}  
	
	public final static short getShort(byte[] buf, boolean asc) {
		if (buf == null) {
			throw new IllegalArgumentException("byte array is null!");
		}
		if (buf.length > 2) {
			throw new IllegalArgumentException("byte array size > 2 !");
		}
		short r = 0;
		if (asc) {
			for (int i = buf.length - 1; i >= 0; i--) {
				r <<= 8;
				r |= (buf[i] & 0x00ff);
			}
		}
		else {
			for (int i = 0; i < buf.length; i++) {
				r <<= 8;
				r |= (buf[i] & 0x00ff);
			}
		}
		return r;
	}

	public final static int getInt(byte[] buf, boolean asc) {
		if (buf == null) {
			throw new IllegalArgumentException("byte array is null!");
		}
		if (buf.length > 4) {
			throw new IllegalArgumentException("byte array size > 4 !");
		}
		int r = 0;
		if (asc){
			for (int i = buf.length - 1; i >= 0; i--) {
				r <<= 8;
				r |= (buf[i] & 0x000000ff);
			}
		}
		else {
			for (int i = 0; i < buf.length; i++) {
				r <<= 8;
				r |= (buf[i] & 0x000000ff);
			}
		}
		return r;
	}

	public final static long getLong(byte[] buf, boolean asc) {
		if (buf == null) {
			throw new IllegalArgumentException("byte array is null!");
		}
		if (buf.length > 8) {
			throw new IllegalArgumentException("byte array size > 8 !");
		}
		long r = 0;
		if (asc) {
			for (int i = buf.length - 1; i >= 0; i--) {
				r <<= 8;
				r |= (buf[i] & 0x00000000000000ff);
			}
		}
		else {
			for (int i = 0; i < buf.length; i++) {
				r <<= 8;
				r |= (buf[i] & 0x00000000000000ff);
			}
		}
		return r;
	}

	/**
	 * 
	* @Title. byteArrayToString
	* @Description. byte数组转为String
	* @param bytes
	* @return String
	* @exception.
	 */
	public final static String byteArrayToString(byte[] bytes){
		StringBuffer str = new StringBuffer();
		for(byte byt:bytes){
			str.append(String.format("%8s", Integer.toBinaryString(byt & 0xFF)).replace(' ', '0'));
		}
		return str.toString();
	}
	/**
	 * 
	 * @Title. shortToBytes
	 * @Description. short 转byte
	 * @param rec
	 * @return byte[]
	 * @exception.
	 */
	public final static byte[] shortToBytes(short rec) {
		byte[] res = new byte[2];
		res[0] = (byte) ((rec >> 8) & 255);
		res[1] = (byte) (rec & 255);
		return res;
	}

	/**
	 * 字符串格式为00000010101010.. 转为bit 再转为byte
	 */
	public final static byte[] strToBytes(String str) {
		byte[] res = {};
		if (null == str) {
			return res;
		}
		int length = str.length();
		if (length % 8 != 0) {
			return res;
		}

		int size = length / 8;
		for (int i = 1; i < size + 1; i++) {
			res = ArrayUtils.add(res,
					BitToByte(str.substring(i * 8 - 8, i * 8)));
		}
		return res;
	}

	/**
	 * Bit转Byte
	 */
	public static byte BitToByte(String byteStr) {
		int re, len;
		if (null == byteStr) {
			return 0;
		}
		len = byteStr.length();
		if (len != 4 && len != 8) {
			return 0;
		}
		if (len == 8) {// 8 bit处理
			if (byteStr.charAt(0) == '0') {// 正数
				re = Integer.parseInt(byteStr, 2);
			} else {// 负数
				re = Integer.parseInt(byteStr, 2) - 256;
			}
		} else {// 4 bit处理
			re = Integer.parseInt(byteStr, 2);
		}
		return (byte) re;
	}
	
	/**
	 * byte 转成 bit 字符串
	 * @param b
	 * @return
	 */
	public static String byteToBit(byte b) {  
		StringBuffer sb = new StringBuffer();
		sb.append((byte)((b >> 7) & 0x1))
		.append(((b >> 6) & 0x1))
		.append(((b >> 5) & 0x1))
		.append(((b >> 4) & 0x1))
		.append(((b >> 3) & 0x1))
		.append(((b >> 2) & 0x1))
		.append(((b >> 1) & 0x1))
		.append(((b >> 0) & 0x1));
		return sb.reverse().toString();
	}
	
	public final static short bytesToShort(byte[] bytes) {
		short res = 0;
		for(int i = bytes.length - 1; i >= 0; i--){
//			res |= (0xff << (i- bytes.length + 1) * 8) & (bytes[i] << (i- bytes.length + 1) * 8);
			res |= (0xff << (bytes.length- i - 1) * 8) & (bytes[i] << (bytes.length- i - 1) * 8);
		}
		return res;
	}
	
	public final static int bytesToInt(byte[] bytes) {
		int res = 0;
		for(int i = bytes.length - 1; i >= 0; i--){
//			res |= (0xff << (i- bytes.length + 1) * 8) & (bytes[i] << (i- bytes.length + 1) * 8);
			res |= (0xff << (bytes.length- i - 1) * 8) & (bytes[i] << (bytes.length- i - 1) * 8);
		}
		return res;
	}
	
	
}
