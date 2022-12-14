package org.jwebppy.platform.core.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

//AES(Advanced Encryption Standard)
public class AES256Cipher
{
	private static volatile AES256Cipher INSTANCE;

	final static String secretKey = "89736549875612354687596342597821"; //32bit
	static String IV = ""; // 16bit

	public static AES256Cipher getInstance()
	{
		if (INSTANCE == null)
		{
			synchronized (AES256Cipher.class)
			{
				if (INSTANCE == null)
				{
					INSTANCE = new AES256Cipher();
				}
			}
		}
		return INSTANCE;
	}

	private AES256Cipher()
	{
		IV = secretKey.substring(0, 16);
	}

	// 암호화
	public String encode(String str)
	{
		byte[] keyData = secretKey.getBytes();

		SecretKey secureKey = new SecretKeySpec(keyData, "AES");

		try
		{
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));

			byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));

			return new String(Base64.encodeBase64(encrypted));
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	// 복호화
	public String decode(String str)
	{
		byte[] keyData = secretKey.getBytes();

		SecretKey secureKey = new SecretKeySpec(keyData, "AES");

		try
		{
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));

			byte[] byteStr = Base64.decodeBase64(str.getBytes());

			return new String(c.doFinal(byteStr), "UTF-8");
		}
		catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	// 키생서
	public static byte[] generationAES256_KEY() throws NoSuchAlgorithmException
	{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(256);
		SecretKey key = kgen.generateKey();

		return key.getEncoded();
	}
}