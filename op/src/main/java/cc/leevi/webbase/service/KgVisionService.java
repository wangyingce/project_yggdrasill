package cc.leevi.webbase.service;

import cc.leevi.webbase.utils.DateUtils;
import cc.leevi.webbase.utils.OperBase1Utils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service

public class KgVisionService {

    @Autowired
    @Qualifier("neo4jJdbcTemplate")
    protected JdbcTemplate neo4jJdbcTemplate;

    /**
     * 遍历被保险人所有关联节点，base-1模型
     * @param insuredName
     * @param insuredCode
     */
    @Transactional(transactionManager = "neo4jTransactionManager")
    public List <Map <String, Object>> findAllDataByInsuredName(String insuredName, String insuredCode) {
        return neo4jJdbcTemplate.queryForList(OperBase1Utils.getAllNodeByNameT(insuredName,insuredCode));
    }


    /**
     * 获取base-1模型的antV标准json
     * @param inputJson
     * @param base1NodeX
     * @param base1NodeY
     * @return
     */
    public String getBase1StandJson(JSONObject inputJson, Integer base1NodeX, Integer base1NodeY,Map<String,String>avjProperty) {
        /**分流数据类型*/
        if(inputJson.containsKey("_labels")){
            String nodeType = inputJson.get("_labels").toString();
            String nodeValue = inputJson.get("name").toString();
            String nodeId = inputJson.get("_id").toString();
            /**返回去重复的节点数据*/
            if(avjProperty.get("id")==null||"".equals(avjProperty.get("id"))||avjProperty.get("id").indexOf("["+nodeId+"]")<0){
                avjProperty.put("id",avjProperty.get("id")+"["+nodeId+"]");
                if(nodeType.indexOf("被保险人")>0){
                    if(avjProperty.get("ins")!=null&&!"".equals(avjProperty.get("ins"))){
                        int mul = avjProperty.get("ins").split("]").length;
                        base1NodeX = mul*200;
                        base1NodeY=50;
                        avjProperty.put("ins",avjProperty.get("ins")+"["+nodeValue+"]");
                    }else{
                        base1NodeX=50;
                        base1NodeY=50;
                        avjProperty.put("ins","["+nodeValue+"]");
                    }
                }
                if(nodeType.indexOf("保单号")>0){
                    if(avjProperty.get("ply")!=null&&!"".equals(avjProperty.get("ply"))){
                        int mul = avjProperty.get("ply").split("]").length;
                        base1NodeX = mul*200;
                        base1NodeY=200;
                        avjProperty.put("ply",avjProperty.get("ply")+"["+nodeValue+"]");
                    }else{
                        base1NodeX=50;
                        base1NodeY=200;
                        avjProperty.put("ply","["+nodeValue+"]");
                    }
                }
                if(nodeType.indexOf("赔案号")>0){
                    if(avjProperty.get("clm")!=null&&!"".equals(avjProperty.get("clm"))){
                        int mul = avjProperty.get("clm").split("]").length;
                        base1NodeX = mul*200;
                        base1NodeY=400;
                        avjProperty.put("clm",avjProperty.get("clm")+"["+nodeValue+"]");
                    }else{
                        base1NodeX=50;
                        base1NodeY=400;
                        avjProperty.put("clm","["+nodeValue+"]");
                    }
                }
                if(nodeType.indexOf("报案电话")>0){
                    if(avjProperty.get("mbl")!=null&&!"".equals(avjProperty.get("mbl"))){
                        int mul = avjProperty.get("mbl").split("]").length;
                        base1NodeX = 100;
                        base1NodeY=600+(mul*200);
                        avjProperty.put("mbl",avjProperty.get("mbl")+"["+nodeValue+"]");
                    }else{
                        base1NodeX= 100;
                        base1NodeY= 500;
                        avjProperty.put("mbl","["+nodeValue+"]");
                    }
                }
                if(nodeType.indexOf("收款人")>0){
                    if(avjProperty.get("pyn")!=null&&!"".equals(avjProperty.get("pyn"))){
                        int mul = avjProperty.get("pyn").split("]").length;
                        base1NodeX = 300;
                        base1NodeY=600+(mul*200);
                        avjProperty.put("pyn",avjProperty.get("pyn")+"["+nodeValue+"]");
                    }else{
                        base1NodeX= 300;
                        base1NodeY= 500;
                        avjProperty.put("pyn","["+nodeValue+"]");
                    }
                }
                if(nodeType.indexOf("银行卡")>0){
                    if(avjProperty.get("pyc")!=null&&!"".equals(avjProperty.get("pyc"))){
                        int mul = avjProperty.get("pyc").split("]").length;
                        base1NodeX = 500;
                        base1NodeY=600+(mul*200);
                        avjProperty.put("pyc",avjProperty.get("pyc")+"["+nodeValue+"]");
                    }else{
                        base1NodeX= 500;
                        base1NodeY= 500;
                        avjProperty.put("pyc","["+nodeValue+"]");
                    }

                }
                /**定义标准antV-G6格式*/
                String base1NodeStand = "\"node\",{ \"id\": \"<id>\", \"shape\":\"circle\", \"label\": \"<name>\",\"labelCfg\": { \"position\": \"bottom\" },\"style\": { \"fill\": \"#c27ba0\", \"stroke\": \"#c27ba0\" }, \"x\": \"<x>\", \"y\": \"<y>\" }";
                /**赋值返回*/
                return base1NodeStand.replace("<id>",nodeId).replace("<name>",nodeValue).replace("<x>",base1NodeX.toString()).replace("<y>",base1NodeY.toString());
            }else{
                return null;
            }
        }else{
            /**定义标准antV-G6格式*/
            String base1LineStand = "\"edge\", { \"id\": \"<id>\", \"source\": \"<source>\", \"target\": \"<target>\", \"label\": \"<label>\",\"style\": {\"endArrow\": \"true\",\"stroke\": \"grey\"},\"labelCfg\": {\"autoRotate\": \"true\", \"style\": {\"stroke\": \"white\",\"lineWidth\": \"5\",\"fill\": \"grey\"}}}";
            String lineStartId = inputJson.get("_startId").toString();
            String lineEndId = inputJson.get("_endId").toString();
            String lineValue = inputJson.get("_type").toString();
            String lineId = inputJson.get("_id").toString();
            /**赋值返回*/
            return base1LineStand.replace("<id>",lineId).replace("<source>",lineStartId).replace("<target>",lineEndId).replace("<label>",lineValue);
        }
    }

