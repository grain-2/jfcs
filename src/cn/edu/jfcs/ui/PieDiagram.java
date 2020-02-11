/*
 *编写者：陈冈
 *高校经费测算系统--
 *编写时间：2006-11-12
 */
package cn.edu.jfcs.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import cn.edu.jfcs.model.Calcresult;
import cn.edu.jfcs.model.YearTeachUnit;
import cn.edu.jfcs.sys.GetCalcResult;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.YearManager;

public class PieDiagram extends ViewPart implements ISelectionListener,
		IPropertyChangeListener {
	private Color textcolor = new Color(Display.getCurrent(), 0x33, 0x33, 0x33);

	private Color bgcolor = new Color(Display.getCurrent(), 0x99, 0x99, 0x99);

	private Color uibgcolor = new Color(Display.getCurrent(), 0xFF, 0x55, 0x55);

	private Color pibgcolor = new Color(Display.getCurrent(), 0x55, 0x55, 0xFF);

	private Color cibgcolor = new Color(Display.getCurrent(), 0x55, 0xFF, 0x55);

	private Color jcjbgcolor = new Color(Display.getCurrent(), 0xFF, 0xFF, 0x00);

	private Color zhywbgcolor = new Color(Display.getCurrent(), 0xE1, 0x00,
			0xE1);

	private Color canvasbgcolor = new Color(Display.getCurrent(), 0xE9, 0xF0,
			0xF9);

	final Font font = new Font(Display.getCurrent(), "楷体", 9, SWT.NONE);

	private Canvas drawingCanvas;

	public PieDiagram() {
	}

	public void createPartControl(Composite parent) {
		IViewPart vp = getViewSite().getPage().findView(
				IAppConstants.TEACH_UNIT_NAME_VIEW_ID);
		Table table = ((TeachUnitName) vp).getTableViewer().getTable();
		YearTeachUnit ytu = (YearTeachUnit) table.getItem(
				table.getSelectionIndex()).getData();
		setPartName(getConfigurationElement().getAttribute("name").toString()
				+ ytu.getTeachunit().getUnitname());
		drawingCanvas = new Canvas(parent, SWT.NONE);
		drawingCanvas.setBackground(canvasbgcolor);
		drawDiagram(parent, YearManager.getInstance().getCurYear(), ytu
				.getTeachunit().getUnitid());
		// 监听TeachUnitView视图的SelectionProvider，以便其发生改变时激活selectionChanged
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addSelectionListener(this);
	}

	// 绘图方法
	private void drawDiagram(final Composite parent, int year, String unitid) {
		drawingCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.setBackground(canvasbgcolor);
				e.gc.fillRectangle(0, 0, ((Canvas) e.widget).getBounds().width,
						((Canvas) e.widget).getBounds().height);
			}
		});
		drawingCanvas.redraw();
		// 获得画图数据
		GetCalcResult gmd = new GetCalcResult(year, unitid);
		List list = new ArrayList();
		list = gmd.getCalcresult();
		if (list.toArray().length == 0)
			return;
		Calcresult cr = (Calcresult) list.get(0);
		// 画背景阴影
		drawingCanvas.addPaintListener(new DrawArc(5, 5, 0, 360, 0, bgcolor, 0,
				0, 0, 0, 0));
		float ui = cr.getUi();
		float pi = cr.getPi();
		float ci = cr.getCi();
		float ryjf = cr.getRyjf();
		float zhywf = cr.getZhywf();
		int uiper = 0, piper = 0, ciper = 0, ryjfper = 0, zhywfper = 0;
		if (ui + pi + ci + ryjf + zhywf == 0)
			drawingCanvas.addPaintListener(new DrawArc(0, 0, 0, 360, -1, parent
					.getDisplay().getSystemColor(SWT.COLOR_WHITE), 0, 0, 0, 0,
					0));
		else {
			float totalMoney = ui + pi + ci + ryjf + zhywf;
			if (totalMoney != 0) {
				// 计算得到弧度
				uiper = (int) (ui / totalMoney * 360);
				piper = (int) (pi / totalMoney * 360);
				ciper = (int) (ci / totalMoney * 360);
				ryjfper = (int) (ryjf / totalMoney * 360);
				zhywfper = 360 - uiper - piper - ciper - ryjfper - zhywfper;
			}

			drawingCanvas.addPaintListener(new DrawArc(0, 0, 0, uiper, 1,
					uibgcolor, ui, pi, ci, ryjf, zhywf));
			drawingCanvas.addPaintListener(new DrawArc(0, 0, uiper, piper, 2,
					pibgcolor, ui, pi, ci, ryjf, zhywf));
			drawingCanvas.addPaintListener(new DrawArc(0, 0, uiper + piper,
					ciper, 3, cibgcolor, ui, pi, ci, ryjf, zhywf));
			drawingCanvas.addPaintListener(new DrawArc(0, 0, uiper + piper
					+ ciper, ryjfper, 4, jcjbgcolor, ui, pi, ci, ryjf, zhywf));
			drawingCanvas.addPaintListener(new DrawArc(0, 0, uiper + piper
					+ ciper + ryjfper, zhywfper, 5, zhywbgcolor, ui, pi, ci,
					ryjf, zhywf));
		}
	}

	public void setFocus() {

	}

	// 画饼图
	private class DrawArc implements PaintListener {
		private int xoffset, yoffset, beginAngle, angle, tag;

		private Color dwcolor;

		private float ui, pi, ci, ryjf, zhywf;

		// 参数分别为圆心坐标x、圆心坐标y、开始角度、结束角度、所画项目代号、颜色、学生经费、
		// 专业培养费、公共课经费、人员经费、综合业务费
		public DrawArc(int xoffset, int yoffset, int beginAngle, int angle,
				int tag, Color dwcolor, float ui, float pi, float ci,
				float ryjf, float zhywf) {
			super();
			this.xoffset = xoffset;
			this.yoffset = yoffset;
			this.beginAngle = beginAngle;
			this.angle = angle;
			this.tag = tag;
			this.dwcolor = dwcolor;
			this.ui = ui;
			this.pi = pi;
			this.ci = ci;
			this.ryjf = ryjf;
			this.zhywf = zhywf;
		}

		public void paintControl(final PaintEvent e) {
			// 获得canvas以便画图
			final Canvas canvas = (Canvas) e.widget;
			final int x = canvas.getBounds().width;
			final int y = canvas.getBounds().height;
			e.gc.setBackground(dwcolor);
			// 画图
			e.gc.fillArc(xoffset + 15, yoffset + 1, x - 30, y - 60, beginAngle,
					angle);
			e.gc.setFont(font);
			switch (tag) {
			case -1:
				e.gc.setForeground(textcolor);
				e.gc.drawString("学生经费、公共课经费等金额均为0！", 1, y - 40, true);
				break;
			case 1:
				e.gc.setForeground(textcolor);
				e.gc.drawString("单位：万元", x / 4, y - 50, true);
				e.gc.setForeground(dwcolor);
				e.gc.drawString("■", 0, y - 38, true);
				e.gc.setForeground(textcolor);
				e.gc.drawString("学生经费" + ui, 12, y - 38, true);
				break;
			case 2:
				e.gc.setForeground(dwcolor);
				e.gc.drawString("■", 102, y - 38, true);
				e.gc.setForeground(textcolor);
				e.gc.drawString("专业培养" + pi, 113, y - 38, true);
				break;
			case 3:
				e.gc.setForeground(dwcolor);
				e.gc.drawString("■", 0, y - 26, true);
				e.gc.setForeground(textcolor);
				e.gc.drawString("公共课" + ci, 12, y - 26, true);
				break;
			case 4:
				e.gc.setForeground(dwcolor);
				e.gc.drawString("■", 102, y - 26, true);
				e.gc.setForeground(textcolor);
				e.gc.drawString("人员经费" + ryjf, 113, y - 26, true);
				break;
			case 5:
				e.gc.setForeground(dwcolor);
				e.gc.drawString("■", 0, y - 14, true);
				e.gc.setForeground(textcolor);
				e.gc.drawString("综合业务" + zhywf, 12, y - 14, true);
				break;
			}
		}
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!selection.isEmpty())
			refreshView();
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals("curYear"))
			refreshView();
	}

	// 刷新视图
	private void refreshView() {
		IViewPart vp = getViewSite().getPage().findView(
				IAppConstants.TEACH_UNIT_NAME_VIEW_ID);
		Table table = ((TeachUnitName) vp).getTableViewer().getTable();
		YearTeachUnit ytu = (YearTeachUnit) table.getItem(
				table.getSelectionIndex()).getData();
		setPartName(getConfigurationElement().getAttribute("name").toString()
				+ "---" + ytu.getTeachunit().getUnitname());
		drawDiagram(getViewSite().getShell(), YearManager.getInstance()
				.getCurYear(), ytu.getTeachunit().getUnitid());
	}
}
