/*
 *编写者：陈冈
 *高校经费测算系统--图形对比
 *编写时间：2006-11-30
 */
package cn.edu.jfcs.ui;

import java.awt.Frame;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import cn.edu.jfcs.model.Calcresult;
import cn.edu.jfcs.sys.ControlContributionLabel;
import cn.edu.jfcs.sys.HibernateSessionFactory;
import cn.edu.jfcs.sys.YearManager;

public class StackedBarChart extends ViewPart {
	private static Frame frame;

	private static Composite composite;

	public void createPartControl(Composite parent) {
		// 添加工具栏
		createToolbarButtons();
		// 画图
		composite = new Composite(parent, SWT.NO_BACKGROUND | SWT.EMBEDDED);
		frame = SWT_AWT.new_Frame(composite);
		frame.add(new ChartPanel(StackedBarChart3D(YearManager.getInstance()
				.getCurYear())));
	}

	private void createToolbarButtons() {
		IToolBarManager toolBarMgr = getViewSite().getActionBars()
				.getToolBarManager();
		toolBarMgr.add(new ControlContributionLabel("yearLabel", "年份"));
		toolBarMgr.add(new YearCombo("yearCombo"));
	}

	private static JFreeChart StackedBarChart3D(int year) {
		// 图表标题、 x轴标签、 y轴标签
		String caption = "经费测算数据对比图", xCaption = "教学单位", yCaption = "金额";
		// 数据集
		CategoryDataset categoryDataset = getDataSet(year);
		JFreeChart chart = ChartFactory.createStackedBarChart3D(caption,
				xCaption, yCaption, categoryDataset, PlotOrientation.VERTICAL,
				true, false, false);
		CategoryPlot categoryPlot = chart.getCategoryPlot();
		categoryPlot.setDomainGridlinesVisible(true);
		NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberAxis.setUpperMargin(0.05);
		CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(0.6));
		BarRenderer3D barRenderer3D = (BarRenderer3D) categoryPlot
				.getRenderer();
		barRenderer3D.setDrawBarOutline(false);
		// 设置柱的最大宽度
		barRenderer3D.setMaximumBarWidth(0.03);
		// 设置标签可视化
		barRenderer3D.setItemLabelsVisible(true);
		ItemLabelPosition itemlabelposition = new ItemLabelPosition();
		barRenderer3D.setPositiveItemLabelPosition(itemlabelposition);
		return chart;

	}

	// 获得数据集
	private static CategoryDataset getDataSet(int year) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		Query query = session
				.getNamedQuery("cn.edu.jfcs.ui.StackedBarChart.getDataSet");
		query.setInteger(0, year);
		ScrollableResults result = query.setCacheMode(CacheMode.IGNORE).scroll(
				ScrollMode.FORWARD_ONLY);
		String uiname = "学生经费", ciname = "公共课经费", piname = "专业培养费";
		while (result.next()) {
			Calcresult calcresult = (Calcresult) result.get(0);
			String untiname = calcresult.getTeachunit().getUnitname();
			float ui = calcresult.getUi();
			float pi = calcresult.getPi();
			float ci = calcresult.getCi();
			dataset.addValue(ui, uiname, untiname);
			dataset.addValue(pi, piname, untiname);
			dataset.addValue(ci, ciname, untiname);
		}
		HibernateSessionFactory.closeSession();
		return dataset;
	}

	// 下拉列表框，需要监听事件，写成内部类
	public static class YearCombo extends ControlContribution {

		protected YearCombo(String id) {
			super(id);
		}

		protected Control createControl(Composite parent) {
			final Combo combo = new Combo(parent, SWT.READ_ONLY);
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
				combo.add(String
						.valueOf(YearManager.getInstance().getCurYear()));
			}
			combo.select(combo.indexOf(String.valueOf(YearManager.getInstance()
					.getCurYear())));
			HibernateSessionFactory.closeSession();
			combo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					// 删除面板内容
					frame.removeAll();
					frame.add(new ChartPanel(StackedBarChart3D(Integer
							.parseInt(combo.getText()))));
					composite.pack();
					// 重新设定大小
					composite.setBounds(0, 0,
							composite.getParent().getBounds().width, composite
									.getParent().getBounds().height);
					// 重绘屏幕
					composite.redraw();
				}
			});
			return combo;
		}
	}

	public void dispose() {
		frame.dispose();
	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
