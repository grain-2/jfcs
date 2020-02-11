/*
 *编写者：陈冈
 *高校经费测算系统--设置文本框的使能状态
 *编写时间：2006-11-24
 */
package cn.edu.jfcs.sys;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class SetTextEnabled {
	public SetTextEnabled(final Control control, boolean isEnabled) {
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			int j = children.length;
			for (int i = 0; i < j; i++) {
				if (children[i] instanceof Text)
					children[i].setEnabled(isEnabled);
			}
		}
	}
}
