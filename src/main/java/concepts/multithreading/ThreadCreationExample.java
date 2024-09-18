package concepts.multithreading;

import java.util.concurrent.*;

public class ThreadCreationExample {
    public static void main(String[] args) {
        // four different ways to create a thread
        // 1. By extending Thread class
        // Although we can create a thread by extending Thread class, it is not recommended
        // because it does not allow multiple inheritance i.e. if we extend Thread class,
        // we cannot extend any other class
        // a better approach is to implement Runnable interface
        Thread thread = new Thread(new MyThread());
        thread.start();

        // 2. By implementing Runnable interface
        // This is the recommended way to create a thread
        Thread runnableThread = new Thread(new RunnableThread());
        runnableThread.start();

        // 3. By implementing Runnable interface using lambda expression
        Thread lambdaThread = new Thread(() ->
            System.out.println("Thread is running using lambda expression"));
        lambdaThread.start();


        // 4. By implementing Callable interface
        // This is another way to create a thread
        // Callable interface is similar to Runnable interface, but it can return a value
        // and can throw a checked exception
        // To run a Callable object, we need to use ExecutorService
        // and call submit() method
        CallableThread callableThread = new CallableThread();
        // To run a Callable object, we need to use ExecutorService
        // and call submit() method
        Future<String> future;
        try (ExecutorService executorService = Executors.newFixedThreadPool(1)) {
            future = executorService.submit(callableThread);
        }
        try {
             System.out.println(future.get());
         } catch (InterruptedException | ExecutionException e) {
             e.printStackTrace();
         }


    }


    // 1. By extending Thread class
    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Thread is running");
        }
    }

    // 2. By implementing Runnable interface
    static class RunnableThread implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread is running using Runnable interface");
        }
    }

    // 4. By implementing Callable interface
    static class CallableThread implements Callable<String> {
        @Override
        public String call() throws Exception {
            return "Thread is running using Callable interface"+Thread.currentThread().getName();
        }
    }
}