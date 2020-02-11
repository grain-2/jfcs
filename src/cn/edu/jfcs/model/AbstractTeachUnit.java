/*
 *编写者：陈冈
 *高校经费测算系统--教学单位抽象类
 *编写时间：2006-11-18
 */
package cn.edu.jfcs.model;

public abstract class AbstractTeachUnit implements ITeachUnit {
	private String unitid;

	private String unitname;

	public String getUnitid() {
		return unitid;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;

	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;

	}

}
