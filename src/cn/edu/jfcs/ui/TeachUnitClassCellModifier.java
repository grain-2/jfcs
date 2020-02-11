/*
 *编写者：陈冈
 *高校经费测算系统--TeachUnitClass视图的编辑器
 *编写时间：2006-11-13
 */
package cn.edu.jfcs.ui;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Item;
import cn.edu.jfcs.model.CourseTreeChildren;
import cn.edu.jfcs.model.CourseTreeParent;
import cn.edu.jfcs.sys.SetControlEnabled;
import cn.edu.jfcs.sys.YearManager;

public class TeachUnitClassCellModifier implements ICellModifier {
	private TreeViewer treeViewer;

	public TeachUnitClassCellModifier(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	public boolean canModify(Object element, String property) {
		if (element instanceof CourseTreeParent) {
			return false;
		}
		if (element instanceof CourseTreeChildren) {
			// 只有最后3列可以修改
			if (property.equals(TeachUnitClass.R1J)
					|| property.equals(TeachUnitClass.R2J)
					|| property.equals(TeachUnitClass.R3J))
				// 非管理员用户、历史数据不可修改
				return new SetControlEnabled().isEnabled(YearManager
						.getInstance().getCurYear());
		}
		return false;
	}

	public Object getValue(Object element, String property) {
		if (element instanceof CourseTreeChildren) {
			CourseTreeChildren ctc = (CourseTreeChildren) element;
			if (property.equals(TeachUnitClass.R1J))
				return ctc.getCourse().getR1j() + ""; // 学生层次系数
			else if (property.equals(TeachUnitClass.R2J))
				return ctc.getCourse().getR2j() + ""; // 课程/专业系数
			else if (property.equals(TeachUnitClass.R3J))
				return ctc.getCourse().getR3j() + ""; // 课程质量系数
		}
		return null;
	}

	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();
		if (element instanceof CourseTreeChildren) {
			Pattern pc = Pattern.compile("[0-9]{1}(\\.[0-9])?");
			Matcher m = pc.matcher((String) value);
			if (!m.matches()) {
				MessageDialog.openError(null, "提示",
						"输入错误，请不要输入负数、字母、空格或者其他非数字数据！");
				return;
			}
			CourseTreeChildren ctc = (CourseTreeChildren) element;
			if (TeachUnitClass.R1J.equals(property))
				ctc.getCourse().setR1j(new BigDecimal((String) value));
			else if (TeachUnitClass.R2J.equals(property))
				ctc.getCourse().setR2j(new BigDecimal((String) value));
			else if (TeachUnitClass.R3J.equals(property))
				ctc.getCourse().setR3j(new BigDecimal((String) value));
		}
		treeViewer.refresh();
	}
}
