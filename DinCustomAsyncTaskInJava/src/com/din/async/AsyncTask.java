package com.din.async;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AsyncTask<Request, Result, Progress> {
	public boolean isBackGroundThreadWorking = true;
	private Result mResult;
	private BlockingQueue<Integer> mProgressQueue = null;
	private static int MAX_PROGRESS = 100;

	public AsyncTask() {
		mProgressQueue = new ArrayBlockingQueue<Integer>(MAX_PROGRESS);
	}

	public Result doInBackground(Request request) {
		isBackGroundThreadWorking = true;
		System.out.println("Background Thread working..");
		for (int i = 0; i < MAX_PROGRESS; i++) {
			mProgressQueue.add(i);
		}
		isBackGroundThreadWorking = false;
		mResult = (Result) "Out put data";
		return mResult;
	}

	public void onPostExecute(Result result) {
		System.out.println("Got the result in Main Thread:" + result);
	}

	public void execute(Request request) {
		BackGroundThread<Request, Result> bThread = new BackGroundThread<Request, Result>(this, request);
		bThread.start();
		try {
			// bThread.join();
			while (isBackGroundThreadWorking | !mProgressQueue.isEmpty()) {
				Progress progress = (Progress) mProgressQueue.take();
				onProgress(progress);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		onPostExecute((Result) bThread.getResult());

		System.out.println("Main Thread Exiting..");
	}

	public void onProgress(Progress progress) {
		System.out.println("Progress.." + progress);
		try {
			Runtime.getRuntime().exec("cls");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
