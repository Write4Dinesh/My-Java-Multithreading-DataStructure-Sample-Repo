package com.din.string.split;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StringMain {

	public static void main(String[] args) {
		// creatFile();
		stringTest();

	}

	public static void stringTest() {
		String delimiter = "\\s";
		String[] stringTokens = null;
		long startTime = System.currentTimeMillis();
		String fileContent = getStringFromFile("data.txt");
		System.out.println("time taken = " + (System.currentTimeMillis() -startTime) + " millis");
		if (fileContent != null && fileContent.length() > 0) {
			stringTokens = fileContent.split(delimiter);
			for (int i = 0; i < stringTokens.length; i++) {
				System.out.println(">" + stringTokens[i] + "<");
			}
		}

	}

	private static String getStringFromFile(String pathOfFileWithName) {
		int bytesRead = -1;
		int chunkSize = 10;
		byte[] rawData = null;
		int totalByteRead = 0;
		int fileSizeInBytes = -1;
		long oneMB = 1024 * 1024;
		FileInputStream fis = null;
		try {
			 
			 fis = new FileInputStream(pathOfFileWithName);
			fileSizeInBytes = fis.available();
			System.out.println("file size = " + fileSizeInBytes + " bytes");
			if (fileSizeInBytes > oneMB) {
				System.out.println("file size is greater than 1MB!! You may run into Memory Issue");
			}
			rawData = new byte[fileSizeInBytes + chunkSize];
			while ((bytesRead = fis.read(rawData, totalByteRead, chunkSize)) != -1) {
				totalByteRead = totalByteRead + bytesRead;
			}
		}catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
		if(fis!=null)
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new String(rawData);
	}

}
