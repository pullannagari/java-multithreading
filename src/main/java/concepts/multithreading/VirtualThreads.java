package concepts.multithreading;

import java.util.ArrayList;
import java.util.List;

public class VirtualThreads {
    /*
    The existing threads in java are called platform threads.
    A platform thread is executed by os thread
    A virtual thread is executed by a platform thread.
    When a virtual thread is created, it is not yet mounted within a platform
    thread, it is internally queued within the platform thread.
    when the platform thread is ready, it mounts the virtual thread and executes it.
    The virtual machine keeps same number of platform threads ready as the number of cores in the cpu.

    When a thread makes a blocking call such as I/O, the platform thread unmounts the virtual thread
    and queues it in unmounted blocking queue.
    When the blocking call is finished, the virtual thread is moved to the ready queue.
    So instead of blocking the whole platform thread, only the virtual thread is blocked.

    Additionally, compared to platform threads, virtual threads are lightweight and consume less memory.
    While it would be expensive to create millions of platform threads, it is possible to create millions of virtual threads.

    Virtual threads are useful for IO bounds tasks, for CPU bound tasks, platform threads are more suitable.
    This concept was introduced in java 21 as part of project loom.
    * */

    public static void main(String[] args) throws InterruptedException {
        // Example 1: creating Runnable, create and start virtual thread

        // we use the same runnable as we previously use in platform threads
        Runnable runnable = () -> {
            for(int i = 0; i < 10; i++){
                System.out.println("Virtual thread is running");
            }
            Runnable runnable2 = () -> {
                for(int i = 0; i < 100; i++){
                    System.out.println("Virtual thread2 is running");
                }
            };
            // Example 2: creating a virtual thread, but not starting it yet
            Thread virtualThread2 = Thread.ofVirtual().unstarted(runnable2);
            virtualThread2.start();

            // we can join a virtual thread just like a platform thread
            // join will block the thread which calls the join() method until the virtual thread finishes
            try{
                System.out.println("Thread1 is waiting for virtualThread2 to finish");
                virtualThread2.join();
                System.out.println("virtualThread2 is finished");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("Thread1 is finished");
        };


        Thread virtualThread = Thread.ofVirtual().start(runnable);
        virtualThread.join();
        System.out.println("Main thread is finished");


        // Example 3: creating 100_000 virtual threads
        // we can create millions of virtual threads
        // but we cannot create millions of platform threads
        List<Thread> virtualThreads = new ArrayList<>();
        for(int i = 0; i < 1000_000; i++){
            int finalI = i;
            Thread vThread = Thread.ofVirtual().start(() -> {
                int result = 1;
                for(int j=0; j<10; j++){
                    result *= (j+1);
                }
                System.out.println("Result from "+ finalI +"th: " + result);
            });
            virtualThreads.add(vThread);
        }

        // wait for all the virtual threads to finish
        for (Thread thread : virtualThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
