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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import static com.alibaba.fastjson.JSON.toJSON;

@Service
@Transactional
public class UserLogicService {

    @Autowired
    @Qualifier("neo4jJdbcTemplate")
    protected JdbcTemplate neo4jJdbcTemplate;

    public Boolean checkUserLogin(String name,String password) {
        List<Map<String, Object>> userlist = neo4jJdbcTemplate.queryForList("match (n:user) where n.name='"+name+"' and n.password='"+password+"' return n");
        if(userlist!=null&&userlist.size()>0){
            return true;
        }else{
            return false;
        }
    }

    public void registUser(Map propertiesMap) {
        String cql = "CREATE(n:user {<property>})";
        Iterator iterator = propertiesMap.keySet().iterator();
        String propertyParam = "";
        while(iterator.hasNext()){
            String ppk = iterator.next().toString();
            String ppv = String.valueOf(propertiesMap.get(ppk));
            propertyParam = propertyParam + ppk+":'"+ppv+"',";
        }
        propertyParam = propertyParam.substring(0,propertyParam.length()-1);
        cql =cql.replace("<property>",propertyParam);
        neo4jJdbcTemplate.update(cql);
    }

    public void createModel(Map propertiesMap) {
        String usern  = String.valueOf(propertiesMap.get("usern"));
        String model  = String.valueOf(propertiesMap.get("modelname"));
        String ncql = "CREATE(n:model {name:'" + model +"'})";
        neo4jJdbcTemplate.update(ncql);
        String lCql =  "MATCH (aa:user {name:'"+usern+"'}), (bb:model {name:'"+model+"'}) \n" +
                          "MERGE (aa) -[:own]-> (bb)";
        neo4jJdbcTemplate.update(lCql);
    }

    public void addUserCookies(String name, String uuid) {
        String updateCql =  "MATCH (aa:user {name:'"+name+"'}) set aa."+UUIDUtils.usercId+"='"+uuid+"'";
        neo4jJdbcTemplate.update(updateCql);
    }

    public List<Map<String, Object>> queryNodeUserByCookies(String property) {
//        String cookiesCql = "MATCH (u:user {"+UUIDUtils.usercId+":'"+property+"'})-[res]->(t:template) return u,t";
        String userCql = "MATCH (u:user {"+UUIDUtils.usercId+":'"+property+"'}) return u";
        return neo4jJdbcTemplate.queryForList(userCql);
//        return neo4jJdbcTemplate.queryForList("MATCH (u:user {"+UUIDUtils.usercId+":'"+property+"'})-[res]->(t:template) return u.name,u.phone,t.name,t.remark");
    }

    public List<Map<String, Object>> queryModelCookies(String property) {
        String temCql = "MATCH (u:user {"+UUIDUtils.usercId+":'"+property+"'})-[res]->(t:template) return t";
        return neo4jJdbcTemplate.queryForList(temCql);
    }

    public String findUserByCookies(String userc) {
        String usercCql = "MATCH (u:user {"+UUIDUtils.usercId+":'"+userc+"'}) return u.name";
        Map<String, Object> usermap = neo4jJdbcTemplate.queryForMap(usercCql);
        if(usermap!=null&&usermap.get("u.name")!=null){
            return String.valueOf(usermap.get("u.name"));
        }else{
            return null;
        }
    }
}
