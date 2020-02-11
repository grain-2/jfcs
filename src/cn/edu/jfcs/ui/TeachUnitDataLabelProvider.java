/*
 *编写者：陈冈
 *高校经费测算系统--教学单位基本情况视图标签提供器
 *编写时间：2006-11-16
 */
package cn.edu.jfcs.ui;

import java.text.DecimalFormat;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import cn.edu.jfcs.model.YearTeachUnit;

public class TeachUnitDataLabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		String text = "";
		if (element instanceof YearTeachUnit) {
			// 设置显示格式
			DecimalFormat dnf = (DecimalFormat) DecimalFormat
					.getPercentInstance();
			dnf.applyPattern("###.##%");
			YearTeachUnit ytu = (YearTeachUnit) element;
			switch (columnIndex) {
			case 0:
				text = ytu.getTeachunit().getUnitname();
				break;
			case 1:
				text = String.valueOf(ytu.getSsb());
				break;
			case 2:
				// 数字后面加％
				text = dnf.format(ytu.getJxywper().doubleValue() / 100);
				break;
			case 3:
				text = dnf.format(ytu.getJxglper().doubleValue() / 100);
				break;
			case 4:
				text = dnf.format(ytu.getJxyjper().doubleValue() / 100);
				break;
			case 5:
				text = dnf.format(ytu.getSzpyper().doubleValue() / 100);
				break;
			}
		}
		return text;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {

	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

}
