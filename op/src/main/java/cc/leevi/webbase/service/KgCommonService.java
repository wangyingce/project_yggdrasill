package cc.leevi.webbase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KgCommonService {

    @Autowired
    @Qualifier("neo4jJdbcTemplate")
    protected JdbcTemplate neo4jJdbcTemplate;
    /**
     * 初始化图形引擎到最初状态——慎用
     */
    public void delAllGraphDatas() {
        try {
            // 清空数据库
            neo4jJdbcTemplate.update("MATCH (n) DETACH DELETE n ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
