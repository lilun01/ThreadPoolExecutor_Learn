package indi.zhuyst.learn.service;

import java.util.Random;

public class RemoteBankService {

    public String  checkCredit(int uid){
        boolean flag;
        long startTime = System.currentTimeMillis();
        System.out.println("银行信用 - 验证开始");
        try {
        	Thread.currentThread().setName("验证行用卡的线程");
            Thread.sleep(8500);
           //flag = new Random().nextBoolean();
            flag = true;
            long endTime = System.currentTimeMillis();
            System.out.println("银行信用 - 验证总共耗时："+(endTime-startTime)/1000f+"秒");
        } catch (InterruptedException e) {
            System.out.println("银行信用 - 验证终止");
            return  Thread.currentThread().getName() + "false";
        }

        if(flag){
            System.out.println("银行信用 - 验证成功");
            return Thread.currentThread().getName() + "true";
        }
        else {
            System.out.println("银行信用 - 验证失败");
            return Thread.currentThread().getName() + "false";
        }
    }
}
