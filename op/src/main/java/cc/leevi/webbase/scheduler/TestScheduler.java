package cc.leevi.webbase.scheduler;

import cc.leevi.webbase.service.BusCommonService;
import cc.leevi.webbase.service.KgFusionService;
import cc.leevi.webbase.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

@Slf4j
@Component
public class TestScheduler {

    @Autowired
    private BusCommonService busCommonService;
    @Autowired
    private KgFusionService kgFusionService;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        /** 等待服务器联通再开放补充数据工具2019-06-18
        System.out.println("The time is now {} " +System.currentTimeMillis());
        //log.info("The time is now {}", System.currentTimeMillis());
        String nDate= DateUtils.dateToString(new Date(), 1);
               try {
            createCVSRe(nDate);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

//    private void createCVSRe(String nDate)throws Exception {
//        //D:\neo4jDBF\neo4jDatabases\database-1166fcd7-ffb3-4ad8-8bd1-78c812ec29f4\installation-3.5.0\import
//        //  /home/jboss/kgsp_base1/neo4j-community-3.5.2/import
//        FileOutputStream fos = new FileOutputStream("/home/jboss/kgsp_base1/neo4j-community-3.5.2/import/"+nDate+".csv");
//        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
//        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("t_Accdnt_Tm", "c_Insured_Nme", "c_Payee_Nme","c_Payee_No",
//                "c_Ply_No","t_Insrnc_Bgn_Tm","t_Insrnc_End_Tm","c_Clm_No","t_Rpt_Tm","c_Rptman_Tel","c_Pay_Liability");
//        CSVPrinter csvPrinter = new CSVPrinter(osw, csvFormat);
//        busCommonService.printClaimStructuredData(nDate,csvPrinter);
//        csvPrinter.flush();
//        csvPrinter.close();
//        /**调用loadCVS，观测数据暂时关闭*/
////        kgFusionService.createModelsBase1("file:///"+nDate+".csv");
//    }
}
