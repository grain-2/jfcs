package cn.edu.jfcs.sys;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class StatusBarContribution extends ContributionItem {

	private String message;

	public void fill(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR);
		StatusLineLayoutData statusLineLayoutData = new StatusLineLayoutData();
		statusLineLayoutData.heightHint = 20;
		separator.setLayoutData(statusLineLayoutData);
		CLabel statusCLabel = new CLabel(parent, SWT.SHADOW_NONE);
		statusLineLayoutData = new StatusLineLayoutData();
		statusLineLayoutData.widthHint = 315;
		statusCLabel.setLayoutData(statusLineLayoutData);
		statusCLabel.setText(message);
		statusCLabel.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.LOG_USER_INFO));
	}

	public StatusBarContribution() {
		super();
	}

	public StatusBarContribution(String msg) {
		message = msg;
	}
}
