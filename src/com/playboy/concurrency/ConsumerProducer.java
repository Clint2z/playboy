package com.playboy.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class ConsumerProducer {
    public static final String EXIT_MSG = "Good bye!";
    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(3);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        new Thread(producer).start();
        new Thread(consumer).start();
    }

    static class Producer implements Runnable {
        private BlockingQueue<String> queue;
        public Producer(BlockingQueue<String> q) {
            this.queue = q;
        }

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                try {
                    Thread.sleep(5L);
                    String message = "Message" + i;
                    System.out.println("Produced new item:" + message);
                    queue.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                System.out.println("Time to say goodbye!");
                queue.put(EXIT_MSG);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Consumer implements Runnable {
        private BlockingQueue<String> queue;
        public Consumer(BlockingQueue<String> q) {
            this.queue = q;
        }

        @Override
        public void run() {
            try {
                String msg;
                while(!EXIT_MSG.equalsIgnoreCase((msg = queue.take()))) {
                    System.out.println("Consumed item:" + msg);
                    Thread.sleep(10L);
                }
                System.out.println("Got exit message, bye!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
