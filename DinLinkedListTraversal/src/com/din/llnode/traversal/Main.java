package com.din.llnode.traversal;

public class Main {

	public static void main(String[] args) {
		Node header = new Node();
		header.data = 0;
		populateList(header);
		//print(header);
		long start = System.currentTimeMillis();
   System.out.println("middle=" + getMiddleNode(header).data);
   long end = System.currentTimeMillis();	
   System.out.println("measure1:" + (long)(end - start) + " millis");

   start = System.currentTimeMillis();
	 System.out.println("middle2=" + getMiddleNode2(header).data);
	  end = System.currentTimeMillis();	
	  System.out.println("measure2:" + (long)(end - start) + " millis");
	}
	private static void print(Node header){
		Node temp = header.next;
		System.out.println("no.of.nodes = " + header.data);
	  while(temp!=null){
		  System.out.println("Node.data" + temp.data);
		  temp = temp.next;
	  }
	}
private static void populateList(Node header){
	Node first = new Node();
	first.data = 1;
	first.next = null;
	header.next = first;
	header.data = ++ header.data;
	
	for(int i = 2; i <= 111113; i++){
	Node temp = new Node();
		temp.data = i;
		temp.next = null;
		first.next = temp;
		first = temp;
		
		header.data = ++header.data;
		
	}
}
	private static Node getMiddleNode(Node header){
		Node temp = header.next;
		int index = 0;
		while(temp!=null){
			temp=temp.next;
			index++;
		}
		int n = index/2;
		 temp = header.next;
		for(int i = 0; i< n; i++){
		temp =temp.next;	
		}
		return temp;
	}
	private static Node getMiddleNode2(Node header){
		Node temp = header.next;
		int index = 0;
		Node middle = header;
		
		while(temp!=null){
			temp=temp.next;
			
			index++;
			if(index%2 !=0){
				middle = middle.next;
			}
		}
		
		return middle;
	}

}
