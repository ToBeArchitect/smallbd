package com.test;


import com.utils.BaiduBaikeUrlSender;
import org.junit.Test;

/**
 * Created by SmallMadRabbit on 2017/7/6 0006.
 */
public class MainTest {

    @Test
    public void main() throws Exception {
        BaiduBaikeUrlSender baiduBaikeUrlSender = new BaiduBaikeUrlSender(0,500,3*60*1000,400,4);
        baiduBaikeUrlSender.start();
    }
}