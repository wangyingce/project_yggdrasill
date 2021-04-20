package cc.leevi.webbase.controller;

import cc.leevi.webbase.utils.CheckoutUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserLogicController {
    @GetMapping("/checkUser")
    public String saveUser(String signature,String timestamp,String nonce,String echostr){
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (signature != null && CheckoutUtils.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return "";
    }
}
