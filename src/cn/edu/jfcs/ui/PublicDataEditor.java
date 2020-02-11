/*
 *编写者：陈冈
 *高校经费测算系统--公共参数设置编辑器
 *编写时间：2006-11-21
 */
package cn.edu.jfcs.ui;

import java.math.BigDecimal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.internal.databinding.provisional.DataBindingContext;
import org.eclipse.jface.internal.databinding.provisional.description.Property;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.part.EditorPart;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.edu.jfcs.model.PubData;
import cn.edu.jfcs.sys.CacheImage;
import cn.edu.jfcs.sys.DataBindingFactory;
import cn.edu.jfcs.sys.GetGridData;
import cn.edu.jfcs.sys.HibernateSessionFactory;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;
import cn.edu.jfcs.sys.SetControlEnabled;
import cn.edu.jfcs.sys.SetTextEnabled;
import cn.edu.jfcs.sys.YearManager;

public class PublicDataEditor extends EditorPart {
	// 全校本年应收金额
	private Text rte;

	// 全校本年实收金额
	private Text mte;

	// 年度拟拨付总额
	private Text mt;

	// 全校缴费率标准
	private Text sjf;

	// 学生经费分割比
	private Text uper;

	// 专业培养费分割比
	private Text pper;

	// 公共课经费分割比
	private Text cper;

	// 奖酬金比率
	private Text jcjper;

	// 学生困难补助比率
	private Text xsknbzper;

	// 学生活动经费比率
	private Text xshdjfper;

	// 学生奖学金比率
	private Text xsjxjper;

	// 教授A系数
	private Text tb1;

	// 教授B系数
	private Text tb2;

	// 教授C系数
	private Text tb3;

	// 教授D系数
	private Text tb4;

	// 副教授A系数
	private Text tb5;

	// 副教授B系数
	private Text tb6;

	// 讲师
	private Text tb7;

	// 助教
	private Text tb8;

	private PubData pubData;

	private boolean isDirty = false;

	private IWorkbenchAction saveAction;

	private ToolItem saveData;

	private Group group_1, group_2, group_3;

	private int curYear = YearManager.getInstance().getMaxYear();

	public PublicDataEditor() {
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(getConfigurationElement().getAttribute("name") + "---"
				+ curYear);
	}

	public void createPartControl(Composite parent) {
		createContents(parent);
		getPubData(curYear);
		bindData(parent);
		saveAction = ActionFactory.SAVE.create(getEditorSite()
				.getWorkbenchWindow());
	}

	private void createContents(Composite parent) {
		final ViewForm vf = new ViewForm(parent, SWT.FLAT | SWT.BORDER);
		// 设置顶部右边工具栏
		vf.setTopRight(createToolbarButtons(vf));
		// 添加主体内容
		vf.setContent(createGroupText(vf));
	}

