package cc.leevi.webbase.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/* 环境
 * 1.JDK8
 * 2.Neo4j3.x
 * 3.切换数据库需要删除C:\Users\xxx\.neo4j\known_hosts
 */

/* 添加jar包
 * 1.neo4j-jdbc-driver-3.0.1.jar
 * 2.D:\Neo4j\neo4j-community-3.2.0-alpha05\lib\*.jar
 */

/* 配置文件：jdbc.properties
 * jdbc.username = neo4j
 * jdbc.password = root
 * jdbc.driver   =
 * jdbc.url      = jdbc:neo4j:bolt://localhost
 */

public class JdbcUtil {

    // 表示定义数据库的用户名
    private static String USERNAME;
    // 定义数据库的密码
    private static String PASSWORD;
    // 定义数据库的驱动信息
    private static String DRIVER;
    // 定义访问数据库的地址
    private static String URL;
    // 定义数据库的链接
    private Connection conn;
    // 定义sql语句的执行对象
    private PreparedStatement st;
    // 定义查询返回的结果集合
    private ResultSet rs;

    static {
        // 加载数据库配置信息，并给相关的属性赋值
        loadConfig();
    }

    /**
     * 加载数据库配置信息，并给相关的属性赋值
     */
    public static void loadConfig() {
        try {
            InputStream inStream = JdbcUtil.class.getResourceAsStream("/jdbc.properties");
            Properties prop = new Properties();
            prop.load(inStream);
            USERNAME = prop.getProperty("jdbc.username");
            PASSWORD = prop.getProperty("jdbc.password");
            DRIVER= prop.getProperty("jdbc.driver");
            URL = prop.getProperty("jdbc.url");
        } catch (Exception e) {
            throw new RuntimeException("读取数据库配置文件异常！", e);
        }
    }

    public JdbcUtil() {
    }

    /**
     * 获取数据库连接
     */
    public Connection getConnection() {
        try {
            // Class.forName(DRIVER); // 注册驱动
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); // 获取连接
        } catch (Exception e) {
            throw new RuntimeException("get connection error!", e);
        }
        return conn;
    }

    /**
     * 执行更新操作
     */
    public int update(String sql, Object... params) throws SQLException {
        st = conn.prepareStatement(sql);
        int index = 1;
        // 填充sql语句中的占位符
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                st.setObject(index++, params[i]);
            }
        }
        return st.executeUpdate();
    }

    /**
     * 执行查询操作
     */
    public List<Map<String, Object>> findList(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        st = conn.prepareStatement(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                st.setObject(index++, params[i]);
            }
        }
        rs = st.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int cols_len = metaData.getColumnCount();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < cols_len; i++) {
                String cols_name = metaData.getColumnName(i + 1);
                Object cols_value = rs.getObject(cols_name);
                if (cols_value == null) {
                    cols_value = "";
                }
                map.put(cols_name, cols_value);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 释放资源
     */
    public void close() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFile(String fineName) {
        File file = new File(fineName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("成功删除文件：" + file.getName());
            } else {
                System.out.println("删除失败：" + file.getName());
            }
        } else {
            System.out.println("文件不存在：" + fineName);
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
//		 deleteFile("C:/Users/LZ/.neo4j/known_hosts");
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        // 查询点
        String sql1 = "MATCH (n) RETURN n LIMIT {1} ";
        // 查询线
        String sql2 = "MATCH ()-[r]->() RETURN r LIMIT {1} ";
        // 查询所有
        String sql3 = "MATCH (a)-[r]->(b) RETURN a,b,r LIMIT {1} ";

        String sql4 = "CREATE (:Word {nid:'737',  name:'XXX'}) ";
        String sql5 = "CREATE (:Company {nid:'101',  name:'北京'}) ";
        String sql6 = "MATCH (aa0:Word { nid: '737' }), (bb0:Company { nid: '101'}) CREATE (aa0)-[:COMPANY{rid:'10001', name:''}]->(bb0) ";

        String sql7 = "MATCH (n:`公司`{名称:{1}}) RETURN n  ";
        String sql8 = "match (c:Company)-[r:COMPANYSTAGE|COMPANYMAJOR]-(n)WHERE not((n)-[r:MAJORSTAGE]-()) RETURN c,r,n limit {1}";
        String sql9 = "match (c:Company{name:'北京'})-[r1]-(m:Major)-[r2:WORDMAJOR]-(w) return c,r1,m,r2,w limit {1}";
        String sql10 = "match (a) -[r:朋友]-> (b) return * limit {1}";
        String sql11 = "match(a:Company{name:'北京'}),(b:Stage) return (a)--(b)as r limit {1}";
        String sql12 = "MATCH (a)-[r]-(b) return labels(a),keys(a),id(r),type(r),startNode(r).name,id(endNode(r)),count(*) limit {1}-95 ";
        String sql13 = "MATCH (na:User) where na.username={1} set na.name={2} return na";
        String sql14 = "CALL db.schema() ";
        try {
            List<Map<String, Object>> result = jdbcUtil.findList(sql1, 1);
            for (Map<String, Object> m : result) {
                System.out.println(m);
            }

            // 更新
            // int update1 = jdbcUtil.update(sql1);
            //System.out.println(update1);
            // int update2 = jdbcUtil.update(sql5, null);
            // int update3 = jdbcUtil.update(sql6, null);
            // System.out.println(update1);
            // System.out.println(update2);
            // System.out.println(update3);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != jdbcUtil) {
                jdbcUtil.close();
            }
        }
        System.out.println("OVER!");
    }
}