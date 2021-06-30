package cc.leevi.webbase.controller;

import cc.leevi.webbase.service.KgCommonService;
import cc.leevi.webbase.service.KgFusionService;
import cc.leevi.webbase.service.UserLogicService;
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
import java.util.Iterator;
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
    private UserLogicService userLogicService;

    @Value("${rangeUrl}")
    private String rangeUrl;


    /**
     * 远程neo4j导数
     *
     * @param remote
     * @throws Exception
     */
    @GetMapping("fusionData")
    public void fusionData(String remote) throws Exception {
        System.out.println("fusionData start at:" + DateUtils.dateToString(new Date(), 3));
//        remote="http://10.0.0.25/health20210102_20210226.csv";
//        dataUrl= "http://192.168.102.229:8080/health20210102_20210225.csv";
//        String filePath = dataUrl;
        kgFusionService.createModelsBaseT(remote);
        System.out.println("fusionData end at:" + DateUtils.dateToString(new Date(), 3));
        kgFusionService.updatePageRankScore();
        System.out.println("updatePageRankScore at:" + DateUtils.dateToString(new Date(), 3));
    }

    /**
     * loadCVS批量导数(推荐)
     *
     * @param time
     * @throws Exception
     */
    @GetMapping("pumpData")
    public Object pumpData(String time) throws Exception {
        Map<String, String> message = new HashedMap();//错误信息收集池
        String cvsUrl = "";
        String remote = rangeUrl + time;
        String urlJson = HttpUtils.httpURLPOSTCase(remote);
        Map urlMap = JSON.parseObject(urlJson);
        if (urlMap != null && (urlMap.get("data")) != null) {
            cvsUrl = (String) urlMap.get("data");
            System.out.println(cvsUrl);
        } else {
            message.put("error", "请求远程链接失败，地址rangeUrl：" + remote + "_at_" + DateUtils.dateToString(new Date(), 3) + "，返回参数：" + cvsUrl);
        }
        System.out.println("pumpData_start_at:" + DateUtils.dateToString(new Date(), 3));
        kgFusionService.createModelsBaseT(cvsUrl);
//        System.out.println("pumpData_end_at:"+DateUtils.dateToString(new Date(), 3));
        message.put("sus", "pumpData_end_at:" + DateUtils.dateToString(new Date(), 3));
        return toJSON(message);
    }

    @GetMapping("saveModel")
    public Object saveModel(String modelString) throws Exception {
        Map<String, String> msg = new HashedMap();
        /**算法运行测试工具**/
//        modelString = "MATCH (n:报案人) WITH collect(n) as nodes CALL apoc.algo.pageRank(nodes) YIELD node,score SET node.pageRankScore=score";
//        kgFusionService.RunAPOC(modelString);
        /**测试报文工具**/
//        String turl = "http://10.6.6.118:8080/visionAllData?insCode=110105201206059440&insName=%E9%82%A3%E4%B9%88";
//        modelString = HttpUtils.httpURLPOSTCase(turl);
        Map modelMap = JSON.parseObject(modelString);
        String userc = String.valueOf(modelMap.get("userc"));
        if (userc != null && !"".equals(userc)) {
            String username = userLogicService.findUserByCookies(userc);
            if (username != null && !"".equals(username)) {
                modelMap.put("usern", username);
                userLogicService.createModel(modelMap);
            } else {
                msg.put("error", "未检索到当前用户_saveModel_99");
            }
        } else {
            msg.put("error", "未登录，请先登录:" + userc);
        }
        kgFusionService.createFreeModels(modelMap);
        kgFusionService.createCSV(modelMap);
        msg.put("sus", "saveModel:" + DateUtils.dateToString(new Date(), 3));
        return toJSON(msg);
    }

    /**
     * 更新node节点
     */
    @GetMapping("updateNode")
    public void updateNode(String id, String properties) throws Exception {
        Map<String, String> error = new HashedMap();//错误信息收集池
        if (id == null || "".equals(id)) {
            error.put("id", "error:更新ID为null");
        }
        if (properties == null || "".equals(properties)) {
            error.put("properties", "error:更新properties为null");
        }
        String cql = "MATCH (s)  WHERE ID(s) = " + id + " set ";

        Map propertiesMap = JSON.parseObject(properties);
        Iterator iterator = propertiesMap.keySet().iterator();
        while (iterator.hasNext()) {
            String propertieskey = iterator.next().toString();
            String propertiesValue = String.valueOf(propertiesMap.get(propertieskey));
            cql = cql + "s." + propertieskey + "='" + propertiesValue + "',";
        }
        cql = cql.substring(0, cql.length() - 1);
        kgFusionService.updateNodeById(cql);
    }

    /**
     * 更新node节点
     */
    @GetMapping("addNode")
    public void addNode(String type, String properties,String userc,String model) throws Exception {
        Map<String, String> error = new HashedMap();//错误信息收集池
        if (type == null || "".equals(type)) {
            error.put("type", "error:更新type为null");
        }
        if (properties == null || "".equals(properties)) {
            error.put("properties", "error:更新properties为null");
        }
        String cql = "MERGE(:"+type+"{<properties>})";

        Map propertiesMap = JSON.parseObject(properties);
        Iterator iterator = propertiesMap.keySet().iterator();
        String propertiesString  = "";
        while (iterator.hasNext()) {
            String propertieskey = iterator.next().toString();
            String propertiesValue = String.valueOf(propertiesMap.get(propertieskey));
            propertiesString = propertiesString + propertieskey + ":'" + propertiesValue + "',";
        }
        String usern = userLogicService.findUserByCookies(userc);
        propertiesString = propertiesString + "usern:'"+usern+"',model:'"+model+"'";
        cql = cql.replace("<properties>",propertiesString);
        kgFusionService.updateNodeById(cql);
    }

    /**
     * 删除node节点属性
     */
    @GetMapping("delNodeProperty")
    public void delNodeProperty(String id, String property) throws Exception {
        Map<String, String> error = new HashedMap();//错误信息收集池
        if (id == null || "".equals(id)) {
            error.put("id", "error:更新ID为null");
        } else if (property == null || "".equals(property)) {
            error.put("property", "error:删除property为null");
        }
        String cql = "MATCH (s)  WHERE ID(s) = " + id + " remove s." + property;
        kgFusionService.updateNodeById(cql);
    }
}
