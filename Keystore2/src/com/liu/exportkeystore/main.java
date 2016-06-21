package com.liu.exportkeystore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;

import sun.misc.BASE64Encoder;
import sun.security.pkcs.PKCS8Key;

public class main {
	
	public static void main(String[] args) {
		
		ExportCertFormKeystore exportCertFormKeystore = new ExportCertFormKeystore();
		File directory = new File("");//设定为当前文件夹 
		for (int i = 0; i < 10; i++) {
			try {
				exportCertFormKeystore.genkey(directory.getCanonicalPath()+"li"+i);
				key2pk8(directory.getCanonicalPath()+"li"+i+".keystore","li","123456","123456");
			} catch (UnrecoverableKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	private static void key2pk8(String str1, String str2, String str3, String str4) throws KeyStoreException, IOException,
			NoSuchAlgorithmException, CertificateException,
			UnrecoverableKeyException, FileNotFoundException,
			CertificateEncodingException {
//		String str1 = paramArrayOfString[0].trim();
//		String str2 = paramArrayOfString[1].trim();
//		String str3 = paramArrayOfString[2].trim();
//		String str4 = paramArrayOfString[3].trim();
		File localFile1 = new File(str1);
		String str5 = localFile1.getAbsolutePath();
		File localFile2 = new File(str5);
		str5 = localFile2.getParent();

		String str6 = localFile1.getName().substring(0,
				localFile1.getName().lastIndexOf("."));

		KeyStore localKeyStore = KeyStore
				.getInstance(KeyStore.getDefaultType());
		FileInputStream localFileInputStream;
		try {
			localFileInputStream = new FileInputStream(localFile1);
		} catch (Exception localException) {
			System.out.println("打开keystore文件时出错！");
			return;
		}

		System.out.println("正在开始从keystore文件里提取公私钥内容....");
		localKeyStore.load(localFileInputStream, str3.toCharArray());

		BASE64Encoder localBASE64Encoder = new BASE64Encoder();

		Certificate localCertificate = localKeyStore.getCertificate(str2);
		Key localKey = localKeyStore.getKey(str2, str4.toCharArray());

		KeyPair localKeyPair = new KeyPair(localCertificate.getPublicKey(),
				(PrivateKey) localKey);
		System.out.println("正在写入" + str5 + ".x509.pem文件！");
		FileOutputStream localFileOutputStream1 = new FileOutputStream(str5
				+ "/" + str6 + ".x509.pem");
		localFileOutputStream1
				.write("-----BEGIN CERTIFICATE-----\n".getBytes());
		localFileOutputStream1.write(localBASE64Encoder.encode(
				localCertificate.getEncoded()).getBytes());
		localFileOutputStream1.write("\n".getBytes());
		localFileOutputStream1.write("-----END CERTIFICATE-----".getBytes());
		localFileOutputStream1.close();

		System.out.println("正在写入" + str5 + ".pk8文件！");
		PKCS8Key localPKCS8Key = (PKCS8Key) localKeyPair.getPrivate();
		FileOutputStream localFileOutputStream2 = new FileOutputStream(str5
				+ "/" + str6 + ".pk8");
		localFileOutputStream2.write(localKeyPair.getPrivate().getEncoded());
		localFileOutputStream2.close();
		System.out.println("转化完成！");
	}

	public void genkeyTest() {  
		//生成密钥测试  
		new ExportCertFormKeystore().genkey(null);  
	}  
	
	public void exportTest() {  
		//导出证书文件测试  
		new ExportCertFormKeystore().export(null);  
	}  
}
