/*
 *编写者：陈冈
 *高校经费测算系统--读写hibernate_mysql.cfg.xml
 *编写时间：2007-1-12
 */
package cn.edu.jfcs.sys;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class ConfigCFGXML {
	boolean connectedSucess = false;

	boolean saveSucess = false;

	public ConfigCFGXML() {

	}

	public boolean testConnect(final String url, final String className,
			final String username, final String password) {
		ProgressMonitorDialog progress = new ProgressMonitorDialog(null);
		try {
			progress.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					monitor.beginTask("正在连接远程数据源服务器，请稍候。。。",
							IProgressMonitor.UNKNOWN);
					monitor.subTask("正在连接【" + url + "】。。。");
					try {
						Class.forName(className);
						Connection conn = DriverManager.getConnection(url,
								username, password);
						conn.close();
						connectedSucess = true;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						monitor.done();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connectedSucess;
	}

	// 将配置写入hibernate_mysql.cfg.xml文件
	public boolean saveConfig(final String url, final String classname,
			final String username, final String password) {
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(null);
		IRunnableWithProgress rwp = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				monitor.beginTask("正在写入xml配置文件。。。", IProgressMonitor.UNKNOWN);
				// 获得文件路径
				String path = null;
				try {
					path = FileLocator.toFileURL(
							Platform.getBundle(IAppConstants.APPLICATION_ID)
									.getEntry("")).getPath().toString()
							+ "cfg/hibernate_mysql.cfg.xml";
				} catch (IOException e) {
					monitor.done();
					return;
				}
				File cfgxmlFile = new File(path);
				// 读取xml文件
				SAXReader reader = new SAXReader();
				Document document = null;
				try {
					document = reader.read(cfgxmlFile);
				} catch (DocumentException e) {
					monitor.done();
					return;
				}
				List list = document.selectNodes("//property");
				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {
					Element element = (Element) iterator.next();
					String attrText = element.attributeValue("name");
					// 设置新值
					if (attrText.equals("hibernate.connection.url"))
						element.setText(url);
					if (attrText.equals("hibernate.connection.driver_class"))
						element.setText(classname);
					if (attrText.equals("hibernate.connection.username"))
						element.setText(username);
					if (attrText.equals("hibernate.connection.password"))
						element.setText(password);

				}
				// 写入xml文件
				try {
					XMLWriter output = new XMLWriter(
							new FileWriter(cfgxmlFile), OutputFormat
									.createPrettyPrint());
					output.write(document);
					output.close();
					saveSucess = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
				monitor.done();
			}
		};
		try {
			pmd.run(true, false, rwp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveSucess;
	}
}
