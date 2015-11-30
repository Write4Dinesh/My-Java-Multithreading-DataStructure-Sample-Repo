package com.din.java.udp;

public class MainUDPDemo {
	public static int UDP_DATAGRAM_PACKET_SIZE = 100;
	public static void main(String[] args) {
		int listeningPort;//send port at other side
		int sendLocalPort;//independent
		int sendRemotPort;// listening port at other side
		
		if(args.length==3){
		 sendLocalPort = Integer.parseInt(args[0]);
		 sendRemotPort = Integer.parseInt(args[1]);
		 listeningPort = Integer.parseInt(args[2]);
		Receiver receiver = new Receiver(listeningPort);
		Thread peerBThread = new Thread() {

			public void run() {
				receiver.start();
			}

		};
		peerBThread.start();
		new Sender(sendLocalPort,sendRemotPort).start();
	}
	
	else{
	System.out.println("No port or host mentioned");	
	}
}
}
