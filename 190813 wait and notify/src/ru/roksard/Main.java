package ru.roksard;

import ru.roksard.cashbox.MainCB;
import ru.roksard.rxasync.MainRx;

public class Main {
	public static void main(String[] args) throws Exception {
		int way = 2;
		switch(way) {
		case 1: 
			MainCB.main(args);
			break;
		case 2:
			MainRx.main(args);
			break;
		default:
		}
	}
}
