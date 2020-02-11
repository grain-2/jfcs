/*
 *编写者：陈冈
 *高校经费测算系统--下拉列表框
 *编写时间：2006-11-17
 */
package cn.edu.jfcs.sys;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

public class YearCombo extends ControlContribution {
	private Combo combo;

	private static YearCombo INSTANCE;

	private YearCombo(String id) {
		super(id);
	}

	public static YearCombo getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new YearCombo("cn.edu.jfcs.yearCombo");
		}
		return INSTANCE;
	}

	public Combo getCombo() {
		return combo;
	}

	public void setCombo(Combo combo) {
		this.combo = combo;
	}

	protected Control createControl(Composite parent) {
		combo = new Combo(parent, SWT.READ_ONLY);
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		ScrollableResults result = session.getNamedQuery(
				"cn.edu.jfcs.sys.YearCombo.createControl").setCacheMode(
				CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
		int i = 0;
		while (result.next()) {
			combo.add(String.valueOf(result.getInteger(0)));
			i++;
		}
		if (i == 0) {
			combo.add(String.valueOf(YearManager.getInstance().getCurYear()));
		}
		HibernateSessionFactory.closeSession();
		combo.select(0);
		YearManager.getInstance().setCurYear(Integer.parseInt(combo.getText()));
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				YearManager.getInstance().setCurYear(
						Integer.parseInt(combo.getText()));
				// 激活属性监听事件
				CuryearPropertyChange.getInstance()
						.firePropertyChangeListener();
			}
		});
		return combo;
	}
}
