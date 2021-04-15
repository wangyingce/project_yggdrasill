package cc.leevi.webbase.service;

import cc.leevi.webbase.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BusinessPropertyService {

    @Autowired
    @Qualifier("springJdbcTemplate")
    protected JdbcTemplate springJdbcTemplate;

    /**
     * 规则：
     * @param sqlString
     * @return
     */
    public List <Map <String, Object>> findBusinessPropertyBySql(String sqlString) {
        List<Map<String, Object>> ikList = springJdbcTemplate.queryForList(sqlString);
        return ikList;
    }

    /**
     * 按照modelNo返回要查询业务库数据
     * @param modelNo
     * @param claimNo
     * @return
     */
    public String queryClaimPortraitByModelAndClaimNo(String modelNo, String claimNo) {
        if("10"==modelNo||"10".equals(modelNo)){
            //报案部分
            String m10Message = "赔案号claimNo，保单号c_Ply_No，被保险人c_Insured_Nme，于t_Accdnt_Tm出险，t_Rpt_Tm报案，" +
                    "目前进展到C_CLM_STS环节，处理人OPER_NAME_，" +
//                    "用时effDay天effHour小时effMin分，" +
                    "未决金额N_PEND_AMT，详情请点击:95303.uclaim.flow?userode=xxx&claimNo="+claimNo;
            m10Message = getClmMessage(m10Message,claimNo);
//            m10Message = getEffMessage(m10Message,claimNo);
            return m10Message;
        }

        return null;
    }

    private String getClmMessage(String m10Message, String claimNo) {
        String msSql = 
                "SELECT" +
                        " s.t_Accdnt_Tm AS 出险时间," +
                        " s.c_Insured_Nme AS 被保险人," +
                        " s.c_Cert_No AS 被保险人证件号," +
                        " u.c_Ply_No AS 保单号," +
                        " u.t_Insrnc_Bgn_Tm AS 起保时间," +
                        " u.t_Insrnc_End_Tm AS 终保时间," +
                        " v.c_Clm_No AS 赔案号," +
                        " v.t_Rpt_Tm AS 报案时间," +
                        " v.c_Rptman_Nme AS 报案人," +
                        " v.c_Rptman_Tel AS 报案电话," +
                        " a.C_CLM_STS AS 节点," +
                        " c.OPER_NAME_ AS 处理人," +
                        " b.N_PEND_AMT AS 未决金额" +
                        " FROM" +
                        " Web_Clm_Accdnt s," +
                        " Web_Clm_Ply_Base u," +
                        " Web_Clm_Rpt v," +
                        " Web_clm_main a," +
                        " WEB_CLM_PEND b," +
                        " jbpm_taskinstance_ext c" +
                        " WHERE" +
                        " s.c_Clm_No = u.c_Clm_No" +
                        " AND s.c_Clm_No = v.C_CLM_NO" +
                        " AND s.c_Clm_No = a.C_CLM_NO" +
                        " AND s.C_CLM_NO = b.C_CLM_NO" +
                        " AND s.C_CLM_NO = c.OBJ_ID_" +
                        " AND b.C_LATEST_MRK = '1'" +
                        " AND c.CURT_TASK_NAME_ = '查勘定损'" +
                        " AND c.CURT_USER_CDE_ != '*'" +
                        " AND s.C_CLM_NO = '"+claimNo+"'";
        List<Map<String, Object>> ikList = springJdbcTemplate.queryForList(msSql);
        if(ikList!=null&&ikList.size()>0) {
            for (Map <String, Object> fk1obj : ikList) {
                m10Message = m10Message
                        .replace("claimNo",(String)fk1obj.get("赔案号"))
                        .replace("c_Ply_No",(String)fk1obj.get("保单号"))
                        .replace("c_Insured_Nme",(String)fk1obj.get("被保险人"))
                        .replace("t_Accdnt_Tm", DateUtils.dateToString((Date)fk1obj.get("出险时间"),9))
                        .replace("t_Rpt_Tm",DateUtils.dateToString((Date)fk1obj.get("报案时间"),9))
                        .replace("C_CLM_STS", (String)fk1obj.get("节点"))
                        .replace("OPER_NAME_", (String)fk1obj.get("处理人"))
                        .replace("N_PEND_AMT", ((BigDecimal)fk1obj.get("未决金额")).toString());
            }
        }
        return m10Message;
    }

    private String getEffMessage(String m10Message, String claimNo) {
        return null;
    }

    public String queryPersonPortraitByModelAndClaimNo(String modelNo, String claimNo) {
        return null;
    }
}
