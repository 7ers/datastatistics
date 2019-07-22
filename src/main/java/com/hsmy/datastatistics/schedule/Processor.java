package com.hsmy.datastatistics.schedule;

import com.hsmy.datastatistics.pojo.ReceiveStat;
import com.hsmy.datastatistics.service.ReceiveStatService;
import com.hsmy.datastatistics.utils.BufferedRandomAccessFile;
import com.hsmy.datastatistics.utils.RedisUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Lazy(false)
@Configuration
@EnableScheduling
public class Processor {
    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    private static final String KEY_FILENAMES = "KEY1";
    private static final String KEY_IPLIST = "KEY2";

    @Value("${filepath.his_path}")
    private String his_path;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ReceiveStatService receiveStatService;

    @Scheduled(cron="0 17 21 * * ?")
//    @Scheduled(fixedDelay = 10000)
    private void process() {
        long startTime = System.currentTimeMillis();
        File folder = new File(his_path);
        if(folder.exists()){
            List<String> oldFileList = (List)redisUtil.get(KEY_FILENAMES);
            if(oldFileList!=null){
                redisUtil.del(KEY_FILENAMES);
                redisUtil.del(KEY_IPLIST);
            }

            String[] fileArray = folder.list();
            if(fileArray == null || fileArray.length == 0){
                return;
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE,-1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date statDate = cal.getTime();
            List<String> fileNameList = delDuplication(sdf.format(statDate), Arrays.asList(fileArray));
            redisUtil.set(KEY_FILENAMES,fileNameList);// 缓存待处理文件列表
            for (String filename : fileNameList) {
                parseFile(his_path+filename);
            }

            ReceiveStat receiveStat = new ReceiveStat();
            receiveStat.setCreatetime(new Date());
            receiveStat.setStatDate(statDate);
            receiveStat.setStatCount(redisUtil.sGetSetSize(KEY_IPLIST));
            receiveStat.setDuration(System.currentTimeMillis()-startTime);
            try{
                receiveStatService.addStat(receiveStat);
            } catch (Exception e){
                logger.error(e.getMessage());
            } finally {
                redisUtil.del(KEY_IPLIST);
            }
        }
    }

    private List<String> delDuplication(String statDate, List<String> fileList) {
        List<String> ids = new ArrayList<>();//用来临时存储
        return fileList.stream().filter(// 过滤去重
                v -> {
                    boolean flag = false;
                    if(statDate.equals(v.substring(0,8))){
                        flag = true;
                        ids.add(v);
                    }
                    return flag;
                }
        ).collect(Collectors.toList());
    }

    private void parseFile(String filename) {
        BufferedRandomAccessFile reader = null;
        try {
            reader = new BufferedRandomAccessFile(filename, "r");
            reader.seek(0);

            boolean bReadEOF = false;
            do{
                String line = reader.readLine();
                if (StringUtils.isEmpty(line)) {
                    bReadEOF = true;
                } else {
                    redisUtil.sSet(KEY_IPLIST, line.split("\\|")[0]);
                }
            }while(!bReadEOF);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public static void main(String args[]){
        System.out.println(new Date());
        System.out.println(System.currentTimeMillis());
    }
}
