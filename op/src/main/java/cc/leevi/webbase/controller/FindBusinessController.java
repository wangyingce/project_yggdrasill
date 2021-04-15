package cc.leevi.webbase.controller;

import cc.leevi.webbase.service.BusinessPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author WangYingce
 *
 */
@RestController
@RequestMapping
public class FindBusinessController {

    @Autowired
    private BusinessPropertyService businessPropertyService;

    @GetMapping("queryClaimPortrait")
    public String queryClaimPortraitByModelAndClaimNo(String modelNo,String claimNo) throws Exception {
        String answer = "";
        if(modelNo!=null&&!"".equals(modelNo)&&claimNo!=null&&!"".equals(claimNo)){
            answer = businessPropertyService.queryClaimPortraitByModelAndClaimNo(modelNo,claimNo);
        }else{
            answer = "E1";
        }
        return answer;
    }

    @GetMapping("queryPersonPortrait")
    public String queryPersonPortraitByModelAndClaimNo(String modelNo,String claimNo) throws Exception {
        String answer = "";
        if(modelNo!=null&&!"".equals(modelNo)&&claimNo!=null&&!"".equals(claimNo)){
            answer = businessPropertyService.queryPersonPortraitByModelAndClaimNo(modelNo,claimNo);
        }else{
            answer = "E1";
        }
        return answer;
    }
}
