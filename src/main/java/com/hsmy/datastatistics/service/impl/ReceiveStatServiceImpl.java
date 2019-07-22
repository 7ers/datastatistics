package com.hsmy.datastatistics.service.impl;

import com.hsmy.datastatistics.dao.ReceiveStatMapper;
import com.hsmy.datastatistics.pojo.ReceiveStat;
import com.hsmy.datastatistics.service.ReceiveStatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReceiveStatServiceImpl implements ReceiveStatService {

    @Resource
    ReceiveStatMapper receiveStatMapper;

    @Override
    public void addStat(ReceiveStat receiveStat) {
        receiveStatMapper.insert(receiveStat);
    }

    @Override
    public List<ReceiveStat> qryStat(String date) {
        return receiveStatMapper.selectByDate(date);
    }
}
