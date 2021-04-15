package cc.leevi.webbase.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class KgBayesUtils {
    /**
     * 身份证算年龄
     * @param insuredCode
     * @return
     */
    public static int InsuredCodeToAge(String insuredCode){
        int leh = insuredCode.length();
        String dates="";
        if (leh == 18) {
            dates = insuredCode.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year=df.format(new Date());
            int u=Integer.parseInt(year)-Integer.parseInt(dates);
            return u;
        }else{
            dates = insuredCode.substring(6, 8);
            return Integer.parseInt(dates);
        }

    }

    public static String getClaimNOListByPayee(String payee, String payeeCard,String insuredName,String insuredCode) {
        return "match (o:被保险人)-[on]->(n:赔案号)-[nm]->(m:收款人{name:'"+payee+"',银行卡:'"+payeeCard+"'})  where o.name<>'"+insuredName+"' and o.身份证号<>'"+insuredCode+"' return n";
    }

    public static Object getTemplateCustomer(String insuredName, String insuredCode) {
        return insuredName+"("+insuredCode+"),年龄"+InsuredCodeToAge(insuredCode);
//        return "身份证:"+insuredCode+",姓名:"+insuredName+",年龄:"+InsuredCodeToAge(insuredCode);
    }

    public static Object getTemplateClaim(List <Map <String, Object>> claimListMap) {
        StringBuffer TemplateClaim = new StringBuffer();
        for(Map <String, Object> claimObj : claimListMap){
            TemplateClaim.append("保单:"+claimObj.get("保单"));
            TemplateClaim.append(",有效期:"+claimObj.get("起期")+"-"+claimObj.get("止期"));
            TemplateClaim.append(",赔案:"+claimObj.get("赔案"));
            TemplateClaim.append(",出险时间:"+claimObj.get("出险时间"));
            TemplateClaim.append(",报案时间:"+claimObj.get("报案时间"));
            TemplateClaim.append(",报案人:"+claimObj.get("报案人"));
            TemplateClaim.append(",报案电话:"+claimObj.get("报案电话"));
            TemplateClaim.append(",支付给了:"+claimObj.get("收款人"));
            TemplateClaim.append(",银行卡:"+claimObj.get("银行卡")+"。");
        }
        return TemplateClaim.toString();
    }
}
