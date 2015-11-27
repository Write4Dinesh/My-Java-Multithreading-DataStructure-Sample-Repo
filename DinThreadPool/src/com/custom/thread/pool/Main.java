package com.custom.thread.pool;

public class Main {
public static int j = 1;
	public static void main(String[] args) {
		
DinThreadPool pool = new DinThreadPool(5);
pool.start();

for(int i=0; i<10; i++){

	pool.addTask(new Runnable(){
		public void run(){
			System.out.println(Thread.currentThread().getName() + " Executed Task" + j++);
		}
	});	
}

try {
	Thread.sleep(5000);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
pool.quit();
	}

}
