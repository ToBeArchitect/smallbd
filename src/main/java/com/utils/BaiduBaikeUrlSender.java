package com.utils;

import com.model.IndexModel;
import com.service.SpilderService;
import com.thread.RunnerThread;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public class BaiduBaikeUrlSender {
    private static final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"Spring/applicationContext.xml","Spring/applicationContext-rabbitmq-send.xml","Spring/applicationContext-redis.xml"});
    private  Integer startNum;
    private  Integer endNum;
    private  Integer outTimeSleepTime;
    private  Integer periodTime;
    private  Integer threadNumber;

    private static List<Thread> threadManage = new ArrayList<Thread>();

    public BaiduBaikeUrlSender(Integer startNum, Integer endNum, Integer outTimeSleepTime, Integer periodTime, Integer threadNumber) {
        this.startNum = startNum;
        this.endNum = endNum;
        this.outTimeSleepTime = outTimeSleepTime;
        this.periodTime = periodTime;
        this.threadNumber = threadNumber;
    }

    public void start(){

        IndexModel.setIndex(this.startNum);
        IndexModel.setEndIndex(this.endNum);

        RunnerThread runnerThread = new RunnerThread();
        runnerThread.setEndNum(endNum);
        runnerThread.setStartNum(startNum);

        for (int i=0;i<threadNumber;i++) {
            Thread thread = new Thread(runnerThread);
            threadManage.add(thread);
        }
        for (int i = 0;i< threadManage.size();i++){
            threadManage.get(i).start();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                continue;
            }
        }

    }





    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getEndNum() {
        return endNum;
    }

    public void setEndNum(Integer endNum) {
        this.endNum = endNum;
    }

    public Integer getOutTimeSleepTime() {
        return outTimeSleepTime;
    }

    public void setOutTimeSleepTime(Integer outTimeSleepTime) {
        this.outTimeSleepTime = outTimeSleepTime;
    }

    public Integer getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(Integer periodTime) {
        this.periodTime = periodTime;
    }
}
