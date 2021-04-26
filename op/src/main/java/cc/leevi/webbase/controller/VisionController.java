package cc.leevi.webbase.controller;

import cc.leevi.webbase.constants.KGConstants;
import cc.leevi.webbase.service.KgFusionService;
import cc.leevi.webbase.service.KgInferenceService;
import cc.leevi.webbase.service.KgVisionService;
import cc.leevi.webbase.constants.KGConstants;
import cc.leevi.webbase.utils.TokenUtils;
import cc.leevi.webbase.vo.KgspInfoVo;
import cc.leevi.webbase.vo.KgspVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import static com.alibaba.fastjson.JSON.toJSON;


/**
 * 展示base-1模型
 * @author WangYingce
 *
 */
@RestController
@RequestMapping
public class VisionController {
    @Autowired
    private KgVisionService kgVisionService;
    @Autowired
    private KgInferenceService kgInferenceService;
    @Autowired
    private KgFusionService kgFusionService;
    private Map<String,String> nodeIdMap =new HashMap<String,String>(0);//去重节点id
    private Map<String,String> edgeIdMap =new HashMap<String,String>(0);//去重关系id
    Map<String, String> nodeTypeMap = new HashMap <>();
    Integer nodeColor = 1;

    @GetMapping("showGraph")
    public Object showGraph(String insuredName, String insuredCode, String token) throws Exception{
        if(TokenUtils.base1TokenValid(token)){
            /**去重获取图谱数据**/
            /**组建客户画像**/
        }else{
            Map<String,String> error= new HashedMap();
            error.put("error","令牌错误");
            return toJSON(error);
        }
        return null;
    }

    /***
     * KG结论信息
     * @param registNo
     * @param token
     * @return
     * @throws Exception
     */
    @GetMapping("kgspInfo")
    public Object kgspInfo(String registNo,String token) throws Exception {
//        kgFusionService.updatePageRankScore();
        Map<String, String> error = new HashedMap();//错误信息收集池
        if (!TokenUtils.base1TokenValid(token)) {//过滤无令牌数据
            error.put("error", "error:错误的令牌");
            return toJSON(error);
        }
        if (registNo == null || "".equals(registNo)) {//过滤无参数数据
            error.put("error", "error:错误的参数");
            return toJSON(error);
        }
        if (!((registNo.substring(7, 13)).startsWith("07"))&&!((registNo.substring(7, 13)).startsWith("06"))) {//）非07险类过滤
            error.put("error", "error:目前只支持06、07险类的数据查看");
            return toJSON(error);
        }
        Map<String, String> insMap = kgVisionService.findInsDataByRegistNo(registNo);
        if (insMap.get("insCode") == null || "".equals(insMap.get("insCode")) || insMap.get("insName") == null || "".equals(insMap.get("insName"))) {//过滤报案号查不到的被保险人的数据
            error.put("error", "error:未查询到赔案号对应的被保险人");
            return toJSON(error);
        }
        KgspVo kgspVo = new KgspVo();
        kgspVo.setInsCode(insMap.get("insCode"));
        kgspVo.setInsName(insMap.get("insName"));
        List<KgspInfoVo> kgspInfos = kgInferenceService.findPageRankByIns(kgspVo.getInsName(),kgspVo.getInsCode());
        kgspVo.setKgspInfoVo(kgspInfos);
        kgspVo.setKgUrl("http://10.6.6.118:8080/web.html?insCode="+kgspVo.getInsCode()+"&insName="+kgspVo.getInsName());
        return toJSON(kgspVo);
    }

    @GetMapping("visionAllData")
    public Object visionAllData(String templateName,String para2, String para3) throws Exception {
        Map<String, String> message = new HashedMap();
        Map<String, List<Map<String, Object>>> stdjsMap = new HashedMap();
        List<Map<String, Object>> allTemplateData = kgVisionService.findAllDataByTemplateName(templateName);
        if (allTemplateData == null || allTemplateData.size() <= 0) {
            message.put("error", "error:为查询到任何数据");
            return toJSON(message);
        }
        String dataGroup = JSON.toJSONString(allTemplateData);
        JSONArray datasList = (JSONArray) JSONArray.parse(dataGroup);
        for(Object obj : datasList){
            JSONObject dataList = JSON.parseObject(String.valueOf(obj));
            Iterator iterator = dataList.keySet().iterator();
            while(iterator.hasNext()){
                JSONArray jsonsList = dataList.getJSONArray(String.valueOf(iterator.next()));
                for(Object json : jsonsList){
                    JSONObject inputJson = JSON.parseObject(String.valueOf(json));
                    getStandardJson(stdjsMap,inputJson);
                }
            }
        }
        nodeIdMap.clear();
        edgeIdMap.clear();
        return toJSON(stdjsMap);
    }

