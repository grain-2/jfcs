/*
*编写者：陈冈
 *高校经费测算系统--教学单位课程明细视图标签提供器
 *编写时间：2006-11-13
*/
package cn.edu.jfcs.ui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import cn.edu.jfcs.model.CourseTreeChildren;
import cn.edu.jfcs.model.CourseTreeParent;
import cn.edu.jfcs.sys.CacheImage;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class TeachUnitClassLabelProvider implements ITableLabelProvider{

	public Image getColumnImage(Object element, int columnIndex) {
		// 如果是父结点列数据
		if (element instanceof CourseTreeParent) {
			CourseTreeParent ctp = (CourseTreeParent) element;
			if (columnIndex == 0
					&& ctp.getCoursename().equals("专业课明细"))
				return CacheImage.getINSTANCE().getImage(IAppConstants.APPLICATION_ID,IImageKey.ZYK_NODE);
			if (columnIndex == 0
					&& ctp.getCoursename().equals("公共课明细"))
				return CacheImage.getINSTANCE().getImage(IAppConstants.APPLICATION_ID,IImageKey.GGK_NODE);
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		String text = "";
		// 如果是父结点列数据
		if (element instanceof CourseTreeParent) {
			CourseTreeParent ctp = (CourseTreeParent) element;
			text = columnIndex == 0 ? ctp.getCoursename() : "";
		} else // 如果是子结点列数据
		if (element instanceof CourseTreeChildren) {
			CourseTreeChildren ctc = (CourseTreeChildren) element;
			switch (columnIndex) {
			case 0:
				text = ctc.getCourse().getCoursename();
				break;
			case 1:
				text = ctc.getCourse().getTerm();
				break;
			case 2:
				text = ctc.getCourse().getN2j() + "";
				break;
			case 3:
				text = ctc.getCourse().getNj() + "";
				break;
			case 4:
				text = ctc.getCourse().getClassname();
				break;
			case 5:
				text = ctc.getCourse().getR1j() + "";
				break;
			case 6:
				text = ctc.getCourse().getR2j() + "";
				break;
			case 7:
				text = ctc.getCourse().getR3j() + "";
				break;
			}
		}
		return text.trim();
	}

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

}
