package cn.edu.jfcs.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import cn.edu.jfcs.model.YearTeachUnit;
import cn.edu.jfcs.sys.CacheImage;
import cn.edu.jfcs.sys.ControlContributionLabel;
import cn.edu.jfcs.sys.CuryearPropertyChange;
import cn.edu.jfcs.sys.HibernateSessionFactory;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;
import cn.edu.jfcs.sys.YearCombo;
import cn.edu.jfcs.sys.YearManager;

public class TeachUnitName extends ViewPart implements IPropertyChangeListener {

	private TableViewer tableViewer;

	public TeachUnitName() {
	}

	public void createPartControl(Composite parent) {
		createTableViewer(parent);
		createToolbar();
		// 注册属性监听器，以便监听curYear数值发生改变时刷新本视图
		CuryearPropertyChange.getInstance().addPropertyChangeListener(
				IAppConstants.TEACH_UNIT_NAME_VIEW_ID, this);
	}

	private void createTableViewer(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL);
		tableViewer.setUseHashlookup(true);
		final Table table = tableViewer.getTable();
		table.setBackgroundImage(CacheImage.getINSTANCE()
				.getImage(IAppConstants.APPLICATION_ID,
						IImageKey.TEACH_UNIT_NAME_VIEW_BG));
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		// 添加教学单位名称、专业课、公共课3列
		final TableColumn unitnameColumn = new TableColumn(table, SWT.LEFT);
		unitnameColumn.setWidth(152);
		final TableColumn haszykColumn = new TableColumn(table, SWT.CENTER);
		haszykColumn.setWidth(14);
		final TableColumn hasggkColumn = new TableColumn(table, SWT.CENTER);
		hasggkColumn.setWidth(14);
		table.setLinesVisible(true);
		// 设置内容器和标签容器
		tableViewer.setContentProvider(new TeachUnitNameContentProvider());
		tableViewer.setLabelProvider(new TeachUnitNameLabelProvider());
		tableViewer.setInput(getTeachUnitName(YearManager.getInstance()
				.getMaxYear()));
		// 滚动到顶端
		table.select(0);
		table.setFocus();
		// 设置监听器服务提供者：TableTreeViewer
		getViewSite().setSelectionProvider(tableViewer);
	}

	// 返回教学单位基本数据列表
	private List getTeachUnitName(int year) {
		List<Object> list = new ArrayList<Object>();
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		Query query = session
				.getNamedQuery("cn.edu.jfcs.ui.TeachUnitName.getTeachUnitName");
		query.setInteger(0, year);
		ScrollableResults result = query.setCacheMode(CacheMode.IGNORE).scroll(
				ScrollMode.FORWARD_ONLY);
		while (result.next()) {
			YearTeachUnit ytu = (YearTeachUnit) result.get(0);
			list.add(ytu);
		}
		HibernateSessionFactory.closeSession();
		if (list.toArray().length == 0)
			list.add(new Object[0]);
		return list;
	}

	// 创建年份下拉列表框
	private void createToolbar() {
		IToolBarManager toolBarMgr = getViewSite().getActionBars()
				.getToolBarManager();
		toolBarMgr.add(new ControlContributionLabel("yearLabel", "年份"));
		toolBarMgr.add(YearCombo.getINSTANCE());
	}

	// 年份下拉列表框发生改变时，刷新本视图数据
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals("curYear")) {
			List list = new ArrayList();
			list = getTeachUnitName(YearManager.getInstance().getCurYear());
			tableViewer.setInput(list);
			tableViewer.setSelection(new StructuredSelection(list.get(0)));
			tableViewer.getTable().setFocus();
		}
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public void setTableViewer(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	public void dispose() {
		CuryearPropertyChange.getInstance().removePropertyChangeListener(
				IAppConstants.TEACH_UNIT_NAME_VIEW_ID);
	}
	public void setFocus() {

	}
}
