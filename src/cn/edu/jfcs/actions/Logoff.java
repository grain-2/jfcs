package cn.edu.jfcs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class Logoff extends Action {

	public Logoff() {
		setId("cn.edu.jfcs.actions.logoff");
		setText("用户注销[&R]");
		setToolTipText("用户注销");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID,IImageKey.LOGOFF));
	}

	public void run() {	
		PlatformUI.getWorkbench().restart();
	}
}
