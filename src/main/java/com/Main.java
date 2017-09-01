package com;

import com.utils.BaiduBaikeUrlReceiver;
import com.utils.BaiduBaikeUrlSender;

public class Main {
    public static void main(String[] args) {
        /*启动提供者*/
       BaiduBaikeUrlSender baiduBaikeUrlSender = new BaiduBaikeUrlSender(0,1000,3*60*1000,400,4);
       baiduBaikeUrlSender.start();
        /*启动消费者*/
        //BaiduBaikeUrlReceiver.start();
    }
}
