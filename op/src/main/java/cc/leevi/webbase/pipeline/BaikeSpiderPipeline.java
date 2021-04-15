package cc.leevi.webbase.pipeline;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Iterator;

/**
 * 百科爬虫服务
 */
public class BaikeSpiderPipeline implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
    @Override
    public Site getSite() {
        return site;
    }

    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(https://baike.baidu\\.com/\\w+/\\w+)").all());
//        page.putField("author", page.getUrl().regex("https://baike.baidu\\.com/(\\w+)/.*").toString());
        String title = page.getHtml().xpath("//h1/text()").toString();
        /**标题*/
        page.putField("title", title);
//        page.putField("name", page.getHtml().xpath("//div[@class='para']/text()").all().iterator());
        /**para节点文本内容*/
        Iterator iterator = page.getHtml().xpath("//div[@class='para']/text()").all().iterator();
        String baikedatas = "";
        String titleValue = "";
        while (iterator.hasNext()) {
            if(titleValue==null||"".equals(titleValue)){
                titleValue = String.valueOf(iterator.next());
            }
            baikedatas = baikedatas +"\n"+ String.valueOf(iterator.next());
        }
        /**标题内容*/
        page.putField("titleValue", titleValue);
        page.putField("name", baikedatas);
        if (page.getResultItems().get("name") == null || page.getResultItems().get("name") == "") {
            //skip this page
            page.setSkip(true);
        }
        /**词组主体，用于二次加工*/
        page.putField("readme", page.getHtml().xpath("tidyText()"));
        /**三项输出*/
        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        System.out.println("title:"+page.getResultItems().get("title"));
        System.out.println("titleValue:"+page.getResultItems().get("titleValue"));
        System.out.println("readme:"+page.getResultItems().get("readme"));

//        /**获取词组主体*/
//        String wAInf =page.getResultItems().get("readme").toString();
//        /**获取词组目录*/
//        String wIndex = wAInf.substring(wAInf.indexOf("目录")+2,wAInf.indexOf("  <>  <>  <> "));
//        String[] wordIndexs = wIndex.split(">");
//        /**处理目录和节点内容*/
//        Map<String,BaikeDataVo> baikeAllMap = new HashMap<>();
//        List<BaikeDataVo> baikeVoList =  new ArrayList<BaikeDataVo>(0);
//        for(int i = 0;i<wordIndexs.length;i++){
//            BaikeDataVo baikeVo =  new BaikeDataVo();
//            //目录
//            String widx = wordIndexs[i];
//            String index = widx.substring(widx.indexOf("#")+1,widx.length());
//            String name  = widx.substring(0,widx.indexOf("<"));
//            String url = widx.substring(widx.indexOf("<")+1,widx.length());
//            if(index.contains("_")){
//                baikeVo.setFather(index.substring(0,index.indexOf("_")));
//            }
//            baikeVo.setIndex(index);
//            baikeVo.setTitle(title);
//            name = pure(name,"a");
//            baikeVo.setName(name+"("+title+")");
//            baikeVo.setUrl(url);
//            //内容
//            String markT = title+name;
//            String v1 = wAInf.substring(wAInf.indexOf(markT),wAInf.length());//防止index倒序
//            String v2 = v1.substring(0,v1.indexOf("<>  <>  <>"));
//            v2 = pure(v2,"0");
//            v2 = pure(v2,"b");
////            System.out.println("markT index:"+v2.indexOf(markT));
//            v2=v2.replace(markT,"");
//            baikeVo.setValue(v2);
//            baikeVoList.add(baikeVo);
//            baikeAllMap.put(index,baikeVo);
//        }
//        page.putField("baikeNodes", baikeVoList);
//        page.putField("baikeRelationsMap", baikeAllMap);
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
