package ru.roksard.rxasync;

public class Task implements Runnable {

	FileStorage storage = null;
	String name = null;
	
	public Task(FileStorage storage, String name) {
		this.storage = storage;
		this.name = name;
	}
	
	@Override
	public void run() {
		storage.lock();
		try {
			storage.list.add(name + " started");
			for(int i = 1; i < 5; i++)
				storage.list.add(name + i);
			storage.list.add(name + " finished");
		} finally {
			storage.free();
		}
			
	}
	

}
