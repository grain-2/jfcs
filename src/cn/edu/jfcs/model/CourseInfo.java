/*
 *编写者：陈冈
 *高校经费测算系统--服务器数据CourseInfo课程实体类
 *编写时间：2006-11-13
 */
package cn.edu.jfcs.model;

public class CourseInfo {
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

	// 课程名称
	private String coursename;

	// 课程类别
	private String coursetype;

	// 班级名称
	private String classname;

	// 学生数
	private int nj;

	// 学时数
	private int n2j;

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
