package cc.leevi.webbase.controller;

import cc.leevi.webbase.service.UserLogicService;
import cc.leevi.webbase.utils.UUIDUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

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
    public Map<String, String> checkUserLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "properties") String properties){
        Boolean checkUserLogin = false;
        Map<String, String> msg = new HashedMap();//错误信息收集池
        if (properties == null || "".equals(properties)) {
            msg.put("error", "checkUserLogin入参为空");
        }else{
            Map propertiesMap = JSON.parseObject(properties);
            checkUserLogin = userLogicService.checkUserLogin(String.valueOf(propertiesMap.get("name")),String.valueOf(propertiesMap.get("password")));
            if(checkUserLogin){
                msg.put("sus", "用户检索成功");
                Cookie cookie1 = new Cookie(UUIDUtils.usercId, UUIDUtils.getUUID());
                cookie1.setMaxAge(1*24*60*60*30);//一个月失效
                response.addCookie(cookie1);
            }else{
                msg.put("error", "未检索到当前用户");
            }
        }
        return msg;
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

    @RequestMapping("/createTemplate")
    public Map<String, String> createTemplate(@RequestParam(value = "properties") String properties) throws Exception {
        Map<String, String> msg = new HashedMap();
        if (properties == null || "".equals(properties)) {
            msg.put("error", "createTemplate入参为空");
        }
        Map propertiesMap = JSON.parseObject(properties);
        //test
//        Map propertiesMap  =  new HashMap();
//        propertiesMap.put("user","wangyingce");
//        propertiesMap.put("name","solarsys");
//        propertiesMap.put("remark","test");
        userLogicService.createTemplate(propertiesMap);
        msg.put("sus", "createTemplate成功");
        return msg;
    }

}
