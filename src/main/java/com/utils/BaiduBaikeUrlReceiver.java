package com.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaiduBaikeUrlReceiver {

    public static void start() {
        Thread t = new Thread(new Runnable() {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"Spring/applicationContext*.xml"});

            public void run() {
                while (true) {

                }
            }
        });
        t.start();
        while (true) {
        }
    }
}
