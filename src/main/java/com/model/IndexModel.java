package com.model;

import com.exception.IndexTouchBound;

import java.io.Serializable;

public class IndexModel implements Serializable{
    private static int index = 0;
    private static int endIndex = 1;

    public static synchronized int getIndex() throws IndexTouchBound {
        index++;
        if (index > endIndex){
            throw new IndexTouchBound();
        }
        return index;
    }

    public static synchronized void setIndex(int index) {
        IndexModel.index = index;
    }

    public static int getEndIndex() {
        return endIndex;
    }

    public static void setEndIndex(int endIndex) {
        IndexModel.endIndex = endIndex;
    }
}
