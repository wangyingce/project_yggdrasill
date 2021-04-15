package cc.leevi.webbase.vo;
import cc.leevi.webbase.mapper.*;

/**
 * 理赔数据抽取泵容器-base模型-1
 * @author WangYingce
 * @category 20190319 2crt
 */
public class PumpClaimDataVo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Web_Clm_Accdnt web_Clm_Accdnt;
    private Web_Clm_Bank web_Clm_Bank;
    private Web_Clm_Ply_Base web_Clm_Ply_Base;
    private Web_Clm_Rpt web_Clm_Rpt;
    private Web_Clm_Adjust web_Clm_Adjust;

    public Web_Clm_Accdnt getWeb_Clm_Accdnt() {
        return web_Clm_Accdnt;
    }
    public void setWeb_Clm_Accdnt(Web_Clm_Accdnt web_Clm_Accdnt) {
        this.web_Clm_Accdnt = web_Clm_Accdnt;
    }
    public Web_Clm_Bank getWeb_Clm_Bank() {
        return web_Clm_Bank;
    }
    public void setWeb_Clm_Bank(Web_Clm_Bank web_Clm_Bank) {
        this.web_Clm_Bank = web_Clm_Bank;
    }
    public Web_Clm_Ply_Base getWeb_Clm_Ply_Base() {
        return web_Clm_Ply_Base;
    }
    public void setWeb_Clm_Ply_Base(Web_Clm_Ply_Base web_Clm_Ply_Base) {
        this.web_Clm_Ply_Base = web_Clm_Ply_Base;
    }
    public Web_Clm_Rpt getWeb_Clm_Rpt() {
        return web_Clm_Rpt;
    }
    public void setWeb_Clm_Rpt(Web_Clm_Rpt web_Clm_Rpt) {
        this.web_Clm_Rpt = web_Clm_Rpt;
    }
    public Web_Clm_Adjust getWeb_Clm_Adjust() {
        return web_Clm_Adjust;
    }
    public void setWeb_Clm_Adjust(Web_Clm_Adjust web_Clm_Adjust) {
        this.web_Clm_Adjust = web_Clm_Adjust;
    }
}