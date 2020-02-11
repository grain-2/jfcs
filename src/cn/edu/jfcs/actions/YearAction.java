/*
 *编写者：陈冈
 *高校经费测算系统--年份导航
 *编写时间：2006-11-19
 */
package cn.edu.jfcs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import cn.edu.jfcs.sys.YearManager;

public class YearAction extends Action {

	private int actionType;

	public YearAction(String text, int actionType, ImageDescriptor image) {
		this.actionType = actionType;
		setToolTipText(text);
		setImageDescriptor(image);
	}

	public void run() {
		int curYear = YearManager.getInstance().getCurYear();
		int minYear = YearManager.getInstance().getMinYear();
		int maxYear = YearManager.getInstance().getMaxYear();
		// 到了最小年份
		if (actionType == 0 && curYear == minYear) {
			MessageDialog.openInformation(null, "提示", "已经到了数据库中的最小年份！");
			return;
		}
		// 到了最近年份
		if (actionType == 1 && curYear == maxYear) {
			MessageDialog.openInformation(null, "提示", "已经到了数据库中的最近年份！");
			return;
		}
		// 上一年
		if (actionType == 0 && curYear > minYear) {
			curYear--;
			YearManager.getInstance().setCurYear(curYear);
		}
		// 下一年
		if (actionType == 1 && curYear < maxYear) {
			curYear++;
			YearManager.getInstance().setCurYear(curYear);
		}
	}
}
