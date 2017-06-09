package com.wechat.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * 数据字典排序加密
 * 
 * @user GJ
 * @since 2016-12-7
 */
public class EncryptUtil {
    private static Logger log = Logger.getLogger(EncryptUtil.class);

    public static String dictionarySort(String... str) {
        int length = str.length;
        Arrays.sort(str);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(str[i]);
        }
        return sb.toString();
    }

    public static String sHA1Encode(String sourceString) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            resultString = byte2hexString(md.digest(sourceString.getBytes()));
        } catch (Exception ex) {
            log.error("SHA1Encode异常：", ex);
        }
        return resultString;
    }

    public static final String byte2hexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return buf.toString().toUpperCase();
    }

    /**
     * MurMurHash算法，是非加密HASH算法，性能很高，
     * 比传统的CRC32,MD5，SHA-1（这两个算法都是加密HASH算法，复杂度本身就很高，带来的性能上的损害也不可避免）
     * 等HASH算法要快很多，而且据说这个算法的碰撞率很低.
     * http://murmurhash.googlepages.com/
     */
    private static Long hashUrl(String key) {

        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }

    /**
     * 将长连接进行哈希后进行短连接化
     * 
     * @param url
     * @return
     */
    public static String shortUrl(String url, String timestamp) {
        // 可以自定义生成加密字符传前的混合 KEY
//        String key = "wuguowei";
        // 要使用生成 URL 的字符
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

        // 对传入网址进行 MD5 加密

        Long hashResult = hashUrl(url);
        String sHashResult = String.valueOf(hashResult);
        int hashLength = sHashResult.length();
        if (hashLength < 10) {
            for (int i = 0; i < (10 - hashLength); i++) {
                sHashResult += "0";
            }
        }
        int timeLength = timestamp.length();
        if (timeLength < 10) {
            for (int i = 0; i < (10 - timeLength); i++) {
                timestamp += "0";
            }
        }
        String hex = sHashResult.substring(0, 10) + timestamp;
        String result = "";
        for (int i = 0; i < 2; i++) {
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 10, i * 10 + 10);
            // 这里需要使用 long 型来转换，因为 Inteper .parseInt()只能处理 31 位 ,首位为符号位 ,否则会越界
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars += chars[(int) index];
                // 每次循环按位右移 5 位
                lHexLong = lHexLong >> 5;
            }

            // 把字符串存入对应索引的输出数组

            result += outChars;
        }
        return result;
    }

    /**
     * 用SHA1算法生成安全签名
     * 
     * @param token
     *            票据
     * @param timestamp
     *            时间戳
     * @param nonce
     *            随机字符串
     * @param encrypt
     *            密文
     * @return 安全签名
     */
    public static Object[] getSHA1(String token, String timestamp, String nonce, String encrypt) throws Exception {
        Object[] result = new Object[2];

        try {
            String[] array = new String[] { token, timestamp, nonce, encrypt };
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            result[0] = 0;
            result[1] = hexstr.toString();
            return result;
        } catch (Exception e) {
            log.error("SHA1算法生成安全签名异常", e);
            result[0] = -40003;
            result[1] = "";
            return result;
        }
    }
    
    /**
     * 兑奖加密串算法
     * 加密规则：md5加密【兑奖时间串后五位+openid前五位+validateSuccess前五位+五位的随机字符串】
     * @param timestamp 兑奖时间串
     * @param openid
     * @param entranceKey 固定validateSuccess
     * @param randomString 生成的随机串
     * @return
     */
    public static String toValidateKey(String timestamp,String openid,String entranceKey,String randomString){
    	StringBuffer sb = new StringBuffer();
    	String timestampNew = timestamp.substring(timestamp.length()-5);   	
    	String openidNew = openid.substring(0,5);
    	String entranceKeyNew = entranceKey.substring(0, 5);
    	try {
			sb.append(md5(timestampNew+openidNew+entranceKeyNew+randomString));
		} catch (Exception e) {
			log.error("兑奖加密安全签名异常", e);
		}
    	return sb.toString();
    }
    
    /**
	 * 生成MD5串
	 * @param str
	 * @return String MD5串
	 */
	public static String md5(String source) throws Exception {
		String s = source;
		if (s == null) {
			return "";
		} else {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(s.getBytes("utf-8"));
			byte[] bytes = md5.digest();
			
			char[] hexDigits = {
				'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'	
			};
			
			int l = bytes.length;
			char[] seq = new char[l*2];
			int k=0;
			for(int i=0; i<l; i++){
				byte b = bytes[i];
				seq[k++] = hexDigits[b>>>4&0xf];
				seq[k++] = hexDigits[b&0xf];
			}

			return new String(seq);
		}
	}
}
