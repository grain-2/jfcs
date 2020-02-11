package cn.edu.jfcs.sys;

import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import cn.edu.jfcs.actions.Logoff;

public class HookSysTray {

	private TrayItem trayItem;

	public HookSysTray() {

	}

	public void createSysTray(final IWorkbenchWindow window) {
		trayItem = initTrayItem(window);
		if (trayItem != null) {
			trayPopupMenu(window);
			trayMinimize(window);
		}
	}

	// 最小化程序窗口
	public void windowMinimized(final Shell shell) {
		shell.setMinimized(true);
		shell.setVisible(false);
	}

	// 最小化程序到托盘
	private void trayMinimize(final IWorkbenchWindow window) {
		window.getShell().addShellListener(new ShellAdapter() {
			public void shellIconified(ShellEvent e) {
				window.getShell().setVisible(false);
			}
		});
		trayItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Shell shell = window.getShell();
				if (!shell.isVisible()) {
					shell.setVisible(true);
					window.getShell().setMinimized(false);
				}
			}
		});
	}

	// 托盘弹出菜单
	private void trayPopupMenu(final IWorkbenchWindow window) {
		trayItem.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				MenuManager trayMenu = new MenuManager();
				Menu menu = trayMenu.createContextMenu(window.getShell());
				fillTrayItem(trayMenu, window);
				menu.setVisible(true);
			}
		});
	}

	// 构造托盘菜单项
	private void fillTrayItem(IMenuManager trayItem,
			final IWorkbenchWindow window) {
		Action exitSystem = new Action("退出系统[&E]", AbstractUIPlugin
				.imageDescriptorFromPlugin(IAppConstants.APPLICATION_ID,
						IImageKey.EXIT_SYSTEM)) {
			public void run() {
				PlatformUI.getWorkbench().close();
			}
		};
		trayItem.add(new Logoff());
		trayItem.add(exitSystem);
	}

	// 初始化托盘项目的文字和图标
	private TrayItem initTrayItem(final IWorkbenchWindow window) {
		final Tray tray = window.getShell().getDisplay().getSystemTray();
		if (tray == null)
			return null;
		trayItem = new TrayItem(tray, SWT.NONE);
		trayItem.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.WINDOW_IMAGE));
		// 定时显示气泡提示文本
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				window.getShell().getDisplay().syncExec(new Runnable() {
					public void run() {
						ToolTip tip = new ToolTip(window.getShell(),
								SWT.BALLOON | SWT.ICON_INFORMATION);
						tip.setAutoHide(true);
						tip.setMessage(IAppConstants.APPLICATION_TITLE);
						tip.setText("欢迎使用");
						trayItem.setToolTip(tip);
						tip.setVisible(true);
					}
				});
			}
		}, 0, 30 * 60 * 1000);
		return trayItem;
	}

	public void Dispose() {
		if (trayItem != null)
			trayItem.dispose();
	}
}