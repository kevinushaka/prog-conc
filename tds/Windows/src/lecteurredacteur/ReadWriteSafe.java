/**
 * File not to be modified
 */
package lecteurredacteur;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReadWriteSafe implements ReadWrite {
    private int readers=0;
    private Semaphore mutex;  // controls access to readerCount
    private Semaphore db;

    public ReadWriteSafe(){
        readers = 0;
        mutex = new Semaphore(1);
        db = new Semaphore(1);
    }

    public void acquireRead() throws InterruptedException {
        mutex.acquire();
        readers++;
        // If I am the first reader tell all others
        // that the database is being read
        if (readers == 1){db.acquire(); }
        //Mutual exclusion for reader count
        mutex.release();


    }
    public void releaseRead() throws InterruptedException {
        //Mutual exclusion for readerCount
        mutex.acquire();
        readers--;
        if (readers == 0){ db.release(); }
        //Mutual exclusion for readerCount
        mutex.release();

    }
    public void acquireWrite() throws InterruptedException {
        db.acquire();
    }
    public void releaseWrite() {
        db.release();
    }
}
/*
/*****************Monitor *****************\
class ReadWriteSafe implements ReadWrite {
    private int readers       = 0;
    private int writers       = 0;

    synchronized public void acquireRead() throws InterruptedException {
        while (writers>0) wait();
        readers++;
        // notifyAll(); inutile car ne réveillera jamais un processus
    }
    synchronized public void releaseRead() {
        // while !(true) wait(); inutile car toujour faux
        readers--;
        if (readers==0) // ajout car sinon le notifyAll() est inutile
            notifyAll();
    }
    synchronized public void acquireWrite() throws InterruptedException {
        while (!(readers+writers==0)) wait();
        writers++;
        // notifyAll(); inutile car ne réveillera jamais un processus
    }
    synchronized public void releaseWrite() {
        writers--;
        notifyAll();
    }
 }
*/
/*
/*****************Lock *****************\
class ReadWriteSafe implements ReadWrite {
    private int readers       = 0;
    private int writers       = 0;
    private int writeRequests = 0;
    ReentrantReadWriteLock lock;
    ReentrantReadWriteLock.ReadLock rlock;
    ReentrantReadWriteLock.WriteLock wlock;
    ReadWriteSafe(){
        ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
        rlock= lock.readLock();
        wlock=lock.writeLock();
    }
    public void acquireRead() throws InterruptedException {
        if(writers > 0 )
            rlock.lock();
        readers++;
    }

    public void releaseRead() {
        readers--;
        if(readers==0) {
            wlock.unlock();
        }
    }

    public void acquireWrite() throws InterruptedException {
        if(readers>0 || writers>0){
            wlock.lock();
        }
        writers++;
    }

    public void releaseWrite() {
        writers--;
        if(writers==0){
            rlock.unlock();
        }
    }
}
*/