package indi.zhuyst.learn;

import indi.zhuyst.learn.thread.BaseCheckThread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

public class CheckFutureTask extends FutureTask<String>{

    private volatile CountDownLatch latch;

    private final int number;

    public CheckFutureTask(BaseCheckThread checkThread, CountDownLatch latch, int number) {
        super(checkThread);
        this.latch = latch;
        this.number = number;
    }

    @Override
    protected void done() {
        try {
            if(!isDone()){
                afterFail();
            }
        } catch (Exception e) {
            afterFail();
        } finally {
            latch.countDown();
        }
    }

    /**
     * 在失败后调用
     */
    private void afterFail(){
        for(int i = 0 ; i < number - 1 ; i++){
            latch.countDown();
        }
    }
}
