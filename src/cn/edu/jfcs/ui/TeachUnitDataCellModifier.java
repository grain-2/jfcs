/*
 *编写者：陈冈
 *高校经费测算系统--教学单位基本情况视图的CellModifier
 *编写时间：2006-11-18
 */
package cn.edu.jfcs.ui;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Item;
import cn.edu.jfcs.model.YearTeachUnit;
import cn.edu.jfcs.sys.SetControlEnabled;
import cn.edu.jfcs.sys.YearManager;

public class TeachUnitDataCellModifier implements ICellModifier {
	private TableViewer tv;

	public TeachUnitDataCellModifier(TableViewer tv) {
		this.tv = tv;
	}

	public boolean canModify(Object element, String property) {
		if (element instanceof Item)
			element = ((Item) element).getData();
		if (element instanceof YearTeachUnit) {
			if (!property.equals(TeachUnitData.UNITNAME))
				// 根据用户类比、年份、用户是否进入编辑状态决定数据是否可修改
				return TeachUnitData.canEdit
						&& (new SetControlEnabled().isEnabled(YearManager
								.getInstance().getCurYear()));
		}
		return false;
	}

	public Object getValue(Object element, String property) {
		if (element instanceof YearTeachUnit) {
			YearTeachUnit ytu = (YearTeachUnit) element;
			if (property.equals(TeachUnitData.SSB))
				return ytu.getSsb() + "";
			else if (property.equals(TeachUnitData.JXYWPER))
				return ytu.getJxywper() + "";// 教学业务费比率
			else if (property.equals(TeachUnitData.JXGLPER))
				return ytu.getJxglper() + "";// 教学管理费比率
			else if (property.equals(TeachUnitData.JXYJPER))
				return ytu.getJxyjper() + ""; // 教学研究费比率
			else if (property.equals(TeachUnitData.SZPYPER))
				return ytu.getSzpyper() + "";// 师资培养费比率
		}
		return "";
	}

	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();
		if (element instanceof YearTeachUnit) {
			String data = (String) value;
			Pattern pc;
			if (property.equals(TeachUnitData.SSB)) {
				pc = Pattern.compile("[0-9]{1,3}");
			} else {
				pc = Pattern.compile("[0-9]{1,3}\\.?[0-9]{0,2}");
			}
			Matcher m = pc.matcher((String) value);
			if (!m.matches()) {
				MessageDialog.openError(null, "提示",
						"输入错误，请不要输入负数、字母、空格或者其他非数字数据。\n对于生师比，只能输入整数！");
				return;
			}
			YearTeachUnit ytu = (YearTeachUnit) element;
			if (property.equals(TeachUnitData.SSB))
				ytu.setSsb(Integer.parseInt(data));
			else if (property.equals(TeachUnitData.JXYWPER))
				ytu.setJxywper(new BigDecimal(data));
			else if (property.equals(TeachUnitData.JXGLPER))
				ytu.setJxglper(new BigDecimal(data));
			else if (property.equals(TeachUnitData.JXYJPER))
				ytu.setJxyjper(new BigDecimal(data));
			else if (property.equals(TeachUnitData.SZPYPER))
				ytu.setSzpyper(new BigDecimal(data));
		}
		tv.refresh();
	}

}
