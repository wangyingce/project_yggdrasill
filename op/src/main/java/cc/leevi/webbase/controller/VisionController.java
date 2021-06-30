package cc.leevi.webbase.controller;

import cc.leevi.webbase.constants.KGConstants;
import cc.leevi.webbase.service.KgVisionService;
import cc.leevi.webbase.service.UserLogicService;
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
    private UserLogicService userLogicService;
    private Map<String,String> nodeIdMap =new HashMap<String,String>(0);//去重节点id
    private Map<String,String> edgeIdMap =new HashMap<String,String>(0);//去重关系id
    Map<String, String> nodeTypeMap = new HashMap <>();
    Integer nodeColor = 1;

//    @GetMapping("showGraph")
//    public Object showGraph(String insuredName, String insuredCode, String token) throws Exception{
//        if(TokenUtils.base1TokenValid(token)){
//            /**去重获取图谱数据**/
//            /**组建客户画像**/
//        }else{
//            Map<String,String> error= new HashedMap();
//            error.put("error","令牌错误");
//            return toJSON(error);
//        }
//        return null;
//    }

    @GetMapping("showUserKg")
    public Object showUserKg(String userc) throws Exception {
        Map<String, String> msg = new HashedMap();
        /**校验用户有效性**/
        String usern  = userLogicService.findUserByCookies(userc);
        if(usern==null||"".equals(usern)||"undefined"==usern){
            return toJSON(msg.put("error", "用户失效，请重新登录"));
        }
        /**组织数据**/
        List<Map<String, Object>> usernKg = kgVisionService.findKgByUsern(usern);
        if (usernKg == null || usernKg.size() <= 0) {
            msg.put("error", "e:usern+model未查询到任何数据");
            return toJSON(msg);
        }
        return StructuredData(usernKg);
    }

    @GetMapping("showUserModelKg")
    public Object showUserModelKg(String userc,String model) throws Exception {
        Map<String, String> msg = new HashedMap();
        /**校验user&model有效性**/
        String usern  = userLogicService.findUserByCookies(userc);
        if(usern==null||"".equals(usern)||"undefined"==usern){
            return toJSON(msg.put("error", "用户失效，请重新登录"));
        }
        if (model == null || model == "") {
            msg.put("error", "e:model为null");
            return toJSON(msg);
        }
        /**组织数据**/
        List<Map<String, Object>> usernModelKg = kgVisionService.findKgByUsernModel(usern,model);
        if (usernModelKg == null || usernModelKg.size() <= 0) {
            msg.put("error", "e:usernModelKg未查询到任何数据");
            return toJSON(msg);
        }
        return StructuredData(usernModelKg);
    }




    @GetMapping("visionAllData")
    public Object visionAllData(String userc,String model) throws Exception {
        /**test 20210517 wyc**/
//        userc= "39e9180c50d1445d98f7198b0dcc9c2a";
//        model = "火地系统";
        Map<String, String> message = new HashedMap();
        String usern  = userLogicService.findUserByCookies(userc);
        /**比对cookie校验用户**/
        if(usern==null||"".equals(usern)||"undefined"==usern){
            message.put("error", "用户失效，请重新登录");
            return toJSON(message);
        }
        if (model == null || model == "") {
            message.put("error", "e:model为null");
            return toJSON(message);
        }

        List<Map<String, Object>> allTemplateData = kgVisionService.findKgByUsernModel(usern,model);
        if (allTemplateData == null || allTemplateData.size() <= 0) {
            message.put("error", "e:usern+model未查询到任何数据");
            return toJSON(message);
        }
        return StructuredData(allTemplateData);
//        String dataGroup = JSON.toJSONString(allTemplateData);
//        JSONArray datasList = (JSONArray) JSONArray.parse(dataGroup);
//        for(Object obj : datasList){
//            JSONObject dataList = JSON.parseObject(String.valueOf(obj));
//            Iterator iterator = dataList.keySet().iterator();
//            while(iterator.hasNext()){
//                JSONArray jsonsList = dataList.getJSONArray(String.valueOf(iterator.next()));
//                for(Object json : jsonsList){
//                    JSONObject inputJson = JSON.parseObject(String.valueOf(json));
//                    getStandardJson(stdjsMap,inputJson);
//                }
//            }
//        }
//        nodeIdMap.clear();
//        edgeIdMap.clear();
//        return toJSON(stdjsMap);
    }

    private Object StructuredData(List<Map<String, Object>> paraMap) {
        /**创建结果数据容器*/
        Map<String, List<Map<String, Object>>> rstMap = new HashedMap();
        String dataGroup = JSON.toJSONString(paraMap);
        JSONArray datasList = (JSONArray) JSONArray.parse(dataGroup);
        for(Object obj : datasList){
            JSONObject dataList = JSON.parseObject(String.valueOf(obj));
            Iterator iterator = dataList.keySet().iterator();
            while(iterator.hasNext()){
                JSONArray jsonsList = dataList.getJSONArray(String.valueOf(iterator.next()));
                for(Object json : jsonsList){
                    JSONObject inputJson = JSON.parseObject(String.valueOf(json));
                    /**标准化数据**/
                    getStandardJson(rstMap,inputJson);
                }
            }
        }
        /**清空id容器**/
        nodeIdMap.clear();
        edgeIdMap.clear();
        return toJSON(rstMap);
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
//                propertiesMap.put("type",nodeType);
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
}
