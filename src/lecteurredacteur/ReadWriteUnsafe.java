/**
 * File to be modified
 */
package lecteurredacteur;
class ReadWriteUnsafe implements ReadWrite {

   synchronized public void acquireRead() throws InterruptedException {}

   synchronized public void releaseRead() {}

   synchronized public void acquireWrite() throws InterruptedException {}

   synchronized public void releaseWrite() {}
 }


