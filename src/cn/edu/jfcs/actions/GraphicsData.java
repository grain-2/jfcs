package cn.edu.jfcs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class GraphicsData extends Action {

	public GraphicsData() {
		setId("cn.edu.jfcs.actions.graphicsData");
		setText("图形对比@ALT+G");
		setToolTipText("图形对比");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID, IImageKey.GRAPHICS_DATA));
	}

	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		try {
			window.getActivePage().showView(
					IAppConstants.STACKED_BAR_CHART_VIEW_ID);

		} catch (PartInitException e) {
			MessageDialog.openError(window.getShell(), "出错", "无法打开图形对比视图！");
		}
	}
}
