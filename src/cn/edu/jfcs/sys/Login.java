package cn.edu.jfcs.sys;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.hibernate.Query;
import org.hibernate.Session;
import cn.edu.jfcs.model.SaveLogInfo;
import cn.edu.jfcs.model.Users;
import cn.edu.jfcs.ui.LoginDialog;

public class Login {
	private boolean isSuccesseful = false;

	public Login() {
	}

	public boolean isLogin() {
		final LoginDialog loginDialog = new LoginDialog(Display.getCurrent()
				.getActiveShell());
		while (!isSuccesseful) {
			if (loginDialog.open() != Window.OK)
				return false;
			String username = SaveLogInfo.getInstance().getUsername();
			String password = SaveLogInfo.getInstance().getPassword();
			isSuccesseful = isValidate(username, password);
			if (!isSuccesseful)
				MessageDialog.openWarning(null, "提示", "用户名或者密码错误，无法登录！");
		}
		return isSuccesseful;
	}

	private boolean isValidate(final String username, final String password) {
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(null);
		IRunnableWithProgress rwp = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				isSuccesseful = exeLogin(monitor, username, password);

			}
		};
		try {
			pmd.run(true, false, rwp);
		} catch (Exception e) {
			isSuccesseful = false;
		}
		return isSuccesseful;

	}

	private boolean exeLogin(final IProgressMonitor monitor, String username,
			String password) {
		boolean tag = false;
		try {
			monitor.beginTask("正在登录数据库，请稍候。。。", 10);
			monitor.subTask("创建Hibernate工厂。。。");
			Session session = HibernateSessionFactory.getSession("hibernate_derby.cfg.xml");
			monitor.subTask("验证用户名、密码。。。");
			Query query = session.getNamedQuery("cn.edu.jfcs.sys.Login.exeLogin");
			query.setString(0, username);
			query.setString(1, password);
			Iterator iterate = query.iterate();
			if (iterate.hasNext()) {
				Users user = (Users) iterate.next();
				SaveLogInfo.getInstance().setUsertag(user.getUsertag());
				tag = true;
			}
			monitor.subTask("销毁Hibernate Session。。。");
			HibernateSessionFactory.closeSession();
		} catch (Exception e) {
			e.printStackTrace();
			tag = false;
		} finally {
			monitor.done();
		}
		return tag;
	}
	
}