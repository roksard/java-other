package ru.roksard.cashbox.copy;
import java.util.Random;

public class Main {

	public static void main(String[] args) throws InterruptedException {
        CashBox cashbox = new CashBox();
        Thread[] customers = new Thread[3];
        Random rand = new Random();
        String[] names = { "Иванов", "Петров", "Сидоров", "Сталин", "Рузвельт", "Черчиль" };
 
        for ( int i = 0; i < customers.length; ++i ) {
            Thread.sleep(rand.nextInt(1000) + 500);
            customers[i] = new Thread(new Customer(names[rand.nextInt(names.length)], rand.nextInt(10000) + 1000, cashbox));
            customers[i].start();
        }
        
        for ( int i = 0; i < customers.length; ++i )
            customers[i].join();
    }

}
