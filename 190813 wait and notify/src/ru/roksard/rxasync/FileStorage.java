package ru.roksard.rxasync;

import java.util.LinkedList;

public class FileStorage {
	public LinkedList<String> list = new LinkedList<>();
	boolean busy = false;
	
	public synchronized void lock() {
		while(busy) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		busy = true;
	}
	
	public synchronized void free() {
		busy = false;
		notifyAll();
	}
	
}
