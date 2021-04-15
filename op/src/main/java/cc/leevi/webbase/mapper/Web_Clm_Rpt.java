package cc.leevi.webbase.mapper;

import java.util.Date;
/**
 * 报案信息表
 * @author WangYingce
 *
 */
public class Web_Clm_Rpt {
	private static final long serialVersionUID = 1L;
	private String c_Clm_No;
	private Date t_Rpt_Tm;
	private String c_Rptman_Tel;
    private Date t_Crt_Tm;
    private Date t_Modify_Tm;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getC_Clm_No() {
		return c_Clm_No;
	}

	public void setC_Clm_No(String c_Clm_No) {
		this.c_Clm_No = c_Clm_No;
	}

	public Date getT_Rpt_Tm() {
		return t_Rpt_Tm;
	}

	public void setT_Rpt_Tm(Date t_Rpt_Tm) {
		this.t_Rpt_Tm = t_Rpt_Tm;
	}

	public String getC_Rptman_Tel() {
		return c_Rptman_Tel;
	}

	public void setC_Rptman_Tel(String c_Rptman_Tel) {
		this.c_Rptman_Tel = c_Rptman_Tel;
	}

	public Date getT_Crt_Tm() {
		return t_Crt_Tm;
	}

	public void setT_Crt_Tm(Date t_Crt_Tm) {
		this.t_Crt_Tm = t_Crt_Tm;
	}

	public Date getT_Modify_Tm() {
		return t_Modify_Tm;
	}

	public void setT_Modify_Tm(Date t_Modify_Tm) {
		this.t_Modify_Tm = t_Modify_Tm;
	}
}