/*
 *编写者：陈冈
 *高校经费测算系统--公共参数实体类
 *编写时间：2006-11-23
 */
package cn.edu.jfcs.model;

import java.math.BigDecimal;

public class PubData {
	private int id;

	private int nian;

	// 年度拟拨付总额
	private BigDecimal mt;

	// 全校本年应收金额
	private BigDecimal rte;

	// 全校本年实收金额
	private BigDecimal mte;

	// 全校缴费率标准
	private BigDecimal sjf;

	// 学生经费分割比
	private BigDecimal uper;

	// 专业培养费分割比
	private BigDecimal pper;

	// 公共课经费分割比
	private BigDecimal cper;

	// 奖酬金比率
	private BigDecimal jcjper;

	// 学生困难补助比率
	private BigDecimal xsknbzper;

	// 学生活动经费比率
	private BigDecimal xshdjfper;

	// 学生奖学金比率
	private BigDecimal xsjxjper;

	// 教授A系数
	private BigDecimal tb1;

	// 教授B系数
	private BigDecimal tb2;

	// 教授C系数
	private BigDecimal tb3;

	// 教授D系数
	private BigDecimal tb4;

	// 副教授A系数
	private BigDecimal tb5;

	// 副教授B系数
	private BigDecimal tb6;

	// 讲师
	private BigDecimal tb7;

	// 助教
	private BigDecimal tb8;

	public BigDecimal getCper() {
		return cper;
	}

	public void setCper(BigDecimal cper) {
		this.cper = cper;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getJcjper() {
		return jcjper;
	}

	public void setJcjper(BigDecimal jcjper) {
		this.jcjper = jcjper;
	}

	public BigDecimal getMt() {
		return mt;
	}

	public void setMt(BigDecimal mt) {
		this.mt = mt;
	}

	public BigDecimal getMte() {
		return mte;
	}

	public void setMte(BigDecimal mte) {
		this.mte = mte;
	}

	public int getNian() {
		return nian;
	}

	public void setNian(int nian) {
		this.nian = nian;
	}

	public BigDecimal getPper() {
		return pper;
	}

	public void setPper(BigDecimal pper) {
		this.pper = pper;
	}

	public BigDecimal getRte() {
		return rte;
	}

	public void setRte(BigDecimal rte) {
		this.rte = rte;
	}

	public BigDecimal getSjf() {
		return sjf;
	}

	public void setSjf(BigDecimal sjf) {
		this.sjf = sjf;
	}

	public BigDecimal getTb1() {
		return tb1;
	}

	public void setTb1(BigDecimal tb1) {
		this.tb1 = tb1;
	}

	public BigDecimal getTb2() {
		return tb2;
	}

	public void setTb2(BigDecimal tb2) {
		this.tb2 = tb2;
	}

	public BigDecimal getTb3() {
		return tb3;
	}

	public void setTb3(BigDecimal tb3) {
		this.tb3 = tb3;
	}

	public BigDecimal getTb4() {
		return tb4;
	}

	public void setTb4(BigDecimal tb4) {
		this.tb4 = tb4;
	}

	public BigDecimal getTb5() {
		return tb5;
	}

	public void setTb5(BigDecimal tb5) {
		this.tb5 = tb5;
	}

	public BigDecimal getTb6() {
		return tb6;
	}

	public void setTb6(BigDecimal tb6) {
		this.tb6 = tb6;
	}

	public BigDecimal getTb7() {
		return tb7;
	}

	public void setTb7(BigDecimal tb7) {
		this.tb7 = tb7;
	}

	public BigDecimal getTb8() {
		return tb8;
	}

	public void setTb8(BigDecimal tb8) {
		this.tb8 = tb8;
	}

	public BigDecimal getUper() {
		return uper;
	}

	public void setUper(BigDecimal uper) {
		this.uper = uper;
	}

	public BigDecimal getXshdjfper() {
		return xshdjfper;
	}

	public void setXshdjfper(BigDecimal xshdjfper) {
		this.xshdjfper = xshdjfper;
	}

	public BigDecimal getXsjxjper() {
		return xsjxjper;
	}

	public void setXsjxjper(BigDecimal xsjxjper) {
		this.xsjxjper = xsjxjper;
	}

	public BigDecimal getXsknbzper() {
		return xsknbzper;
	}

	public void setXsknbzper(BigDecimal xsknbzper) {
		this.xsknbzper = xsknbzper;
	}

}