    private void getStandardJson(Map<String, List<Map<String, Object>>> stdjsTopMap,JSONObject inputJson) {
        /**分流数据类型*/
        if(inputJson.containsKey("_labels")){
            Map<String, Object> nodeMap = new HashMap <>();
            Map<String, Object> propertiesMap = new HashMap <>();
            String nodeType = "";
            String nodeValue = "";
            String nodeId = "";
            Iterator iterator = inputJson.keySet().iterator();
            while(iterator.hasNext()){
                String inputJsonKey = String.valueOf(iterator.next());
                if("name" == inputJsonKey||"name".equals(inputJsonKey)){
                    nodeValue =  (String) inputJson.get(inputJsonKey);
                }else if("_id" == inputJsonKey||"_id".equals(inputJsonKey)){
                    nodeId =  (inputJson.get(inputJsonKey)).toString();
                }else if("_labels" == inputJsonKey||"_labels".equals(inputJsonKey)){
                    nodeType =  String.valueOf(inputJson.get(inputJsonKey));
                    nodeType = nodeType.substring(nodeType.indexOf("[")+2,nodeType.indexOf("]")-1);
                }else{
                    propertiesMap.put(inputJsonKey,(String) inputJson.get(inputJsonKey));
                }
            }
            /**ID去重判断*/
            if(nodeIdMap.get("id")==null||"".equals(nodeIdMap.get("id"))||nodeIdMap.get("id").indexOf("["+nodeId+"]")<0){
                /**样式定义*/
                Map<String, Object> styles = new HashMap <>();
                styles.put("stroke","#fff");//node边框颜色
                /**非重复更新ID记录数据*/
                nodeIdMap.put("id",nodeIdMap.get("id")+"["+nodeId+"]");
                nodeMap.put("id",nodeId);
                /**节点通用属性定义*/
                nodeMap.put("name",nodeValue);
                nodeMap.put("type",nodeType);
                if(nodeTypeMap.get(nodeType)==null){
                    nodeTypeMap.put(nodeType, KGConstants.NodeColorMap.get(nodeColor));
                    styles.put("fill",nodeTypeMap.get(nodeType));
                    nodeColor = nodeColor + 1;
                }else{
                    styles.put("fill",nodeTypeMap.get(nodeType));
                }
                propertiesMap.put("name",nodeValue);

                nodeMap.put("style",styles);
                nodeMap.put("properties",propertiesMap);

                /**第一次遍历新增，其他更新或添加*/
                if(stdjsTopMap.get("nodes")!=null&&!"".equals(stdjsTopMap.get("nodes"))){
                    stdjsTopMap.get("nodes").add(nodeMap);
                }else{
                    List<Map<String, Object>> stdjsList = new ArrayList <>();
                    stdjsList.add(nodeMap);
                    stdjsTopMap.put("nodes",stdjsList);
                }
            }
        }else{
            String lineId = "r"+inputJson.get("_id").toString();
            String lineStartId = inputJson.get("_startId").toString();
            String lineEndId = inputJson.get("_endId").toString();
            String lineType = inputJson.get("_type").toString();
            Map<String, Object> edgesMap = new HashMap <>();
            /**组装边的数据*/
            if(edgeIdMap.get("id")==null||"".equals(edgeIdMap.get("id"))||edgeIdMap.get("id").indexOf("["+lineId+"]")<0){
                edgeIdMap.put("id",edgeIdMap.get("id")+"["+lineId+"]");
                edgesMap.put("id",lineId);
                edgesMap.put("source",lineStartId);
                edgesMap.put("target",lineEndId);
                edgesMap.put("type",lineType);
                edgesMap.put("size","1");
                edgesMap.put("color","#545454");
                if(stdjsTopMap.get("edges")!=null&&!"".equals(stdjsTopMap.get("edges"))){
                    stdjsTopMap.get("edges").add(edgesMap);
                }else{
                    List<Map<String, Object>> stdjsList = new ArrayList <>();
                    stdjsList.add(edgesMap);
                    stdjsTopMap.put("edges",stdjsList);
                }
            }
        }
    }


//    /**
//     * 展示被保险人base-1模型图谱（neo4j to antV-G6）
//     * @param insCode
//     * @param insName
//     * @return
//     * @throws Exception
//     */
//    @GetMapping("visionAllData")
//    public Object visionAllData(String token,String insCode, String insName) throws Exception {
//        Map<String, String> error = new HashedMap();//错误信息收集池
//        /**过滤无参数数据*/
//        if (!TokenUtils.base1TokenValid(token)) {
//            error.put("error", "error:错误的令牌");
//            return toJSON(error);
//        }else if (insCode == null || "".equals(insCode)) {
//            error.put("error", "error:缺少被保险人编号参数");
//            return toJSON(error);
//        }else if (insName == null || "".equals(insName)) {
//            error.put("error", "error:缺少被保险人姓名参数");
//            return toJSON(error);
//        }
//        Map<String, List<Map<String, Object>>> stdjsMap = new HashedMap();
//        List<Map<String, Object>> informationMapList = new ArrayList<Map<String, Object>>();
//        /**加入被保险人、赔案、收款人、报案人统计*/
//        Map<String, Object> insuredMap = new HashMap<String, Object>();
//        Map<String, Object> claimMap = new HashMap<String, Object>();
//        Map<String, Object> payeeMap = new HashMap<String, Object>();
//        Map<String, Object> reportManMap = new HashMap<String, Object>();
//        /**查询被保险人关联的所有节点*/
//        List<Map<String, Object>> allRelationData = kgVisionService.findAllDataByInsuredName(insName, insCode);//过滤无效被保险人
//        if (allRelationData == null || allRelationData.size() <= 0) {
//            error.put("error", "error:未查询到被保险人对应的关联信息");
//            return toJSON(error);
//        }
//        /**拆解neo4j的json数据 -data group层*/
//        String dataGroup = JSON.toJSONString(allRelationData);
//        JSONArray datasList = (JSONArray) JSONArray.parse(dataGroup);
//        for(Object obj : datasList){
//            JSONObject dataList = JSON.parseObject(String.valueOf(obj));
//            Iterator iterator = dataList.keySet().iterator();
//            while(iterator.hasNext()){
//                /**取出所有节点与边的json串*/
//                JSONArray jsonsList = dataList.getJSONArray(String.valueOf(iterator.next()));
//                for(Object json : jsonsList){
//                    JSONObject inputJson = JSON.parseObject(String.valueOf(json));
//                    /**将单个json串转换为antV-G6的格式*/
//                    getBase1StandJsonRe(stdjsMap,inputJson,nodeIdMap,edgeIdMap,insName,insuredMap,claimMap,payeeMap,reportManMap);
//                }
//            }
//        }
//        nodeIdMap.clear();
//        edgeIdMap.clear();
//        /**加入信息MAP*/
//        Map<String,Object> ifmCypherMap = new HashMap<String,Object>();
//        String countNodeInfo = "关联被保险人"+(insuredMap.size()-1)+"个，关联赔案"+claimMap.size()+"个，关联收款人"+payeeMap.size()+"个，关联报案人"+reportManMap.size()+"个";
//        ifmCypherMap.put("cypher", OperBase1Utils.getAllNodeByNameT(insName,insCode));
//        ifmCypherMap.put("insured",insuredMap);
//        ifmCypherMap.put("claim",claimMap);
//        ifmCypherMap.put("payee",payeeMap);
//        ifmCypherMap.put("reportMan",reportManMap);
//        ifmCypherMap.put("countNodeInfo",countNodeInfo);
//        /**0-4类型的分析*/
//        Map<String,Object> hybridInferentialMap = kgInferenceService.hybridInferentialAnalysis(insName,insCode,ifmCypherMap);
//        informationMapList.add(hybridInferentialMap);
//        stdjsMap.put("informations",informationMapList);
//        /**推理分析部分*/
//        return toJSON(stdjsMap);
//    }

