/**
 * File to be modified
 */
class SafeBridge extends Bridge {

    private int nred  = 0;
    private int nblue = 0;

    synchronized void redEnter() {
    	nred++;
    }

    synchronized void redExit(){
    	nred--;
    }

    synchronized void blueEnter() {
    	nblue++;
    }

    synchronized void blueExit(){
    	nblue--;
    }
}
