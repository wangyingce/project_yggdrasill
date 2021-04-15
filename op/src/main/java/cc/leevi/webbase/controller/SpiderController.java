package cc.leevi.webbase.controller;

import cc.leevi.webbase.pipeline.BaikeSpiderPipeline;
import cc.leevi.webbase.pipeline.Neo4jSavePipeline;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;


@RestController
@RequestMapping
public class SpiderController {
    /***
     * 单词百科爬虫和图谱组织
     * @param wds
     * @return
     * @throws Exception
     */
    @GetMapping("SpiderBaike")
    public Object spiderDataTest(String wds) throws Exception {
        Spider.create(new BaikeSpiderPipeline()).addUrl("https://baike.baidu.com/item/"+wds).addPipeline(new Neo4jSavePipeline()).thread(5).run();
        return null;
    }
}
