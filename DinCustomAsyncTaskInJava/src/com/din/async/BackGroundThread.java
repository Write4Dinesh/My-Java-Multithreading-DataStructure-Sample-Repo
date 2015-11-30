package com.din.async;

public class BackGroundThread<Request, Result> extends Thread {
	private AsyncTask<Request, ?, ?> mAsyncTask = null;
	private Request mRequest;
	private Result mResult;

	public BackGroundThread(AsyncTask<Request, ?, ?> asyncTask, Request request) {
		mAsyncTask = asyncTask;
		mRequest = request;
	}

	public void run() {
		mResult = (Result) mAsyncTask.doInBackground(mRequest);
	}

	public Result getResult() {
		return mResult;
	}
}
