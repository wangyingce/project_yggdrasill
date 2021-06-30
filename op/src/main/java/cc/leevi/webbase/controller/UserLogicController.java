package cc.leevi.webbase.controller;

import cc.leevi.webbase.service.UserLogicService;
import cc.leevi.webbase.utils.UUIDUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSON;

@RestController
@RequestMapping
public class UserLogicController {
    @Autowired
    private UserLogicService userLogicService;

    @RequestMapping("/checkUserLogin")
    public Map<String, String> checkUserLogin(HttpServletResponse response, @RequestParam(value = "properties") String properties){
        Boolean checkUserLogin = false;
        Map<String, String> msg = new HashedMap();//错误信息收集池
        if (properties == null || "".equals(properties)) {
            msg.put("error", "checkUserLogin入参为空");
        }else{
            Map propertiesMap = JSON.parseObject(properties);
            checkUserLogin = userLogicService.checkUserLogin(String.valueOf(propertiesMap.get("name")),String.valueOf(propertiesMap.get("password")));
            if(checkUserLogin){
                msg.put("sus", "用户检索成功");
                String uuid = UUIDUtils.getUUID();
                Cookie cookie1 = new Cookie(UUIDUtils.usercId, uuid);
                cookie1.setMaxAge(1*24*60*60*30);//一个月失效
                response.addCookie(cookie1);
                userLogicService.addUserCookies(String.valueOf(propertiesMap.get("name")),uuid);
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

//    @RequestMapping("/createTemplate")
//    public Map<String, String> createTemplate(@RequestParam(value = "properties") String properties) throws Exception {
//        Map<String, String> msg = new HashedMap();
//        if (properties == null || "".equals(properties)) {
//            msg.put("error", "createTemplate入参为空");
//        }
//        Map propertiesMap = JSON.parseObject(properties);
//        userLogicService.createTemplate(propertiesMap);
//        msg.put("sus", "createTemplate成功");
//        return msg;
//    }

    @RequestMapping("/initUserInfo")
    public Object initUserInfo(@RequestParam(value = "property") String userc) throws Exception {
    //test,property
//    public Map<String, String> initUserInfo(String property) throws Exception {
//        property = "cd25464c7e724c289382bbf260ca21f0";
        Map<String, Object> msg = new HashedMap();
        if(userc==null||"".equals(userc)||"undefined".equals(userc)){
            msg.put("error", "请先登录");
        }else{
            List<Map<String, Object>> userlist =  userLogicService.queryNodeUserByCookies(userc);
            if(userlist!=null&&userlist.size()>0){
                for(Map<String, Object> user :userlist){
                    msg.put("user",user.get("u").toString());
                }
                List<Map<String, Object>> mlist =  userLogicService.queryModelCookies(userc);
                if(mlist!=null&&mlist.size()>0){
                    List<Object> modeList = new ArrayList<>();
                    for(Map<String, Object> m :mlist){
                        modeList.add(m.get("m"));
                    }
                    msg.put("model",modeList);
                }
                msg.put("sus", "initUserInfo成功");
            }else{
                msg.put("error", "initUserInfo未查询到用户信息");
            }
        }
        return toJSON(msg);
    }

}
