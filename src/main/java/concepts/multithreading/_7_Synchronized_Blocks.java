package concepts.multithreading;

public class _7_Synchronized_Blocks {

    public class SynchronizedExchanger {
        protected Object object = null;

        // given an instance of this synchronized exchanger class
        // only one thread can access the setObject() method at a time
        public synchronized void setObject(Object object) {
            this.object = object;
        }

        // since there are two synchronized blocks in this method
        // only one thread can access either of the blocks at a time
        public synchronized Object getObject() {
            return this.object;
        }

        public void setObj(Object o){
            // we can just create a synchronized block instead of synchronizing the whole method
            // even the synchronized blocks can be executed by only one thread at a time
            // declaring a method as synchronized is equivalent to declaring synchronized(this) block
            synchronized (this) {
                this.object = o;
            }
        }

        // in the above example, we have created just one instance of the synchronized exchanger
        // and we can have multiple threads sharing this instance

        //
    }

    // we can use static synchronized class as monitor object as well
    public static class StaticSynchronizedExchanger {
        protected static Object object = null;

        public static synchronized void setObject(Object object) {
            StaticSynchronizedExchanger.object = object;
        }

        public static synchronized Object getObject() {
            return StaticSynchronizedExchanger.object;
        }

        public static void setObj(Object o){
            // here we are using the class itself as the monitor object
            synchronized (StaticSynchronizedExchanger.class) {
                StaticSynchronizedExchanger.object = o;
            }
        }
    }

    public class MixedSynchronization {
        public static Object staticObj = null;

        // this method's synchronized on the class object
        public static synchronized void setStaticObj(Object o){
            staticObj = o;
        }

        public Object instanceObj = null;

        // this method's synchronized on the instance object
        public synchronized void setInstanceObj(Object o){
            instanceObj = o;
        }

        // this means that the thread can access the synchronized block
        // only if it has the lock on the instance object or the class object
        // so there is a possibility that two threads can access the synchronized blocks
        // simultaneously cause there are two different monitor objects
    }

    public class MultipleMonitorObjects {
        private Object monitor1 = new Object();
        private Object monitor2 = new Object();

        private int counter1 = 0;
        private int counter2 = 0;

        // below methods are having different monitor objects
        // so they can be accessed by two different threads simultaneously
        // however, on the same instance of MultipleMonitorObjects, only one thread can access
        // incrementCounter1() or incrementCounter2() at the same time
        public void incrementCounter1(){
            synchronized (monitor1){
                counter1++;
            }
        }

        public void incrementCounter2(){
            synchronized (this.monitor2){
                counter2++;
            }
        }

    }

    // monitor object is shared, so only one thread can access the synchronized block at a time
    // so if we have two threads, with the same shared monitor object, only one thread can access
    // the synchronized block at a time and the other thread has to wait for the first thread to finish
    public class SharedMonitorObject {
        private Object monitor = new Object();
        private int counter = 0;

        public SharedMonitorObject(Object monitor) {
            if(monitor == null) {
                throw new IllegalArgumentException("Monitor object cannot be null");
            }
            this.monitor = monitor;
        }

        public void incrementCounter(){
            synchronized (monitor){
                counter++;
            }
        }

        public int getCounter(){
            synchronized (monitor){
                return counter;
            }
        }
    }

    // sharing the same monitor object between different instances of the class
    // is an advanced topic and can create advanced synchronization mechanisms
    // across different instances of the class
    // But it becomes hard to see if the monitor object is shared between different instances
    // correctly and is synchronized properly

    // monitor objects cannot be instances of primitive types, such as Strings, Integers, etc.
    // stick to using Objects or instances of your own classes

    // we can use synchronized blocks inside the lambda expressions as well

    public class ReentrantLock {
        private int counter = 0;

        public synchronized void inc(){
            this.counter++;
        }

        public synchronized int incAndGet(){
            // when calling another synchronized method from a synchronized method
            // the lock is reentrant, so the thread can access the synchronized method
            // because it already has the lock on the monitor object
            this.inc();
            return this.counter;
        }
    }

    // synchronized visibility is guaranteed by the JVM
    // it also ensures happens-before relationship
    // so the changes made to the shared variables are visible to other threads
    // and the changes made to the shared variables are not reordered by the CPU
    public class SynchronizedVisibility {
        private int counter = 0;

        public synchronized void inc(){
            this.counter++;
        }

        public synchronized int get(){
            return this.counter;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        SynchronizedExchanger synchronizedExchanger = new _7_Synchronized_Blocks().new SynchronizedExchanger();
        Thread t1 = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<1000; i++){
                            synchronizedExchanger.setObject(i);
                        }
                    }
                }
        );
        Thread t2 = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<1000; i++){
                            System.out.println(synchronizedExchanger.getObject());
                        }
                    }
                }
        );
        //t1.start();
        //t2.start();

        SynchronizedVisibility sv = new _7_Synchronized_Blocks().new SynchronizedVisibility();
        Thread t3 = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<1_000_000; i++){
                            sv.inc();
                        }
                        System.out.println(sv.get());
                    }
                }
        );
        Thread t4 = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<1_000_000; i++){
                            sv.inc();
                        }
                        System.out.println(sv.get());
                    }

                }
        );
        t3.start();
        t4.start();
    }

    // limitations of synchronized blocks:
    // 1. only one thread can access the synchronized block at a time
            // we may need some thread to be blocked if they want to write, but allow multiple threads to read simultaneously
    // 2. there is no guarantee that the thread which is waiting first for the lock will get the lock
            // so there is no fairness in synchronized blocks
            // so this may lead to thread starvation, we can use locks to achieve fairness
    // 3. entering and exiting synchronized blocks incurs some performance overhead
            // if there is another thread waiting for the lock, the thread has to wait. then the thread has to be woken up
            // and the thread has to be scheduled to run this causes some performance overhead
            // but, if there is no contention, the performance overhead is minimal
    // 4. synchronized blocks can only be used to block threads that are running in the same JVM
            // if we have multiple JVMs running on different machines, we cannot use synchronized blocks
            // we can use distributed locks to achieve synchronization across different JVMs
}
