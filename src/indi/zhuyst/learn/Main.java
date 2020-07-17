package indi.zhuyst.learn;

import indi.zhuyst.learn.thread.RemoteBankThread;
import indi.zhuyst.learn.thread.RemoteLoanThread;
import indi.zhuyst.learn.thread.RemotePassportThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static ThreadPoolExecutor executor;
    private static ExecutorService fixedThreadPool;

    public static void main(String[] args) {
        final int poolSize = 3;
        final int maxPoolSize = poolSize * 4;

        executor = new ThreadPoolExecutor(poolSize,maxPoolSize,
                1,TimeUnit.MINUTES, new LinkedBlockingQueue<>(),new MyThreadFactory());

        fixedThreadPool = Executors.newFixedThreadPool(3);
        
        boolean result = check();
        System.out.println(result ? "验证成功" : "验证失败");
       
        //executor.shutdownNow();
        fixedThreadPool.shutdown();
    }

    private static boolean check(){
        int taskNumber = 3;
        int uid = 1;
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(taskNumber);
        List<FutureTask<String>> tasks = new ArrayList<>();
        tasks.add(new CheckFutureTask(new RemoteBankThread(uid),latch,taskNumber));
        tasks.add(new CheckFutureTask(new RemoteLoanThread(uid),latch,taskNumber));
        tasks.add(new CheckFutureTask(new RemotePassportThread(uid),latch,taskNumber));

        for(FutureTask<String> task : tasks){
            //executor.execute(task);
            fixedThreadPool.execute(task);
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("启动线程总共耗时："+(endTime1-startTime)/1000f+"秒");
        try {
        	//主线程等待所有的线程完成，才往下走,主线程最多等9s
            latch.await(5,TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            System.out.println("总共耗时："+(endTime-startTime)/1000f+"秒");

            //因为前面有个一个失败，全部是失败的操作，所以这里把还在进行的任务取消掉
            for(FutureTask<String> task : tasks){
            	//拿每一个结果时，主线程也最多等待4秒， 也就是说 一个子线程最大执行时间是  5+4  ，如果时间还没返回 就超时异常
            	System.out.println("获取的结果："+task.get(4,TimeUnit.SECONDS));
            	System.out.println("task.isCancelled() ="+task.isCancelled() );//是否被取消
            	System.out.println("task.isDone() ="+task.isDone());//是否正常完成，并且返回结果
                task.cancel(true);//用于取消异步的任务，它传入一个boolean类型的参数，传入true会中断线程停止任务，而传入false则会让线程正常执行至完成，并返回false。 
            }

            for(FutureTask<String> task : tasks){
            	System.out.println("====task.isCancelled() ="+task.isCancelled() );
            	System.out.println("====task.isDone() ="+task.isDone());
                if(!task.isCancelled() ){
                    return false;
                }
            }

        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }

        return true;
    }
}
