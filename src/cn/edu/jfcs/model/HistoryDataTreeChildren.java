/*
 *编写者：陈冈
 *高校经费测算系统--历史数据子结点
 *编写时间：2007-1-2
 */
package cn.edu.jfcs.model;

public class HistoryDataTreeChildren {
	// 父结点
	private HistoryDataTreeParent treeParent;

	// 测算结果
	private Calcresult calcresult;

	public Calcresult getCalcresult() {
		return calcresult;
	}

	public void setCalcresult(Calcresult calcresult) {
		this.calcresult = calcresult;
	}

	public HistoryDataTreeParent getTreeParent() {
		return treeParent;
	}

	public void setTreeParent(HistoryDataTreeParent treeParent) {
		this.treeParent = treeParent;
	}

}
