package com.din.java.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Sender {
	private DatagramPacket mUDPacket = null;
	private DatagramSocket mServerSocket = null;
	private byte[] serverBuffer = null;
	private String mRemoteHost = null;
	   private int mRemotePort;
	   private int mLocalPort;
	public Sender( int localPort,int remotePort) {
		mRemotePort = remotePort;
		mLocalPort = localPort;
		try {
			mServerSocket = new DatagramSocket(remotePort);// bind
																		// itself
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		try {
			while (!("QUIT".equals(line = reader.readLine()))) {

				serverBuffer = line.getBytes();
				mUDPacket = new DatagramPacket(serverBuffer, serverBuffer.length, InetAddress.getLocalHost(),
						mRemotePort);

				mServerSocket.send(mUDPacket);
				System.out.println("Client got data:" + new String(mUDPacket.getData()));

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
