package cn.edu.jfcs.model;

import java.math.BigDecimal;

public class YearTeachUnit {
	private int id;

	private int nian;

	private String unitid;

	// 是否有专业课
	private String haszyk;

	// 是否有公共课
	private String hasggk;

	// 生师比
	private int ssb;

	// 学费应收金额
	private BigDecimal jfys;

	// 学费实收金额
	private BigDecimal jfss;

	// 教学业务费比率
	private BigDecimal jxywper;

	// 教学管理费比率
	private BigDecimal jxglper;

	// 教学研究费比率
	private BigDecimal jxyjper;

	// 师资培养费比率
	private BigDecimal szpyper;

	// 教授A等人数
	private int ta1;

	// 教授B等人数
	private int ta2;

	// 教授C等人数
	private int ta3;

	// 教授D等人数
	private int ta4;

	// 副教授A等人数
	private int ta5;

	// 副教授B等人数
	private int ta6;

	// 讲师人数
	private int ta7;

	// 助教人数
	private int ta8;

	// 所属教学单位
	private TeachUnit teachunit;

	public String getHasggk() {
		return hasggk;
	}

	public void setHasggk(String hasggk) {
		this.hasggk = hasggk;
	}

	public String getHaszyk() {
		return haszyk;
	}

	public void setHaszyk(String haszyk) {
		this.haszyk = haszyk;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getJfss() {
		return jfss;
	}

	public void setJfss(BigDecimal jfss) {
		this.jfss = jfss;
	}

	public BigDecimal getJfys() {
		return jfys;
	}

	public void setJfys(BigDecimal jfys) {
		this.jfys = jfys;
	}

	public BigDecimal getJxglper() {
		return jxglper;
	}

	public void setJxglper(BigDecimal jxglper) {
		this.jxglper = jxglper;
	}

	public BigDecimal getJxyjper() {
		return jxyjper;
	}

	public void setJxyjper(BigDecimal jxyjper) {
		this.jxyjper = jxyjper;
	}

	public BigDecimal getJxywper() {
		return jxywper;
	}

	public void setJxywper(BigDecimal jxywper) {
		this.jxywper = jxywper;
	}

	public int getNian() {
		return nian;
	}

	public void setNian(int nian) {
		this.nian = nian;
	}

	public int getSsb() {
		return ssb;
	}

	public void setSsb(int ssb) {
		this.ssb = ssb;
	}

	public BigDecimal getSzpyper() {
		return szpyper;
	}

	public void setSzpyper(BigDecimal szpyper) {
		this.szpyper = szpyper;
	}

	public int getTa1() {
		return ta1;
	}

	public void setTa1(int ta1) {
		this.ta1 = ta1;
	}

	public int getTa2() {
		return ta2;
	}

	public void setTa2(int ta2) {
		this.ta2 = ta2;
	}

	public int getTa3() {
		return ta3;
	}

	public void setTa3(int ta3) {
		this.ta3 = ta3;
	}

	public int getTa4() {
		return ta4;
	}

	public void setTa4(int ta4) {
		this.ta4 = ta4;
	}

	public int getTa5() {
		return ta5;
	}

	public void setTa5(int ta5) {
		this.ta5 = ta5;
	}

	public int getTa6() {
		return ta6;
	}

	public void setTa6(int ta6) {
		this.ta6 = ta6;
	}

	public int getTa7() {
		return ta7;
	}

	public void setTa7(int ta7) {
		this.ta7 = ta7;
	}

	public int getTa8() {
		return ta8;
	}

	public void setTa8(int ta8) {
		this.ta8 = ta8;
	}

	public TeachUnit getTeachunit() {
		return teachunit;
	}

	public void setTeachunit(TeachUnit teachunit) {
		this.teachunit = teachunit;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

}