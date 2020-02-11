package cn.edu.jfcs.actions;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.Session;

import cn.edu.jfcs.model.SaveLogInfo;
import cn.edu.jfcs.sys.HibernateSessionFactory;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class DataBak extends Action {
	boolean isSucess = false;

	public DataBak() {
		setId("cn.edu.jfcs.actions.dataBak");
		setText("数据备份@ALT+D");
		setToolTipText("数据备份");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID, IImageKey.DATA_BAK));
		setEnabled(SaveLogInfo.getInstance().getUsertag().equals("2") ? true
				: false);
	}

	public void run() {
		// 选择备份目标文件夹对话框
		DirectoryDialog directoryDialog = new DirectoryDialog(Display
				.getCurrent().getActiveShell());
		// 对话框标题
		directoryDialog.setText("系统数据备份");
		// 对话框提示文字
		directoryDialog.setMessage("请选择备份目标文件夹：");
		final String detsFolder = directoryDialog.open();
		if (detsFolder == null)
			return;

		ProgressMonitorDialog pmd = new ProgressMonitorDialog(null);
		IRunnableWithProgress rwp = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				monitor.beginTask("正在备份数据。。。", IProgressMonitor.UNKNOWN);
				Session session = HibernateSessionFactory
						.getSession("hibernate_derby.cfg.xml");
				String baksql = "{CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)}";
				Connection conn = session.connection();
				try {
					CallableStatement cs = conn.prepareCall(baksql);
					cs.setString(1, detsFolder);
					cs.execute();
					isSucess = true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				HibernateSessionFactory.closeSession();
				monitor.done();
			}
		};
		try {
			pmd.run(true, false, rwp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isSucess)
			MessageDialog.openInformation(null, "提示", "数据备份成功！");
		else
			MessageDialog.openError(null, "提示", "数据备份失败！");
	}
}
