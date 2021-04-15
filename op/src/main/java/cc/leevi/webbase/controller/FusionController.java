package cc.leevi.webbase.controller;

import cc.leevi.webbase.service.KgCommonService;
import cc.leevi.webbase.service.KgFusionService;
import cc.leevi.webbase.utils.DateUtils;
import cc.leevi.webbase.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSON;


/**
 * base-1，数据融合
 * @author WangYingce
 *
 */
@RestController
@RequestMapping
public class FusionController {
    @Autowired
    private KgFusionService kgFusionService;
    @Autowired
    private KgCommonService kgCommonService;

    @Value("${rangeUrl}")
    private String rangeUrl;
    /**
     * 查询导数（不推荐）
     * @throws Exception
     */
    @GetMapping("pumpClaimData")
    public void pumpClaimData() throws Exception{
//        Date dateSeqStart= DateUtils.stringToDate("2019-03-05", 0);
//        Date dateSeqEnd= DateUtils.stringToDate("2019-03-09", 0);
////        System.out.println("ate-kg getClaimStructuredData begin at:"+DateUtils.dateToString(new Date(), 3));
//        //get_all结构化数据
//        List<PumpClaimDataVo> pumps = kgFusionService.getClaimStructuredData(DateUtils.dateToString(dateSeqStart, 1),DateUtils.dateToString(dateSeqEnd, 1));
////        测试用，重复测试
////        System.out.println("delAllGraphDatas begin at:"+DateUtils.dateToString(new Date(), 3));
//        kgCommonService.delAllGraphDatas();
////        创建所有和admin用户
////        System.out.println("createPumpIndex begin at:"+DateUtils.dateToString(new Date(), 3));
////        kgFusionService.createPumpIndex();
//        if(pumps!=null&&pumps.size()>0){
////            System.out.println("at-entropy-------------createNodeKGBase1re begin at:"+DateUtils.dateToString(new Date(), 3));
//            kgFusionService.createNodeKGBase1re(pumps);
////            System.out.println("ate-kg createLineKGBase1 begin at:"+DateUtils.dateToString(new Date(), 3));
//            kgFusionService.createLineKGBase1(pumps);
//        }
////        System.out.println("ate-kg all action end at:"+DateUtils.dateToString(new Date(), 3));
    }


    /**
     * 远程neo4j导数
     * @param remote
     * @throws Exception
     */
    @GetMapping("fusionData")
    public void fusionData(String remote) throws Exception{
        System.out.println("fusionData start at:"+DateUtils.dateToString(new Date(), 3));
//        remote="http://10.0.0.25/health20210102_20210226.csv";
//        dataUrl= "http://192.168.102.229:8080/health20210102_20210225.csv";
//        String filePath = dataUrl;
        kgFusionService.createModelsBaseT(remote);
        System.out.println("fusionData end at:"+DateUtils.dateToString(new Date(), 3));
        kgFusionService.updatePageRankScore();
        System.out.println("updatePageRankScore at:"+DateUtils.dateToString(new Date(), 3));
    }

    /**
     * loadCVS批量导数(推荐)
     * @param time
     * @throws Exception
     */
    @GetMapping("pumpData")
    public Object pumpData(String time) throws Exception{
        Map<String, String> message = new HashedMap();//错误信息收集池
        String cvsUrl = "";
        String remote = rangeUrl+time;
        String urlJson = HttpUtils.httpURLPOSTCase(remote);
        Map urlMap = JSON.parseObject(urlJson);
        if(urlMap!=null&&(urlMap.get("data"))!=null){
            cvsUrl = (String) urlMap.get("data");
            System.out.println(cvsUrl);
        }else{
            message.put("error", "请求远程链接失败，地址rangeUrl："+remote+"_at_"+DateUtils.dateToString(new Date(), 3)+"，返回参数："+cvsUrl);
        }
        System.out.println("pumpData_start_at:"+DateUtils.dateToString(new Date(), 3));
        kgFusionService.createModelsBaseT(cvsUrl);
//        System.out.println("pumpData_end_at:"+DateUtils.dateToString(new Date(), 3));
        message.put("sus", "pumpData_end_at:"+DateUtils.dateToString(new Date(), 3));
        return toJSON(message);
    }

    @GetMapping("saveModel")
    public Object saveModel(String modelString) throws Exception{
        Map<String, String> message = new HashedMap();
        /**算法运行测试工具**/
//        modelString = "MATCH (n:报案人) WITH collect(n) as nodes CALL apoc.algo.pageRank(nodes) YIELD node,score SET node.pageRankScore=score";
//        kgFusionService.RunAPOC(modelString);
        /**测试报文工具**/
//        String turl = "http://10.6.6.118:8080/visionAllData?insCode=110105201206059440&insName=%E9%82%A3%E4%B9%88";
//        modelString = HttpUtils.httpURLPOSTCase(turl);

        Map modelMap = JSON.parseObject(modelString);
        kgFusionService.createFreeModels(modelMap);
        kgFusionService.createCSV(modelMap);
        message.put("sus", "saveModel:"+DateUtils.dateToString(new Date(), 3));
        return toJSON(message);
    }

    /**
     * 更新node节点
     */
    @GetMapping("updateNode")
    public void updateNode(String id,String name,String identNum,String policyNum,String stard,String endd,String accd,String payNum,String reportNum) throws Exception{
        Map<String, String> error = new HashedMap();//错误信息收集池
        if (id == null || "".equals(id)) {
            error.put("id", "error:更新ID为null");
        }
        String cql = "MATCH (s)  WHERE ID(s) = "+id+" set ";
        if(name!=null&&!"".equals(name)){
            cql = cql + "s.name = '"+name+"',";
        }
        if(identNum!=null&&!"".equals(identNum)){
            cql = cql + "s.身份证号 = '"+identNum+"',";
        }
        if(policyNum!=null&&!"".equals(policyNum)){
            cql = cql + "s.保单号 = '"+policyNum+"',";
        }
        if(stard!=null&&!"".equals(stard)){
            cql = cql + "s.保险起期 = '"+stard+"',";
        }
        if(endd!=null&&!"".equals(endd)){
            cql = cql + "s.保险止期 = '"+endd+"',";
        }
        if(accd!=null&&!"".equals(accd)){
            cql = cql + "s.出险时间 = '"+accd+"',";
        }
        if(payNum!=null&&!"".equals(payNum)){
            cql = cql + "s.银行卡 = '"+payNum+"',";
        }
        if(reportNum!=null&&!"".equals(reportNum)){
            cql = cql + "s.报案电话 = '"+reportNum+"',";
        }
        cql = cql.substring(0,cql.length()-1);
        kgFusionService.updateNodeById(cql);
    }
}
