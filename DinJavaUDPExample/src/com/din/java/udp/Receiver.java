package com.din.java.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Receiver {
	private boolean isRunning;
	private DatagramPacket mUDPacket = null;
	private DatagramSocket mClientSocket = null;
	private byte[] buffer = null;
   
   private int mLocalPort;
	public Receiver(int localPort) {
		
		mLocalPort = localPort;
		try {
			mClientSocket = new DatagramSocket(mLocalPort);
		} catch (SocketException e) {
			
			e.printStackTrace();
		}
	}

public void start(){
	isRunning = true;
	buffer = new byte[MainUDPDemo.UDP_DATAGRAM_PACKET_SIZE];
	while(isRunning){
		mUDPacket = new DatagramPacket(buffer, buffer.length);

            try {
				mClientSocket.receive(mUDPacket);
				System.out.println("Client got data:" + new String(mUDPacket.getData()));
        	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
		
	}	
	
}
public void stop(){
	isRunning = false;// not guaranteed
}
}
