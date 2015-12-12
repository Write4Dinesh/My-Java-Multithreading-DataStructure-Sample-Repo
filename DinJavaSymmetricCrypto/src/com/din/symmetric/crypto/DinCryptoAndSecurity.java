package com.din.symmetric.crypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.security.cert.Certificate;

import org.apache.commons.codec.binary.Base64;

public class DinCryptoAndSecurity {
	private static final String CRYPTO_ALGORITHM = "AES";
	private static final String CRYPTO_MODE = "CBC";
	private static final String CRYPTO_PADDING = "PKCS5PADDING";

	public String encrypt(String key, String initVector, String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), CRYPTO_ALGORITHM);
			String transformation = CRYPTO_ALGORITHM + "/" + CRYPTO_MODE + "/" + CRYPTO_PADDING;
			Cipher cipher = Cipher.getInstance(transformation);// algorithm/mode/padding"
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			/*
			 * System.out.println("encrypted string: " +
			 * Base64.encodeBase64String(encrypted));
			 */

			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public String decrypt(String key, String initVector, String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public void processCryptographyServiceProviders() {
		Provider[] providers = Security.getProviders();// List of CSP that
														// implements SPI
		System.out.println("****List Of Providers******");

		for (Provider p : providers) {

			System.out.println(p.getName());
		}
		System.out.println("**********************");
	}

	public void processMessageDigest() {// ex. SHA-1, SHA-256
		 String theHugeMessage = "message that is sent of size 10MB";
		 
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			System.out.println("MessageDigest Algo:" + md.getAlgorithm());
			System.out.println("MessageDigest Provider:" + md.getProvider());
			md.update(theHugeMessage.getBytes());
			byte[] digiSignature = md.digest();
			
			//sendMessageToServer(theHugeMessage, encryptedDigitalSignature);
			// the Messsage Digest is useful as we no need to encrypt entire Huge message
			System.out.println();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processKeyStore() {// storage for certs and keys
		String certificatePath = "certs/dinKeyStore.jks";
		String certPassword = "dincertpass";
		String alias = "sampleAlias";
		String keyStorePassword = "dinStorePassword";
		Certificate selfSignedCertificate = null;
		try {
			KeyStore myKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream streamToCertFile = new FileInputStream(certificatePath); 
			try {
				myKeyStore.load(streamToCertFile, keyStorePassword.toCharArray());
				if(myKeyStore.containsAlias(alias)){
				 selfSignedCertificate = myKeyStore.getCertificate(alias);
				}
				PublicKey myPublicKey = selfSignedCertificate.getPublicKey();
				System.out.println("public key=" + myPublicKey);
				KeyManagerFactory kMgrFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kMgrFactory.init(myKeyStore, certPassword.toCharArray());
				KeyManager[] kMgrs = kMgrFactory.getKeyManagers();
				
			}catch(UnrecoverableKeyException e){
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("KeyStore type:" + myKeyStore.getType());
			System.out.println("KeyStore Provider:" + myKeyStore.getProvider());
			//java.security.cert.Certificate certificate = store.getCertificate("");
			// Certificate is an identity of a person/group/company/corporation that is given others for authenticity
		} catch (KeyStoreException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processCipher() {// deals with Encryption/Dycription
		try {
			Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM + "/" + CRYPTO_MODE + "/" + CRYPTO_PADDING);
			System.out.println("Cipher Algo:" + cipher.getAlgorithm());
			System.out.println("Cipher Provider:" + cipher.getProvider());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processDigitalSignature() {// sign data/doc using PrivateKey & verify using PublicKey
		try {
			Signature digitalSignature = Signature.getInstance("NONEwithRSA");
			System.out.println("Signature Algo:" + digitalSignature.getAlgorithm());
			System.out.println("Signature Provider:" + digitalSignature.getProvider());
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processSecretyKeyFactory() {// The factory to create & manage SecretKey for Symmetric encryption algorithms
		// In Asymmetric algorithms, we use 2 keys called private key and public key.
		try {
			SecretKeyFactory sKeyFactory = SecretKeyFactory.getInstance("DES");
			System.out.println("SecretKeyFactory Algo:" + sKeyFactory.getAlgorithm());
			System.out.println("SecretKeyFactory Provider:" + sKeyFactory.getProvider());
			
			//SecretKey key = generateSecretKey("my password".toCharArray());
			// use this key to for symmetric crypto operations
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void processKeyAgreement() {// Exchange secrete keys between parties
		try {
			KeyAgreement kAgreement = KeyAgreement.getInstance("DiffieHellman");
			System.out.println("KeyAgreement Algo:" + kAgreement.getAlgorithm());
			System.out.println("KeyAgreement Provider:" + kAgreement.getProvider());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processKeyFactory() {// converts keys into key specs
		try {
			KeyFactory kFactory = KeyFactory.getInstance("DSA");
			System.out.println("KeyFactory Algo:" + kFactory.getAlgorithm());
			System.out.println("KeyFactory Provider:" + kFactory.getProvider());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processKeyPairGenerator() {// Used to generate public and private key pairs
		try {
			KeyPairGenerator kPGenerator = KeyPairGenerator.getInstance("DSA");
			System.out.println("KeyPairGenerator Algo:" + kPGenerator.getAlgorithm());
			System.out.println("KeyPairGenerator Provider:" + kPGenerator.getProvider());
			KeyPair kPair = kPGenerator.generateKeyPair();
			PrivateKey privateKey = kPair.getPrivate();
			PublicKey publicKey = kPair.getPublic();
			System.out.println("PrivateKey Format:" + privateKey.getFormat());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processMac() {// Message Authentication Code(MAC)
		try {
			//Mac is generally used to verify the integrity of the data sent over network
			// the signature is calculated by using MAC at server side and is encrypted using Key
			// at other side, cliet side, the same process is reversed ... 
			Mac mac = Mac.getInstance("HmacMD5");//Mac by Hashing 
			System.out.println("Mac Algo:" + mac.getAlgorithm());
			System.out.println("Mac Provider:" + mac.getProvider());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  SecretKey generateSecretKey(char[] password) {
		PBEKeySpec pbeKeySpec = new PBEKeySpec(password, "saltysaltsaltsalt".getBytes(), 1000);
		SecretKeyFactory keyFactory;
		SecretKey secretKey = null;

		// SecretKey Creation that is used to perform Symmetric cryptography operation
		try {
			keyFactory = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
			SecretKey tempKey = keyFactory.generateSecret(pbeKeySpec);
			secretKey = new SecretKeySpec(tempKey.getEncoded(), "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		// Zero out sensative Information
		for (int i = 0; i < password.length; i++) {
			password[i] = 0;

		}
		return secretKey;
	}
}
