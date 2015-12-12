package com.binary.tree.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoppingCart {
private List<String> mItems = new ArrayList<String>();
private int cartId;
public ShoppingCart(List<String> items){
	if(items!=null)
	mItems = items;
}
public ShoppingCart(String[] items){
	if(items!=null)
	mItems = Arrays.asList(items);
}
public ShoppingCart(){
	
}
public void addItem(String name){
	mItems.add(name);
}
public void removeItem(String name){
	if(mItems.contains(name)) mItems.remove(name);
}
public int getNumItems(){
	return mItems.size();
}
@Override
public boolean equals(Object obj) {
	
	return this.getNumItems()== ((ShoppingCart)obj).getNumItems();
}
@Override
public int hashCode() {
	return super.hashCode();
}
@Override
public String toString() {
	return "CART_" + this.getNumItems();
}
}
