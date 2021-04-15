package cc.leevi.webbase.mapper;

import java.util.Date;

/**
 * 赔付信息表
 * @author WangYingce
 *
 */
public class Web_Clm_Adjust implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String c_Adjust_Pk_Id;
	private String c_Clm_No;
	private String c_Pay_Liability;
	private Date t_Crt_Tm;
	private Date t_Modify_Tm;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getC_Adjust_Pk_Id() {
		return c_Adjust_Pk_Id;
	}

	public void setC_Adjust_Pk_Id(String c_Adjust_Pk_Id) {
		this.c_Adjust_Pk_Id = c_Adjust_Pk_Id;
	}

	public String getC_Clm_No() {
		return c_Clm_No;
	}

	public void setC_Clm_No(String c_Clm_No) {
		this.c_Clm_No = c_Clm_No;
	}

	public String getC_Pay_Liability() {
		return c_Pay_Liability;
	}

	public void setC_Pay_Liability(String c_Pay_Liability) {
		this.c_Pay_Liability = c_Pay_Liability;
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