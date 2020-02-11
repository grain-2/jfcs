/*
 *编写者：陈冈
 *高校经费测算系统--教学单位基本情况视图
 *编写时间：2006-11-16
 */
package cn.edu.jfcs.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.internal.databinding.provisional.DataBindingContext;
import org.eclipse.jface.internal.databinding.provisional.description.Property;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.edu.jfcs.actions.YearAction;
import cn.edu.jfcs.model.YearTeachUnit;
import cn.edu.jfcs.sys.CuryearPropertyChange;
import cn.edu.jfcs.sys.DataBindingFactory;
import cn.edu.jfcs.sys.GetGridData;
import cn.edu.jfcs.sys.HibernateSessionFactory;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;
import cn.edu.jfcs.sys.SetControlEnabled;
import cn.edu.jfcs.sys.SetTableColColorListener;
import cn.edu.jfcs.sys.SetTextEnabled;
import cn.edu.jfcs.sys.YearCombo;
import cn.edu.jfcs.sys.YearManager;

public class TeachUnitData extends ViewPart implements IPropertyChangeListener {
	private TableViewer tableViewer;

	private Text jfys;// 应收金额

	private Text jfss;// 实收金额

	private Text ta1;// 教授A人数

	private Text ta2;// 教授B人数

	private Text ta3;// 教授C人数

	private Text ta4;// 教授D人数

	private Text ta5;// 副教授A人数

	private Text ta6;// 副教授B人数

	private Text ta7;// 讲师人数

	private Text ta8; // 助教人数

	private YearTeachUnit yearTeachUnit;

	// 是否进入编辑状态
	public static boolean canEdit = false;

	// 表格的列名，同时也作为编辑器的属性名
	public static final String UNITNAME = "unitname";// 教学单位名称

	public static final String SSB = "ssb"; // 生师比

	public static final String JXYWPER = "jxywper"; // 教学业务费比率

	public static final String JXGLPER = "jxglper";// 教学管理费比率

	public static final String JXYJPER = "jxyjper";// 教学研究费比率

	public static final String SZPYPER = "szpyper";// 师资培养费比率

	// 作为tableViewer的ColumnProperties属性，列的别名
	public static final String[] PROPERTIES = { UNITNAME, SSB, JXYWPER,
			JXGLPER, JXYJPER, SZPYPER };

	private Action saveDataAction, editDataAction;

	private Group group;

	private List<YearTeachUnit> list = new ArrayList<YearTeachUnit>();

	public TeachUnitData() {
	}

	public void createPartControl(Composite parent) {
		createContents(parent);
		createToolbarButtons();
		getCurrentRowData((IStructuredSelection) tableViewer.getSelection());
		bindData(parent);
		// 注册属性监听器，以便监听curYear数值发生改变时刷新视图
		CuryearPropertyChange.getInstance().addPropertyChangeListener(
				IAppConstants.TEACH_UNIT_DATA_VIEW_ID, this);
	}

	private void createContents(Composite parent) {
		setPartName(getConfigurationElement().getAttribute("name") + "---"
				+ YearManager.getInstance().getCurYear() + "年");
		Composite contanier = new Composite(parent, SWT.NONE);
		final GridLayout gl = new GridLayout(1, false);
		contanier.setLayout(gl);
		createTableViewer(contanier);
		createTextGroup(contanier);
	}

