package concepts.multithreading;

public class _2_StoppableRunnable implements Runnable {
    // java has Thread.stop() method to stop a thread,
    // but it is deprecated because it is inherently unsafe
    // because it can cause the object to be left in an inconsistent state
    // we instead use a boolean flag to stop a thread
    private boolean stop = false;

    public synchronized void requestStop() {
        this.stop = true;
    }

    public synchronized boolean isStopRequested() {
        return stop;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        System.out.println("StoppableRunnable is running");
        while(!isStopRequested()){
            sleep(1000);
            System.out.print("...");
        }
        System.out.println("StoppableRunnable is stopped");
    }

    public static void main(String[] args) throws InterruptedException {
        _2_StoppableRunnable stoppableRunnable = new _2_StoppableRunnable();
        Thread thread = new Thread(stoppableRunnable);
        thread.start();
        Thread.sleep(5000);
        stoppableRunnable.requestStop();

        // Threads can be run as deamons by calling setDaemon(true) method
        // Deamon threads do not prevent the JVM from exiting when the program finishes
        // Deamon threads are used for background tasks such as garbage collection
        // or other tasks that should be run as long as the program is running
        // Deamon threads are terminated by the JVM when the program finishes
        // Threads should be set as deamons before starting them
        Runnable r = () -> {
            for(int i = 0; i < 5; i++) {
                System.out.println("Deamon thread is running");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //        Thread deamonThread = new Thread(r);
        //        deamonThread.setDaemon(true);
        //        deamonThread.start();
        //        Thread.sleep(5000);

        // since we may not know how long a thread will take to finish
        // we can wait for a thread to finish by calling join() method
        // join() method waits for the thread to finish
        // join() method can also take a timeout in milliseconds
        // if the thread does not finish within the timeout, the join() method will return
        // and the program will continue
        Thread deamonThreadJoin = new Thread(r);
        deamonThreadJoin.setDaemon(true);
        deamonThreadJoin.start();
        deamonThreadJoin.join();
        // main thread waits for the daemon thread to finish since we called join() method
        System.out.println("Main thread is finished");

        /*
        PROJECT LOOM - fibers or virtual threads
        Threads by default are managed by the os that java is running on
        they are known as os threads and os threads are usually more expensive
        in terms of resources like memory and cpu time to address this java loom project
        was created to provide lightweight threads known as virtual threads
        * */
    }
}
