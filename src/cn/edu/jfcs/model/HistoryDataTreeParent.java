/*
 *编写者：陈冈
 *高校经费测算系统--历史数据父结点
 *编写时间：2007-1-2
 */
package cn.edu.jfcs.model;

import java.util.ArrayList;
import java.util.List;

public class HistoryDataTreeParent {
	private int year;

	private List<HistoryDataTreeChildren> treeChildren;

	public HistoryDataTreeParent(int year) {
		this.year = year;
		treeChildren = new ArrayList<HistoryDataTreeChildren>();
	}

	// 添加子结点
	public void add(HistoryDataTreeChildren children) {
		treeChildren.add(children);
		// 设置子结点的父结点
		children.setTreeParent(this);
	}

	public List<HistoryDataTreeChildren> getTreeChildren() {
		return treeChildren;
	}

	public void setTreeChildren(List<HistoryDataTreeChildren> treeChildren) {
		this.treeChildren = treeChildren;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
