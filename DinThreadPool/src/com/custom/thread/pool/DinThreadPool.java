package com.custom.thread.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DinThreadPool extends Thread {
	private BlockingQueue<Runnable> mTasksQueue = null;
	private int mPoolSize = 5;
	private boolean isRunning;
	private List<CustomThread> mThreadList = null;

	public DinThreadPool(int poolSize) {
		mTasksQueue = new ArrayBlockingQueue<Runnable>(20);
		mPoolSize = poolSize;
		mThreadList = new ArrayList<CustomThread>(mPoolSize);
	}

	public void quit() {
		System.out.println("Trying to Quit the Thread pool...");
		isRunning = false;
		for (CustomThread t : mThreadList) {
			t.interrupt();
		}
	}

	@Override
	public void run() {
		isRunning = true;
		for (int i = 0; i < mPoolSize; i++) {
			CustomThread t = new CustomThread();
			t.setName("thread-" + i);
			t.start();
			mThreadList.add(t);

		}

	}

	public void addTask(Runnable task) {
		mTasksQueue.add(task);

	}

	private Runnable getTask() {
		try {
			return mTasksQueue.take();
		} catch (InterruptedException e) {
			return null;
		}

	}

	private class CustomThread extends Thread {
		
		public CustomThread() {
			
		}

		public void run() {
		int numOfTaskExecuted = 0;
			while (isRunning) {
				Runnable r = getTask();
				if (r != null)
					r.run();++numOfTaskExecuted;
			}
			System.out.println(Thread.currentThread().getName() + " has Quit,no.of.tasks=" + numOfTaskExecuted);
		}
	}
}