    private List<Map<String,Object>> structureStandJson(List<Map<String, Object>> todoData, String type) {
        String dataGroup = JSON.toJSONString(todoData);
        JSONArray datasList = (JSONArray) JSONArray.parse(dataGroup);
        for(Object obj : datasList){
            JSONObject dataList = JSON.parseObject(String.valueOf(obj));
            Iterator iterator = dataList.keySet().iterator();
            while(iterator.hasNext()){
                /**取出所有节点与边的json串*/
                JSONArray jsonsList = dataList.getJSONArray(String.valueOf(iterator.next()));
                for(Object json : jsonsList){
                    JSONObject inputJson = JSON.parseObject(String.valueOf(json));
                    if("justBodyData".equals(type)||"justBodyData"==type){

                    }
                }
            }
        }
        return null;
    }

    /***
     * 格式化neo4j数据去重和组装antV-G6格式数据
     * @param stdjsTopMap
     * @param inputJson
     * @param nodeIdMap
     * @param edgeIdMap
     * @param insuredMap
     * @param claimMap
     * @param payeeMap
     * @param reportManMap
     */
    private void getBase1StandJsonRe(Map <String, List <Map <String, Object>>> stdjsTopMap, JSONObject inputJson, Map <String, String> nodeIdMap, Map <String, String> edgeIdMap, String insuredName, Map <String, Object> insuredMap, Map <String, Object> claimMap, Map <String, Object> payeeMap, Map <String, Object> reportManMap) {
        /**分流数据类型*/
        if(inputJson.containsKey("_labels")){
            String nodeType = inputJson.get("_labels").toString();
            nodeType = nodeType.substring(nodeType.indexOf("[")+2,nodeType.indexOf("]")-1);
            String nodeValue = inputJson.get("name").toString();
            String nodeId = inputJson.get("_id").toString();
            Map<String, Object> nodeMap = new HashMap <>();
            /**ID去重判断*/
            if(nodeIdMap.get("id")==null||"".equals(nodeIdMap.get("id"))||nodeIdMap.get("id").indexOf("["+nodeId+"]")<0){
                /**非重复更新ID记录数据*/
                nodeIdMap.put("id",nodeIdMap.get("id")+"["+nodeId+"]");
                nodeMap.put("id",nodeId);
                /**节点通用样式定义*/
                Map<String, Object> styles = new HashMap <>();
                styles.put("stroke","#fff");//node边框颜色
//                styles.put("shadowColor","rgba(0,0,0, 0.3)");//阴影颜色
//                styles.put("shadowBlur","3");//阴影大小
//                styles.put("shadowOffsetX","2");//阴影坐标X
//                styles.put("shadowOffsetY","2");//阴影坐标Y
                nodeMap.put("size","20");//node大小
                /**节点通用属性定义*/
                Map<String, Object> labelCfgs = new HashMap <>();
                nodeMap.put("name",nodeValue);
//                labels.add(nodeType);
                nodeMap.put("type",nodeType);
                Map<String, Object> propertiesMap = new HashMap <>();
                propertiesMap.put("name",nodeValue);
                /**定义各种类型的节点*/
                if("被保险人".equals(nodeType)||nodeType=="被保险人"){
                    Map<String, Object> labelCfgsStyle = new HashMap <>();
                    labelCfgs.put("position","middle");
                    labelCfgs.put("style",labelCfgsStyle);
                    nodeMap.put("labelCfg",labelCfgs);
                    if(nodeValue.equals(insuredName)||nodeValue==insuredName){
                        nodeMap.put("label","被保险人("+nodeValue+")");
                        nodeMap.put("size","40");
                        labelCfgsStyle.put("fill","grey");
                        labelCfgsStyle.put("fontSize","14");
//                        labelCfgsStyle.put("lineWidth",5);
                    }
//                    else{
//                        nodeMap.put("label",nodeValue);
//                        nodeMap.put("size","20");
////                        labelCfgsStyle.put("fill","grey");
//                    }
                    styles.put("fill","#BE3430");
//                    styles.put("shadowBlur","3");//阴影大小
//                    styles.put("shadowOffsetX","3");//阴影坐标X
//                    styles.put("shadowOffsetY","5");//阴影坐标Y
//                    nodeMap.put("size","30");//node大小
                    propertiesMap.put("身份证",inputJson.get("身份证号").toString());
                    insuredMap.put(inputJson.get("身份证号").toString(),nodeValue);
//                    statisticsInsList.add(nodeValue);
                }
//                if("保单号".equals(nodeType)||nodeType=="保单号"){
//                    styles.put("fill","#6e7074");
//                    propertiesMap.put("起期",inputJson.get("保险起期").toString());
//                    propertiesMap.put("止期",inputJson.get("保险止期").toString());
//                }
                if("赔案号".equals(nodeType)||nodeType=="赔案号"){
                    styles.put("fill","#2F4554");
                    propertiesMap.put("出险",inputJson.get("出险时间").toString());
                    propertiesMap.put("保单",inputJson.get("保单号").toString());
                    propertiesMap.put("报案",inputJson.get("报案时间").toString());
                    propertiesMap.put("起期",inputJson.get("保险起期").toString());
                    propertiesMap.put("止期",inputJson.get("保险止期").toString());
                    claimMap.put(nodeValue,nodeValue);
                }
                if("收款人".equals(nodeType)||nodeType=="收款人"){
                    styles.put("fill","#609EA6");
                    propertiesMap.put("银行卡",inputJson.get("银行卡").toString());
                    if(inputJson.get("pageRankScore")==null||"".equals(inputJson.get("pageRankScore"))){
                        propertiesMap.put("热度得分","热度得分未更");
                    }else{
                        propertiesMap.put("热度得分",inputJson.get("pageRankScore").toString());
                    }
                    propertiesMap.put("热度得分",inputJson.get("pageRankScore").toString());
                    payeeMap.put(inputJson.get("银行卡").toString(),nodeValue);
                }
//                if("银行卡".equals(nodeType)||nodeType=="银行卡"){
//                    styles.put("fill","#619fa7");
//                }
                if("报案人".equals(nodeType)||nodeType=="报案人"){
                    styles.put("fill","#C98622");
                    propertiesMap.put("报案电话",inputJson.get("报案电话").toString());
                    reportManMap.put(inputJson.get("报案电话").toString(),nodeValue);
                }
                nodeMap.put("properties",propertiesMap);
                nodeMap.put("style",styles);
                /**第一次遍历新增，其他更新或添加*/
                if(stdjsTopMap.get("nodes")!=null&&!"".equals(stdjsTopMap.get("nodes"))){
                    stdjsTopMap.get("nodes").add(nodeMap);
                }else{
                    List<Map<String, Object>> stdjsList = new ArrayList <>();
                    stdjsList.add(nodeMap);
                    stdjsTopMap.put("nodes",stdjsList);
                }
//                statisticsMap.put("被保险人",statisticsInsList);
            }
        }else{
            String lineId = "r"+inputJson.get("_id").toString();
            String lineStartId = inputJson.get("_startId").toString();
            String lineEndId = inputJson.get("_endId").toString();
            String lineType = inputJson.get("_type").toString();

            Map<String, Object> edgesMap = new HashMap <>();
            /**组装边的数据*/
            if(edgeIdMap.get("id")==null||"".equals(edgeIdMap.get("id"))||edgeIdMap.get("id").indexOf("["+lineId+"]")<0){
                edgeIdMap.put("id",edgeIdMap.get("id")+"["+lineId+"]");
                edgesMap.put("id",lineId);
                edgesMap.put("source",lineStartId);
                edgesMap.put("target",lineEndId);
                edgesMap.put("type",lineType);
                edgesMap.put("size","1");
                edgesMap.put("color","#545454");
                if(stdjsTopMap.get("edges")!=null&&!"".equals(stdjsTopMap.get("edges"))){
                    stdjsTopMap.get("edges").add(edgesMap);
                }else{
                    List<Map<String, Object>> stdjsList = new ArrayList <>();
                    stdjsList.add(edgesMap);
                    stdjsTopMap.put("edges",stdjsList);
                }
            }
        }
    }
}
