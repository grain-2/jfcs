/*
 *编写者：陈冈
 *高校经费测算系统--远程服务器数据源配置对话框
 *编写时间：2007-1-5
 */
package cn.edu.jfcs.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import cn.edu.jfcs.sys.CacheImage;
import cn.edu.jfcs.sys.ConfigCFGXML;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class RemoteDataSourceConfig extends Dialog {
	private Text username;

	private Text password;

	private Text classname;

	private Text url;

	private Button saveBtn;

	public RemoteDataSourceConfig(Shell parent) {
		super(parent);
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 10;
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);

		final CLabel label_0 = new CLabel(container, SWT.NONE);
		label_0.setAlignment(SWT.RIGHT);
		label_0.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 2, 1));
		label_0.setText("远程数据源服务器配置");
		label_0.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.DATA_SOURCE_CONFIG));

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setAlignment(SWT.RIGHT);
		label_1
				.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label_1.setText("数据库服务器URL");

		url = new Text(container, SWT.BORDER);
		url.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		url.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				saveBtn.setEnabled(false);
			}
		});

		final Label label_2 = new Label(container, SWT.NONE);
		label_2
				.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label_2.setAlignment(SWT.RIGHT);
		label_2.setText("驱动程序类名");

		classname = new Text(container, SWT.BORDER);
		classname
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		classname.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				saveBtn.setEnabled(false);
			}
		});

		final Label label_3 = new Label(container, SWT.NONE);
		label_3
				.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label_3.setAlignment(SWT.RIGHT);
		label_3.setText("用   户   名");

		username = new Text(container, SWT.BORDER);
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		username.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				saveBtn.setEnabled(false);
			}
		});

		final Label label_4 = new Label(container, SWT.NONE);
		label_4
				.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label_4.setAlignment(SWT.RIGHT);
		label_4.setText("密          码");

		password = new Text(container, SWT.BORDER);
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		password.setEchoChar('*');
		password.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				saveBtn.setEnabled(false);
			}
		});

		final Button button = new Button(container, SWT.NONE);
		button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false,
				2, 1));
		button.setText("测试连接");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// 测试连接
				String urltxt = url.getText().trim();
				String classnametxt = classname.getText().trim();
				String usernametxt = username.getText().trim();
				String passwordtxt = password.getText().trim();
				boolean isConnected = new ConfigCFGXML().testConnect(urltxt,
						classnametxt, usernametxt, passwordtxt);
				saveBtn.setEnabled(isConnected);
				if (isConnected)
					MessageDialog.openInformation(null, "提示", "远程数据源服务器连接成功！");
				else
					MessageDialog.openError(null, "提示", "无法连接远程数据源服务器，连接失败！");
			}
		});

		return container;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		saveBtn = createButton(parent, IDialogConstants.YES_ID, "保存", true);
		saveBtn.setEnabled(false);
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final String urltxt = url.getText().trim();
				final String classnametxt = classname.getText().trim();
				final String usernametxt = username.getText().trim();
				final String passwordtxt = password.getText().trim();
				if (new ConfigCFGXML().saveConfig(urltxt, classnametxt,
						usernametxt, passwordtxt))
					MessageDialog.openInformation(null, "提示",
							"远程数据源服务器配置信息已经保存！");
				else
					MessageDialog.openError(null, "提示", "远程数据源服务器配置信息保存失败！");

			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID, "退出", false);
	}

	protected Point getInitialSize() {
		return new Point(380, 230);
	}

	protected void configureShell(Shell parent) {
		parent.setText("数据源配置");
		super.configureShell(parent);
	}

}
