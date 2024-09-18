package concepts.multithreading;

public class RaceConditions implements Runnable{
    private int count = 0;

    @Override
    public void run() {
        for(int i = 0; i < 1_000_000; i++){
            count ++;
            // we can use synchronized, but it is not efficient
            // because only one thread can access the synchronized block at a time
            // so the other thread has to wait,
            // but it is still works
//            synchronized (this){
//                count++;
//            }
        }
        System.out.println("The final count is: " + count);
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable r = new RaceConditions();
        // first thread should count to 1 million
        Thread thread1 = new Thread(r);
        // second thread should also count to 1 million
        Thread thread2 = new Thread(r);

        Thread thread3 = new Thread(r);
        // so the total count should be 3 million
        // but because this count variable is shared between two threads
        // the final count will be less than 3 million
        // because the sometimes, update of the count variable is not seen by the other thread
        // so the update is lost...
        thread1.start();
        thread2.start();
        thread3.start();
        thread2.join();
        thread1.join();
        thread3.join();
        // only one thread will print the final count of 3 million
        // and that is the thread which finishes last

    }

}
