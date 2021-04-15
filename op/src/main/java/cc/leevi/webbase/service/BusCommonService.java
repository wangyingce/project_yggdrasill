package cc.leevi.webbase.service;

import cc.leevi.webbase.constants.CodeConst;
import cc.leevi.webbase.vo.PumpClaimDataVo;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BusCommonService {

    @Autowired
    @Qualifier("springJdbcTemplate")
    private JdbcTemplate springJdbcTemplate;

    public void printClaimStructuredData(String nDate, CSVPrinter csvPrinter) throws IOException {
        List<PumpClaimDataVo> pumps = new ArrayList<PumpClaimDataVo>(0);
        StringBuffer hql = new StringBuffer();
        hql.append("SELECT" +
                "  s.t_Accdnt_Tm," +
                "  s.c_Insured_Nme," +
                "  t.c_Payee_Nme," +
                "  t.C_BANK_NUM      AS c_Payee_No," +
                "  u.c_Ply_No," +
                "  u.t_Insrnc_Bgn_Tm," +
                "  u.t_Insrnc_End_Tm," +
                "  v.c_Clm_No," +
                "  v.t_Rpt_Tm," +
                "  v.c_Rptman_Tel," +
                "  w.c_Pay_Liability" +
                "  FROM Web_Clm_Accdnt s," +
                "  Web_Clm_Bank t," +
                "  Web_Clm_Ply_Base u," +
                "  Web_Clm_Rpt v," +
                "  Web_Clm_Adjust w " +
                "  WHERE s.c_Clm_No = t.c_Clm_No" +
                "    AND t.c_Clm_No = u.c_Clm_No" +
                "    AND u.c_Clm_No = v.c_Clm_No" +
                "    AND v.c_Clm_No = w.c_Clm_No" +
                "    AND t.c_Pay_Typ = 'N9025001'" +
                "    AND t.C_PUB_PIV = '2'" +
                "    AND u.C_PROD_NO LIKE '07%'");
//        hql.append(" and v.t_Rpt_Tm > '"+nDate+"'");
        hql.append(" and v.t_Rpt_Tm > '2019-06-01'");
        List<Map<String, Object>> findsList = springJdbcTemplate.queryForList(hql.toString());
        if(findsList!=null&&findsList.size()>0){
            for(Map<String, Object> fdlst:findsList){
                String liability = "";
                String c_Pay_Liability = (String) fdlst.get("c_Pay_Liability");
                if("1".equals(c_Pay_Liability)||c_Pay_Liability=="1"){
                    liability= CodeConst.ConfigCode.C_PAY_LIABILITY_1;
                }else if("2".equals(c_Pay_Liability)||c_Pay_Liability=="2"){
                    liability=CodeConst.ConfigCode.C_PAY_LIABILITY_2;
                }else if("3".equals(c_Pay_Liability)||c_Pay_Liability=="3"){
                    liability=CodeConst.ConfigCode.C_PAY_LIABILITY_3;
                }else if("4".equals(c_Pay_Liability)||c_Pay_Liability=="4"){
                    liability=CodeConst.ConfigCode.C_PAY_LIABILITY_4;
                }else{
                    liability="(NULL)";
                }
                csvPrinter.printRecord(
                        fdlst.get("t_Accdnt_Tm"),
                        fdlst.get("c_Insured_Nme"),
                        fdlst.get("c_Payee_Nme"),
                        fdlst.get("c_Payee_No"),
                        fdlst.get("c_Ply_No"),
                        fdlst.get("t_Insrnc_Bgn_Tm"),
                        fdlst.get("t_Insrnc_End_Tm"),
                        fdlst.get("c_Clm_No"),
                        fdlst.get("t_Rpt_Tm"),
                        fdlst.get("c_Rptman_Tel"),
                        liability
                );
            }
        }
    }
}
