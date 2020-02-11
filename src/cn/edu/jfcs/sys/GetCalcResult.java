/*
 *编写者：陈冈
 *高校经费测算系统--获得测算结果数据
 *编写时间：2006-11-12
 */
package cn.edu.jfcs.sys;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import cn.edu.jfcs.model.Calcresult;

public class GetCalcResult {
	
	List<Calcresult> calcresult = new ArrayList<Calcresult>();

	public List getCalcresult() {
		return calcresult;
	}

	public void setCalcresult(List<Calcresult> calcresult) {
		this.calcresult = calcresult;
	}

	public GetCalcResult() {
		super();
	}

	// 获得指定教学单位的测算结果
	public GetCalcResult(int year, String unitid) {
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		Query query = session.getNamedQuery("cn.edu.jfcs.sys.GetCalcResult.GetCalcResult1");
		query.setInteger(0, year);
		query.setString(1,unitid);
		ScrollableResults result = query.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
		int i=0;
		while (result.next()) {
			Calcresult cs=(Calcresult) result.get(i);
			calcresult.add(cs);
		}
		HibernateSessionFactory.closeSession();
	}

	// 获得指定年份全部教学单位的测算数据
	public GetCalcResult(int year) {
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		Query query = session.getNamedQuery("cn.edu.jfcs.sys.GetCalcResult.GetCalcResult2");
		query.setInteger(0, year);
		ScrollableResults result = query.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
		int i=0;
		while (result.next()) {
			Calcresult cs=(Calcresult) result.get(i);
			calcresult.add(cs);
		}
		HibernateSessionFactory.closeSession();
	}
}
