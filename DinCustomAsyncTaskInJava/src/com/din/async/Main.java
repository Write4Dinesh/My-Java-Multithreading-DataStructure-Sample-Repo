package com.din.async;

public class Main {

	public static void main(String[] args) {
		AsyncTask<String,String,Integer> task = new AsyncTask<String,String,Integer>();
		task.execute("Input data");
	}

}
