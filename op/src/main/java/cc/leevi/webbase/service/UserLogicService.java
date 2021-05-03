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

@Service
@Transactional
public class UserLogicService {

    @Autowired
    @Qualifier("neo4jJdbcTemplate")
    protected JdbcTemplate neo4jJdbcTemplate;

    public Boolean checkUser(String ppValue) {
        List<Map<String, Object>> userlist = neo4jJdbcTemplate.queryForList("match (n) where n.name='"+ppValue+"' return n");
        if(userlist!=null&&userlist.size()>0){
            return false;
        }else{
            return true;
        }
    }

    //match (n:user) where n.name='1231231' and n.password='sdf' return n
    public Boolean checkUserLogin(String name,String password) {
        List<Map<String, Object>> userlist = neo4jJdbcTemplate.queryForList("match (n:user) where n.name='"+name+"' and n.password='"+password+"' return n");
        if(userlist!=null&&userlist.size()>0){
            return false;
        }else{
            return true;
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
}
