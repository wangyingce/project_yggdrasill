package cc.leevi.webbase.mapper;
import java.util.Date;

/**
 * 出险信息表
 * @author WangYingce
 *
 */
public class Web_Clm_Accdnt implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String c_Ins_Acc_Pk_Id;
	private String c_Clm_No;
	private Date t_Accdnt_Tm;
	private String c_Cert_No;
	private String c_Insured_Nme;
	private Date t_Crt_Tm;
	private Date t_Modify_Tm;
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getC_Ins_Acc_Pk_Id() {
		return c_Ins_Acc_Pk_Id;
	}

	public void setC_Ins_Acc_Pk_Id(String c_Ins_Acc_Pk_Id) {
		this.c_Ins_Acc_Pk_Id = c_Ins_Acc_Pk_Id;
	}

	public String getC_Clm_No() {
		return c_Clm_No;
	}

	public void setC_Clm_No(String c_Clm_No) {
		this.c_Clm_No = c_Clm_No;
	}

	public Date getT_Accdnt_Tm() {
		return t_Accdnt_Tm;
	}

	public void setT_Accdnt_Tm(Date t_Accdnt_Tm) {
		this.t_Accdnt_Tm = t_Accdnt_Tm;
	}

	public String getC_Cert_No() {
		return c_Cert_No;
	}

	public void setC_Cert_No(String c_Cert_No) {
		this.c_Cert_No = c_Cert_No;
	}

	public String getC_Insured_Nme() {
		return c_Insured_Nme;
	}

	public void setC_Insured_Nme(String c_Insured_Nme) {
		this.c_Insured_Nme = c_Insured_Nme;
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