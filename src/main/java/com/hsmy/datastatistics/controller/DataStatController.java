package com.hsmy.datastatistics.controller;

import com.hsmy.datastatistics.pojo.ReceiveStat;
import com.hsmy.datastatistics.service.ReceiveStatService;
import com.hsmy.datastatistics.utils.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DataStatController {
    private final static Logger logger = LoggerFactory.getLogger(DataStatController.class);

    @Resource
    ReceiveStatService receiveStatService;

    @GetMapping("/receivedStat")
    public JsonResult receivedStat(String date){
        List<ReceiveStat> list = receiveStatService.qryStat(date);
        JsonResult jr = new JsonResult();
        jr.setCode("0");
        jr.setMsg("success");
        jr.setObj(list);
        return jr;
    }
}
