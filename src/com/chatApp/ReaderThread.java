package com.chatApp;

public abstract class ReaderThread extends Thread {

	public void run() {
		checkForRead();
	}

	public abstract void disconnect();

	public abstract void checkForRead();
}