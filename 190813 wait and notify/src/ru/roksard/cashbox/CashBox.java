package ru.roksard.cashbox;
class CashBox {
    private boolean busy = false;
 
    public synchronized void accept() {
        while ( busy ) {
            try {
                wait();
            }
            catch ( InterruptedException ie ) {
                System.out.println("Ожидание прервано!");
            }
        }
        busy = true;
    }
    
    public synchronized void done() {
        busy = false;
        notifyAll();
    }
}
