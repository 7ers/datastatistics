package com.hsmy.datastatistics.dao;

import com.hsmy.datastatistics.pojo.ReceiveStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReceiveStatMapper {
    int insert(ReceiveStat record);

    List<ReceiveStat> selectByDate(String date);
}