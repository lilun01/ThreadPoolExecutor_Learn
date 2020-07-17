package indi.zhuyst.learn.service;

import java.util.Random;

public class RemotePassportService {

    public String checkAuth(int uid){
        boolean flag;

        System.out.println("黑名单 - 验证开始");
        try {
            Thread.sleep(3000);
            Thread.currentThread().setName("验证黑名单的线程");
            //flag = new Random().nextBoolean();
            flag = true;
        } catch (InterruptedException e) {
            System.out.println("黑名单 - 验证终止");
            return Thread.currentThread().getName() + false;
        }

        if(flag){
            System.out.println("黑名单 - 验证成功");
            return  Thread.currentThread().getName() + "true";
        }
        else {
            System.out.println("黑名单 - 验证失败");
            return Thread.currentThread().getName() + false;
        }
    }
}
