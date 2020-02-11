/*
 *编写者：陈冈
 *高校经费测算系统--历史数据
 *编写时间：2007-1-2
 */
package cn.edu.jfcs.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import cn.edu.jfcs.model.Calcresult;
import cn.edu.jfcs.model.HistoryDataTreeChildren;
import cn.edu.jfcs.model.HistoryDataTreeParent;
import cn.edu.jfcs.sys.HibernateSessionFactory;
import cn.edu.jfcs.sys.SetTableColColorListener;

public class HistoryData extends ViewPart {
	private TreeViewer treeViewer;

	public HistoryData() {
	}

	public void createPartControl(Composite parent) {
		createTableTreeViewer(parent);
	}

	private void createTableTreeViewer(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		Composite actions = new Composite(composite, SWT.NONE);
		actions.setLayout(new GridLayout(3, false));
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.heightHint = 28;
		gd.widthHint = 140;
		actions.setLayoutData(gd);
		// 创建查询文本框、按钮
		createToolbarButtons(actions);
		treeViewer = new TreeViewer(composite, SWT.BORDER | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		treeViewer.setUseHashlookup(true);
		treeViewer.setContentProvider(new HistoryDataContentProvider());
		treeViewer.setLabelProvider(new HistoryDataLabelProvider());
		treeViewer.setInput(getHistoryData());
		// 创建表头列
		final Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		// 记录改变时设置当前行颜色
		tree.addListener(SWT.EraseItem, new SetTableColColorListener(tree));
		final TreeColumn tcl1 = new TreeColumn(tree, SWT.CENTER);
		tcl1.setText("年份");
		tcl1.setWidth(186);
		// 默认显示降序箭头
		treeViewer.getTree().setSortColumn(tcl1);
		treeViewer.getTree().setSortDirection(SWT.DOWN);
		tcl1.setToolTipText("单击排序数据");
		// 单击时按照年份排序
		tcl1.addSelectionListener(new SelectionAdapter() {
			boolean isUp = false;

			int curcategory = 0;

			public void widgetSelected(SelectionEvent arg0) {
				isUp = !isUp;
				// 修改年份列排序箭头
				treeViewer.getTree().setSortDirection(isUp ? SWT.UP : SWT.DOWN);
				// 添加排序器
				treeViewer.setSorter(new ViewerSorter() {
					// 按照类别排序
					public int category(Object element) {
						return isUp ? curcategory-- : curcategory++;
					}
				});
			}
		});
		final TreeColumn tcl2 = new TreeColumn(tree, SWT.CENTER);
		tcl2.setText("学生经费");
		tcl2.setWidth(66);
		final TreeColumn tcl3 = new TreeColumn(tree, SWT.CENTER);
		tcl3.setText("专业培养费");
		tcl3.setWidth(80);
		final TreeColumn tcl4 = new TreeColumn(tree, SWT.CENTER);
		tcl4.setText("公共课经费");
		tcl4.setWidth(80);
		final TreeColumn tcl5 = new TreeColumn(tree, SWT.CENTER);
		tcl5.setText("人员经费");
		tcl5.setWidth(66);
		final TreeColumn tcl6 = new TreeColumn(tree, SWT.CENTER);
		tcl6.setText("综合业务费");
		tcl6.setWidth(80);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		treeViewer.expandAll();
		treeViewer.refresh();
	}

	// 获得历史数据
	private List getHistoryData() {
		List<HistoryDataTreeParent> list = new ArrayList<HistoryDataTreeParent>();
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		Query query = session
				.getNamedQuery("cn.edu.jfcs.ui.HistoryData.getHistoryData");
		ScrollableResults result = query.setCacheMode(CacheMode.IGNORE).scroll(
				ScrollMode.FORWARD_ONLY);
		int oldYear = -1, curYear = 0;
		HistoryDataTreeParent hdtp = null;
		while (result.next()) {
			Calcresult calcresult = (Calcresult) result.get(0);
			curYear = calcresult.getNian();
			if (curYear != oldYear) {
				if (hdtp != null) {
					list.add(hdtp);
				}
				hdtp = new HistoryDataTreeParent(curYear);
			}
			oldYear = curYear;
			HistoryDataTreeChildren hdtc = new HistoryDataTreeChildren();
			hdtc.setCalcresult(calcresult);
			hdtp.add(hdtc);
		}
		if (hdtp != null) {
			list.add(hdtp);
		}
		HibernateSessionFactory.closeSession();
		return list;
	}

	// 查询文本框、按钮控件
	private void createToolbarButtons(Composite parent) {
		final CLabel label = new CLabel(parent, SWT.NONE);
		label.setText("年份");
		final Text year = new Text(parent, SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = 30;
		year.setLayoutData(gd);
		final Button button = new Button(parent, SWT.NONE);
		button.setText("查  询");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				treeViewer.resetFilters();
				if (year.getText() != null && year.getText().length() > 0) {
					treeViewer.addFilter(new FilterData(Integer.parseInt(year
							.getText())));
				}
				treeViewer.expandAll();
			}
		});
	}

	// 过滤器
	private class FilterData extends ViewerFilter {
		private int year;

		public FilterData(int year) {
			super();
			this.year = year;
		}

		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			boolean filterTag = true;
			if (parentElement instanceof HistoryDataTreeParent) {
				HistoryDataTreeParent hdtp = (HistoryDataTreeParent) parentElement;
				HistoryDataTreeChildren hdtc = (HistoryDataTreeChildren) element;
				filterTag = filterTag && hdtp.getYear() == year
						&& hdtc.getCalcresult().getNian() == year;
			} else {
				HistoryDataTreeParent hdtp = (HistoryDataTreeParent) element;
				filterTag = filterTag && hdtp.getYear() == year;
			}
			return filterTag;
		}

	}

	public void setFocus() {
	}

}
