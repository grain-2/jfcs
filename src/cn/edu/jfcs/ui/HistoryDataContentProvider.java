/*
 *编写者：陈冈
 *高校经费测算系统--历史数据视图内容提供器
 *编写时间：2007-1-2
 */
package cn.edu.jfcs.ui;

import java.util.List;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import cn.edu.jfcs.model.HistoryDataTreeChildren;
import cn.edu.jfcs.model.HistoryDataTreeParent;

public class HistoryDataContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof HistoryDataTreeParent)
			return ((HistoryDataTreeParent) parentElement).getTreeChildren()
					.toArray();
		return new Object[0];
	}

	public Object getParent(Object element) {
		if (element instanceof HistoryDataTreeChildren)
			return ((HistoryDataTreeChildren) element).getTreeParent();
		return null;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	public Object[] getElements(Object inputElement) {
		return ((List) inputElement).toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
