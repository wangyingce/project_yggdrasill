package cc.leevi.webbase.mapper;

import java.util.Date;

/**
 * 支付信息表
 * @author WangYingce
 *
 */
public class Web_Clm_Bank {
	private static final long serialVersionUID = 1L;
    private String c_Bank_Id;
    private String c_Clm_No;
    private String c_Payee_Nme;
    private String c_Payee_No;
    private Date t_Crt_Tm;
    private Date t_Modify_Tm;
    private String c_Pay_Typ;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getC_Bank_Id() {
		return c_Bank_Id;
	}

	public void setC_Bank_Id(String c_Bank_Id) {
		this.c_Bank_Id = c_Bank_Id;
	}

	public String getC_Clm_No() {
		return c_Clm_No;
	}

	public void setC_Clm_No(String c_Clm_No) {
		this.c_Clm_No = c_Clm_No;
	}

	public String getC_Payee_Nme() {
		return c_Payee_Nme;
	}

	public void setC_Payee_Nme(String c_Payee_Nme) {
		this.c_Payee_Nme = c_Payee_Nme;
	}

	public String getC_Payee_No() {
		return c_Payee_No;
	}

	public void setC_Payee_No(String c_Payee_No) {
		this.c_Payee_No = c_Payee_No;
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

	public String getC_Pay_Typ() {
		return c_Pay_Typ;
	}

	public void setC_Pay_Typ(String c_Pay_Typ) {
		this.c_Pay_Typ = c_Pay_Typ;
	}
}