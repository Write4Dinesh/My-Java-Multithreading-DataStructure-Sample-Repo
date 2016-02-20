package com.din.symmetric.crypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
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
	private static final String CRYPTO_ALGORITHM = "AES";// Only AES and DES are
															// symmetric(secret
															// key) algorithms
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

	public void listCryptographicProviders() {
           beginDemo("List Crypto Service Provider");
		Provider[] providers = Security.getProviders();// List of CSP that
		for (Provider p : providers) {
			System.out.println(p.getName());
		}
		endDemo("List Crypto Service Provider");
	}

	public void demoMessageDigestForIntegrity() {// other algos that can be used
													// ex. SHA-1, SHA-256
		beginDemo("MessageDigest-DEMO");
		String theVeryBigMessage = "Its a very huge message with size 10MB";
		System.out.println("the message to be to sent server:\n" + theVeryBigMessage);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			System.out.println("Algorithm Used:" + md.getAlgorithm());
			System.out.println("Provider:" + md.getProvider());
			md.update(theVeryBigMessage.getBytes("UTF-8"));
			byte[] fingerPrint = md.digest();
			System.out.println("fingerPrint Calculated:" + new String(fingerPrint, "UTF-8"));
			// Send to server by encrypting the finger-print
			// Verify at server side by regenerating the finger-print
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		endDemo("MessageDigest-DEMO");
	}

	private KeyStore loadKeyStoreFromFile(String path, char[] password) {
		InputStream streamToCertFile;
		KeyStore myKeyStore = null;
		try {
			streamToCertFile = new FileInputStream(path);
			myKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());// we
																			// can
																			// give
																			// our
																			// keystore
																			// file
																			// path
																			// here
			myKeyStore.load(streamToCertFile, password);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			for (int i = 0; i < password.length; i++) {
				password[i] = 0;
			}
		}

		return myKeyStore;
	}

	private Certificate extractCertificateFromKeyStore(String certAlias, KeyStore keyStore) {
		Certificate cert = null;
		try {
			if (keyStore.containsAlias(certAlias)) {
				cert = keyStore.getCertificate(certAlias);
			}
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cert;
	}

	private byte[] encrypt(String message, String transformation, PublicKey pubKey) {
		byte[] encrypted = null;
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			encrypted = cipher.doFinal(message.getBytes());
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encrypted;
	}

	private byte[] decrypt(byte[] encrypted, String transformation, PrivateKey privateKey) {
		byte[] decrypted = null;

		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			decrypted = cipher.doFinal(encrypted);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return decrypted;
	}

	public PublicKey loadJavaKeystoreAndExtractKeys() {// storage for certs and
														// keys
		beginDemo("LoadJavaKeyStoreFileAndCertsAndExtractKeys");
		String myKeyStoreFilePath = "certs/dinKeyStore.jks";
		char[] certPassword = "dincertpass".toCharArray();
		String certificateAlias = "sampleAlias";
		char[] keyStorePassword = "dinStorePassword".toCharArray();
		Certificate selfSignedCertificate = null;
		PrivateKey privateKey = null;
		PublicKey myPublicKey = null;
		KeyStore myKeyStore = null;
		try {

			try {
				myKeyStore = loadKeyStoreFromFile(myKeyStoreFilePath, keyStorePassword);
				privateKey = (PrivateKey) myKeyStore.getKey(certificateAlias, certPassword);
				selfSignedCertificate = extractCertificateFromKeyStore(certificateAlias, myKeyStore);
				myPublicKey = selfSignedCertificate.getPublicKey();
				byte[] encrypted = encrypt("What an idea sirjee", "RSA/ECB/PKCS1Padding", myPublicKey);
				// send the encrypted data to server along with the certificate
				byte[] decrypted = decrypt(encrypted, "RSA/ECB/PKCS1Padding", privateKey);
				System.out.println("THE DECRYPTED STRING =" + new String(decrypted));

				

			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				e.printStackTrace();
			}

			System.out.println("KeyStore type:" + myKeyStore.getType());
			System.out.println("KeyStore Provider:" + myKeyStore.getProvider());
			// java.security.cert.Certificate certificate =
			// store.getCertificate("");
			// Certificate is an identity of a person/group/company/corporation
			// that is given others for authenticity
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return myPublicKey;
	}
	public void configureKeyManagerForSSLHandshake(){
		String myKeyStoreFilePath = "certs/dinKeyStore.jks";
		char[] certPassword = "dincertpass".toCharArray();
		char[] keyStorePassword = "dinStorePassword".toCharArray();
		
		System.out.println(
				"Put Keystore into Manager so that it is used by manager to authenticate the local computer with its peer");
		KeyStore myKeyStore = loadKeyStoreFromFile(myKeyStoreFilePath, keyStorePassword);
		KeyManagerFactory kMgrFactory;
		try {
			kMgrFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kMgrFactory.init(myKeyStore, certPassword);
			KeyManager[] kMgrs = kMgrFactory.getKeyManagers();
		} catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void processDigitalSignature() {// sign data/doc using PrivateKey &
											// verify using PublicKey
		try {
			Signature digitalSignature = Signature.getInstance("NONEwithRSA");
			System.out.println("Signature Algo:" + digitalSignature.getAlgorithm());
			System.out.println("Signature Provider:" + digitalSignature.getProvider());

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processSecretyKeyFactory() {// The factory to create & manage
											// SecretKey for Symmetric
											// encryption algorithms
		// In Asymmetric algorithms, we use 2 keys called private key and public
		// key.
		try {
			SecretKeyFactory sKeyFactory = SecretKeyFactory.getInstance("DES");
			System.out.println("SecretKeyFactory Algo:" + sKeyFactory.getAlgorithm());
			System.out.println("SecretKeyFactory Provider:" + sKeyFactory.getProvider());

			// SecretKey key = generateSecretKey("my password".toCharArray());
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

	public void generateKeysFromKeyPairGenerator() {
		   beginDemo("GenerateKeyPairsFrom KeyPairGenerator");
		try {
			KeyPairGenerator kPGenerator = KeyPairGenerator.getInstance("RSA");
			System.out.println("KeyPairGenerator Algo:" + kPGenerator.getAlgorithm());
			System.out.println("KeyPairGenerator Provider:" + kPGenerator.getProvider());
			KeyPair kPair = kPGenerator.generateKeyPair();
			PrivateKey privateKey = kPair.getPrivate();
			PublicKey publicKey = kPair.getPublic();
			System.out.println("PrivateKey Format:" + privateKey.getFormat());
			byte[] encrypted = encrypt("God is Great", "RSA/ECB/PKCS1Padding", publicKey);
			byte[] decrypted = decrypt(encrypted,"RSA/ECB/PKCS1Padding", privateKey);
			System.out.println("The decrypted string is =" + new String(decrypted,"UTF-8"));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		endDemo("GenerateKeyPairsFrom KeyPairGenerator");
	}

	public void demoMacForIntegrity() {// Message Authentication Code(MAC):
										// message digest with a secret key
		beginDemo("MAC-DEMO");
		try {
			// Mac is generally used to verify the integrity of the data sent
			// over network
			// the signature is calculated by using MAC at server side and is
			// encrypted using Key
			// at other side, cliet side, the same process is reversed ...
			Mac mac = Mac.getInstance("HmacMD5");// Mac by Hashing
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
			SecretKey secretKey = keyGen.generateKey();
			mac.init(secretKey);
			String message = "Message Authentication Code(Mac) = MessageDigest + secretKey";
			mac.update(message.getBytes());
			byte[] fingerPrint = mac.doFinal();
			// Verifying side

			System.out.println("finger Print calculated my MAC=\n" + new String(fingerPrint, "UTF-8"));
		} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		endDemo("MAC-DEMO");
	}

	public SecretKey generateSecretKey(char[] password) {
		PBEKeySpec pbeKeySpec = new PBEKeySpec(password, "saltysaltsaltsalt".getBytes(), 1000);
		SecretKeyFactory keyFactory;
		SecretKey secretKey = null;

		// SecretKey Creation that is used to perform Symmetric cryptography
		// operation
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

	public PrivateKey loadCertAndGetPrivateKey() {// storage for certs and keys
		String certificatePath = "certs/dinKeyStore.jks";
		String certPassword = "dincertpass";
		String alias = "sampleAlias";
		String keyStorePassword = "dinStorePassword";
		Certificate selfSignedCertificate = null;
		PrivateKey privateKey = null;
		PublicKey myPublicKey = null;
		try {
			KeyStore myKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream streamToCertFile = new FileInputStream(certificatePath);
			try {
				myKeyStore.load(streamToCertFile, keyStorePassword.toCharArray());
				if (myKeyStore.containsAlias(alias)) {
					selfSignedCertificate = myKeyStore.getCertificate(alias);
					privateKey = (PrivateKey) myKeyStore.getKey(alias, certPassword.toCharArray());

				}
				myPublicKey = selfSignedCertificate.getPublicKey();
				KeyPair pair = new KeyPair(myPublicKey, privateKey);
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.ENCRYPT_MODE, myPublicKey);
				String payload = "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";
				byte[] encrypted = cipher.doFinal("What an idea sirjee".getBytes());
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				byte[] decrypted = cipher.doFinal(Base64.decodeBase64(payload));
				System.out.println("THE DECRYPTED STRING =" + new String(decrypted));

				/*
				 * 
				 * System.out.println("public key=" + myPublicKey);
				 * KeyManagerFactory kMgrFactory =
				 * KeyManagerFactory.getInstance(KeyManagerFactory.
				 * getDefaultAlgorithm()); kMgrFactory.init(myKeyStore,
				 * certPassword.toCharArray()); KeyManager[] kMgrs =
				 * kMgrFactory.getKeyManagers();
				 */

			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
			} catch (InvalidKeyException e) {
			} catch (BadPaddingException e) {
			} catch (IllegalBlockSizeException e) {
			} catch (UnrecoverableKeyException e) {
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
			// java.security.cert.Certificate certificate =
			// store.getCertificate("");
			// Certificate is an identity of a person/group/company/corporation
			// that is given others for authenticity
		} catch (KeyStoreException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return privateKey;
	}

	public void demoPubKeyEncryption() {
		try {
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
			KeyPair keyPair = keyGenerator.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
			byte[] pubKeyBytes = publicKey.getEncoded();
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			byte[] message = "I Love Cryptography".getBytes();
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encrypted = cipher.doFinal(message);
			System.out.println("encrypted:\n" + new String(encrypted, "UTF-8"));
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decrypted = cipher.doFinal(encrypted);
			System.out.println("decrypte:\n" + new String(decrypted, "UTF-8"));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void beginDemo(String title) {
		System.out.println("*************************** START-" + title + "****************************");
	}

	private void endDemo(String title) {
		System.out.println("*************************** END-" + title + "****************************");
	}
}
