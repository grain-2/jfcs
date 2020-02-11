/*
 *编写者：陈冈
 *高校经费测算系统--TeachUnitData视图的内容提供器
 *编写时间：2006-11-16
 */
package cn.edu.jfcs.ui;

import java.util.List;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TeachUnitDataContentProvider implements IStructuredContentProvider {

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List)
			return ((List) inputElement).toArray();
		return new Object[0];
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

}
