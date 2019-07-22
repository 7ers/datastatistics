package com.hsmy.datastatistics.service;

import com.hsmy.datastatistics.pojo.ReceiveStat;

import java.util.List;

public interface ReceiveStatService {
    public void addStat(ReceiveStat receiveStat);
    public List<ReceiveStat> qryStat(String date);
}
