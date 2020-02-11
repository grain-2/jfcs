/*
 *编写者：陈冈
 *高校经费测算系统--返回布局数据
 *编写时间：2006-11-16
 */
package cn.edu.jfcs.sys;

import org.eclipse.swt.layout.GridData;

public class GetGridData {
	public GridData getGridData(final int widthHint, final int heightHint,
			final int verticalIndent, final int verticalSpan,
			final int verticalAlignment, final int fillStyle,
			final int horizontalSpan) {
		final GridData gd = new GridData(fillStyle);
		if (widthHint != 0)
			gd.widthHint = widthHint;
		if (heightHint != 0)
			gd.heightHint = heightHint;
		if (verticalIndent != 0)
			gd.verticalIndent = verticalIndent;
		if (verticalSpan != 0)
			gd.verticalSpan = verticalSpan;
		if (verticalAlignment != 0)
			gd.verticalAlignment = verticalAlignment;
		if (horizontalSpan != 0)
			gd.horizontalSpan = horizontalSpan;
		return gd;
	}
}
