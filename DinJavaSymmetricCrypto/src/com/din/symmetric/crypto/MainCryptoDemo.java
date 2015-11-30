package com.din.symmetric.crypto;

public class MainCryptoDemo {

	 public static void main(String[] args) {
	        String encryptionKey = "dinesh@masthaiah"; // 128 bit key (16 bytes)
	        String initVector = "RandomInitVector"; // 16 bytes IV
	        DinCryptoAndSecurity cryptoAndSecurity = new DinCryptoAndSecurity();
	        
	        String sourceString = "Hello World";
	        System.out.println("sourceString:" + sourceString);
	       
	        String encrypted =  cryptoAndSecurity.encrypt(encryptionKey, initVector, sourceString);
	       System.out.println("encrypted string:" + encrypted);
	      
	       String decrypted = cryptoAndSecurity.decrypt("dinesh@masthaiah", initVector,encrypted);
	        System.out.println("decrypted string:" + decrypted);
	        
	        cryptoAndSecurity.processCipher();
	        cryptoAndSecurity.processDigitalSignature();
	        cryptoAndSecurity.processKeyAgreement();
	        cryptoAndSecurity.processKeyFactory();
	        cryptoAndSecurity.processKeyPairGenerator();
	       cryptoAndSecurity.processKeyStore();
	        cryptoAndSecurity.processMac();
	        cryptoAndSecurity.processMessageDigest();
	        cryptoAndSecurity.processSecretyKeyFactory();
	        cryptoAndSecurity.processCryptographyServiceProviders();
	    }

}
