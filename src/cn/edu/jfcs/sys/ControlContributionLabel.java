/*
 *编写者：陈冈
 *高校经费测算系统--视图工具栏标签
 *编写时间：2006-12-1
 */
package cn.edu.jfcs.sys;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;

public class ControlContributionLabel extends ControlContribution {
	private String text;

	protected ControlContributionLabel(String id) {
		super(id);
	}

	public ControlContributionLabel(String id, String text) {
		super(id);
		this.text = text;
	}

	protected Control createControl(Composite parent) {
		ToolBar toolBar = (ToolBar) parent;
		OS.SetWindowLong(toolBar.handle, OS.GWL_STYLE, OS.GetWindowLong(
				toolBar.handle, OS.GWL_STYLE) ^ 0x1000);
		final CLabel label = new CLabel(parent, SWT.NONE);
		label.setText(text);
		return label;
	}
}
