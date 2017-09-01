package com.thread;

import com.exception.IndexTouchBound;
import com.model.IndexModel;
import com.service.SpilderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class RunnerThread implements Runnable {
    private static Logger log = Logger.getLogger(RunnerThread.class);
    @Autowired
    private SpilderService spilderService;
    private Integer index;
    private Integer startNum;
    private Integer endNum;
    private Integer outTimeSleepTime;
    private Integer periodTime;

    private Boolean flag = true;

    public RunnerThread() {
    }

    public RunnerThread(Integer startNum, Integer endNum, Integer outTimeSleepTime, Integer periodTime) {
        this.startNum = startNum;
        this.endNum = endNum;
        this.outTimeSleepTime = outTimeSleepTime;
        this.periodTime = periodTime;
    }

    public void run() {
        if (startNum == null || endNum == null) {
            log.error("startNum 或 endNum 不能为空");
            return;
        }
        if (outTimeSleepTime == null || outTimeSleepTime < 0) {
            outTimeSleepTime = 3 * 60 * 1000;
        }
        if (periodTime == null || periodTime < 0) {
            periodTime = 200;
        }
        while (flag) {
            try {
                index = IndexModel.getIndex();
                spilderService.getUrl(index);
                Thread.sleep(periodTime);
            } catch(IndexTouchBound e){
                log.debug("触发结束条件");
                this.setFlag(false);
                break;
            } catch (SocketTimeoutException e) {
                try {
                    Thread.sleep(outTimeSleepTime);
                    spilderService.getUrl(index);
                } catch (InterruptedException e2) {
                    continue;
                } catch (IOException e3) {
                    continue;
                }

            } catch (Exception e){
                log.error(e.getMessage());
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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
