package cz.vsb.application;

import java.util.concurrent.Semaphore;

public class GrammarLock {

    private static final int MAX_LOCKS = 50;
    private static Semaphore semaphore = new Semaphore(MAX_LOCKS);

    public static Semaphore getSemaphore(){
        return semaphore;
    }

    public static int getMaxLocks(){
        return MAX_LOCKS;
    }
}
