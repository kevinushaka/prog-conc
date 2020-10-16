/**
 * File to be modified
 */

class FairBridge extends Bridge {

    private int nred  = 0;
    private int nblue = 0;
    private int waitblue = 0;
    private int waitred = 0;
    private boolean blueturn = true;

    synchronized void redEnter() throws InterruptedException {
    }

    synchronized void redExit(){
    }

    synchronized void blueEnter()  throws InterruptedException {
    }

    synchronized void blueExit(){
    }
}
