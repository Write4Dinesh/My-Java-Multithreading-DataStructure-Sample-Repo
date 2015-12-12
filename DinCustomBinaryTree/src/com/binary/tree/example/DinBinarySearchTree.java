package com.binary.tree.example;

import java.util.ArrayList;
import java.util.List;

public class DinBinarySearchTree {
	private Node mRoot;

	public DinBinarySearchTree() {
		mRoot = null;
	}

	public boolean add(ShoppingCart cart) {
		Node temp = new Node(cart);

		addItem(mRoot, temp);

		return true;
	}

	private void addItem(Node target, Node temp) {
		if (mRoot == null) {// In case of first node
			mRoot = temp;
			return;
		}
		if (temp.cart.getNumItems() > target.cart.getNumItems()) {
			if (target.right == null) {
				target.right = temp;
				return;
			}
			addItem(target.right, temp);// go to right sub-tree

		} else {
			if (target.left == null) {
				target.left = temp;
				return;
			}
			addItem(target.left, temp);// go to left sub-tree

		}

	}

	public ShoppingCart removeCart(ShoppingCart cart) {
		Node temp = remove(new Node(cart));
		return temp.cart;
	}

	private Node remove(Node temp) {
		temp = getNode(temp);
		Node parent = getParentNode(temp, mRoot, null);
		if (parent.left.equals(temp)) {
			parent.left = temp.left;
			temp.left = null;
		} else if (parent.right.equals(temp)) {
			parent.right = temp.right;
			temp.right = null;
		}
		return temp;
	}

	private Node getNode(Node temp) {
		Node parent = getParentNode(temp, mRoot, null);
		if (parent != null) {
			temp = temp.equals(parent.left) ? parent.left : parent.right;
		}
		return temp;
	}

	public ShoppingCart popLeastItemsCart() {

		return null;
	}

	public ShoppingCart getParentCart(ShoppingCart cart) {
		Node temp = new Node(cart);
		Node parent = getParentNode(temp, mRoot, null);
		return parent.cart;
	}

	private Node getParentNode(Node temp, Node root, Node parent) {
		if (root == null) {
			return null;
		}
		if (root.equals(temp)) {
			return parent;
		}
		parent = getParentNode(temp, root.right, root);
		if (parent != null)
			return parent;
		return getParentNode(temp, root.left, root);
	}

	public ShoppingCart[] getChildren(ShoppingCart parent) {
		Node[] children = getChildren(mRoot, new Node(parent), null);
		ShoppingCart[] childrenCarts = new ShoppingCart[children.length];
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null && children[i].cart != null)
				childrenCarts[i] = children[i].cart;
		}
		return childrenCarts;
	}

	private Node[] getChildren(Node root, Node temp, Node[] children) {
		if (root == null) {
			return null;
		}
		if (root.equals(temp)) {
			return new Node[] { root.left, root.right };
		}
		children = getChildren(root.right, temp, children);
		if (children != null)
			return children;
		return getChildren(root.left, temp, children);
	}

	public ShoppingCart getCartWithMinItems() {
		if (mRoot == null)
			return null;
		return getNodeWithMinItems(mRoot).cart;
	}

	private Node getNodeWithMinItems(Node first) {
		if (first.left == null)
			return first;
		return getNodeWithMinItems(first.left);

	}

	public ShoppingCart getCartWithMaxItems() {
		if (mRoot == null)
			return null;
		return getNodeWithMaxItems(mRoot).cart;
	}

	private Node getNodeWithMaxItems(Node first) {
		if (first.right == null)
			return first;
		return getNodeWithMaxItems(first.right);
	}

	public void treeTraverse() {
		List<Node> traverseList = new ArrayList<Node>();
 traverseList = preOrderTraverse(mRoot,traverseList);
 StringBuilder sb = new StringBuilder();
 for(Node node : traverseList){
	 sb.append(node.cart.getNumItems() + ",");
 }
 System.out.println("Pre-order:{" +sb.toString() + "}");
 traverseList.clear();
 traverseList = inOrderTraverse(mRoot,traverseList);
  sb = new StringBuilder();
 for(Node node : traverseList){
	 sb.append(node.cart.getNumItems() + ",");
 }
 System.out.println("In-order:{" +sb.toString() + "}");
 traverseList.clear();
 traverseList = postOrderTraverse(mRoot,traverseList);
  sb = new StringBuilder();
 for(Node node : traverseList){
	 sb.append(node.cart.getNumItems() + ",");
 }
 System.out.println("Post-order:{" +sb.toString() + "}");
	}
	
private List<Node> preOrderTraverse(Node root,List<Node> preList){
	if(root==null){
		return preList;
	}
	preList.add(root);
	preOrderTraverse(root.left,preList);
	preOrderTraverse(root.right,preList);
	return preList;
}
private List<Node> inOrderTraverse(Node root,List<Node> preList){
	if(root==null){
		return preList;
	}
	
	inOrderTraverse(root.left,preList);
	preList.add(root);
	inOrderTraverse(root.right,preList);
	return preList;
}
private List<Node> postOrderTraverse(Node root,List<Node> preList){
	if(root==null){
		return preList;
	}
	
	postOrderTraverse(root.left,preList);
	postOrderTraverse(root.right,preList);
	preList.add(root);
	return preList;
}
	public void printPost() {

	}

	private class Node {
		public Node left;
		public Node right;
		public ShoppingCart cart;

		public Node(ShoppingCart data) {
			this.cart = data;
			this.right = null;
			this.left = null;
		}

		public Node() {

		}

		@Override
		public boolean equals(Object node) {
			return this.cart.equals(((Node) node).cart);
		}

	}
}
