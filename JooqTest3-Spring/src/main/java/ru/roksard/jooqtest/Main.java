package ru.roksard.jooqtest;

public class Main {

	public static void main(String[] args) {
		DBInteract db = new DBInteract();
		db.insertData();
		db.extractDataAfterInsert();
	}

}
