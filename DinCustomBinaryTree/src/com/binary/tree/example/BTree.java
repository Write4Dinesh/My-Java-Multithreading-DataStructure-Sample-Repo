package com.binary.tree.example;

public class BTree {
	Node mRoot;

	public boolean add(int data) {

		if (mRoot == null) {

			mRoot = new Node(data);

			return true;

		} else

			return mRoot.add(data);

	}

	class Node {
		private int mData;
		private Node mLeft;
		private Node mRight;

		public Node(int data) {
			mData = data;
		}

		public boolean add(int data) {

			if (data == this.mData)

				return false;

			else if (data < this.mData) {

				if (mLeft == null) {

					mLeft = new Node(data);

					return true;

				} else

					return mLeft.add(data);

			} else if (data > this.mData) {

				if (mRight == null) {

					mRight = new Node(data);

					return true;

				} else

					return mRight.add(data);

			}

			return false;

		}
	}
}
