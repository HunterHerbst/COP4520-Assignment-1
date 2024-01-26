# Assignment 1

Hunter Herbst  
COP4520  
Spring 2024  

## How to run

Running the program is simple. Just navigate to the directory containing `Main.java`, and run `javac Main.java` then `java Main`. The program will begin outputting information, such as when a thread is dispatched, and thread completion. When done, the program will print "Done." and exit.  

I used Java 17 for this project, but I think it should work with Java 8 or higher.

## Approach and Testing

* Create a class, PrimeTask, that implements Runnable, which calculates all primes in a given range.
* Create a thread pool using the ExecutorService class.
* Create the tasks using the PrimeTask class, and submit them to the thread pool.
  * Ranges are calculated by dividing the total range by the number of threads.
  * Since 10^8 is divisible by 8, I did not worry about remainders. This is something that would need to be addressed if the range was not divisible by the number of threads.
* Wait for all threads to complete using the awaitTermination method.
* Calculate all information required by the assignment and write it to a file at the end

The prime number checker is pretty much just lifted from class work I did in COP4020 (Functional Programming), because we had to write that algorithm in multiple different languages across the course. Adapting it from Haskell to Java was pretty simple. In order to make sure that threads were in fact running concurrently, I printed out all thread starts and their end times. Threads starting out of order and the overall execution time being just a bit larger than the longest thread execution time should be enough evidence to conclude that the threads are running concurrently.