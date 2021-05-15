package cc.leevi.webbase.utils;

public class Neo4jOperationUtils {
    public static String getNeo4jDataByUsernTame(String usern, String model) {
        return "match dat=((na)<-[*]->(nb)) where na.usern= nb.usern='"+usern+"' and na.model= nb.model='"+model+"' return dat as d1";
    }
}
