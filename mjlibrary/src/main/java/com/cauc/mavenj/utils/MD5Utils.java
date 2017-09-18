package com.cauc.mavenj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	private MD5Utils() {
	}

	public static String getMD5(String content) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(content.getBytes());
			return getHashString(digest);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

	/**
	 * 获取字符串的 MD5
	 */
	public static String encode(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes("UTF-8"));
			byte messageDigest[] = md5.digest();
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) {
				hexString.append(String.format("%02X", b));
			}
			return hexString.toString().toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取文件的 MD5
	 */
	public static String encode(File file) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			FileInputStream inputStream = new FileInputStream(file);
			DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
			//必须把文件读取完毕才能拿到md5
			byte[] buffer = new byte[4096];
			while (digestInputStream.read(buffer) > -1) {
			}
			MessageDigest digest = digestInputStream.getMessageDigest();
			digestInputStream.close();
			byte[] md5 = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : md5) {
				sb.append(String.format("%02X", b));
			}
			return sb.toString().toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
