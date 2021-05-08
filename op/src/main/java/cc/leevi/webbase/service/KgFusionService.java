package cc.leevi.webbase.service;

import cc.leevi.webbase.utils.*;
import cc.leevi.webbase.vo.BaikeDataVo;
import cc.leevi.webbase.vo.PumpClaimDataVo;
import cc.leevi.webbase.vo.PumpClaimMapVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Service
@Transactional
public class KgFusionService {

    @Autowired
    @Qualifier("neo4jJdbcTemplate")
    protected JdbcTemplate neo4jJdbcTemplate;

    HashMap<String, String> linesMap = new HashMap<String, String>();
    PumpClaimMapVo initlaimMapVo =  new PumpClaimMapVo();

    /**
     * 创建抽水泵的索引，KG-base-1
     */
    public void createPumpIndex() {
        try {
            //如果查询不到管理账号则说明未有数据，手动创建索引与管理员账号
            List<Map<String, Object>> findList = neo4jJdbcTemplate.queryForList("MATCH (na:User) where na.username='admin' return na");
            if(findList==null||findList.size()<=0){
                // 创建索引
                neo4jJdbcTemplate.update("CREATE INDEX ON :被保险人(name)");
                neo4jJdbcTemplate.update("CREATE INDEX ON :保单号(name)");
                neo4jJdbcTemplate.update("CREATE INDEX ON :赔案号(name)");
                neo4jJdbcTemplate.update("CREATE INDEX ON :收款人(name)");
                neo4jJdbcTemplate.update("CREATE INDEX ON :银行卡(name)");
                neo4jJdbcTemplate.update("CREATE INDEX ON :报案电话(name)");
                neo4jJdbcTemplate.update("CREATE INDEX ON :User(name)");
                neo4jJdbcTemplate.update("CREATE INDEX ON :User(username)");
                neo4jJdbcTemplate.update("CREATE (na:User{username:'admin',name:'管理员',role:'1',password:{1}}) ", MD5Utils.MD5Encode("admin", "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建KG-base-1节点
     * @throws SQLException
     */
    public void createNodeKGBase1re(List<PumpClaimDataVo> pumps) {
        StringBuffer line = new StringBuffer();
        try {
            for(PumpClaimDataVo pump:pumps){
                createNodesRe("被保险人",pump,line);
                createNodesRe("保单号",pump,line);
                createNodesRe("赔案号",pump,line);
                createNodesRe("收款人",pump,line);
                createNodesRe("银行卡",pump,line);
                createNodesRe("报案电话",pump,line);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建KG-base-1节点
     * @param nodeName
     * @param pump
     * @param line
     * @throws SQLException
     */
    private void createNodesRe(String nodeName, PumpClaimDataVo pump, StringBuffer line) throws SQLException {
        line.append("MERGE (:"+nodeName);
        if("被保险人".equals(nodeName)){
            line.append(" {name:'" + pump.getWeb_Clm_Accdnt().getC_Insured_Nme() + "' ");
        }
        if("保单号".equals(nodeName)){
            line.append(" {name:'" + pump.getWeb_Clm_Ply_Base().getC_Ply_No() + "' ");
            line.append(" ,保单起期:'"+ DateUtils.dateToString(pump.getWeb_Clm_Ply_Base().getT_Insrnc_Bgn_Tm(),3) + "' ");
            line.append(" ,保单止期:'"+DateUtils.dateToString(pump.getWeb_Clm_Ply_Base().getT_Insrnc_End_Tm(),3) + "' ");
        }
        if("赔案号".equals(nodeName)){
            line.append(" {name:'" + pump.getWeb_Clm_Rpt().getC_Clm_No() + "' ");
            line.append(" ,报案时间:'"+DateUtils.dateToString(pump.getWeb_Clm_Rpt().getT_Rpt_Tm(),3) + "' ");
            line.append(" ,出险时间:'"+DateUtils.dateToString(pump.getWeb_Clm_Accdnt().getT_Accdnt_Tm(),3) + "' ");
            line.append(" ,赔付类型:'"+pump.getWeb_Clm_Adjust().getC_Pay_Liability() + "' ");
        }
        if("收款人".equals(nodeName)){
            line.append(" {name:'" + pump.getWeb_Clm_Bank().getC_Payee_Nme() + "' ");
        }
        if("银行卡".equals(nodeName)){
            line.append(" {name:'" + pump.getWeb_Clm_Bank().getC_Payee_No() + "' ");
        }

        if("报案电话".equals(nodeName)){
            line.append(" {name:'" + pump.getWeb_Clm_Rpt().getC_Rptman_Tel() + "' ");
        }
//        if("赔付类型".equals(nodeName)){
//            line.append(" {name:'" + pump.getWeb_Clm_Adjust().getC_Pay_Liability() + "' ");
//        }
        line.append( "})");
        neo4jJdbcTemplate.update(line.toString());
    }
    /**
     * 创建KG-base-1节点关系，连线
     * @param node1Name：节点2名
     * @param node1Value：节点1值
     * @param lineName：线说明
     * @param node2Name：节点2名
     * @param node2Value：节点2值
     */
    private void createKGLine(String node1Name,String node1Value,String lineName,String node2Name,String node2Value){
        if(node1Value==null||"".equals(node1Value)||"NA".equals(node1Value)){
            node1Value = "空"+node1Name;
        }
        if(node2Value==null||"".equals(node2Value)||"NA".equals(node2Value)){
            if(node2Name.endsWith("时间")){
                node2Value = "1999-01-01 00:00:00";
            }
            node2Value = "空"+node2Name;
        }
        linesMap.put("MATCH (aa:"+node1Name+" {name:'" + node1Value + "'}), (bb:"+node2Name+" {name:'" + node2Value
                + "'}) MERGE (aa) -[:"+lineName+"{name:''}]-> (bb)", "");
    }

    /**
     * 创建KG-base-1节点关系，连线
     */
    public void createLineKGBase1(List<PumpClaimDataVo> pumps){
        /**KG节点连线*/
        for(PumpClaimDataVo pump:pumps){
            String insNme = StringUtils.toStandStrings(pump.getWeb_Clm_Accdnt().getC_Insured_Nme());
            String plyNo = StringUtils.toStandStrings(pump.getWeb_Clm_Ply_Base().getC_Ply_No());
            String clmNo = StringUtils.toStandStrings(pump.getWeb_Clm_Rpt().getC_Clm_No());
            String payNme = StringUtils.toStandStrings(pump.getWeb_Clm_Bank().getC_Payee_Nme());
            String PayNo = StringUtils.toStandStrings(pump.getWeb_Clm_Bank().getC_Payee_No());
            String rptTel = StringUtils.toStandStrings(pump.getWeb_Clm_Rpt().getC_Rptman_Tel());

            createKGLine("被保险人",insNme,"拥有保单","保单号",plyNo);
            createKGLine("保单号",plyNo,"发生赔案","赔案号",clmNo);
            createKGLine("赔案号",clmNo,"报案电话是","报案电话",rptTel);
            createKGLine("赔案号",clmNo,"赔款支付给","收款人",payNme);
            createKGLine("收款人",payNme,"银行卡是","银行卡",PayNo);
        }
        Set<String> set = linesMap.keySet();
        for (String string : set) {
            neo4jJdbcTemplate.update(string);
        }
    }

    /***
     * loadCVS批量导数
     * @param filePath：cvs文件路径
     */
    public void createModelsBase1(String filePath) {
        /**load cvs to creating nodes*/
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateNodes(filePath,"n1"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateNodes(filePath,"n2"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateNodes(filePath,"n3"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateNodes(filePath,"n4"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateNodes(filePath,"n5"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateNodes(filePath,"n6"));

        /**load cvs to writing lines*/
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateLines(filePath,"l1"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateLines(filePath,"l2"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateLines(filePath,"l3"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateLines(filePath,"l4"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateLines(filePath,"l5"));
    }

    public void createBaikeRelation(Map<String, BaikeDataVo> baikeAllMap, String title) {
        Set<String> set = baikeAllMap.keySet();
        for (String keyString : set) {
//            System.out.println("keyString:"+keyString);
            String father = baikeAllMap.get(keyString).getFather();
            StringBuffer relationCql =  new StringBuffer("");
            if(father!=null&&!"".equals(father)){
                relationCql.append("MATCH (aa:节点 {name:'" + baikeAllMap.get(father).getName() + "'}), (bb:节点 {name:'" + baikeAllMap.get(keyString).getName()+ "'}) MERGE (aa) -[:包含{name:''}]-> (bb)");
            }else{
                relationCql.append("MATCH (aa:标题 {name:'" + title + "'}), (bb:节点 {name:'" + baikeAllMap.get(keyString).getName()+ "'}) MERGE (aa) -[:包含{name:''}]-> (bb)");
            }
            neo4jJdbcTemplate.update(relationCql.toString());
        }
    }

    public void createBaikeNode(BaikeDataVo baikeVo) {
//        System.out.println("value:"+baikeVo.getValue());
        StringBuffer nodeCql = new StringBuffer("");
        nodeCql.append("MERGE (:节点 {name:'" + baikeVo.getName() + "',value:'"+baikeVo.getValue()+"',url:'"+ baikeVo.getUrl() + "' })");
        neo4jJdbcTemplate.update(nodeCql.toString());
    }

    public void createBaikeTitle(String title,String titleValue) {
        List <Map <String, Object>> findList = neo4jJdbcTemplate.queryForList("MATCH (na:标题) where na.name='" + title + "' return na");
        if (findList == null || findList.size() <= 0) {
            neo4jJdbcTemplate.update("CREATE (na:标题{name:'" + title + "',titleValue:'"+titleValue+"'}) ");
        }
    }

    public void createModelsBaseT(String filePath) {
        System.out.println("all node start at:"+DateUtils.dateToString(new Date(), 3));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateNodest(filePath,"n1"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateNodest(filePath,"n2"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateNodest(filePath,"n3"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateNodest(filePath,"n4"));
        System.out.println("all line start at:"+DateUtils.dateToString(new Date(), 3));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateLiness(filePath,"l1"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateLiness(filePath,"l2"));
        neo4jJdbcTemplate.update(OperBase1Utils.lcCreateLiness(filePath,"l3"));
        System.out.println("all line end at:"+DateUtils.dateToString(new Date(), 3));
    }

    public void bayesModelsInjection(String filePath) {
        System.out.println("bayesLc-2 start:"+DateUtils.dateToString(new Date(), 3));

        System.out.println("bayesLc-2 end:"+DateUtils.dateToString(new Date(), 3));

    }

    public void updateNodeById(String cql) {
        neo4jJdbcTemplate.update(cql);
    }

    /****
     * 用loadcsv的方式分别创建node和edges，并且保存loadcsv的cql模版
     * @param modelMap
     */
    public void createFreeModels(Map modelMap) {
        JSONArray nodesJsonList = (JSONArray) modelMap.get("nodes");
        String modelname = String.valueOf(modelMap.get("user")) + "_" + String.valueOf(modelMap.get("modelname"));
        Map<String,String> resMap = new HashMap();

        String lcnc = "";
        Map<String,String> lcResMap = new HashMap();

        for(Object nodeJson : nodesJsonList){
            String createNodeCql = "MERGE(:<type>{<properties>})";
            JSONObject inputJson = JSON.parseObject(String.valueOf(nodeJson));
            String type = inputJson.get("type").toString();

            /**loadcsv 的cql**/
            lcnc = createNodeCql = createNodeCql.replace("<type>",type);
//            String properties =  "name:'"+ inputJson.get("name").toString() + "'";
            String properties = "";
            String lccnp = "";

            if(inputJson.get("properties")!=null&&!"".equals(inputJson.get("properties"))) {
                JSONObject propertiesJson = (JSONObject) inputJson.get("properties");
                Iterator iterator = propertiesJson.keySet().iterator();
                while(iterator.hasNext()){
                    String kname = iterator.next().toString();
                    String pname = type+"_"+kname;
                    String pvalue = (String) propertiesJson.get(kname);
                    properties = properties + "," +kname+":'"+pvalue+"'";
                    /**生成loadcsv属性的cql语句然后直接存起来与模版匹配**/
                    lccnp = lccnp + kname+":line." + pname+",";
                }
            }
            properties  = properties.substring(1,properties.length());

            properties = properties + ",model:'"+modelname+"'";//添加模版标签
            resMap.put(inputJson.get("id").toString(),":"+type+"{"+properties+"}");
            createNodeCql = createNodeCql.replace("<properties>",properties);

            lccnp = lccnp.substring(0,lccnp.length()-1);
            lccnp = lccnp + ",model:line."+modelname;//添加模版标签
            lcnc = lcnc.replace("<properties>",lccnp);
            lcResMap.put(inputJson.get("id").toString(),":"+type+"{"+lccnp+"}");
            System.out.println("createNodeCql:"+createNodeCql+"_"+DateUtils.dateToString(new Date(), 3));
            System.out.println("loadcsvNodeCql:"+lcnc+"_"+DateUtils.dateToString(new Date(), 3));
            neo4jJdbcTemplate.update(createNodeCql);
        }

        JSONArray edgesJsonList = (JSONArray) modelMap.get("edges");
        String lcec = "";
        for(Object edgesJson : edgesJsonList){
            String createEdgeCql = lcec = "MATCH (aa<sidA>), (bb<sidB>) MERGE (aa) -[:<type>]-> (bb)";
            JSONObject inputJson = JSON.parseObject(String.valueOf(edgesJson));
            String type = inputJson.get("type").toString();

            /**创建边cql**/
            String sidA = resMap.get(inputJson.get("source").toString());
            String sidB = resMap.get(inputJson.get("target").toString());
            createEdgeCql = createEdgeCql.replace("<sidA>",sidA).replace("<sidB>",sidB).replace("<type>",type);

            /**loadcsv边的cql**/
            String lcSidA = lcResMap.get(inputJson.get("source").toString());
            String lcSidB = lcResMap.get(inputJson.get("target").toString());
            lcec = lcec.replace("<sidA>",lcSidA).replace("<sidB>",lcSidB).replace("<type>",type);

            System.out.println("createEdgeCql:"+createEdgeCql+"_"+DateUtils.dateToString(new Date(), 3));
            System.out.println("loadcsvEdgeCql:"+lcec+"_"+DateUtils.dateToString(new Date(), 3));
            neo4jJdbcTemplate.update(createEdgeCql);
        }
    }

    public void RunAPOC(String modelString) {
        neo4jJdbcTemplate.update(modelString);
        System.out.println("RunAPOC:sus"+"_"+DateUtils.dateToString(new Date(), 3));
    }

    public void createCSV(Map modelMap) throws IOException {
        JSONArray nodesJsonList = (JSONArray) modelMap.get("nodes");
        String modelname = String.valueOf(modelMap.get("modelname"));
        Map<String, String> propertyNode = new HashMap<>();
        for(Object nodeJson : nodesJsonList){
            JSONObject inputJson = JSON.parseObject(String.valueOf(nodeJson));
            String type = inputJson.get("type").toString();
            JSONObject propertiesJson = (JSONObject) inputJson.get("properties");
            Iterator iterator = propertiesJson.keySet().iterator();
            while(iterator.hasNext()){
                String kname = iterator.next().toString();
                String pname = type+"_"+kname;
                String pvalue = (String) propertiesJson.get(kname);
                propertyNode.put(pname,pvalue);
            }
        }
        propertyNode.put("model",modelname);
        JSONArray propertyList = new JSONArray();
        propertyList.add(JSON.parseObject(JSON.toJSONString(propertyNode)));
        String s = ConvertCsv.toString(propertyList);
        FileUtils.writeStringToFile(new File("./"+modelname+".csv"), s);
    }

    public void updatePageRankScore() {
        neo4jJdbcTemplate.update("MATCH (n:收款人) WITH collect(n) as nodes CALL apoc.algo.pageRank(nodes) YIELD node,score SET node.pageRankScore=score");
        neo4jJdbcTemplate.update("MATCH (n:报案人) WITH collect(n) as nodes CALL apoc.algo.pageRank(nodes) YIELD node,score SET node.pageRankScore=score");

    }
}
