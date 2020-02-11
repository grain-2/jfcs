/*
 *编写者：陈冈
 *高校经费测算系统--系统年份管理
 *编写时间：2006-11-9
 */
package cn.edu.jfcs.sys;

import java.util.Calendar;
import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

public class YearManager {
	private int maxYear;

	private int minYear;

	int curYear = Calendar.getInstance().get(Calendar.YEAR);

	private static final YearManager INSTANCE = new YearManager();

	public int getMaxYear() {
		return maxYear;
	}

	public void setMaxYear(final int maxYear) {
		this.maxYear = maxYear;
	}

	public int getMinYear() {
		return minYear;
	}

	public void setMinYear(final int minYear) {
		this.minYear = minYear;
	}

	public int getCurYear() {
		return curYear;
	}

	public void setCurYear(final int curYear) {
		this.curYear = curYear;
	}

	private YearManager() {
		setMaxMinYear();
	}

	public static final YearManager getInstance() {
		return INSTANCE;
	}

	// 从数据库获取并设置最小、最大年份
	private void setMaxMinYear() {
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		ScrollableResults result = session.getNamedQuery(
				"cn.edu.jfcs.sys.YearManager.setMaxMinYear").setCacheMode(
				CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
		int i = 0;
		while (result.next()) {
			setMinYear(result.getInteger(0));
			setMaxYear(result.getInteger(1));
			i++;
		}
		if (i == 0) {
			setMinYear(curYear);
			setMaxYear(curYear);
		}
		HibernateSessionFactory.closeSession();
	}
}
