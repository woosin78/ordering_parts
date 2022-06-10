package org.jwebppy.platform.mgmt.sso.uitils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherSecurity {
	public String key1 = "2376324786a76c76762e623982348273";
	public String key2 = "49590348278320345789354348973498";
	public String sp = "#@X@#";

	/*
	 * public static void main(String args[]) throws Exception { String aaa =
	 * getEncryptDoobiz("korea","kim","system"); System.out.println(aaa); String
	 * bbb[] = getDecryptDoobiz(aaa);
	 * System.out.println(bbb[0]+" "+bbb[1]+" "+bbb[2]); }
	 */
	public String getEncryptDoobiz(String id, String pw, String system) throws Exception {
		String str = id + sp + pw + sp + system;
		return getEncrypt(str);
	}

	public String[] getDecryptDoobiz(String str) throws Exception {
		String decrypt = getDecrypt(str);
		return decrypt.split(sp);
	}

	public String getEncrypt(String str) throws Exception {
		Key key = generateKey("AES", toBytes(key1, 16));
		byte[] iv = toBytes(key2, 16);
		String transformation = "AES/CBC/PKCS5Padding";

		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

		byte[] plain = str.getBytes();
		byte[] encrypt = cipher.doFinal(plain);

		return toHexString(encrypt);
	}

	public String getDecrypt(String str) throws Exception {
		Key key = generateKey("AES", toBytes(key1, 16));
		byte[] iv = toBytes(key2, 16);
		String transformation = "AES/CBC/PKCS5Padding";

		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

		byte[] encrypt = toBytesFromHexString(str);

		cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
		byte[] decrypt = cipher.doFinal(encrypt);

		return byteToString(decrypt);
	}

	public Key generateKey(String algorithm) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
		SecretKey key = keyGenerator.generateKey();
		return key;
	}

	public Key generateKey(String algorithm, byte[] keyData)
			throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
		String upper = algorithm.toUpperCase();
		if ("DES".equals(upper)) {
			KeySpec keySpec = new DESKeySpec(keyData);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;
		} else if ("DESede".equals(upper) || "TripleDES".equals(upper)) {
			KeySpec keySpec = new DESedeKeySpec(keyData);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;
		} else {
			SecretKeySpec keySpec = new SecretKeySpec(keyData, algorithm);
			return keySpec;
		}
	}

	public String byteToString(byte[] sVal) {
		String str = new String(sVal);
		str.toString();
		return str;
	}

	public Byte DEFAULT_BYTE = new Byte((byte) 0);

	public byte toByte(String value, byte defaultValue) {
		try {
			return Byte.parseByte(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Byte toByteObject(String value, Byte defaultValue) {
		try {
			return new Byte(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public int unsignedByte(byte b) {
		return b & 0xFF;
	}

	public int toInt(byte[] src, int srcPos) {
		int dword = 0;
		for (int i = 0; i < 4; i++) {
			dword = (dword << 8) + (src[i + srcPos] & 0xFF);
		}
		return dword;
	}

	public int toInt(byte[] src) {
		return toInt(src, 0);
	}

	public long toLong(byte[] src, int srcPos) {
		long qword = 0;
		for (int i = 0; i < 8; i++) {
			qword = (qword << 8) + (src[i + srcPos] & 0xFF);
		}
		return qword;
	}

	public long toLong(byte[] src) {
		return toLong(src, 0);
	}

	public void toBytes(int value, byte[] dest, int destPos) {
		for (int i = 0; i < 4; i++) {
			dest[i + destPos] = (byte) (value >> ((7 - i) * 8));
		}
	}

	public byte[] toBytes(int value) {
		byte[] dest = new byte[4];
		toBytes(value, dest, 0);
		return dest;
	}

	public void toBytes(long value, byte[] dest, int destPos) {
		for (int i = 0; i < 8; i++) {
			dest[i + destPos] = (byte) (value >> ((7 - i) * 8));
		}
	}

	public byte[] toBytes(long value) {
		byte[] dest = new byte[8];
		toBytes(value, dest, 0);
		return dest;
	}

	public byte[] toBytes(String digits, int radix) throws IllegalArgumentException, NumberFormatException {
		if (digits == null) {
			return null;
		}
		if (radix != 16 && radix != 10 && radix != 8) {
			throw new IllegalArgumentException("For input radix: \"" + radix + "\"");
		}
		int divLen = (radix == 16) ? 2 : 3;
		int length = digits.length();
		if (length % divLen == 1) {
			throw new IllegalArgumentException("For input string: \"" + digits + "\"");
		}
		length = length / divLen;
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			int index = i * divLen;
			bytes[i] = (byte) (Short.parseShort(digits.substring(index, index + divLen), radix));
		}
		return bytes;
	}

	public byte[] toBytesFromHexString(String digits) throws IllegalArgumentException, NumberFormatException {
		if (digits == null) {
			return null;
		}
		int length = digits.length();
		if (length % 2 == 1) {
			throw new IllegalArgumentException("For input string: \"" + digits + "\"");
		}
		length = length / 2;
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			int index = i * 2;
			bytes[i] = (byte) (Short.parseShort(digits.substring(index, index + 2), 16));
		}
		return bytes;
	}

	public String toHexString(byte b) {
		StringBuffer result = new StringBuffer(3);
		result.append(Integer.toString((b & 0xF0) >> 4, 16));
		result.append(Integer.toString(b & 0x0F, 16));
		return result.toString();
	}

	public String toHexString(byte[] bytes) {
		if (bytes == null) {
			return null;
		}

		StringBuffer result = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			result.append(Integer.toString((b & 0xF0) >> 4, 16));
			result.append(Integer.toString(b & 0x0F, 16));
		}
		return result.toString();
	}

	public String toHexString(byte[] bytes, int offset, int length) {
		if (bytes == null) {
			return null;
		}

		StringBuffer result = new StringBuffer();
		for (int i = offset; i < offset + length; i++) {
			result.append(Integer.toString((bytes[i] & 0xF0) >> 4, 16));
			result.append(Integer.toString(bytes[i] & 0x0F, 16));
		}
		return result.toString();
	}

	public boolean equals(byte[] array1, byte[] array2) {
		if (array1 == array2) {
			return true;
		}

		if (array1 == null || array2 == null) {
			return false;
		}

		if (array1.length != array2.length) {
			return false;
		}

		for (int i = 0; i < array1.length; i++) {
			if (array1[i] != array2[i]) {
				return false;
			}
		}

		return true;
	}
}
