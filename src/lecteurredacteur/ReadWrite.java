/**
 * File not to be modified
 */
//@author: j.n.magee 11/11/96

//************************************************************
// the interface for ReadWrite monitor implementations
package lecteurredacteur;

interface ReadWrite {

   public void acquireRead() throws InterruptedException;

   public void releaseRead() throws InterruptedException;

   public void acquireWrite() throws InterruptedException;

   public void releaseWrite();
 }


