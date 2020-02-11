/*
 *编写者：陈冈
 *高校经费测算系统--属性改变监听模式里的属性提供方
 *编写时间：2006-11-9
 */
package cn.edu.jfcs.sys;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class CuryearPropertyChange {
	private static final CuryearPropertyChange INSTANCE = new CuryearPropertyChange();

	private static final Map<String, IPropertyChangeListener> map = new HashMap<String, IPropertyChangeListener>();

	private CuryearPropertyChange() {
		super();
	}

	public static CuryearPropertyChange getInstance() {
		return INSTANCE;
	}

	// 激发属性改变监听事件
	public void firePropertyChangeListener() {
		final Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			map.get(iter.next()).propertyChange(
					new PropertyChangeEvent(this, "curYear", null, YearManager
							.getInstance().getCurYear()));
		}
	}

	// 注册属性改变监听器
	public void addPropertyChangeListener(final String listenerID,
			final IPropertyChangeListener listener) {
		if (!map.containsKey(listenerID))
			map.put(listenerID, listener);
	}

	// 注销属性改变监听器
	public void removePropertyChangeListener(final String listenerID) {
		if (map.containsKey(listenerID))
			map.remove(listenerID);
	}
}
