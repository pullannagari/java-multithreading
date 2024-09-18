package concepts.multithreading;

import java.awt.*;

public class MemoryModel {
    // we will go over how the memory is organized and accessed in
    // the context of multithreading in java

    // Memory is organized in two parts: stack and heap
    // Stack is used for static memory allocation, such as local variables, including references to objects
    // Heap is used for dynamic memory allocation, such as objects

    // Each thread has its own stack, but all threads share the heap

    static class PersonPOJO{
        private String name;
        private int age;

        public PersonPOJO(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    static class MyRunnable implements Runnable {

        private PersonPOJO sharedObject;
        private int count;

        public MyRunnable(PersonPOJO sharedObject) {
            this.sharedObject = sharedObject;
        }

        @Override
        public void run() {
            // local variables are stored in the stack
            int localVariable = 10;
            System.out.println("localVariable: " + localVariable);
            this.count ++;
            this.sharedObject.name = "Alice";
            System.out.println("count: " + count);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // we will create two threads and run them
        // each thread will have its own stack, but they will share the heap
        // below code creates two different runnable objects, so count variable is not shared
        // but the sharedVariable inside the object is same and shared
        PersonPOJO sharedObject = new PersonPOJO("John", 30);

        MyRunnable myRunnable1 = new MyRunnable(sharedObject);
        MyRunnable myRunnable2 = new MyRunnable(sharedObject);
        Thread thread1 = new Thread(myRunnable1);
        Thread thread2 = new Thread(myRunnable2);
        thread1.start();
        thread2.start();
        thread2.join();
        thread1.join();

        System.out.println("sharedObject name: " + sharedObject.name);

        // below code creates two threads using the same runnable object, so count variable is shared
        MyRunnable myRunnable = new MyRunnable(sharedObject);
        Thread thread3 = new Thread(myRunnable);
        Thread thread4 = new Thread(myRunnable);
        thread3.start();
        thread4.start();
        thread4.join();
        thread3.join();

        System.out.println("sharedObject name: " + sharedObject.name);

        // There are three layers of memory in Java: registers, L1/L2/L3 cache, and RAM(heap)
        // Registers are the fastest memory, but they are limited in number
        // L1/L2/L3 cache are slower than registers, but faster than RAM, and they are per core of the CPU
        // RAM is the slowest memory, but it is the largest
        // When a thread reads a variable, it reads it from the cache and makes a copy in the registers
        // When a thread writes a variable, it copies the var into to the registers, updates it, and writes it back to the cache specific to the core that is running the thread

        // When threads run in parallel and share the same variable, they may have different copies of the variable in their registers and caches
        // This is called "visibility" problem, and it can lead to race conditions in multithreading.
        // To solve this problem, we can use the "volatile" keyword, which ensures that the variable is read from and written to RAM directly

        // Even when the threads don't run in parallel, the update made to a shared variables by one thread may not be visible to another thread
        // This is because the update may be made in the registers and cache of the thread, but not in the RAM and this update may take random
        // amount of time to be visible to other threads

        // Sometimes when a cpu wants to write the value of a variable to the cache, it may write through the cache to the RAM
        // Some CPU's may have cache coherence protocol to ensure that the caches are coherent, meaning that if the cpu reads a value from the cache
        // of one core, it can also access the caches of other cores to see if the value is the same in other caches.
        // If the value is different in other caches, the cpu will update the value in the other caches to make them coherent
        // This is called cache coherence, but we will still not know when the value is updated to cache from the registers.

        // To solve this problem, we can use the "synchronized" keyword, which ensures that only one thread can access the shared variable at a time.


    }
}
