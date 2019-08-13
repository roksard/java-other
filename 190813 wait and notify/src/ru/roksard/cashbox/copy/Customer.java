package ru.roksard.cashbox.copy;
public class Customer implements Runnable {
    private String name;
    private long dealtime;
    private CashBox cashbox;
    
    Customer(String n, long dt, CashBox cb) {
        name = n;
        dealtime = dt;
        cashbox = cb;
    }
 
    @Override
    public void run() {
        System.out.println(name + " подходит к кассе...");
        cashbox.accept();
        System.out.println(name + " общается с кассиром...");
        try {
            Thread.sleep(dealtime);
        }
        catch ( InterruptedException ie ) {
            System.out.println("Общение с кассиром прервано!");
        }
        cashbox.done();
        System.out.println(name + " уходит.");
    }
}
