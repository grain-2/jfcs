/*
*编写者：陈冈
 *高校经费测算系统--教学单位课程明细内容提供器
 *编写时间：2006-11-13
*/
package cn.edu.jfcs.ui;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cn.edu.jfcs.model.CourseTreeChildren;
import cn.edu.jfcs.model.CourseTreeParent;

public class TeachUnitClassContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof CourseTreeParent)
			return ((CourseTreeParent) parentElement).getCourseTreeChildren().toArray();
		return new Object[0];
	}

	public Object getParent(Object element) {
		if (element instanceof CourseTreeChildren)
			return ((CourseTreeChildren) element).getCourseParent();
		return null;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	public Object[] getElements(Object inputElement) {
		return ((List) inputElement).toArray();
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

}
