package cn.edu.jfcs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;
import cn.edu.jfcs.ui.AboutSystemDialog;

public class AboutSystem extends Action {

	public AboutSystem() {
		setId("cn.edu.jfcs.actions.aboutSystem");
		setText("关于经费测算系统@ALT+A");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID, IImageKey.ABOUT_SYSTEM));
	}

	public void run() {
		new AboutSystemDialog(Display.getCurrent().getActiveShell()).open();
	}
}