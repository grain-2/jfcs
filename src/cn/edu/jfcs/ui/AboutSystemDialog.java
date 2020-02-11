/*
 *编写者：陈冈
 *高校经费测算系统--关于系统对话框
 *编写时间：2007-1-10
 */
package cn.edu.jfcs.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import cn.edu.jfcs.sys.CacheImage;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class AboutSystemDialog extends Dialog {
	private Color bgcolor;

	public AboutSystemDialog(Shell parentShell) {
		super(parentShell);
	}

	protected Control createDialogArea(final Composite parent) {
		bgcolor = new Color(parent.getDisplay(), 0xDF, 0xDF, 0xDF);
		parent.setBackground(bgcolor);
		final GridData gridData = new GridData(GridData.CENTER,
				GridData.CENTER, false, false);
		gridData.widthHint = 260;
		gridData.heightHint = 120;
		final Canvas canvas = new Canvas(parent, SWT.NONE);
		canvas.setLayoutData(gridData);
		canvas.setBackground(bgcolor);
		canvas.setSize(parent.getClientArea().width,
				parent.getClientArea().height);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				event.gc.drawImage(CacheImage.getINSTANCE().getImage(
						IAppConstants.APPLICATION_ID, IImageKey.COPY_INFO), 0,
						0);
			}
		});
		return parent;
	}

	protected void createButtonsForButtonBar(final Composite parent) {
		parent.setBackground(bgcolor);
		createButton(parent, IDialogConstants.CANCEL_ID, "确定", true);
	}

	protected Point getInitialSize() {
		return new Point(280, 190);
	}

	public boolean close() {
		if (bgcolor != null)
			bgcolor.dispose();
		return super.close();
	}

	// 设置窗口标题
	protected void configureShell(Shell parent) {
		super.configureShell(parent);
		parent.setText("关于经费测算系统");
		// titleImage = AbstractUIPlugin.imageDescriptorFromPlugin(
		// IAppConstants.APPLICATION_ID, IImageKeys.ABOUT_SYSTEM)
		// .createImage();
		// parent.setImage(titleImage);
	}
}
