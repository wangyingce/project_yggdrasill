package cc.leevi.webbase.mapper;

import java.util.Date;

/**
 * 抄单信息表
 * @author WangYingce
 *
 */
public class Web_Clm_Ply_Base {
	private static final long serialVersionUID = 1L;
    private String c_Pk_Id;
	private String c_Clm_No;
	private String c_Ply_No;
    private Date t_Crt_Tm;
    private Date t_Modify_Tm;
    private Date t_Insrnc_Bgn_Tm;
    private Date t_Insrnc_End_Tm;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getC_Pk_Id() {
		return c_Pk_Id;
	}

	public void setC_Pk_Id(String c_Pk_Id) {
		this.c_Pk_Id = c_Pk_Id;
	}

	public String getC_Clm_No() {
		return c_Clm_No;
	}

	public void setC_Clm_No(String c_Clm_No) {
		this.c_Clm_No = c_Clm_No;
	}

	public String getC_Ply_No() {
		return c_Ply_No;
	}

	public void setC_Ply_No(String c_Ply_No) {
		this.c_Ply_No = c_Ply_No;
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

	public Date getT_Insrnc_Bgn_Tm() {
		return t_Insrnc_Bgn_Tm;
	}

	public void setT_Insrnc_Bgn_Tm(Date t_Insrnc_Bgn_Tm) {
		this.t_Insrnc_Bgn_Tm = t_Insrnc_Bgn_Tm;
	}

	public Date getT_Insrnc_End_Tm() {
		return t_Insrnc_End_Tm;
	}

	public void setT_Insrnc_End_Tm(Date t_Insrnc_End_Tm) {
		this.t_Insrnc_End_Tm = t_Insrnc_End_Tm;
	}
}