	// 创建TableViewer内容
	private void createTableViewer(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.H_SCROLL
				| SWT.FULL_SELECTION);
		tableViewer.setUseHashlookup(true);
		final Table table = tableViewer.getTable();
		table.setToolTipText("注意：数据输入完毕请敲回车键，否则该数据不予保存！");
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		// 添加列名称
		final TableColumn unitnameColumn = new TableColumn(table, SWT.CENTER);
		unitnameColumn.setText("教学单位");
		unitnameColumn.setWidth(146);
		final TableColumn zykperColumn = new TableColumn(table, SWT.CENTER);
		zykperColumn.setText("生师比");
		zykperColumn.setWidth(52);
		final TableColumn jxywperColumn = new TableColumn(table, SWT.CENTER);
		jxywperColumn.setText("教学业务费率");
		jxywperColumn.setWidth(90);
		final TableColumn jxglperColumn = new TableColumn(table, SWT.CENTER);
		jxglperColumn.setText("教学管理费率");
		jxglperColumn.setWidth(90);
		final TableColumn jxyjperColumn = new TableColumn(table, SWT.CENTER);
		jxyjperColumn.setText("教学研究费率");
		jxyjperColumn.setWidth(90);
		final TableColumn szpyperColumn = new TableColumn(table, SWT.CENTER);
		szpyperColumn.setText("师资培养费率");
		szpyperColumn.setWidth(90);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// 设置内容器和标签容器
		tableViewer.setContentProvider(new TeachUnitDataContentProvider());
		tableViewer.setLabelProvider(new TeachUnitDataLabelProvider());
		list = getData(YearManager.getInstance().getCurYear());
		tableViewer.setInput(list);
		// 设置表格行移动时的背景颜色
		table.addListener(SWT.EraseItem, new SetTableColColorListener(table));
		// 表格记录行变化时刷新group基本数据中的内容
		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();
						getCurrentRowData(selection);
						// 数据绑定
						bindData(getViewSite().getShell());
					}
				});
		// 创建编辑器,第1列为空，不允许修改
		final CellEditor[] editors = new CellEditor[6];
		editors[1] = new TextCellEditor(table);
		editors[2] = new TextCellEditor(table);
		editors[3] = new TextCellEditor(table);
		editors[4] = new TextCellEditor(table);
		editors[5] = new TextCellEditor(table);
		tableViewer.setColumnProperties(PROPERTIES);
		tableViewer.setCellModifier(new TeachUnitDataCellModifier(tableViewer));
		tableViewer.setCellEditors(editors);
		// 滚动到顶端
		table.select(0);
		table.setFocus();
	}

	// 获得TableViewer的输入数据
	private static List<YearTeachUnit> getData(int year) {
		List<YearTeachUnit> list = new ArrayList<YearTeachUnit>();
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		Query query = session
				.getNamedQuery("cn.edu.jfcs.ui.TeachUnitData.getTeachUnitData");
		query.setInteger(0, year);
		ScrollableResults result = query.setCacheMode(CacheMode.IGNORE).scroll(
				ScrollMode.FORWARD_ONLY);
		while (result.next()) {
			YearTeachUnit ytu = (YearTeachUnit) result.get(0);
			list.add(ytu);
		}
		HibernateSessionFactory.closeSession();
		return list;
	}

	// 创建基本数据文本框
	private void createTextGroup(Composite parent) {
		Color foreColor = new Color(Display.getCurrent(), 0x00, 0x40, 0x80);
		group = new Group(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gd);
		group.setText("基本数据");
		group.setForeground(foreColor);
		final GridLayout gridLayout_1 = new GridLayout(10, false);
		group.setLayout(gridLayout_1);

		// 第1行
		final Label label1 = new Label(group, SWT.NONE);
		label1.setText("应收金额");
		label1.setAlignment(SWT.RIGHT);
		label1.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		jfys = new Text(group, SWT.BORDER | SWT.RIGHT);
		jfys.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		final Label label2 = new Label(group, SWT.NONE);
		label2.setText("实收金额");
		label2.setAlignment(SWT.RIGHT);
		label2.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		jfss = new Text(group, SWT.BORDER | SWT.RIGHT);
		jfss.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		final Label label3 = new Label(group, SWT.NONE);
		label3.setText("教授A");
		label3.setAlignment(SWT.RIGHT);
		label3.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		ta1 = new Text(group, SWT.BORDER | SWT.RIGHT);
		ta1.setLayoutData(new GetGridData().getGridData(35, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		final Label label4 = new Label(group, SWT.NONE);
		label4.setText("教授B");
		label4.setAlignment(SWT.RIGHT);
		label4.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		ta2 = new Text(group, SWT.BORDER | SWT.RIGHT);
		ta2.setLayoutData(new GetGridData().getGridData(35, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		final Label label5 = new Label(group, SWT.NONE);
		label5.setText("教授C");
		label5.setAlignment(SWT.RIGHT);
		label5.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		ta3 = new Text(group, SWT.BORDER | SWT.RIGHT);
		ta3.setLayoutData(new GetGridData().getGridData(35, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		// 第2行
		final Label label6 = new Label(group, SWT.NONE);
		label6.setText("教授D");
		label6.setAlignment(SWT.RIGHT);
		label6.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		ta4 = new Text(group, SWT.BORDER | SWT.RIGHT);
		ta4.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		final Label label7 = new Label(group, SWT.NONE);
		label7.setText("副教授A");
		label7.setAlignment(SWT.RIGHT);
		label7.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		ta5 = new Text(group, SWT.BORDER | SWT.RIGHT);
		ta5.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		final Label label8 = new Label(group, SWT.NONE);
		label8.setText("副教授B");
		label8.setAlignment(SWT.RIGHT);
		label8.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		ta6 = new Text(group, SWT.BORDER | SWT.RIGHT);
		ta6.setLayoutData(new GetGridData().getGridData(35, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		final Label label9 = new Label(group, SWT.NONE);
		label9.setText("讲师");
		label9.setAlignment(SWT.RIGHT);
		label9.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		ta7 = new Text(group, SWT.BORDER | SWT.RIGHT);
		ta7.setLayoutData(new GetGridData().getGridData(35, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		final Label label10 = new Label(group, SWT.NONE);
		label10.setText("助教");
		label10.setAlignment(SWT.RIGHT);
		label10.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		ta8 = new Text(group, SWT.BORDER | SWT.RIGHT);
		ta8.setLayoutData(new GetGridData().getGridData(35, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		// 定制文本框的焦点切换
		CustomText(jfys, jfss);
		CustomText(jfss, ta1);
		CustomText(ta1, ta2);
		CustomText(ta2, ta3);
		CustomText(ta3, ta4);
		CustomText(ta4, ta5);
		CustomText(ta5, ta6);
		CustomText(ta6, ta7);
		CustomText(ta7, ta8);
		CustomText(ta8, jfys);
		// 文本框初始使能状态
		new SetTextEnabled(group, new SetControlEnabled().isEnabled(YearManager
				.getInstance().getCurYear()));
		foreColor.dispose();
	}

	// 定制数据输入文本框
	// 参数：text，需要定义的Text对象；nextText，下一个获得焦点的Text对象
	private void CustomText(final Text text, final Text nextText) {
		text.addModifyListener(new ModifyListener() {
			// 数据修改时保存到数据实体
			public void modifyText(ModifyEvent e) {
				YearTeachUnit ytu = (YearTeachUnit) ((IStructuredSelection) tableViewer
						.getSelection()).getFirstElement();
				int index = list.indexOf(ytu);
				// 根据TableViewer当前选择行保存用户在文本框中输入的数据
				if (index >= 0) {
					String textValue = text.getText();
					if (text == jfys)
						list.get(index).setJfys(new BigDecimal(textValue));
					else if (text == jfss)
						list.get(index).setJfss(new BigDecimal(textValue));
					else if (text == ta1)
						list.get(index).setTa1(Integer.parseInt(textValue));
					else if (text == ta2)
						list.get(index).setTa2(Integer.parseInt(textValue));
					else if (text == ta3)
						list.get(index).setTa3(Integer.parseInt(textValue));
					else if (text == ta4)
						list.get(index).setTa4(Integer.parseInt(textValue));
					else if (text == ta5)
						list.get(index).setTa5(Integer.parseInt(textValue));
					else if (text == ta6)
						list.get(index).setTa6(Integer.parseInt(textValue));
					else if (text == ta7)
						list.get(index).setTa7(Integer.parseInt(textValue));
					else if (text == ta8)
						list.get(index).setTa8(Integer.parseInt(textValue));
				}
			}
		});

		text.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.character == SWT.CR) {
					if (nextText instanceof Text)
						nextText.forceFocus();
				}
			}
		});
	}

	// 创建导航按钮
	private void createToolbarButtons() {
		saveDataAction = new Action("保存数据", AbstractUIPlugin
				.imageDescriptorFromPlugin(IAppConstants.APPLICATION_ID,
						IImageKey.SAVE_DATA)) {
			public void run() {
				canEdit = false; // 退出数据可编辑状态
				saveData();
			}
		};
		editDataAction = new Action("修改数据", AbstractUIPlugin
				.imageDescriptorFromPlugin(IAppConstants.APPLICATION_ID,
						IImageKey.EDITT_DATA)) {
			public void run() {
				canEdit = true;
			}
		};
		IToolBarManager toolBar = getViewSite().getActionBars()
				.getToolBarManager();
		Action preYear = new YearAction("上一年", 0, AbstractUIPlugin
				.imageDescriptorFromPlugin(IAppConstants.APPLICATION_ID,
						IImageKey.PRE_YEAR)) {
			public void run() {
				super.run();
				refreshData();
			}
		};
		Action nextYear = new YearAction("上一年", 1, AbstractUIPlugin
				.imageDescriptorFromPlugin(IAppConstants.APPLICATION_ID,
						IImageKey.NEXT_YEAR)) {
			public void run() {
				super.run();
				refreshData();
			}
		};
		toolBar.add(preYear);
		toolBar.add(nextYear);
		toolBar.add(editDataAction);
		toolBar.add(saveDataAction);
		// 设置初始的使能状态
		editDataAction.setEnabled(new SetControlEnabled().isEnabled(YearManager
				.getInstance().getCurYear()));
		saveDataAction.setEnabled(new SetControlEnabled().isEnabled(YearManager
				.getInstance().getCurYear()));
	}

	// 刷新数据
	private void refreshData() {
		// 激活属性监听事件
		CuryearPropertyChange.getInstance().firePropertyChangeListener();
		// 刷新TeachUnitInfo视图的下拉列表框
		Combo combo = YearCombo.getINSTANCE().getCombo();
		combo
				.select(combo.indexOf(YearManager.getInstance().getCurYear()
						+ ""));
		combo.setFocus();
	}

	// 根据TableViewer当前选择行获得对应文本框数据并保存到实体变量yearTeachUnit
	// 以便进行数据绑定
	private void getCurrentRowData(IStructuredSelection selection) {
		if (selection.getFirstElement() instanceof YearTeachUnit) {
			yearTeachUnit = new YearTeachUnit();
			YearTeachUnit ytu = (YearTeachUnit) selection.getFirstElement();
			yearTeachUnit.setJfys(ytu.getJfys());
			yearTeachUnit.setJfss(ytu.getJfss());
			yearTeachUnit.setTa1(ytu.getTa1());
			yearTeachUnit.setTa2(ytu.getTa2());
			yearTeachUnit.setTa3(ytu.getTa3());
			yearTeachUnit.setTa4(ytu.getTa4());
			yearTeachUnit.setTa5(ytu.getTa5());
			yearTeachUnit.setTa6(ytu.getTa6());
			yearTeachUnit.setTa7(ytu.getTa7());
			yearTeachUnit.setTa8(ytu.getTa8());
		}
	}

	// 数据绑定
	private void bindData(Composite parent) {
		DataBindingContext dbc = DataBindingFactory.createContext(parent);
		dbc.bind(jfys, new Property(yearTeachUnit, "jfys"), null);
		dbc.bind(jfss, new Property(yearTeachUnit, "jfss"), null);
		dbc.bind(ta1, new Property(yearTeachUnit, "ta1"), null);
		dbc.bind(ta2, new Property(yearTeachUnit, "ta2"), null);
		dbc.bind(ta3, new Property(yearTeachUnit, "ta3"), null);
		dbc.bind(ta4, new Property(yearTeachUnit, "ta4"), null);
		dbc.bind(ta5, new Property(yearTeachUnit, "ta5"), null);
		dbc.bind(ta6, new Property(yearTeachUnit, "ta6"), null);
		dbc.bind(ta7, new Property(yearTeachUnit, "ta7"), null);
		dbc.bind(ta8, new Property(yearTeachUnit, "ta8"), null);
	}

	// 保存数据
	private void saveData() {
		// 判断有无记录需要保存
		Iterator data = ((List) tableViewer.getInput()).iterator();
		if (!data.hasNext()) {
			MessageDialog.openWarning(null, "提示", "没有需要保存的数据！");
			return;
		}

		Job job = new Job("正在保存数据。。。") {
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("正在保存数据。。。", IProgressMonitor.UNKNOWN);
				Session session = HibernateSessionFactory
						.getSession("hibernate_derby.cfg.xml");
				Transaction tx = session.beginTransaction();
				Iterator data = ((List) tableViewer.getInput()).iterator();
				int count = 0;
				while (data.hasNext()) {
					YearTeachUnit ytu = (YearTeachUnit) data.next();
					monitor.worked(1);
					session.update(ytu);
					// 设置批量更新块大小
					if (++count % 2 == 0) {
						session.flush();
						session.clear();
					}
					// 测试用，导出产品时删除
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
					}

				}
				tx.commit();
				HibernateSessionFactory.closeSession();
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		job.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				final boolean isSucess = event.getResult().isOK();
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						if (isSucess)
							MessageDialog.openInformation(null, "提示",
									"数据已经成功保存！");
						else
							MessageDialog.openError(null, "提示", "出错，数据保存失败！");
					}
				});
			}
		});
		job.setUser(false);
		job.setPriority(Job.SHORT);
		job.schedule();
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals("curYear")) {
			setPartName(getConfigurationElement().getAttribute("name") + "---"
					+ YearManager.getInstance().getCurYear() + "年");
			editDataAction.setEnabled(new SetControlEnabled()
					.isEnabled(YearManager.getInstance().getCurYear()));
			saveDataAction.setEnabled(new SetControlEnabled()
					.isEnabled(YearManager.getInstance().getCurYear()));
			new SetTextEnabled(group, new SetControlEnabled()
					.isEnabled(YearManager.getInstance().getCurYear()));
			list = getData(YearManager.getInstance().getCurYear());
			tableViewer.setInput(list);
			tableViewer.setSelection(new StructuredSelection(
					((List) tableViewer.getInput()).get(0)));
		}
	}

	// 注销监听器
	public void dispose() {
		CuryearPropertyChange.getInstance().removePropertyChangeListener(
				IAppConstants.TEACH_UNIT_DATA_VIEW_ID);
	}

	public void setFocus() {

	}

}
