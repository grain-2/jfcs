package cn.edu.jfcs.ui;

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
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.edu.jfcs.model.Course;
import cn.edu.jfcs.model.CourseTreeChildren;
import cn.edu.jfcs.model.CourseTreeParent;
import cn.edu.jfcs.model.YearTeachUnit;
import cn.edu.jfcs.sys.CalcMoney;
import cn.edu.jfcs.sys.HibernateSessionFactory;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;
import cn.edu.jfcs.sys.SetControlEnabled;
import cn.edu.jfcs.sys.SetTableColColorListener;
import cn.edu.jfcs.sys.YearManager;

public class TeachUnitClass extends ViewPart implements ISelectionListener {
	private TreeViewer treeViewer;

	private Action saveAction, calcAction, closeTreeAction, openTreeAction;

	// 表格的列名，同时也作为编辑器的属性名
	// 课程名称
	public static final String COURSENAME = "coursename";

	// 学期
	public static final String TERM = "term";

	// 学时数
	public static final String N2J = "n2j";

	// 学生数
	public static final String NJ = "nj";

	// 班级名
	public static final String CLASSNAME = "classname";

	// 学生层次系数
	public static final String R1J = "r1j";

	// 课程/专业系数
	public static final String R2J = "r2j";

	// 课程质量系数
	public static final String R3J = "r3j";

	// 作为treeViewer的ColumnProperties属性，列的别名
	public static final String[] PROPERTIES = { COURSENAME, TERM, N2J, NJ,
			CLASSNAME, R1J, R2J, R3J };

	public TeachUnitClass() {
		super();
	}

	public void createPartControl(Composite parent) {
		createTreeViewer(parent);
		createActions();
		createToolbarButtons();
		setHelpContextID();
		// 监听TeachUnitView视图的SelectionProvider，以便其发生改变时激活selectionChanged
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addSelectionListener(this);
	}

	// 创建treeViewer内容
	private void createTreeViewer(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		treeViewer.setUseHashlookup(true);
		treeViewer.setContentProvider(new TeachUnitClassContentProvider());
		treeViewer.setLabelProvider(new TeachUnitClassLabelProvider());
		// 创建表头列
		final Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		final TreeColumn tbcl1 = new TreeColumn(tree, SWT.LEFT);
		tbcl1.setText("课程名称");
		tbcl1.setWidth(175);
		final TreeColumn tbcl2 = new TreeColumn(tree, SWT.CENTER);
		tbcl2.setText("学期");
		tbcl2.setWidth(40);
		final TreeColumn tbcl3 = new TreeColumn(tree, SWT.CENTER);
		tbcl3.setText("学时");
		tbcl3.setWidth(40);
		final TreeColumn tbcl4 = new TreeColumn(tree, SWT.CENTER);
		tbcl4.setText("学生数");
		tbcl4.setWidth(52);
		final TreeColumn tbcl5 = new TreeColumn(tree, SWT.CENTER);
		tbcl5.setText("班级");
		tbcl5.setWidth(112);
		final TreeColumn tbcl6 = new TreeColumn(tree, SWT.CENTER);
		tbcl6.setText("层次");
		tbcl6.setWidth(40);
		final TreeColumn tbcl7 = new TreeColumn(tree, SWT.CENTER);
		tbcl7.setText("课程/专业");
		tbcl7.setWidth(70);
		final TreeColumn tbcl8 = new TreeColumn(tree, SWT.CENTER);
		tbcl8.setText("质量");
		tbcl8.setWidth(40);
		// 创建编辑器，前面5列为null，表示该列不允许修改
		final CellEditor[] editors = new CellEditor[8];
		editors[5] = new TextCellEditor(tree);
		editors[6] = new TextCellEditor(tree);
		editors[7] = new TextCellEditor(tree);
		treeViewer.setColumnProperties(PROPERTIES);
		treeViewer.setCellModifier(new TeachUnitClassCellModifier(treeViewer));
		treeViewer.setCellEditors(editors);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		treeViewer.expandToLevel(2);
		tree.setFocus();
		// 记录改变时当前行突出显示
		tree.addListener(SWT.EraseItem, new SetTableColColorListener(tree));
	}

