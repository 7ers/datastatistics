package com.hsmy.datastatistics.schedule;

import com.hsmy.datastatistics.pojo.ReceiveStat;
import com.hsmy.datastatistics.service.ReceiveStatService;
import com.hsmy.datastatistics.utils.BufferedRandomAccessFile;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Component
public class Processor {
    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    @Value("${filepath.his_path}")
    private String his_path;

    @Resource
    private ReceiveStatService receiveStatService;

    @Scheduled(cron="0 35 5 * * ?")
    private void process() {
        long startTime = System.currentTimeMillis();
        logger.error("process start...");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date statDate = cal.getTime();
        String new_path = his_path+sdf.format(statDate)+"/";
        File folder = new File(new_path);
        if(folder.exists()){
            String[] fileArray = folder.list();
            if(fileArray == null || fileArray.length == 0){
                return;
            }

            List<String> fileNameList = Arrays.asList(fileArray);
            byte[][][][] ipMap = new byte[256][256][256][256];
            long counter = 0;
            for (String filename : fileNameList) {
                counter += parseFile(new_path+filename,ipMap);
                logger.error("parsed filename:"+filename+" IP'num is "+counter);
            }

            ReceiveStat receiveStat = new ReceiveStat();
            receiveStat.setCreatetime(new Date());
            receiveStat.setStatDate(statDate);
            receiveStat.setStatCount(counter);
            receiveStat.setDuration(System.currentTimeMillis()-startTime);
            try{
                receiveStatService.addStat(receiveStat);
            } catch (Exception e){
                logger.error(e.getMessage());
            }
            logger.error("finish.");
        }
    }

    private long parseFile(String filename, byte[][][][] ipMap) {
        BufferedRandomAccessFile reader = null;
        long count = 0;
        try {
            reader = new BufferedRandomAccessFile(filename, "r");
            reader.seek(0);

            boolean bReadEOF = false;
            do{
                String line = reader.readLine();
                if (StringUtils.isEmpty(line)) {
                    bReadEOF = true;
                } else {
                    String[] ipa = line.split("\\|")[0].split("\\.");
                    if(!isIP(ipa)){
                        continue;
                    }
                    int a = Integer.parseInt(ipa[0]);
                    int b = Integer.parseInt(ipa[1]);
                    int c = Integer.parseInt(ipa[2]);
                    int d = Integer.parseInt(ipa[3]);
                    if (ipMap[a][b][c][d] == 0) {
                        ipMap[a][b][c][d] = 1;
                        ++count;
                    }
                }
            }while(!bReadEOF);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return count;
    }

    private boolean isIP(String[] ip){
        if(ip.length == 4){
            return true;
        }else{
            return false;
        }
    }
}
