package cn.edu.jfcs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class Update extends Action {
	public Update() {
		setId("cn.edu.jfcs.actions.update");
		setText("在线升级@ALT+O");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID, IImageKey.UPDATE));
	}

	public void run() {
	}
}
