package cc.leevi.webbase.vo;

import java.util.List;

public class KgspVo {
    private String insCode;
    private String insName;
    private String kgUrl;//查看知识图谱
    private String remark;//说明
    private List <KgspInfoVo> kgspInfoVo;

    public String getInsCode() {
        return insCode;
    }

    public void setInsCode(String insCode) {
        this.insCode = insCode;
    }

    public String getInsName() {
        return insName;
    }

    public void setInsName(String insName) {
        this.insName = insName;
    }

    public List <KgspInfoVo> getKgspInfoVo() {
        return kgspInfoVo;
    }

    public void setKgspInfoVo(List <KgspInfoVo> kgspInfoVo) {
        this.kgspInfoVo = kgspInfoVo;
    }

    public String getKgUrl() {
        return kgUrl;
    }

    public void setKgUrl(String kgUrl) {
        this.kgUrl = kgUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}