	// 创建文本框
	private Composite createGroupText(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		Color foreColor = new Color(Display.getCurrent(), 0x00, 0x40, 0x80);
		group_1 = new Group(composite, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		group_1.setLayoutData(gd);
		group_1.setText("金额设置");
		group_1.setForeground(foreColor);
		final GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginRight = 10;
		gridLayout.verticalSpacing = 10;
		gridLayout.marginHeight = 10;
		group_1.setLayout(gridLayout);
		// 第1行
		final Label label1 = new Label(group_1, SWT.NONE);
		label1.setText("本年应收金额");
		label1.setAlignment(SWT.RIGHT);
		label1.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		rte = new Text(group_1, SWT.BORDER | SWT.RIGHT);
		rte.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_1, SWT.None).setText("万元");
		final Label label2 = new Label(group_1, SWT.NONE);
		label2.setText("本年实收金额");
		label2.setAlignment(SWT.RIGHT);
		label2.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		mte = new Text(group_1, SWT.BORDER | SWT.RIGHT);
		mte.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_1, SWT.None).setText("万元");
		// 第2行
		final Label label3 = new Label(group_1, SWT.NONE);
		label3.setText("年度拟拨付总额");
		label3.setAlignment(SWT.RIGHT);
		label3.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		mt = new Text(group_1, SWT.BORDER | SWT.RIGHT);
		mt.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_1, SWT.None).setText("万元");
		final Label label4 = new Label(group_1, SWT.NONE);
		label4.setText("全校缴费率标准");
		label4.setAlignment(SWT.RIGHT);
		label4.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		sjf = new Text(group_1, SWT.BORDER | SWT.RIGHT);
		sjf.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_1, SWT.None).setText("%");

		// 经费的划分与调整
		group_2 = new Group(composite, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		group_2.setLayoutData(gd);
		group_2.setText("经费的划分与调整系数");
		group_2.setForeground(foreColor);
		group_2.setLayout(gridLayout);
		// 第1行
		final Label label5 = new Label(group_2, SWT.NONE);
		label5.setText("学生经费分割比");
		label5.setAlignment(SWT.RIGHT);
		label5.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		uper = new Text(group_2, SWT.BORDER | SWT.RIGHT);
		uper.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_2, SWT.None).setText("%");
		final Label label6 = new Label(group_2, SWT.NONE);
		label6.setText("专业培养费分割比");
		label6.setAlignment(SWT.RIGHT);
		label6.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		pper = new Text(group_2, SWT.BORDER | SWT.RIGHT);
		pper.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_2, SWT.None).setText("%");
		// 第2行
		final Label label7 = new Label(group_2, SWT.NONE);
		label7.setText("公共课经费分割比");
		label7.setAlignment(SWT.RIGHT);
		label7.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		cper = new Text(group_2, SWT.BORDER | SWT.RIGHT);
		cper.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_2, SWT.None).setText("%");
		final Label label8 = new Label(group_2, SWT.NONE);
		label8.setText("奖酬金比率");
		label8.setAlignment(SWT.RIGHT);
		label8.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		jcjper = new Text(group_2, SWT.BORDER | SWT.RIGHT);
		jcjper.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_2, SWT.None).setText("%");
		// 第3行
		final Label label9 = new Label(group_2, SWT.NONE);
		label9.setText("学生困难补助比率");
		label9.setAlignment(SWT.RIGHT);
		label9.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		xsknbzper = new Text(group_2, SWT.BORDER | SWT.RIGHT);
		xsknbzper.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_2, SWT.None).setText("%");
		final Label label10 = new Label(group_2, SWT.NONE);
		label10.setText("学生活动经费比率");
		label10.setAlignment(SWT.RIGHT);
		label10.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		xshdjfper = new Text(group_2, SWT.BORDER | SWT.RIGHT);
		xshdjfper.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_2, SWT.None).setText("%");
		// 第4行
		final Label label11 = new Label(group_2, SWT.NONE);
		label11.setText("学生奖学金比率");
		label11.setAlignment(SWT.RIGHT);
		label11.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		xsjxjper = new Text(group_2, SWT.BORDER | SWT.RIGHT);
		xsjxjper.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_2, SWT.None).setText("%");

		// 职称系数
		group_3 = new Group(composite, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		group_3.setLayoutData(gd);
		group_3.setText("职称调整系数");
		group_3.setForeground(foreColor);
		group_3.setLayout(gridLayout);
		// 第1行
		final Label label12 = new Label(group_3, SWT.NONE);
		label12.setText("教授A系数");
		label12.setAlignment(SWT.RIGHT);
		label12.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		tb1 = new Text(group_3, SWT.BORDER | SWT.RIGHT);
		tb1.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_3, SWT.None).setText("");
		final Label label13 = new Label(group_3, SWT.NONE);
		label13.setText("教授B系数");
		label13.setAlignment(SWT.RIGHT);
		label13.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		tb2 = new Text(group_3, SWT.BORDER | SWT.RIGHT);
		tb2.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_3, SWT.None).setText("");
		// 第2行
		final Label label14 = new Label(group_3, SWT.NONE);
		label14.setText("教授C系数");
		label14.setAlignment(SWT.RIGHT);
		label14.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		tb3 = new Text(group_3, SWT.BORDER | SWT.RIGHT);
		tb3.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_3, SWT.None).setText("");
		final Label label15 = new Label(group_3, SWT.NONE);
		label15.setText("教授D系数");
		label15.setAlignment(SWT.RIGHT);
		label15.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		tb4 = new Text(group_3, SWT.BORDER | SWT.RIGHT);
		tb4.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_3, SWT.None).setText("");
		// 第3行
		final Label label16 = new Label(group_3, SWT.NONE);
		label16.setText("副教授A系数");
		label16.setAlignment(SWT.RIGHT);
		label16.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		tb5 = new Text(group_3, SWT.BORDER | SWT.RIGHT);
		tb5.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_3, SWT.None).setText("");
		final Label label17 = new Label(group_3, SWT.NONE);
		label17.setText("副教授B系数");
		label17.setAlignment(SWT.RIGHT);
		label17.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		tb6 = new Text(group_3, SWT.BORDER | SWT.RIGHT);
		tb6.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_3, SWT.None).setText("");
		// 第4行
		final Label label18 = new Label(group_3, SWT.NONE);
		label18.setText("讲师系数");
		label18.setAlignment(SWT.RIGHT);
		label18.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		tb7 = new Text(group_3, SWT.BORDER | SWT.RIGHT);
		tb7.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_3, SWT.None).setText("");
		final Label label19 = new Label(group_3, SWT.NONE);
		label19.setText("助教系数");
		label19.setAlignment(SWT.RIGHT);
		label19.setLayoutData(new GetGridData().getGridData(50, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		tb8 = new Text(group_3, SWT.BORDER | SWT.RIGHT);
		tb8.setLayoutData(new GetGridData().getGridData(60, 0, 0, 0, 0,
				GridData.FILL_HORIZONTAL, 0));
		new Label(group_3, SWT.None).setText("");
		// 定制文本框焦点切换、数据编辑的保存
		setCustomText(group_1);
		setCustomText(group_2);
		setCustomText(group_3);
		// 设置文本框的使能状态
		new SetTextEnabled(group_1, new SetControlEnabled().isEnabled(curYear));
		new SetTextEnabled(group_2, new SetControlEnabled().isEnabled(curYear));
		new SetTextEnabled(group_3, new SetControlEnabled().isEnabled(curYear));
		return composite;
	}

	// 创建导航按钮
	private ToolBar createToolbarButtons(Composite parent) {
		final ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
		final ToolItem preYear = new ToolItem(toolBar, SWT.NONE);
		preYear.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.PRE_YEAR));
		preYear.setToolTipText("上一年");
		preYear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				if (curYear == YearManager.getInstance().getMinYear()) {
					MessageDialog.openInformation(null, "提示", "已经到了数据库中的最小年份！");
					return;
				}
				if (curYear > YearManager.getInstance().getMinYear()) {
					curYear--;
				}
				refreshData();
			}
		});
		final ToolItem nextYear = new ToolItem(toolBar, SWT.NONE);
		nextYear.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.NEXT_YEAR));
		nextYear.setToolTipText("下一年");
		nextYear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				if (curYear == YearManager.getInstance().getMaxYear()) {
					MessageDialog.openInformation(null, "提示", "已经到了数据库中的最近年份！");
					return;
				}
				if (curYear < YearManager.getInstance().getMaxYear()) {
					curYear++;
				}
				refreshData();
			}
		});
		saveData = new ToolItem(toolBar, SWT.NONE);
		saveData.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.SAVE_DATA));
		saveData.setToolTipText("保存数据");
		saveData.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				saveAction.run();
			}
		});
		// 设置初始的使能状态
		saveData.setEnabled(new SetControlEnabled().isEnabled(curYear));
		return toolBar;
	}

	// 获得当前年度数据
	private void getPubData(int year) {
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		Query query = session
				.getNamedQuery("cn.edu.jfcs.ui.PublicDataEditor.getPubData");
		query.setInteger(0, year);
		ScrollableResults result = query.setCacheMode(CacheMode.IGNORE).scroll(
				ScrollMode.FORWARD_ONLY);
		if (result.next()) {
			pubData = (PubData) result.get(0);
		}
		HibernateSessionFactory.closeSession();
	}

	// 数据绑定
	private void bindData(Composite parent) {
		DataBindingContext dbc = DataBindingFactory.createContext(parent);
		dbc.bind(rte, new Property(pubData, "rte"), null);
		dbc.bind(mte, new Property(pubData, "mte"), null);
		dbc.bind(mt, new Property(pubData, "mt"), null);
		dbc.bind(sjf, new Property(pubData, "sjf"), null);
		dbc.bind(uper, new Property(pubData, "uper"), null);
		dbc.bind(pper, new Property(pubData, "pper"), null);
		dbc.bind(cper, new Property(pubData, "cper"), null);
		dbc.bind(jcjper, new Property(pubData, "jcjper"), null);
		dbc.bind(xsknbzper, new Property(pubData, "xsknbzper"), null);
		dbc.bind(xshdjfper, new Property(pubData, "xshdjfper"), null);
		dbc.bind(xsjxjper, new Property(pubData, "xsjxjper"), null);
		dbc.bind(tb1, new Property(pubData, "tb1"), null);
		dbc.bind(tb2, new Property(pubData, "tb2"), null);
		dbc.bind(tb3, new Property(pubData, "tb3"), null);
		dbc.bind(tb4, new Property(pubData, "tb4"), null);
		dbc.bind(tb5, new Property(pubData, "tb5"), null);
		dbc.bind(tb6, new Property(pubData, "tb6"), null);
		dbc.bind(tb7, new Property(pubData, "tb7"), null);
		dbc.bind(tb8, new Property(pubData, "tb8"), null);
	}

	// 定制数据输入文本框
	// 参数：text，需要定义的Text对象；nextText，下一个获得焦点的Text对象
	private void CustomText(final Text text, final Text nextText) {
		text.addModifyListener(new ModifyListener() {
			// 数据修改时保存到数据实体
			public void modifyText(ModifyEvent e) {
				if (pubData != null) {
					BigDecimal textValue = new BigDecimal(text.getText());
					if (text == rte)
						pubData.setRte(textValue);
					else if (text == mte)
						pubData.setMte(textValue);
					else if (text == mt)
						pubData.setMt(textValue);
					else if (text == sjf)
						pubData.setSjf(textValue);
					else if (text == uper)
						pubData.setUper(textValue);
					else if (text == pper)
						pubData.setPper(textValue);
					else if (text == cper)
						pubData.setCper(textValue);
					else if (text == jcjper)
						pubData.setJcjper(textValue);
					else if (text == xsknbzper)
						pubData.setXsknbzper(textValue);
					else if (text == xshdjfper)
						pubData.setXshdjfper(textValue);
					else if (text == xsjxjper)
						pubData.setXsjxjper(textValue);
					else if (text == tb1)
						pubData.setTb1(textValue);
					else if (text == tb2)
						pubData.setTb2(textValue);
					else if (text == tb3)
						pubData.setTb3(textValue);
					else if (text == tb4)
						pubData.setTb4(textValue);
					else if (text == tb5)
						pubData.setTb5(textValue);
					else if (text == tb6)
						pubData.setTb6(textValue);
					else if (text == tb7)
						pubData.setTb7(textValue);
					else if (text == tb8)
						pubData.setTb8(textValue);
				}
			}
		});
		text.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.character == SWT.CR) {
					if (nextText instanceof Text)
						nextText.forceFocus();
				}
				if (e.keyCode >= 48 && e.keyCode <= 57 && !isDirty()) {
					isDirty = true;
					// 刷新界面状态
					firePropertyChange(IEditorPart.PROP_DIRTY);
				}
			}
		});
	}

	// 定制文本框
	private void setCustomText(final Control control) {
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			int j = children.length;
			for (int i = 0; i < j; i++) {
				if (children[i] instanceof Text) {
					// 最后一个文本框，焦点切换到第一个文本框
					if (i == j - 2)
						CustomText((Text) children[i], (Text) children[1]);
					if ((i + 3 < j) && (children[i + 3] instanceof Text))
						CustomText((Text) children[i], (Text) children[i + 3]);
				}
			}
		}
	}

	public void doSave(IProgressMonitor monitor) {
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		final Transaction tx = session.beginTransaction();
		session.update(pubData);
		tx.commit();
		monitor.done();
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (tx.wasCommitted())
					MessageDialog.openInformation(getEditorSite().getShell(),
							"提示", "数据保存成功！");
				else
					MessageDialog.openError(getEditorSite().getShell(), "提示",
							"数据保存失败！");
			}
		});
		if (isDirty && tx.wasCommitted()) {
			isDirty = false;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
		HibernateSessionFactory.closeSession();
	}

	public boolean isDirty() {
		return isDirty;
	}

	// 关闭时打开原来的透视图
	public void dispose() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.resetPerspective();
	}

	public void refreshData() {
		setPartName(getConfigurationElement().getAttribute("name") + "---"
				+ curYear);
		// 重新获得数据
		getPubData(curYear);
		// 重新数据绑定
		bindData(getSite().getShell());
		// 设置使能状态
		new SetTextEnabled(group_1, new SetControlEnabled().isEnabled(curYear));
		new SetTextEnabled(group_2, new SetControlEnabled().isEnabled(curYear));
		new SetTextEnabled(group_3, new SetControlEnabled().isEnabled(curYear));
		saveData.setEnabled(new SetControlEnabled().isEnabled(curYear));
	}

	public void doSaveAs() {
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public void setFocus() {
	}
}