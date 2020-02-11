package cn.edu.jfcs.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import cn.edu.jfcs.model.Course;
import cn.edu.jfcs.model.CourseInfo;
import cn.edu.jfcs.model.SaveLogInfo;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;
import cn.edu.jfcs.sys.YearManager;

public class GetServerData extends Action {
	private boolean isSucess = false;

	public GetServerData() {
		setId("cn.edu.jfcs.actions.getServerData");
		setText("获取数据@ALT+S");
		setToolTipText("获取服务器数据");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID, IImageKey.GET_SERVER_DATA));
		setEnabled(SaveLogInfo.getInstance().getUsertag().equals("2") ? true
				: false);
	}

	public void run() {
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(null);
		IRunnableWithProgress rwp = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				monitor.beginTask("获取服务器数据。。。", IProgressMonitor.UNKNOWN);
				// 本地数据库
				monitor.subTask("正在创建本地数据库Hibernate配置。。。");
				SessionFactory sf_local = new Configuration().configure(
						"hibernate_derby.cfg.xml").buildSessionFactory();
				Session session_local = sf_local.openSession();
				// 获得年份
				int year = YearManager.getInstance().getMaxYear();
				// 服务器数据库
				monitor.subTask("正在创建远程数据库Hibernate配置。。。");
				String filePath;
				try {
					filePath = FileLocator.toFileURL(
							Platform.getBundle(IAppConstants.APPLICATION_ID)
									.getEntry("")).getPath().toString()
							+ "cfg/hibernate_mysql.cfg.xml";
				} catch (IOException e) {
					monitor.done();
					return;
				}
				SessionFactory sf_server = new Configuration().configure(
						new File(filePath)).buildSessionFactory();
				Session session_server = sf_server.openSession();
				// 获取服务器当前测算年度Courseinfo数据
				monitor.subTask("正在获取服务器数据库信息。。。");
				Query query_server_course = session_server
						.getNamedQuery("cn.edu.jfcs.actions.GetServerData.run.getServerCourse");
				query_server_course.setInteger(0, year);
				ScrollableResults result_server_course = query_server_course
						.setCacheMode(CacheMode.IGNORE).scroll(
								ScrollMode.FORWARD_ONLY);
				int count = 0;
				// 获取本地数据库需要更新的当前测算年度Course数据
				monitor.subTask("正在下载数据到本地数据库。。。");
				Query query_local_course = session_local
						.getNamedQuery("cn.edu.jfcs.actions.GetServerData.run.getLocalCourse");
				query_local_course.setInteger(0, year);
				ScrollableResults result_local_course = query_local_course
						.setCacheMode(CacheMode.IGNORE).scroll(
								ScrollMode.SCROLL_SENSITIVE);
				Transaction tx = session_local.beginTransaction();
				boolean isInsert = true;
				while (result_server_course.next()) {
					CourseInfo courseinfo = (CourseInfo) result_server_course
							.get(0);
					if (result_local_course.next())
						isInsert = false;
					// 本地数据库中无记录，则Insert数据，否则Update数据
					if (isInsert) {
						Course course = new Course();
						course.setNian(year);
						course.setTerm(courseinfo.getTerm());
						course.setUnitid(courseinfo.getUnitid());
						course.setCourseid(courseinfo.getCourseid());
						course.setCoursetype(courseinfo.getCoursetype());
						course.setCoursename(courseinfo.getCoursename());
						course.setClassname(courseinfo.getClassname());
						course.setNj(courseinfo.getNj());
						course.setN2j(courseinfo.getN2j());
						course.setR1j(new BigDecimal("0"));
						course.setR2j(new BigDecimal("0"));
						course.setR3j(new BigDecimal("0"));
						session_local.save(course);
					} else {
						String stag = courseinfo.getTerm().trim()
								+ courseinfo.getCourseid().trim()
								+ courseinfo.getClassname().trim();
						result_local_course.beforeFirst();
						while (result_local_course.next()) {
							Course course = (Course) result_local_course.get(0);
							String tag = course.getTerm().trim()
									+ course.getCourseid().trim()
									+ course.getClassname().trim();
							if (tag.toLowerCase().equals(stag.toLowerCase())) {
								course.setClassname(courseinfo.getClassname());
								course.setCourseid(courseinfo.getCourseid());
								course
										.setCoursename(courseinfo
												.getCoursename());
								course
										.setCoursetype(courseinfo
												.getCoursetype());
								course.setNj(courseinfo.getNj());
								course.setN2j(courseinfo.getN2j());
								break;
							}
						}
					}
					if (++count % 20 == 0) {
						session_local.flush();
						session_local.clear();
					}
				}
				tx.commit();
				sf_server.close();
				sf_local.close();
				monitor.done();
				if (tx.wasCommitted())
					isSucess = true;
			}
		};
		try {
			pmd.run(true, false, rwp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isSucess
				&& MessageDialog.openQuestion(null, "提示",
						"成功获取服务器数据！需要重新启动测算系统，现在就重启？"))
			PlatformUI.getWorkbench().restart();
		if (!isSucess)
			MessageDialog.openError(null, "提示", "获取服务器数据失败!");
	}
}
