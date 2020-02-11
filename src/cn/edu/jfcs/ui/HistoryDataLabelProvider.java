/*
 *编写者：陈冈
 *高校经费测算系统--
 *编写时间：2007-1-2
 */
package cn.edu.jfcs.ui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import cn.edu.jfcs.model.HistoryDataTreeChildren;
import cn.edu.jfcs.model.HistoryDataTreeParent;

public class HistoryDataLabelProvider implements ITableLabelProvider {
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		String text = "";
		// 如果是父结点列数据
		if (element instanceof HistoryDataTreeParent) {
			HistoryDataTreeParent hdtp = (HistoryDataTreeParent) element;
			text = columnIndex == 0 ? hdtp.getYear() + "年" : "";
		} else // 如果是子结点列数据
		if (element instanceof HistoryDataTreeChildren) {
			HistoryDataTreeChildren hdtc = (HistoryDataTreeChildren) element;
			switch (columnIndex) {
			case 0:
				text = hdtc.getCalcresult().getTeachunit().getUnitname();
				break;
			case 1:
				text = String.valueOf(hdtc.getCalcresult().getUi());
				break;
			case 2:
				text = String.valueOf(hdtc.getCalcresult().getPi());
				break;
			case 3:
				text = String.valueOf(hdtc.getCalcresult().getCi());
				break;
			case 4:
				text = String.valueOf(hdtc.getCalcresult().getRyjf());
				break;
			case 5:
				text = String.valueOf(hdtc.getCalcresult().getZhywf());
				break;
			}
		}
		return text;
	}

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}