package com.infogain.gcp.poc.consumer.component;

import java.util.ArrayList;
import java.util.List;

public class BatchList {

    private static List<Long> allBatchTimeList = new ArrayList<>();

    public List<Long> getAllBatchTimeInMillis() {
        return allBatchTimeList;
    }

    public void setTime(Long allBatchTimeInMillis) {
       allBatchTimeList.add(allBatchTimeInMillis);
    }

}