	// 创建Action对象
	private void createActions() {
		saveAction = new Action("保存数据", AbstractUIPlugin
				.imageDescriptorFromPlugin(IAppConstants.APPLICATION_ID,
						IImageKey.SAVE_DATA)) {
			public void run() {
				saveData();
			}
		};
		calcAction = new Action("经费测算", AbstractUIPlugin
				.imageDescriptorFromPlugin(IAppConstants.APPLICATION_ID,
						IImageKey.CALC)) {
			public void run() {
				new CalcMoney(YearManager.getInstance().getCurYear());
			}
		};
		closeTreeAction = new Action("收缩数据", AbstractUIPlugin
				.imageDescriptorFromPlugin(IAppConstants.APPLICATION_ID,
						IImageKey.CLOSE_TREE)) {
			public void run() {
				treeViewer.collapseAll();
			}
		};
		openTreeAction = new Action("展开数据", AbstractUIPlugin
				.imageDescriptorFromPlugin(IAppConstants.APPLICATION_ID,
						IImageKey.OPEN_TREE)) {
			public void run() {
				treeViewer.expandAll();
			}
		};
	}

	// 创建导航按钮
	private void createToolbarButtons() {
		IToolBarManager toolBar = getViewSite().getActionBars()
				.getToolBarManager();
		toolBar.add(saveAction);
		toolBar.add(calcAction);
		toolBar.add(closeTreeAction);
		toolBar.add(openTreeAction);
	}

	// 监听教学单位视图教学单位选择状态的变化
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!selection.isEmpty()) {
			saveAction.setEnabled(new SetControlEnabled().isEnabled(YearManager
					.getInstance().getCurYear()));
			IViewPart vp = getViewSite().getPage().findView(
					IAppConstants.TEACH_UNIT_NAME_VIEW_ID);
			// 获得数据
			Table table = ((TeachUnitName) vp).getTableViewer().getTable();
			YearTeachUnit ytu = (YearTeachUnit) table.getItem(
					table.getSelectionIndex()).getData();
			treeViewer.setInput(getCourse(ytu.getTeachunit().getUnitid(),
					YearManager.getInstance().getCurYear()));
			// 设置标题栏教学单位名称
			setPartName(getConfigurationElement().getAttribute("name")
					.toString()
					+ "---" + ytu.getTeachunit().getUnitname());
			treeViewer.expandToLevel(2);
		}
	}

	// 获得数据
	private List getCourse(String unitid, int year) {
		List<CourseTreeParent> list = new ArrayList<CourseTreeParent>();
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		// 获得课程明细数据
		Query query = session
				.getNamedQuery("cn.edu.jfcs.ui.TeachUnitClass.getCourse");
		query.setInteger(0, year);
		query.setString(1, unitid);
		int oldType = -1, curType = 0;
		CourseTreeParent ctp = null;
		for (Iterator iter = query.iterate(); iter.hasNext();) {
			Course course = (Course) iter.next();
			curType = Integer.parseInt(course.getCoursetype());
			String nodeName = curType == 1 ? "专业课明细" : "公共课明细";
			if (curType != oldType) {
				if (ctp != null) {
					list.add(ctp);
				}
				ctp = new CourseTreeParent(nodeName);
			}
			oldType = curType;
			CourseTreeChildren ctc = new CourseTreeChildren();
			ctc.setCourse(course);
			ctp.add(ctc);
		}
		if (ctp != null) {
			list.add(ctp);
		}
		HibernateSessionFactory.closeSession();
		return list;
	}

	// 保存数据
	private void saveData() {
		// 判断有无记录需要保存
		Iterator data = ((List) treeViewer.getInput()).iterator();
		if (!data.hasNext()) {
			MessageDialog.openWarning(null, "提示", "当前教学单位没有课程明细数据！");
			return;
		}

		Job job = new Job("正在保存数据。。。") {
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("正在保存数据。。。", IProgressMonitor.UNKNOWN);
				Session session = HibernateSessionFactory
						.getSession("hibernate_derby.cfg.xml");
				Transaction tx = session.beginTransaction();
				Iterator data = ((List) treeViewer.getInput()).iterator();
				int count = 0;
				while (data.hasNext()) {
					CourseTreeParent ctp = (CourseTreeParent) data.next();
					Iterator iterator = ((List) ctp.getCourseTreeChildren())
							.iterator();
					while (iterator.hasNext()) {
						CourseTreeChildren ctc = (CourseTreeChildren) iterator
								.next();
						monitor.worked(1);
						Course course = ctc.getCourse();
						session.update(course);
						// 设置批量更新块大小
						if (++count % 20 == 0) {
							session.flush();
							session.clear();
						}
						// 测试用，导出产品时删除
						try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
						}
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

	// 设置上下文相关帮助
	private void setHelpContextID() {
		IWorkbenchHelpSystem helpSystem = getSite().getWorkbenchWindow()
				.getWorkbench().getHelpSystem();
		helpSystem.setHelp(treeViewer.getControl(),
				IAppConstants.APPLICATION_ID + ".teachunitclass_view");
	}

	public void setFocus() {
		// TODO Auto-generated method stub
	}
}
