/*
 *编写者：陈冈
 *高校经费测算系统--数据绑定工厂类
 *编写时间：2006-11-18
 */
package cn.edu.jfcs.sys;

import org.eclipse.jface.internal.databinding.provisional.DataBindingContext;
import org.eclipse.jface.internal.databinding.provisional.beans.BeanObservableFactory;
import org.eclipse.jface.internal.databinding.provisional.factories.DefaultBindSupportFactory;
import org.eclipse.jface.internal.databinding.provisional.factories.DefaultBindingFactory;
import org.eclipse.jface.internal.databinding.provisional.swt.SWTObservableFactory;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

public class DataBindingFactory {
	// 创建一个与SWT控件关联的数据绑定上下文对象
	public static DataBindingContext createContext(Control control) {
		final DataBindingContext context = createContext();
		control.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				context.dispose();
			}
		});
		return context;
	}

	public static DataBindingContext createContext() {
		DataBindingContext context = new DataBindingContext();
		// 初始化context，添加观察者工厂
		context.addObservableFactory(new BeanObservableFactory(context, null,
				new Class[] { Widget.class }));
		context.addObservableFactory(new SWTObservableFactory());
		context.addBindSupportFactory(new DefaultBindSupportFactory());
		context.addBindingFactory(new DefaultBindingFactory());
		return context;
	}
}