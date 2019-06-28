package rx.webindexer.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Hasher {
	
	/**
	 * Метод хеширует пароль пользователя с использованием случайной соли. Возвращает соль (первые 16 байт) и 
	 * сам хеш пароля.
	 * @param password пароль, который необходимо хешировать
	 * @return  Возвращает соль (первые 16 байт) и 
	 * сам хеш пароля.
	 */
	static byte[] globSalt;
	public static byte[] hashPasswordRandom(String password)  {
		SecureRandom rand = new SecureRandom();
		byte[] salt =  new byte[16];
		rand.nextBytes(salt);
		byte[] hashedPassword = hashPassword(password, salt);
		//соль и хеш (соль+пароль) в одной строке
		byte[] result = new byte[salt.length + hashedPassword.length];
		System.arraycopy(salt, 0, result, 0, salt.length);
		System.arraycopy(hashedPassword, 0, result, salt.length, hashedPassword.length);
		return result;
		
		
	}
	
	/**
	 * Метод хеширует пароль пользователя с использованием указанной соли. Возвращает хеш пароля.
	 * @param password пароль, который необходимо хешировать
	 * @param salt соль, которая добавляется при хешировании пароля
	 * @return  Возвращает byte[] массив, представляющий собой хеш пароля.
	 */
	public static byte[] hashPassword(String password, byte[] salt)  {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			//исключения быть не должно, т.к мы используем стандартный алгоритм и он должен найтись
			e.printStackTrace();
		}
		//md.reset();
		md.update(salt);
		byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
		return hashedPassword;
	}
	
	/**
	 * Метод возвращает byte[] массив байтов, представляющий собой соль, использованную при хешировании пароля 
	 * пользователя.
	 * @param hashWSalt byte[] массив, представляющий собой соль и хеш пароля (соль записана в первых 16 байтах) 
	 * (первые 16 байт это соль)
	 * @return возвращает byte[] массив байтов, представляющий собой соль, использованную при хешировании пароля
	 */
	public static byte[] extractSalt(byte[] hashWSalt) {
		if(hashWSalt == null)
			return new byte[0];
		byte[] bytes = Arrays.copyOfRange(hashWSalt, 0, 16);
		return bytes;
	}
	
	
	/**
	 * Метод возвращает byte[] массив байтов, представляющий собой хеш пароля пользователя.
	 * @param hashWSalt byte[] массив, представляющий собой соль и хеш пароля (соль записана в первых 16 байтах) 
	 * (первые 16 байт это соль)
	 * @return возвращает хеш пароля пользователя
	 */
	public static byte[] extractHash(byte[] hashWSalt) {
		final int saltSize = 16;
		if(hashWSalt == null)
			return new byte[0];
		byte[] bytes = Arrays.copyOfRange(hashWSalt, saltSize, saltSize+64);
		return bytes;
	}
	

}
