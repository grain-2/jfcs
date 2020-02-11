package cn.edu.jfcs.app;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import cn.edu.jfcs.sys.IAppConstants;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		// 加入教学单位视图
		layout.addStandaloneView(IAppConstants.TEACH_UNIT_NAME_VIEW_ID, true,
				IPageLayout.LEFT, 0.26f, editorArea);
		layout.getViewLayout(IAppConstants.TEACH_UNIT_NAME_VIEW_ID)
				.setCloseable(false);
		layout.getViewLayout(IAppConstants.TEACH_UNIT_NAME_VIEW_ID)
				.setMoveable(false);
		// 加入经费概览视图
		layout.addStandaloneView(IAppConstants.PIE_DIAGRAM_VIEW_ID, true,
				IPageLayout.BOTTOM, 0.72f,
				IAppConstants.TEACH_UNIT_NAME_VIEW_ID);
		layout.getViewLayout(IAppConstants.PIE_DIAGRAM_VIEW_ID).setCloseable(
				false);
		layout.getViewLayout(IAppConstants.PIE_DIAGRAM_VIEW_ID).setMoveable(
				false);
		// 加入教学单位课程明细视图
		layout.addView(IAppConstants.TEACH_UNIT_CLASS_VIEW_ID,
				IPageLayout.RIGHT, 0.74f, editorArea);
		layout.getViewLayout(IAppConstants.TEACH_UNIT_CLASS_VIEW_ID)
				.setCloseable(false);
		layout.getViewLayout(IAppConstants.TEACH_UNIT_CLASS_VIEW_ID)
				.setMoveable(true);
	}
}
