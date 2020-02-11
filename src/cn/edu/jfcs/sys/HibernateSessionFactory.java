package cn.edu.jfcs.sys;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
	// Session的单例模式
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

	// 配置文件的单例模式
	private static final Configuration cfg = new Configuration();

	private static SessionFactory sf;

	// 为配置文件名称，例如hibernate_mysql.cfg.xml
	public static Session getSession(String configFile)
			throws HibernateException {
		Session session = (Session) threadLocal.get();
		if (session == null || !session.isOpen()) {
			if (sf == null) {
				try {
					cfg.configure(configFile);
					sf = cfg.buildSessionFactory();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			session = (sf == null) ? null : sf.openSession();
			threadLocal.set(session);
		}

		return session;
	}

	// 关闭Session
	public static void closeSession() throws HibernateException {
		Session session = (Session) threadLocal.get();
		threadLocal.set(null);
		if (session != null)
			session.close();
	}

	private HibernateSessionFactory() {
	}

}
