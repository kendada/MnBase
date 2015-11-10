package cc.mnbase.disklru;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-10-23
 * Time: 15:22
 * Version 1.0
 */

public class MD5 {

    public static String hashKeyForDisk(String key){
        String cacheKey = null;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = byteToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String byteToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();

        for(int i=0; i< bytes.length; i++){
            String hex = Integer.toHexString(0XFF & bytes[i]);
            if(hex.length() == 1){
                sb.append('0');
            }
            sb.append(hex);
        }

        return sb.toString();
    }
}
