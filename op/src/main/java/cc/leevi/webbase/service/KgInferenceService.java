package cc.leevi.webbase.service;

import cc.leevi.webbase.utils.DateUtils;
import cc.leevi.webbase.utils.KgBayesUtils;
import cc.leevi.webbase.utils.OperBase1Utils;
import cc.leevi.webbase.vo.KgspInfoVo;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class KgInferenceService {

    @Autowired
    @Qualifier("neo4jJdbcTemplate")
    protected JdbcTemplate neo4jJdbcTemplate;

    /**
     * 规则-1：保险起止期与出险日期过近，返回起期差
     * @param registNo
     * @param policyNo
     * @return
     */
    public String getInferenceKgBase11(String registNo,String policyNo) {
        String messageKgBase = "";
        StringBuffer fk11 = new StringBuffer();
        fk11.append("MATCH (a:赔案号),(b:保单号)  WHERE a.name = '"+registNo+"' AND b.name = '"+policyNo+"' RETURN a.出险时间,b.保险起期,b.保险止期");
        List<Map<String, Object>> ik1List = neo4jJdbcTemplate.queryForList(fk11.toString());
        if(ik1List!=null&&ik1List.size()>0){
            for(Map<String, Object> fk1obj:ik1List){
                Date acctm= DateUtils.stringToDate((String)fk1obj.get("a.出险时间"),102);
                Date plystm= DateUtils.stringToDate((String)fk1obj.get("b.保险起期"),102);
                Date plyetm= DateUtils.stringToDate((String)fk1obj.get("b.保险止期"),102);
                int accbgn = DateUtils.dateCalSubtraction(plyetm,acctm);
                int endacc = DateUtils.dateCalSubtraction(acctm,plystm);
                if(accbgn<2||endacc<2){
                    messageKgBase = "出险时间和保险起止期间隔未超过2天，请关注；";
                }else{
                    messageKgBase = "出险时间和保险起止期间隔超过2天，无异常；";
                }
            }
        }
        return messageKgBase;
    }
    /**
     * 规则-2：多次理赔赔付类型相同
     * @param registNo
     * @param policyNo
     * @param insuredName
     * @return
     */
    public String getInferenceKgBase12(String registNo, String policyNo,String insuredName) {
        String messageKgBase = "";
        StringBuffer ik2 = new StringBuffer();
        ik2.append("MATCH (A:");
        if(insuredName!=null&&!"".equals(insuredName)&&!"NA".equals(insuredName)){
            ik2.append("被保险人{name:'"+insuredName+"'})-[R1]-(B:保单号)-[R2]-(C:赔案号) return C.赔付类型;");
        }else if(policyNo!=null&&!"".equals(policyNo)&&!"NA".equals(policyNo)){
            ik2.append("保单号{name:'"+policyNo+"'})-[R2]-(C:赔案号) return C.赔付类型;");
        }
        List<Map<String, Object>> ik2List = neo4jJdbcTemplate.queryForList(ik2.toString());
        if(ik2List!=null&&ik2List.size()>0&&ik2List.size()>=2){
            messageKgBase = "被保险人赔付类型重复超过2次，请关注;";
        }else{
            messageKgBase = "被保险人赔付类型重复未超过2次，无异常;";
        }
        return messageKgBase;
    }

    /**
     * 补充参数：1-报案号缺失，2-保单号缺失，3-被保险人姓名，4-被保险人证件号
     * @param registNo
     * @param policyNo
     * @param insuredName
     * @param type
     * @return
     */
    public String getBase1Parameter(String registNo, String policyNo, String insuredName, int type) {
        String rst = "";
        if(type==2){
            StringBuffer ik2 = new StringBuffer();
            ik2.append("MATCH (a:被保险人)-[r1]-(b:保单号)-[r2]-(c:赔案号{name:'"+registNo+"'}) RETURN a.name,b.name,b.身份证,c.name");
            List<Map<String, Object>> ik1List = neo4jJdbcTemplate.queryForList(ik2.toString());
            if(ik1List!=null&&ik1List.size()>0) {
                for (Map <String, Object> fk1obj : ik1List) {
                    rst = (String)fk1obj.get("b.name");
                }
            }
        }
        return rst;
    }

    public BigDecimal queryPageRankScore(String type) {
        List<Map<String, Object>> pRlist = neo4jJdbcTemplate.queryForList(OperBase1Utils.pageRankScore(type));
        BigDecimal prScore = null;
        if(pRlist!=null&&pRlist.size()>0){
            for (Map <String, Object> pRObj : pRlist) {
                prScore = new BigDecimal((Double)pRObj.get("avg(n.pageRankScore)"));
            }
        }
        return prScore;
    }

    public List<Map<String,Object>> queryNowScore(String insuredName, String insuredCode) {
        List<Map<String, Object>> nowScoreList = neo4jJdbcTemplate.queryForList(OperBase1Utils.nodeScore(insuredName,insuredCode));
        return nowScoreList;
    }

    public Map<String,Object> associatedRiskAnalysis(String insuredName, String insuredCode) throws IOException {
        Map<String,Object> associatedResult = new HashedMap();
        //PageRank分析中心性
        List<Map<String, Object>> targetNodeList = neo4jJdbcTemplate.queryForList(OperBase1Utils.nodeScore(insuredName,insuredCode));
        if(targetNodeList!=null&&targetNodeList.size()>0){
            BigDecimal avgPayeePRScore =  queryPageRankScore("收款人");//get收款人标准PageRank分
            associatedResult.put("avgPayeePRScore",avgPayeePRScore.setScale(2, BigDecimal.ROUND_HALF_UP));
            for (Map <String, Object> nsObj : targetNodeList) {
                //get目标与标准的倍数,以标准值的倍数作为风险等级，倍数越高风险越高
                BigDecimal payeePageRankScore = new BigDecimal((Double)nsObj.get("收款人评分"));
                BigDecimal payeeMultiple = payeePageRankScore.divide(avgPayeePRScore,2, BigDecimal.ROUND_HALF_UP);
                String payeeInfo = "收款人_"+(String)nsObj.get("收款人")+"("+(String)nsObj.get("收款人账号")+")";
                associatedResult.put(payeeInfo+"_关联风险等级:",payeeMultiple.stripTrailingZeros().toPlainString());
                if(payeeMultiple.stripTrailingZeros().compareTo(new BigDecimal(1))>0){
                    //多赔案分析
//                    List<Map<String, Object>> claimNosList = neo4jJdbcTemplate.queryForList(OperBase1Utils.getClaimNOListByPayee((String)nsObj.get("收款人"),(String)nsObj.get("收款人账号")));
//                    if(claimNosList.size()>5){
//                        associatedResult.put(payeeInfo,"与多比其他赔案存在关联："+claimNosList.size());
//                        //分析账号有效性，调用https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=622908376184364816&cardBinCheck=true
////                        checkBankCard("622908376184364816");
////                        JSONObject payeeCardCheckResult = checkBankCard((String)nsObj.get("收款人账号"));
//                        if((Boolean) ((checkBankCard((String)nsObj.get("收款人账号"))).get("validated"))){
//                            associatedResult.put(payeeInfo,"_银行ID有效_可以支付，由于关联多个其他被保险人及赔案，请及时关注。");
//                        }else{
//                            associatedResult.put(payeeInfo,"_银行ID无效_无法支付_默认业务操作");
//                        }
//                    }
                    //亲属分析
                    //不是被保险人本人
                    if(!(insuredName.equals((String)nsObj.get("收款人")))&&insuredName!=(String)nsObj.get("收款人")){
                        //姓氏相同
                        if(((String) nsObj.get("收款人")).startsWith(insuredName.substring(0,1))){
                            associatedResult.put(payeeInfo+"与本案被保险人_"+insuredName+"("+insuredCode+")","疑似亲属");
                        }
                    }
                }
//                if(payeeMultiple.stripTrailingZeros().compareTo(new BigDecimal(1))>0) {//如果超过标准系统，则针对node进行，亲属分析、委托人分析、中介公司检查，欺诈关联检查。
//                    //关联赔案检查，如果关联赔案问题较多，则本案问题风险概率上升
//                    List<Map<String, Object>> claimNoList = neo4jJdbcTemplate.queryForList(OperBase1Utils.getClaimNOList(insuredName));
//
//                    //sense-1，姓氏形同
//                    //sense-2，关联姓氏存在相同得数据
//                }
                BigDecimal avgReportManPRScore =  queryPageRankScore("报案人");//get报案人标准PageRank分
                associatedResult.put("avgReportManPRScore",avgReportManPRScore.setScale(2, BigDecimal.ROUND_HALF_UP));
                BigDecimal reportManPageRankScore = new BigDecimal ((Double)nsObj.get("报案人评分"));
                BigDecimal reportManMultiple = reportManPageRankScore.divide(avgReportManPRScore,2, BigDecimal.ROUND_HALF_UP);
                String reportManInfo = "报案人_"+(String)nsObj.get("报案人")+"("+(String)nsObj.get("报案人电话")+")";
                associatedResult.put(reportManInfo+"_关联风险等级:",reportManMultiple.stripTrailingZeros().toPlainString());
                //亲属判断，基于相同姓氏
                if(((String) nsObj.get("报案人")).startsWith(insuredName.substring(0,1))){
                    associatedResult.put(reportManInfo+"与本案被保险人_"+insuredName+"("+insuredCode+")","疑似亲属");
                }
                if(reportManMultiple.stripTrailingZeros().compareTo(new BigDecimal(1))>0){
                    //关联赔案检查，如果关联赔案问题较多，则本案问题风险概率上升
//                    List<Map<String, Object>> claimNosList = neo4jJdbcTemplate.queryForList(OperBase1Utils.getClaimNOListByReportMan((String)nsObj.get("报案人"),(String)nsObj.get("报案人电话")));
//                    if(claimNosList.size()>5){//报案人报案次数大于5，判断未中介代理商
//                        associatedResult.put(reportManInfo,"疑似中介_代理报案次数："+claimNosList.size());
//                    }
                    //sense-1 出险时间和起保时间、终保时间接近（1周）
//                    for(Map <String, Object> claimNoMap : claimNosList){
//                        Map <String, Object> claimMap = (Map <String, Object>) claimNoMap.get("n");
//                        int i1 = DateUtils.dateCalSubtraction(DateUtils.stringToDate((String)claimMap.get("出险时间"),102),DateUtils.stringToDate((String)claimMap.get("保险起期"),102));
//                        int i2 = DateUtils.dateCalSubtraction(DateUtils.stringToDate((String)claimMap.get("出险时间"),102),DateUtils.stringToDate((String)claimMap.get("保险止期"),102));
//                        if(i1<2||i2<2){
//                            associatedResult.put(reportManInfo,"的关联赔案_"+claimMap.get("name")+"存在有效期与出险时间异常");
//                        }
//                    }
                }
            }
        }
        return associatedResult;
    }

    private JSONObject checkBankCard(String BankCardId) throws ClientProtocolException, IOException {
        String url="https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo="+BankCardId+"&cardBinCheck=true";
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonResult = null;
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpResponse response = httpclient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        jsonResult = JSONObject.parseObject(result);
        return jsonResult;
    }

    public List<KgspInfoVo> findPageRankByIns(String insuredName, String insuredCode) throws IOException {
        //组织基础数据-1
        List<Map<String, Object>> targetNodeList = neo4jJdbcTemplate.queryForList(OperBase1Utils.nodeScore(insuredName,insuredCode));
        /**平均分*/
        BigDecimal avgPayPagerank =  queryPageRankScore("收款人").setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal avgReportPagerank =  queryPageRankScore("报案人").setScale(2, BigDecimal.ROUND_HALF_UP);
        List<KgspInfoVo> kgspInfoVoList = new ArrayList <>();

        Map<String,String> pagerankMap = new HashedMap();
        /**记录最高分值*/
        BigDecimal topRank = new BigDecimal(0);
        if(targetNodeList!=null&&targetNodeList.size()>0){
            for (Map <String, Object> nsObj : targetNodeList) {
                /**P1 收款人*/
                BigDecimal payPagerank = new BigDecimal((Double)nsObj.get("收款人评分"));
                /**用当前分值和平均值算倍数作为等级*/
                BigDecimal payMultiple = payPagerank.divide(avgPayPagerank,2, BigDecimal.ROUND_HALF_UP);
                String payRiskLevel = riskLevelCrt(payMultiple);
                KgspInfoVo kgspPayInfoVo = new KgspInfoVo();
                kgspPayInfoVo.setNodeType("收款人");
                kgspPayInfoVo.setNodeName((String)nsObj.get("收款人"));
                kgspPayInfoVo.setNodeValue((String)nsObj.get("收款人账号"));
                kgspPayInfoVo.setPageRank(payPagerank.setScale(2, BigDecimal.ROUND_HALF_UP));
                kgspPayInfoVo.setAvgpageRank(avgPayPagerank);
                kgspPayInfoVo.setPageRankLevel(payRiskLevel);
                kgspInfoVoList.add(kgspPayInfoVo);
                /**P2: 报案人部分*/
                BigDecimal reportPageRank = new BigDecimal ((Double)nsObj.get("报案人评分"));
                BigDecimal reportMultiple = reportPageRank.divide(avgReportPagerank,2, BigDecimal.ROUND_HALF_UP);
                String reportRiskLevel = riskLevelCrt(reportMultiple);
                KgspInfoVo kgspReportInfoVo = new KgspInfoVo();
                kgspReportInfoVo.setNodeType("报案人");
                kgspReportInfoVo.setNodeName((String)nsObj.get("报案人"));
                kgspReportInfoVo.setNodeValue((String)nsObj.get("报案人电话"));
                kgspReportInfoVo.setPageRank(reportPageRank.setScale(2, BigDecimal.ROUND_HALF_UP));
                kgspReportInfoVo.setAvgpageRank(avgReportPagerank);
                kgspReportInfoVo.setPageRankLevel(reportRiskLevel);
                kgspInfoVoList.add(kgspReportInfoVo);
            }
        }
        return kgspInfoVoList;
    }

    public Map<String,Object> hybridInferentialAnalysis(String insuredName, String insuredCode, Map <String, Object> ifmCypherMap) throws IOException {
        List<Map<String, Object>> hNodeList = new ArrayList <>();
        Map<String,Object> hNodeMap = new HashedMap();
        List<Map<String, Object>> hEdgeList = new ArrayList <>();
        Map<String,Object> hEdgeMap = new HashedMap();
        Map<String,Object> hybridInferentialMap = new HashedMap();

        //基准线-1，客户信息
        hNodeList.add(makeNodeInfo("customer",insuredName,"0","2","customer"));
        String age = KgBayesUtils.InsuredCodeToAge(insuredCode)+"";
        //基准线-1.1，年龄节点
        hNodeList.add(makeNodeInfo("age",age,"1","true","customer"));
        //基准线-1.2，身份证
        hNodeList.add(makeNodeInfo("idCode",insuredCode,"1","true","customer"));
        //基准线-1.3，连线
        hEdgeList.add(makeEdgeInfo("customer","age"));
        hEdgeList.add(makeEdgeInfo("customer","idCode"));

        //结束回写
        hybridInferentialMap.put("nodes",hNodeList);
        hybridInferentialMap.put("edges",hEdgeList);

        /**P1 - 获取图谱中的客户信息*/
        hybridInferentialMap.put("Customer", KgBayesUtils.getTemplateCustomer(insuredName,insuredCode));

        /**P2 - 名下赔案信息*/
        List<Map<String, Object>> claimListMap = neo4jJdbcTemplate.queryForList(OperBase1Utils.getClaimByIns(insuredName,insuredCode));
        if(claimListMap!=null&&claimListMap.size()>0){
            if(claimListMap.size()>10){
                hybridInferentialMap.put("Claim","当前客户名下赔案较多，请去信息系统功能查询");
            }else{
                hybridInferentialMap.put("Claim", KgBayesUtils.getTemplateClaim(claimListMap));
            }
        }else{
            hybridInferentialMap.put("Claim", "当前客户名下没有可用赔案");
        }
        /**P3 - 数据情况信息*/
        hybridInferentialMap.put("Statistics", ifmCypherMap.get("countNodeInfo"));

        /**P4 - 风险关系分析信息*/
        //组织基础数据-1
        List<Map<String, Object>> targetNodeList = neo4jJdbcTemplate.queryForList(OperBase1Utils.nodeScore(insuredName,insuredCode));
        BigDecimal avgPayeePRScore =  queryPageRankScore("收款人");//get收款人标准PageRank分
        BigDecimal avgReportManPRScore =  queryPageRankScore("报案人");//get报案人标准PageRank分
        //记录sense-0_基于PR值的风险等级运算
        Map<String,Object> rankMap = new HashedMap();
        //记录sense-0.1_记录最高PR值
        BigDecimal topRank = new BigDecimal(0);
        //记录sense-1_目标值有效性
        Map<String,Object> validityMap = new HashedMap();
        //sense-2_中介代理报案
        Map<String,Object> intermediaryMap = new HashedMap();
        //、sense-3_亲属可能性
        Map<String,Object> relativesMap = new HashedMap();
        //记录计算参数，四舍五入
//        hybridInferentialMap.put("avgPayeePRScore",avgPayeePRScore.setScale(2, BigDecimal.ROUND_HALF_UP));
//        hybridInferentialMap.put("avgReportManPRScore",avgReportManPRScore.setScale(2, BigDecimal.ROUND_HALF_UP));
        if(targetNodeList!=null&&targetNodeList.size()>0){
            //循环每一个终端类型的节点，计算风险信息
            for (Map <String, Object> nsObj : targetNodeList) {
                //组织基础数据-2
                String payeeInfo = "收款人-"+(String)nsObj.get("收款人")+"（"+(String)nsObj.get("收款人账号")+"）";
                String reportManInfo = "报案人-"+(String)nsObj.get("报案人")+"（"+(String)nsObj.get("报案人电话")+"）";

                /** sense-0_基于PR值的风险等级运算 */
                //part 收款人
                BigDecimal payeePageRankScore = new BigDecimal((Double)nsObj.get("收款人评分"));
                BigDecimal payeeMultiple = payeePageRankScore.divide(avgPayeePRScore,2, BigDecimal.ROUND_HALF_UP);
                if(payeeMultiple.compareTo(topRank)>-1){
                    topRank = payeeMultiple;
                }
                String payeeRiskLevel = riskLevelCrt(payeeMultiple);
                rankMap.put(payeeInfo+"关联风险等级：",payeeMultiple.stripTrailingZeros().toPlainString()+"，"+payeeRiskLevel+"。");
                //part 报案人部分
                BigDecimal reportManPageRankScore = new BigDecimal ((Double)nsObj.get("报案人评分"));
                BigDecimal reportManMultiple = reportManPageRankScore.divide(avgReportManPRScore,2, BigDecimal.ROUND_HALF_UP);
                if(reportManMultiple.compareTo(topRank)>-1){
                    topRank = reportManMultiple;
                }
                String reportManRiskLevel = riskLevelCrt(reportManMultiple);
                rankMap.put(reportManInfo+"关联风险等级：",reportManMultiple.stripTrailingZeros().toPlainString()+"，"+reportManRiskLevel+"。");

                /**sense-1_目标值有效性 */
                //part收款人，只有在外放节点存在3个关联赔案时进行账号有效性校验
                List<Map<String, Object>> payeeToclaimNos = neo4jJdbcTemplate.queryForList(OperBase1Utils.getClaimNOListByPayee((String)nsObj.get("收款人"),(String)nsObj.get("收款人账号"),insuredName,insuredCode));
                if(payeeToclaimNos.size()>2){
                    validityMap.put(payeeInfo,"与多笔其他赔案存在关联："+payeeToclaimNos.size());
                    if((Boolean) ((checkBankCard((String)nsObj.get("收款人账号"))).get("validated"))){
                        validityMap.put(payeeInfo,",银行ID有效可以支付，由于关联多个其他被保险人及赔案，请及时关注。");
                    }else{
                        validityMap.put(payeeInfo,",银行ID无效,无法支付,默认业务操作");
                    }
                }else{
                    validityMap.put(payeeInfo,"无多笔其他赔案存在关联情况存在");
                    if((Boolean) ((checkBankCard((String)nsObj.get("收款人账号"))).get("validated"))){
                        validityMap.put(payeeInfo,"，银行ID有效");
                    }else{
                        validityMap.put(payeeInfo,",银行ID无效,无法支付,请及时关注。");
                    }
                }
                /**sense-2_中介代理报案 */
                //part报案人，
                List<Map<String, Object>> reportToClaimNos = neo4jJdbcTemplate.queryForList(OperBase1Utils.getClaimNOListByReportMan((String)nsObj.get("报案人"),(String)nsObj.get("报案人电话"),insuredName,insuredCode));
                if(reportToClaimNos.size()>3){
                    intermediaryMap.put(reportManInfo,"与其他"+reportToClaimNos.size()+"个赔案存在关联。");
                    if(reportToClaimNos.size()>5){
                        intermediaryMap.put(reportManInfo,"疑似中介，共进行"+reportToClaimNos.size()+"次代理报案。");
                    }else{
                        intermediaryMap.put(reportManInfo,"无疑似中介情况");
                    }
                }
            }
        }
        /**sense-3_亲属可能性*/
        //组织基础数据，以本案被保险人为轴心，遍历其他被保险人、收款人、报案人可能存在的亲属关系
        String insuredNameInfo = insuredName+"("+insuredCode+")";
        //part 本案被保险人 - 关联上的被保险人
        Map<String,Object> insuredMap = (Map <String, Object>) ifmCypherMap.get("insured");
        Set<String> insKeys = insuredMap.keySet();
        for (String insKey : insKeys) {
            String insStart = insuredMap.get(insKey).toString().substring(0,1);
            String insName = insuredMap.get(insKey).toString();
            if(insuredName.startsWith(insStart)&&insuredName!=insName&&!insuredName.equals(insName)){
                String otherInsInfo = insuredMap.get(insKey)+"("+insKey+")";
                relativesMap.put("本案被保险人："+insuredNameInfo+"，与关联被保险人："+otherInsInfo,"存在疑似亲属情况");
            }else{
                relativesMap.put("本案被保险人："+insuredNameInfo+"无与其他关联被保险人","存在疑似亲属情况");
            }
        }
        //part 本案被保险人 - 关联上的收款人
        Map<String,Object> payeeMap = (Map <String, Object>) ifmCypherMap.get("payee");
        Set<String> payeeKeys = payeeMap.keySet();
        for (String payKey : payeeKeys) {
            String payeeStart = payeeMap.get(payKey).toString().substring(0,1);
            String payeeName = payeeMap.get(payKey).toString();
            if(insuredName.startsWith(payeeStart)&&insuredName!=payeeName&&!insuredName.equals(payeeName)){
                String otherPayeeInfo = payeeMap.get(payKey)+"("+payKey+")";
                relativesMap.put("本案被保险人："+insuredNameInfo+"，与关联收款人："+otherPayeeInfo,"存在疑似亲属情况");
            }else{
                relativesMap.put("本案被保险人："+insuredNameInfo+"无与关联收款人","存在疑似亲属情况");
            }
        }
        //part 本案被保险人 - 关联上的收款人
        Map<String,Object> reportMap = (Map <String, Object>) ifmCypherMap.get("reportMan");
        Set<String> reportKeys = reportMap.keySet();
        for (String reportKey : reportKeys) {
            String reportManStart = reportMap.get(reportKey).toString().substring(0,1);
            String reportManName = reportMap.get(reportKey).toString();
            if(insuredName.startsWith(reportManStart)&&insuredName!=reportManName&&!insuredName.equals(reportManName)){
                String otherReportInfo = payeeMap.get(reportKey)+"("+reportKey+")";
                relativesMap.put("本案被保险人："+insuredNameInfo+"，与关联报案人："+otherReportInfo,"_疑似亲属");
            }else{
                relativesMap.put("本案被保险人："+insuredNameInfo+"无与关联报案人：","存在疑似亲属情况");
            }
        }

        //sense-0_风险等级
        if(!rankMap.isEmpty()){
            StringBuffer riskBuffer = new StringBuffer();
            Set<String> rankKeys = rankMap.keySet();
            for (String rankKey : rankKeys) {
                riskBuffer.append(rankKey).append(rankMap.get(rankKey));
            }
            hybridInferentialMap.put("Rank",riskBuffer.toString());
        }

        //sense-1_目标有效性
        if(!validityMap.isEmpty()){
            StringBuffer validityBuffer = new StringBuffer();
            Set<String> validityKeys = validityMap.keySet();
            for (String validityKey : validityKeys) {
                validityBuffer.append(validityKey).append(validityMap.get(validityKey));
            }
            hybridInferentialMap.put("Validity",validityBuffer.toString());
        }

        //sense-2_中介代理报案
        if(!intermediaryMap.isEmpty()){
            StringBuffer intermediaryBuffer = new StringBuffer();
            Set<String> intermediaryKeys = intermediaryMap.keySet();
            for (String intermediaryKey : intermediaryKeys) {
                intermediaryBuffer.append(intermediaryKey).append(intermediaryMap.get(intermediaryKey));
            }
            hybridInferentialMap.put("Intermediary",intermediaryBuffer.toString());
        }

        //sense-3_亲属可能性
        if(!relativesMap.isEmpty()){
            StringBuffer relativesBuffer = new StringBuffer();
            Set<String> relativesKeys = relativesMap.keySet();
            for (String relativesKey : relativesKeys) {
                relativesBuffer.append(relativesKey).append(relativesMap.get(relativesKey));
            }
            hybridInferentialMap.put("Relatives",relativesBuffer.toString());
        }
        String topRankString = riskLevelCrt(topRank);
        hybridInferentialMap.put("topRank",topRankString);
        return hybridInferentialMap;
    }

    private Map<String, Object> makeEdgeInfo(String source, String target) {
        Map<String, Object> hEdgeMap = new HashedMap();
        hEdgeMap.put("source",source);
        hEdgeMap.put("target",target);
        return hEdgeMap;
    }

    private Map<String, Object> makeNodeInfo(String id, String name, String level, String childrenNum, String tag) {
        Map<String, Object> hNodeMap = new HashedMap();
        hNodeMap.put("id",id);
        hNodeMap.put("name",name);
        hNodeMap.put("level",level);
        if(level=="0"||"0".equals(level)){
            hNodeMap.put("childrenNum",childrenNum);
        }else if(level=="1"||"1".equals(level)){
            hNodeMap.put("isLeaf",childrenNum);
        }
        hNodeMap.put("tag",tag);
        return hNodeMap;
    }

    private String riskLevelCrt(BigDecimal mathCode) {
        if(mathCode.compareTo(new BigDecimal(3.00))>=0){
            return "风险指数:高";
        }else if(mathCode.compareTo(new BigDecimal(2.00))>=0){
            return "风险指数:中";
        }else{
            return "风险指数:低";
        }
    }
}
