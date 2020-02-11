package cn.edu.jfcs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class HistoryData extends Action {

	public HistoryData() {
		setId("cn.edu.jfcs.actions.historyData");
		setText("历史数据@ALT+O");
		setToolTipText("历史数据");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID, IImageKey.HISTORY_DATA));
	}

	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		try {
			window.getActivePage().showView(IAppConstants.HISTORY_DATA_VIEW_ID);

		} catch (PartInitException e) {
			MessageDialog.openError(window.getShell(), "出错", "无法打开历史数据视图！");
		}
	}

}
