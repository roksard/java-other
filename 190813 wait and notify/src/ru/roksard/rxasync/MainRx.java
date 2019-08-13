package ru.roksard.rxasync;

import java.util.LinkedList;

public class MainRx {

	public static void main(String[] args) throws InterruptedException {
		String[] names = {"марти", "кренк", "джулиа", "виктор"};
		LinkedList<Thread> threads = new LinkedList<>();
		FileStorage fs = new FileStorage();
		for(int i = 0; i < names.length; i++) {
			Thread newt = new Thread(
					new Task(fs, names[i]));
			threads.add(newt);
			newt.start();
		}
		for(Thread t : threads){
			t.join();
		}
		for(String s : fs.list) {
			System.out.println(s);
		}
	}

}
