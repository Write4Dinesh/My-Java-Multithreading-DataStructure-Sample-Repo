package com.binary.tree.example;

public class Main {

	public static void main(String[] args) {
		ShoppingCart oneItemCart =	new ShoppingCart(new String[] {"Item1"});
     DinBinarySearchTree billingQueue = new DinBinarySearchTree();
     billingQueue.add(new ShoppingCart(new String[] {"Item1","Item2","Item3","Item4","Item5","Item6","Item7","Item8","Item9"}));
     billingQueue.add(new ShoppingCart(new String[] {"Item1","Item2","Item3"}));
     billingQueue.add(oneItemCart);
     billingQueue.add(new ShoppingCart(new String[] {"Item1","Item2","Item3","Item4"}));
     billingQueue.add(new ShoppingCart(new String[] {"Item1","Item2"}));
     billingQueue.add(new ShoppingCart(new String[] {"Item1","Item2","Item3","Item4","Item5"}));
     billingQueue.add(new ShoppingCart(new String[] {"Item1","Item2","Item3","Item4","Item5","Item6"}));
     System.out.println("Min=" + billingQueue.getCartWithMinItems().getNumItems());
     System.out.println("Max=" + billingQueue.getCartWithMaxItems().getNumItems());
     System.out.println("oneItemCart parent=" + billingQueue.getParentCart(oneItemCart));
     ShoppingCart[] carts = billingQueue.getChildren(oneItemCart);
     System.out.println("oneItemCart child1=" + carts[0] + " ,child2=" +  carts[1]);
     //System.out.println("remove cart =" + billingQueue.removeCart(oneItemCart).getNumItems());
     billingQueue.treeTraverse();
   }
	
}
