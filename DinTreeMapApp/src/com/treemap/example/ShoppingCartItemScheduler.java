package com.treemap.example;

import java.util.TreeMap;

public class ShoppingCartItemScheduler {
private TreeMap<String, Integer> mCardQueue;

public ShoppingCartItemScheduler(){
	mCardQueue = new TreeMap<String, Integer>();
}

public boolean addCart(String name, int numOfItems){
	mCardQueue.put(name, numOfItems);
	return false;
}
}
