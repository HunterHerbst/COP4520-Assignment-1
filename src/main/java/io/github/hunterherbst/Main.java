package io.github.hunterherbst;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) {
        // Hard coded values
        int start = 2,
            end = (int) Math.pow(10, 8), // This one should eventually be replaced with 10^8
            nT = 8,
            tRange = (end - start) / nT,
            maxTimeSeconds = 60;

        // Array to store pointers to tasks (not pointers because Java, but you get the idea. I wanna get information back later)
        PrimeTask[] tasks = new PrimeTask[nT];

        // List to store primes
        ArrayList<Integer> primes = new ArrayList<Integer>();

        long startTime = System.currentTimeMillis();

        // Thread pool party
        ExecutorService ex = Executors.newFixedThreadPool(nT);

        // Submit threads to pool
        for(int i = 0; i < nT; i++) {
            //ex.submit(new PrimeTask(Integer.toString(i),start + (i * tRange), start + ((i + 1) * tRange), primes));
            tasks[i] = new PrimeTask(Integer.toString(i),start + (i * tRange), start + ((i + 1) * tRange), primes);
            ex.submit(tasks[i]);
        }

        // Tell the thread pool to go home (the party is over)
        ex.shutdown();

        // Now start :sittin: and :waiting: while the threads do their thing (but not waiting too long, in fact, only 60 seconds)
        try {
            ex.awaitTermination(maxTimeSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("ERROR: Threads were interrupted (possibly timed out)\n" + e.getMessage());
        }

        // Sort em
        primes.sort(null);

        long endTime = System.currentTimeMillis();

        // Print the stats for this play-through
        System.out.println("Total time: " + (endTime - startTime) + "ms");
        System.out.println("Total primes: " + primes.size());
        System.out.println("Top 10 primes:");
        String top10 = Integer.toString(primes.get(primes.size() - 1));
        for(int i = primes.size() - 2; i > primes.size() - 11; i--) {
            top10 = primes.get(i) + ", " + top10;
        }
        System.out.println(top10);
    }
}

class PrimeTask implements Runnable {

    // Prime calculation variables
    private int start;
    private int end;
    private ArrayList<Integer> primes;

    // Informational variables
    private String name;
    private long execTime;

    public PrimeTask(String name, int start, int end, ArrayList<Integer> primes) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.primes = primes;
        this.execTime = 0;
    }

    @Override
    public void run() {
        System.out.println("Thread " + name + " started");
        long startTime = System.currentTimeMillis();

        // Check em
        for (int i = start; i < end; i++) {
            if (isPrime(i)) {
                synchronized (primes) {
                    primes.add(i);
                }
            }
        }

        long endTime = System.currentTimeMillis();
        execTime = (endTime - startTime);
        System.out.println("Thread " + name + " finished in " + execTime + "ms");
    }

    public String getName() {
        return name;
    }

    public long getExecTime() {
        return execTime;
    }

    private boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        if (n == 2 || n == 3) {
            return true;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        int sqrtN = (int) Math.sqrt(n) + 1;
        for (int i = 6; i <= sqrtN; i += 6) {
            if (n % (i - 1) == 0 || n % (i + 1) == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Thread " + name + " took " + execTime + "ms";
    }
}