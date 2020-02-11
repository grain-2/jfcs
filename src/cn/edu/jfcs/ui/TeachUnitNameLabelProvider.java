/*
 *编写者：陈冈
 *高校经费测算系统--教学单位视图标签提供器
 *编写时间：2006-11-9
 */
package cn.edu.jfcs.ui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import cn.edu.jfcs.model.YearTeachUnit;
import cn.edu.jfcs.sys.CacheImage;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class TeachUnitNameLabelProvider implements ITableLabelProvider {

	public TeachUnitNameLabelProvider() {
	}

	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof YearTeachUnit) {
			YearTeachUnit ytu = (YearTeachUnit) element;
			switch (columnIndex) {
			case 0:
				return CacheImage.getINSTANCE().getImage(
						IAppConstants.APPLICATION_ID, IImageKey.FOUR_STAR);
			case 1:
				String haszyk = ytu.getHaszyk();
				return haszyk.equals("1") ? CacheImage.getINSTANCE().getImage(
						IAppConstants.APPLICATION_ID, IImageKey.ZYK_GGK_TAG)
						: null;
			case 2:
				String hasggk = ytu.getHasggk();
				return hasggk.equals("1") ? CacheImage.getINSTANCE().getImage(
						IAppConstants.APPLICATION_ID, IImageKey.ZYK_GGK_TAG)
						: null;
			}
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof YearTeachUnit && columnIndex == 0) {
			YearTeachUnit ytu = (YearTeachUnit) element;
			return ytu.getTeachunit().getUnitname();
		}
		return null;
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