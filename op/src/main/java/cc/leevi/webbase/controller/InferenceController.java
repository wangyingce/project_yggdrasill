package cc.leevi.webbase.controller;

import cc.leevi.webbase.service.BusinessPropertyService;
import cc.leevi.webbase.service.KgInferenceService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 指向型推理
 * @author WangYingce
 *
 */
@RestController
@RequestMapping
public class InferenceController {

    @Autowired
    private KgInferenceService kgInferenceService;

    @Autowired
    private BusinessPropertyService businessPropertyService;

    /**
     * base-1文本结果
     *
     * @param registNo
     * @param policyNo
     * @param insuredName
     * @return
     * @throws Exception
     */
    @GetMapping("InferenceKgBase1")
    public Map <String, String> InferenceKgBase1(String registNo, String policyNo, String insuredName) throws Exception {
        Map <String, String> rstmap = new HashedMap();
        /**补充参数：1-报案号缺失，2-保单号缺失，3-被保险人姓名缺失*/
        if (policyNo == null || "".equals(policyNo)) {
            policyNo = kgInferenceService.getBase1Parameter(registNo, policyNo, insuredName, 2);
        }
        /**规则-1：保险起止期与出险日期过近，返回起期差，<2则提示*/
        String info1 = kgInferenceService.getInferenceKgBase11(registNo, policyNo);
        rstmap.put("info1", info1);
        /**规则-2：多次理赔赔付类型相同*/
        String info2 = kgInferenceService.getInferenceKgBase12(registNo, policyNo, insuredName);
        rstmap.put("info2", info2);
        return rstmap;
    }

//    @GetMapping("FindBusinessProperty")
//    public List <Map <String, Object>> FindBusinessProperty(String sqlString) throws Exception {
//        sqlString = "select t.C_INSURED_CDE from web_clm_accdnt t where t.C_CLM_NO='4000001061001190000000024'";
//        List <Map <String, Object>> resultMap = businessPropertyService.findBusinessPropertyBySql(sqlString);
//        return resultMap;
//    }
}
