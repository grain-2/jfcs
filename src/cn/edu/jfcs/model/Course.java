/*
 *编写者：陈冈
 *高校经费测算系统--课程实体类
 *编写时间：2006-11-13
 */
package cn.edu.jfcs.model;

import java.math.BigDecimal;

public class Course {
	// 记录标识
	private int id;

	// 学期
	private String term;

	// 年份
	private int nian;

	// 教学单位代码
	private String unitid;

	// 课程代码
	private String courseid;

	// 课程类别
	private String coursetype;

	// 课程名称
	private String coursename;

	// 班级名称
	private String classname;

	// 学生数
	private int nj;

	// 学时数
	private int n2j;

	// 学生层次系数
	private BigDecimal r1j;

	// 课程/专业系数
	private BigDecimal r2j;

	// 课程质量系数
	private BigDecimal r3j;

	// 所属的教学单位
	private TeachUnit teachunit;

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getCoursetype() {
		return coursetype;
	}

	public void setCoursetype(String coursetype) {
		this.coursetype = coursetype;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getN2j() {
		return n2j;
	}

	public void setN2j(int n2j) {
		this.n2j = n2j;
	}

	public int getNian() {
		return nian;
	}

	public void setNian(int nian) {
		this.nian = nian;
	}

	public int getNj() {
		return nj;
	}

	public void setNj(int nj) {
		this.nj = nj;
	}

	public BigDecimal getR1j() {
		return r1j;
	}

	public void setR1j(BigDecimal r1j) {
		this.r1j = r1j;
	}

	public BigDecimal getR2j() {
		return r2j;
	}

	public void setR2j(BigDecimal r2j) {
		this.r2j = r2j;
	}

	public BigDecimal getR3j() {
		return r3j;
	}

	public void setR3j(BigDecimal r3j) {
		this.r3j = r3j;
	}

	public TeachUnit getTeachunit() {
		return teachunit;
	}

	public void setTeachunit(TeachUnit teachunit) {
		this.teachunit = teachunit;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

}
