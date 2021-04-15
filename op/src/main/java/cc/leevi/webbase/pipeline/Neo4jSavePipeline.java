package cc.leevi.webbase.pipeline;

import cc.leevi.webbase.service.KgFusionService;
import cc.leevi.webbase.support.SpringApplicationContextHolder;
import cc.leevi.webbase.vo.BaikeDataVo;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashMap;
import java.util.Map;

public class Neo4jSavePipeline implements Pipeline {
    private KgFusionService kgFusionService;

    /**
     * neo4j落库流水线
     */
    private void ensureService(){
        if(kgFusionService == null){
            kgFusionService = SpringApplicationContextHolder.getBean(KgFusionService.class);
        }
    }

    public void process(ResultItems resultItems, Task task) {
        ensureService();
        /**处理resultItems*/
        String wAInf =resultItems.getAll().get("readme").toString();
        String title = resultItems.getAll().get("title").toString();
        String titleValue = resultItems.getAll().get("titleValue").toString();
        String wIndex = wAInf.substring(wAInf.indexOf("目录")+2,wAInf.indexOf("  <>  <>  <> "));
//        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
//        System.out.println(wAInf);
        String[] wordIndexs = wIndex.split(">");
        Map<String,BaikeDataVo> baikeAllMap = new HashMap <>();
        kgFusionService.createBaikeTitle(title,titleValue);
        for(int i = 0;i<wordIndexs.length;i++){
            BaikeDataVo baikeVo =  new BaikeDataVo();
            //目录
            String widx = wordIndexs[i];
            String index = widx.substring(widx.indexOf("#")+1,widx.length());
            String name  = widx.substring(0,widx.indexOf("<"));
            String url = widx.substring(widx.indexOf("<")+1,widx.length());
            if(index.contains("_")){
                baikeVo.setFather(index.substring(0,index.indexOf("_")));
            }
            baikeVo.setIndex(index);
            baikeVo.setTitle(title);
            name = pure(name,"a");
            baikeVo.setName(name+"("+title+")");
            baikeVo.setUrl(url);
            //内容
            String markT = title+name;
            String v1 = wAInf.substring(wAInf.indexOf(markT),wAInf.length());//防止index倒序
            String v2 = v1.substring(0,v1.indexOf("<>  <>  <>"));
            v2 = pure(v2,"0");
            v2 = pure(v2,"b");
//            System.out.println("markT index:"+v2.indexOf(markT));
            v2=v2.replace(markT,"");
            baikeVo.setValue(v2);
            kgFusionService.createBaikeNode(baikeVo);
            baikeAllMap.put(index,baikeVo);
        }
        kgFusionService.createBaikeRelation(baikeAllMap,title);
    }

    private String pure(String name,String type) {
        name = name.replace("\n","").replace(" ","").replace("*","").replace("▪","").replaceAll("\\d+", "");
        //概念名去杂
        if("a".equals(type)||"a"==type){
            name = name.replace(">","").replace("<","");
        }
        //内容去杂
        if("b".equals(type)||"b"==type){
            name = name.replace("编辑","").replace("","");
            //结尾特殊处理
            if(name.indexOf("参考资料")>-1){
                name = name.replace(name.substring(name.indexOf("参考资料"),name.length()),"");
            }
        }
        return name;
    }
}
