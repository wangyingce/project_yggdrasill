package cc.leevi.webbase.controller;

import cc.leevi.webbase.service.UserLogicService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping
public class UserLogicController {
    @Autowired
    private UserLogicService userLogicService;


    @GetMapping("/checkUser")
    public Boolean checkUser(String properties){
        Boolean checkFlag = false;
        Map<String, String> error = new HashedMap();//错误信息收集池
        if (properties == null || "".equals(properties)) {
            error.put("error", "checkUser入参为空");
        }
        Map propertiesMap = JSON.parseObject(properties);
        Iterator iterator = propertiesMap.keySet().iterator();
        while(iterator.hasNext()){
            String ppkey = iterator.next().toString();
            if(ppkey=="name"||"name".equals(ppkey)){
                checkFlag = userLogicService.checkUser(String.valueOf(propertiesMap.get(ppkey)));
            }
        }
        return checkFlag;
    }

    @RequestMapping("/checkUserLogin")
    public Boolean checkUserLogin(@RequestParam(value = "properties") String properties){
        Boolean checkUserLogin = false;
        Map<String, String> error = new HashedMap();//错误信息收集池
        if (properties == null || "".equals(properties)) {
            error.put("error", "checkUserLogin入参为空");
        }
        Map propertiesMap = JSON.parseObject(properties);
        checkUserLogin = userLogicService.checkUserLogin(String.valueOf(propertiesMap.get("name")),String.valueOf(propertiesMap.get("password")));
        return checkUserLogin;
    }

    @RequestMapping("/registUser")
    public Map<String, String> registUser(@RequestParam(value = "properties") String properties) throws Exception {
        Map<String, String> msg = new HashedMap();
        if (properties == null || "".equals(properties)) {
            msg.put("error", "registUser入参为空");
        }
        Map propertiesMap = JSON.parseObject(properties);
        userLogicService.registUser(propertiesMap);
        msg.put("sus", "registUser成功");
        return msg;
    }
}
