package cc.leevi.webbase.service;

import cc.leevi.webbase.utils.DateUtils;
import cc.leevi.webbase.utils.Neo4jOperationUtils;
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

    public List<Map<String, Object>> findKgByTname(String templateName) {
        return neo4jJdbcTemplate.queryForList(OperBase1Utils.getNeo4jDataByTemplateName(templateName));
    }

    /**
     * 查询：用户 -》 模版的图谱
     * @param usern
     * @param model
     * @return
     */
    public List<Map<String, Object>> findKgByUsernModel(String usern, String model) {
        return neo4jJdbcTemplate.queryForList(Neo4jOperationUtils.getNeo4jDataByUsernModel(usern,model));
    }

    public List<Map<String, Object>> findKgByUsern(String usern) {
        return neo4jJdbcTemplate.queryForList(Neo4jOperationUtils.getNeo4jDataByUsern(usern));
    }
}
