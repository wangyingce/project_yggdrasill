package cc.leevi.webbase.utils;

public class Neo4jOperationUtils {
    /**查询user.model的数据**/
    public static String getNeo4jDataByUsernModel(String usern, String model) {
        return "match dat=((na)<-[*]->(nb)) where na.usern= nb.usern='"+usern+"' and na.model= nb.model='"+model+"' return dat as d1";
//        return "match dat=((na)) where na.usern='"+usern+"' and na.model='"+model+"' return dat as d1";
    }
    /**查询user的数据**/
    public static String getNeo4jDataByUsern(String usern) {
        return "match dat=((na)<-[*]->(nb)) where na.usern= nb.usern='"+usern+"' return dat as d1";
    }
}
