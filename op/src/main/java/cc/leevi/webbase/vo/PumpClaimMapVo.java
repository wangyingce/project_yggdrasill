package cc.leevi.webbase.vo;

import java.util.HashMap;

/**
 * 理赔数据抽取泵容器-base模型-1
 * @author WangYingce
 * @category 20190319 2crt
 */
public class PumpClaimMapVo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private HashMap<String, Integer> insuredNmeMap = new HashMap<String, Integer>();
    //保单
    private HashMap<String, Integer> plyNoMap = new HashMap<String, Integer>();
    //赔案号
    private HashMap<String, Integer> clmNoMap = new HashMap<String, Integer>();
    //收款人姓名
    private HashMap<String, Integer> payNmeMap = new HashMap<String, Integer>();
    //收款人卡号
    private HashMap<String, Integer> payNoMap = new HashMap<String, Integer>();
    //报案时间
    private HashMap<String, Integer> rptTmMap = new HashMap<String, Integer>();
    //出险时间
    private HashMap<String, Integer> accTmMap = new HashMap<String, Integer>();
    //报案电话
    private HashMap<String, Integer> rptTelMap = new HashMap<String, Integer>();
    //保单起期
    private HashMap<String, Integer> plyBgnMap = new HashMap<String, Integer>();
    //保单止期
    private HashMap<String, Integer> plyEndMap = new HashMap<String, Integer>();



    public HashMap<String, Integer> getInsuredNmeMap() {
        return insuredNmeMap;
    }
    public void setInsuredNmeMap(HashMap<String, Integer> insuredNmeMap) {
        this.insuredNmeMap = insuredNmeMap;
    }
    public HashMap<String, Integer> getPlyNoMap() {
        return plyNoMap;
    }
    public void setPlyNoMap(HashMap<String, Integer> plyNoMap) {
        this.plyNoMap = plyNoMap;
    }
    public HashMap<String, Integer> getClmNoMap() {
        return clmNoMap;
    }
    public void setClmNoMap(HashMap<String, Integer> clmNoMap) {
        this.clmNoMap = clmNoMap;
    }
    public HashMap<String, Integer> getPayNmeMap() {
        return payNmeMap;
    }
    public void setPayNmeMap(HashMap<String, Integer> payNmeMap) {
        this.payNmeMap = payNmeMap;
    }
    public HashMap<String, Integer> getPayNoMap() {
        return payNoMap;
    }
    public void setPayNoMap(HashMap<String, Integer> payNoMap) {
        this.payNoMap = payNoMap;
    }
    public HashMap<String, Integer> getRptTelMap() {
        return rptTelMap;
    }
    public void setRptTelMap(HashMap<String, Integer> rptTelMap) {
        this.rptTelMap = rptTelMap;
    }
    public HashMap<String, Integer> getRptTmMap() {
        return rptTmMap;
    }
    public void setRptTmMap(HashMap<String, Integer> rptTmMap) {
        this.rptTmMap = rptTmMap;
    }
    public HashMap<String, Integer> getAccTmMap() {
        return accTmMap;
    }
    public void setAccTmMap(HashMap<String, Integer> accTmMap) {
        this.accTmMap = accTmMap;
    }
    public HashMap<String, Integer> getPlyBgnMap() {
        return plyBgnMap;
    }
    public void setPlyBgnMap(HashMap<String, Integer> plyBgnMap) {
        this.plyBgnMap = plyBgnMap;
    }
    public HashMap<String, Integer> getPlyEndMap() {
        return plyEndMap;
    }
    public void setPlyEndMap(HashMap<String, Integer> plyEndMap) {
        this.plyEndMap = plyEndMap;
    }
}