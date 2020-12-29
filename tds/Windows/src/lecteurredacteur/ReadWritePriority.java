/**
 * File to be modified
 */
package lecteurredacteur;
class ReadWritePriority implements ReadWrite {

   private int readers       = 0;
   private int writers       = 0;
   private int waitingWriters = 0;

   synchronized public void acquireRead() throws InterruptedException {
      while (!(writers+waitingWriters==0)) wait();
      readers += 1;
   }
   synchronized public void releaseRead() {
      readers -= 1;
      if (readers==0)
         notifyAll();
   }
   synchronized public void acquireWrite() throws InterruptedException {
      waitingWriters += 1;
      while (!(readers+writers==0)) wait();
      waitingWriters -= 1;
      writers += 1;
   }
   synchronized public void releaseWrite() {
      writers -= 1;
      notifyAll();
   }
 }


