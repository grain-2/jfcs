package cn.edu.jfcs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;
import cn.edu.jfcs.ui.PublicDataEditorInput;

public class PublicData extends Action {

	public PublicData() {
		setId("cn.edu.jfcs.actions.publicData");
		setText("公共参数设置@ALT+P");
		setToolTipText("公共参数设置");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID, IImageKey.PUBLIC_DATA));
	}

	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		PublicDataEditorInput input = new PublicDataEditorInput();
		try {
			window.getActivePage().savePerspective();
			window.getActivePage().openEditor(input,IAppConstants.PUBLIC_DATA_EDITOR_ID, true);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