    /**
     * 报案号查被保险人和身份证
     * @param registNo
     * @return
     */
    public Map <String, String> findInsDataByRegistNo(String registNo) {
        List <Map <String, Object>> insListMap = neo4jJdbcTemplate.queryForList(OperBase1Utils.getInsByRegist(registNo));
        Map <String, String> insMap = getResultValue(insListMap);
        return insMap;
    }

    public Map<String, String> findInsDataByPlyNo(String plyNo) {
        List <Map <String, Object>> insListMap = neo4jJdbcTemplate.queryForList(OperBase1Utils.getInsByPly(plyNo));
        Map <String, String> insMap = getResultValue(insListMap);
        return insMap;
    }

    private Map<String, String> getResultValue(List<Map<String, Object>> insListMap) {
        Map <String, String> insMap = new HashMap <>();
        if(insListMap!=null&&insListMap.size()>0){
            for(Map<String, Object> ins:insListMap){
                String insName = ins.get("姓名")+"";
                String insCode = ins.get("身份证号")+"";
                insMap.put("insCode",insCode);
                insMap.put("insName",insName);
            }
        }
        return insMap;
    }

    public Map<String, String> findInsDataByInsCd(String insuredCode) {
        List <Map <String, Object>> insListMap = neo4jJdbcTemplate.queryForList(OperBase1Utils.getInsByInsCd(insuredCode));
        Map <String, String> insMap = getResultValue(insListMap);
        return insMap;
    }

    public Map<String, String> findInsDataByInsNm(String insuredName) {
        List <Map <String, Object>> insListMap = neo4jJdbcTemplate.queryForList(OperBase1Utils.getInsByInsNm(insuredName));
        Map <String, String> insMap = getResultValue(insListMap);
        return insMap;
    }
}
