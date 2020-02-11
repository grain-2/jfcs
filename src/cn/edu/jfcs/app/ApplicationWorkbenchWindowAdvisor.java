package cn.edu.jfcs.app;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import cn.edu.jfcs.sys.CacheImage;
import cn.edu.jfcs.sys.HookSysTray;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private HookSysTray hookSysTray;

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	private void createSystemTray() {
		hookSysTray = new HookSysTray();
		hookSysTray.createSysTray(getWindowConfigurer().getWindow());
	}

	public boolean preWindowShellClose() {
		hookSysTray.windowMinimized(getWindowConfigurer().getWindow()
				.getShell());
		return false;
	}

	public void postWindowOpen() {
		// 设置窗口自动居中
		Shell shell = getWindowConfigurer().getWindow().getShell();
		Rectangle screenSize = Display.getDefault().getClientArea();
		Rectangle frameSize = shell.getBounds();
		shell.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		// 创建系统托盘
		createSystemTray();
		// 设置状态栏临时信息
		IStatusLineManager statusline = getWindowConfigurer()
				.getActionBarConfigurer().getStatusLineManager();
		statusline.setMessage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.AUTHOR),
				"Powered by ChenGang");
		// 根据系统参数决定是否最小化
		String[] args = Platform.getApplicationArgs();
		if (args.length == 1 && args[0].equals("system"))
			getWindowConfigurer().getWindow().getShell().setMinimized(true);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(800, 600));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShellStyle(SWT.MIN | SWT.CLOSE);
		configurer.setTitle(IAppConstants.APPLICATION_TITLE);
		configurer.setShowProgressIndicator(true);
	}

	public void dispose() {
		hookSysTray.Dispose();
		CacheImage.getINSTANCE().dispose();
	}
}
