/*
 *编写者：陈冈
 *高校经费测算系统--设置表格行渐变颜色
 *编写时间：2006-11-13
 */
package cn.edu.jfcs.sys;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

public final class SetTableColColorListener implements Listener {
	private Table table = null;

	private Tree tree = null;

	public SetTableColColorListener(Table table) {
		this.table = table;
	}

	public SetTableColColorListener(Tree tree) {
		this.tree = tree;
	}

	public void handleEvent(Event event) {
		if ((event.detail & SWT.SELECTED) != 0) {
			GC gc = event.gc;
			Rectangle area = null;
			if (tree != null)
				area = tree.getClientArea();
			if (table != null)
				area = table.getClientArea();
			Rectangle rect = event.getBounds();
			Color foreColor = new Color(Display.getCurrent(), 0x8c, 0xb3, 0xe1);
			gc.setForeground(foreColor);
			gc.setBackground(Display.getCurrent().getSystemColor(
					SWT.COLOR_WHITE));
			gc.fillGradientRectangle(0, rect.y - 3, area.width, rect.height,
					true);
			event.detail &= ~SWT.SELECTED;
			foreColor.dispose();
			gc.dispose();
		}
	}
}