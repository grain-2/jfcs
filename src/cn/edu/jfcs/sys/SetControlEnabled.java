/*
 *编写者：陈冈
 *高校经费测算系统--控件使能状态设置
 *编写时间：2006-11-15
 */
package cn.edu.jfcs.sys;

import cn.edu.jfcs.model.SaveLogInfo;

public class SetControlEnabled {

	public SetControlEnabled() {
	}

	public boolean isEnabled(int curYear) {
		return (SaveLogInfo.getInstance().getUsertag().equals("1") || SaveLogInfo
				.getInstance().getUsertag().equals("2"))
				&& curYear == YearManager.getInstance().getMaxYear();
	}
}
