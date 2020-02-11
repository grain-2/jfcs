package cn.edu.jfcs.app;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.IWorkbenchWindow;
import cn.edu.jfcs.actions.AboutSystem;
import cn.edu.jfcs.actions.AddNewYear;
import cn.edu.jfcs.actions.DataBak;
import cn.edu.jfcs.actions.GetServerData;
import cn.edu.jfcs.actions.GraphicsData;
import cn.edu.jfcs.actions.HistoryData;
import cn.edu.jfcs.actions.Logoff;
import cn.edu.jfcs.actions.PublicData;
import cn.edu.jfcs.actions.RemoteDataSRConfig;
import cn.edu.jfcs.actions.TeachUnitData;
import cn.edu.jfcs.actions.Update;
import cn.edu.jfcs.actions.UserManag;
import cn.edu.jfcs.model.SaveLogInfo;
import cn.edu.jfcs.sys.StatusBarContribution;
import org.eclipse.ui.internal.intro.IntroMessages;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	// 基础数据菜单：公共参数设置、教学单位情况
	private Action publicData, teachUnitData;

	// 测算数据菜单：历史数据、图形对比
	private Action historyData, graphicsData;

	// 系统管理菜单：获取数据、用户管理、数据备份、数据恢复、远程数据源配置、新增年度
	private Action getServerData, userManag, dataBak, remoteDataSourceConfig,
			addNewYear;

	// 帮助菜单：欢迎画面、帮助信息
	private IWorkbenchAction welcomeAction, helpInfo;

	// 帮助菜单：关于系统
	private Action aboutSystem;

	// 工具栏：用户注销
	private Action logoff, update;

	// 状态栏Contribution对象
	private StatusBarContribution statusBarContribution;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	// 生成Action对象
	protected void makeActions(IWorkbenchWindow window) {
		welcomeAction = ActionFactory.INTRO.create(window);
		welcomeAction.setText("欢迎使用");
		welcomeAction.setAccelerator(SWT.ALT + 87); // 设置快捷键为ALT+W
		IntroMessages.Intro_default_title = "经费测算，开源节流";
		register(welcomeAction);

		helpInfo = ActionFactory.HELP_CONTENTS.create(window);
		helpInfo.setText("系统帮助@ALT+H");
		helpInfo.setToolTipText("系统帮助");
		register(helpInfo);
		publicData = new PublicData();
		register(publicData);
		teachUnitData = new TeachUnitData();
		register(teachUnitData);
		historyData = new HistoryData();
		register(historyData);
		graphicsData = new GraphicsData();
		register(graphicsData);
		getServerData = new GetServerData();
		register(getServerData);
		userManag = new UserManag(window);
		register(userManag);
		dataBak = new DataBak();
		register(dataBak);
		remoteDataSourceConfig = new RemoteDataSRConfig();
		register(remoteDataSourceConfig);
		addNewYear = new AddNewYear();
		register(addNewYear);
		aboutSystem = new AboutSystem();
		register(aboutSystem);
		logoff = new Logoff();
		register(logoff);
		update = new Update();
		register(update);

		// 配置状态栏
		String msg = "登录用户：" + SaveLogInfo.getInstance().getUsername();
		String usertag = SaveLogInfo.getInstance().getUsertag();
		usertag = usertag.equals("0") ? "普通用户" : (usertag.equals("1") ? "教务处"
				: "管理员");
		msg += "\t\t身份：" + usertag;
		statusBarContribution = new StatusBarContribution(msg);
		statusBarContribution.setVisible(true);
	}

	// 生成菜单对象
	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager baseDataMenu = new MenuManager("基础数据[&B]", "baseDataMenu");
		menuBar.add(baseDataMenu);
		baseDataMenu.add(publicData);
		baseDataMenu.add(teachUnitData);
		MenuManager calcDataMenu = new MenuManager("测算数据[&C]", "calcDataMenu");
		menuBar.add(calcDataMenu);
		calcDataMenu.add(historyData);
		calcDataMenu.add(graphicsData);
		MenuManager sysManagMenu = new MenuManager("系统管理[&M]", "sysManagMenu");
		menuBar.add(sysManagMenu);
		sysManagMenu.add(getServerData);
		sysManagMenu.add(userManag);
		sysManagMenu.add(dataBak);
		sysManagMenu.add(remoteDataSourceConfig);
		sysManagMenu.add(addNewYear);
		sysManagMenu.add(update);
		MenuManager helpMenu = new MenuManager("帮助[&H]", "helpManagMenu");
		menuBar.add(helpMenu);
		helpMenu.add(welcomeAction);
		helpMenu.add(helpInfo);
		helpMenu.add(aboutSystem);
	}

	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolBar = new ToolBarManager(SWT.FLAT | SWT.SHADOW_OUT);
		coolBar.add(toolBar);
		toolBar.add(publicData);
		toolBar.add(teachUnitData);
		toolBar.add(new Separator());
		toolBar.add(historyData);
		toolBar.add(graphicsData);
		toolBar.add(new Separator());
		toolBar.add(getServerData);
		toolBar.add(dataBak);
		toolBar.add(logoff);
		toolBar.add(new Separator());
		toolBar.add(helpInfo);
	}

	protected void fillStatusLine(IStatusLineManager statusLine) {
		statusLine.add(statusBarContribution);
	}

}
