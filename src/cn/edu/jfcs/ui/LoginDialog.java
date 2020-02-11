package cn.edu.jfcs.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.jfcs.model.SaveLogInfo;
import cn.edu.jfcs.sys.CacheImage;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class LoginDialog extends Dialog {

	private Text usernameText;

	private Text passwordText;

	private boolean loginSucess = false;

	// private Users users;
	public LoginDialog(Shell parentShell) {
		super(parentShell);
	}

	// 创建对话框的内容
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginRight = 6;
		container.setLayout(gridLayout);

		final CLabel label = new CLabel(container, SWT.NONE);
		label.setText("用户名");
		label.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.USER_NAME));
		usernameText = new Text(container, SWT.BORDER);
		usernameText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false));

		final CLabel label2 = new CLabel(container, SWT.NONE);
		label2.setText("密　码");
		label2.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.PASSWORD));
		passwordText = new Text(container, SWT.BORDER);
		passwordText.setEchoChar('*');
		passwordText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false));
		return container;
	}

	// 创建对话框按钮
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "登录", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "退出", false);
	}

	// 检查用户输入是否为空
	protected void okPressed() {
		String username = usernameText.getText().trim();
		String password = passwordText.getText().trim();
		MessageBox mb;
		if (username == null || username.trim().length() == 0) {
			mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
			mb.setText("提示");
			mb.setMessage("用户名不能为空！");
			mb.open();
			usernameText.setFocus();
			return;
		}
		if (password == null || password.trim().length() == 0) {
			mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
			mb.setText("提示");
			mb.setMessage("密码不能为空！");
			mb.open();
			passwordText.setFocus();
			return;
		}
		SaveLogInfo.getInstance().setUsername(username);
		SaveLogInfo.getInstance().setPassword(password);
		super.okPressed();
	}

	public boolean close() {
		return super.close();
	}

	// 设置对话框初始大小
	protected Point getInitialSize() {
		return new Point(230, 150);
	}

	// 设置对话框窗口标题和图标
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("用户身份验证");
		newShell.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.WINDOW_IMAGE));
	}

	public boolean getLoginSucess() {
		return loginSucess;
	}
}
