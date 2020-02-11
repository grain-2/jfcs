package cn.edu.jfcs.actions;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.edu.jfcs.model.Calcresult;
import cn.edu.jfcs.model.PubData;
import cn.edu.jfcs.model.SaveLogInfo;
import cn.edu.jfcs.model.YearTeachUnit;
import cn.edu.jfcs.sys.HibernateSessionFactory;
import cn.edu.jfcs.sys.YearManager;

public class AddNewYear extends Action {
	private boolean isSucess = false;

	public AddNewYear() {
		setId("cn.edu.jfcs.actions.addnewyear");
		setText("新增年度@ALT+N");
		setEnabled(SaveLogInfo.getInstance().getUsertag().equals("2") ? true
				: false);
	}

	public void run() {
		if (!MessageDialog.openQuestion(null, "提示",
				"警告，新增年度后，当前年度数据将成为历史数据从而不可操作！\n确认新增年度？"))
			return;
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(null);
		IRunnableWithProgress rwp = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				monitor.beginTask("正在新增年度。。。", IProgressMonitor.UNKNOWN);
				Session session = HibernateSessionFactory
						.getSession("hibernate_derby.cfg.xml");
				monitor.subTask("新增经费测算结果数据。。。");
				int maxYear = YearManager.getInstance().getMaxYear();
				// 新增测算结果数据
				Query query = session
						.getNamedQuery("cn.edu.jfcs.actions.AddNewYear.run.getPreCalcresult");
				query.setInteger(0, maxYear);
				ScrollableResults result = query.setCacheMode(CacheMode.IGNORE)
						.scroll(ScrollMode.FORWARD_ONLY);
				Transaction tx = session.beginTransaction();
				int count = 0;
				while (result.next()) {
					Calcresult calcresult = (Calcresult) result.get(0);
					Calcresult newCalcresult = new Calcresult();
					newCalcresult.setNian(maxYear + 1);
					newCalcresult.setUnitid(calcresult.getUnitid());
					session.save(newCalcresult);
					if (++count % 20 == 0) {
						session.flush();
						session.clear();
					}
				}
				monitor.subTask("新增公共参数。。。");
				query = session
						.getNamedQuery("cn.edu.jfcs.actions.AddNewYear.run.getPrePubData");
				query.setInteger(0, maxYear);
				result = query.setCacheMode(CacheMode.IGNORE).scroll(
						ScrollMode.FORWARD_ONLY);
				if (result.next()) {
					PubData pubData = (PubData) result.get(0);
					PubData newPubData = new PubData();
					newPubData.setNian(maxYear + 1);
					newPubData.setMt(new BigDecimal("0"));
					newPubData.setRte(new BigDecimal("0"));
					newPubData.setMte(new BigDecimal("0"));
					newPubData.setSjf(pubData.getSjf());
					newPubData.setUper(pubData.getUper());
					newPubData.setPper(pubData.getPper());
					newPubData.setCper(pubData.getCper());
					newPubData.setJcjper(pubData.getJcjper());
					newPubData.setXsknbzper(pubData.getXsknbzper());
					newPubData.setXshdjfper(pubData.getXshdjfper());
					newPubData.setXsjxjper(pubData.getXsjxjper());
					newPubData.setTb1(pubData.getTb1());
					newPubData.setTb2(pubData.getTb2());
					newPubData.setTb3(pubData.getTb3());
					newPubData.setTb4(pubData.getTb4());
					newPubData.setTb5(pubData.getTb5());
					newPubData.setTb6(pubData.getTb6());
					newPubData.setTb7(pubData.getTb7());
					newPubData.setTb8(pubData.getTb8());
					session.save(newPubData);
					if (++count % 20 == 0) {
						session.flush();
						session.clear();
					}
				}
				monitor.subTask("新增教学单位基本情况数据。。。");
				query = session
						.getNamedQuery("cn.edu.jfcs.actions.AddNewYear.run.getPreYearTeachUnit");
				query.setInteger(0, maxYear);
				result = query.setCacheMode(CacheMode.IGNORE).scroll(
						ScrollMode.FORWARD_ONLY);
				while (result.next()) {
					YearTeachUnit ytu = (YearTeachUnit) result.get(0);
					YearTeachUnit newYearTeachUnit = new YearTeachUnit();
					newYearTeachUnit.setNian(maxYear + 1);
					newYearTeachUnit.setUnitid(ytu.getUnitid());
					newYearTeachUnit.setHaszyk(ytu.getHaszyk());
					newYearTeachUnit.setHasggk(ytu.getHasggk());
					newYearTeachUnit.setSsb(ytu.getSsb());
					newYearTeachUnit.setJfys(new BigDecimal("0"));
					newYearTeachUnit.setJfss(new BigDecimal("0"));
					newYearTeachUnit.setJxywper(ytu.getJxywper());
					newYearTeachUnit.setJxglper(ytu.getJxglper());
					newYearTeachUnit.setJxyjper(ytu.getJxyjper());
					newYearTeachUnit.setSzpyper(ytu.getSzpyper());
					newYearTeachUnit.setTa1(ytu.getTa1());
					newYearTeachUnit.setTa2(ytu.getTa2());
					newYearTeachUnit.setTa3(ytu.getTa3());
					newYearTeachUnit.setTa4(ytu.getTa4());
					newYearTeachUnit.setTa5(ytu.getTa5());
					newYearTeachUnit.setTa6(ytu.getTa6());
					newYearTeachUnit.setTa7(ytu.getTa7());
					newYearTeachUnit.setTa8(ytu.getTa8());
					session.save(newYearTeachUnit);
					if (++count % 20 == 0) {
						session.flush();
						session.clear();
					}
				}
				tx.commit();
				HibernateSessionFactory.closeSession();
				monitor.done();
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
						"操作成功！需要重新启动测算系统，现在就重启？"))
			PlatformUI.getWorkbench().restart();
		if (!isSucess)
			MessageDialog.openError(null, "提示", "新增年度操作失败!");
	}
}