/*
 *编写者：陈冈
 *高校经费测算系统--教学单位情况Action
 *编写时间：2006-11-16
 */
package cn.edu.jfcs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class TeachUnitData extends Action {

	public TeachUnitData() {
		setId("cn.edu.jfcs.actions.teachUnitData");
		setText("教学单位情况@ALT+T");
		setToolTipText("教学单位情况");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID, IImageKey.TEACH_UNIT_DATA));
	}

	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		try {
			window.getActivePage().showView(
					IAppConstants.TEACH_UNIT_DATA_VIEW_ID);

		} catch (PartInitException e) {
			MessageDialog.openError(window.getShell(), "出错", "无法打开教学单位基本情况视图！");
		}
	}
}
