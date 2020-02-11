/*
 *编写者：陈冈
 *高校经费测算系统--用户管理
 *编写时间：2007-1-8
 */
package cn.edu.jfcs.ui;

import java.util.Iterator;
import java.util.Vector;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.edu.jfcs.model.Users;
import cn.edu.jfcs.sys.CacheImage;
import cn.edu.jfcs.sys.HibernateSessionFactory;
import cn.edu.jfcs.sys.IAppConstants;
import cn.edu.jfcs.sys.IImageKey;

public class UserManagerDialog extends TitleAreaDialog {
	private Text confirmpwdadd;

	private Text passwordadd;

	private Combo username;

	private Text usernameadd;

	private Text confirmpwd;

	private Text password;

	private CTabFolder tabFolder;

	private Button userTypeAdmin, userTypeJWC, userTypeComm;

	private Vector userVector = new Vector();

	public UserManagerDialog(Shell parentShell) {
		super(parentShell);
	}

	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		tabFolder = new CTabFolder(container, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setSimple(false);

		// 修改用户密码页
		final CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.USER_NAME));
		tabItem.setText("修改用户密码");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginTop = 10;
		gl.marginLeft = 30;
		composite.setLayout(gl);

		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = 57;
		final Label label1 = new Label(composite, SWT.NONE);
		label1.setAlignment(SWT.RIGHT);
		label1.setText("用  户  名");
		label1.setLayoutData(gd);
		username = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.widthHint = 110;
		username.setLayoutData(gd);
		userVector = getUserName();
		Iterator iter = userVector.iterator();
		int i = 0;
		while (iter.hasNext()) {
			if (i % 2 == 0)
				username.add(iter.next().toString());
			i++;
		}
		username.select(0);

		final Label label2 = new Label(composite, SWT.NONE);
		label2.setAlignment(SWT.RIGHT);
		label2.setText("新  密  码");
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = 57;
		gd.verticalIndent = 10;
		label2.setLayoutData(gd);
		password = new Text(composite, SWT.BORDER);
		password.setEchoChar('*');
		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.widthHint = 120;
		gd.verticalIndent = 10;
		password.setLayoutData(gd);

		final Label label3 = new Label(composite, SWT.NONE);
		label3.setAlignment(SWT.RIGHT);
		label3.setText("确认密码");
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = 57;
		gd.verticalIndent = 10;
		label3.setLayoutData(gd);
		confirmpwd = new Text(composite, SWT.BORDER);
		confirmpwd.setEchoChar('*');
		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.widthHint = 120;
		gd.verticalIndent = 10;
		confirmpwd.setLayoutData(gd);

		// 添加新用户页
		final CTabItem tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
		tabItem_1.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.PASSWORD));
		tabItem_1.setText("增加新用户");

		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 4;
		gl2.marginTop = 10;
		gl2.marginLeft = 20;
		composite_1.setLayout(gl2);
		tabItem_1.setControl(composite_1);

		final Label label4 = new Label(composite_1, SWT.NONE);
		label4.setAlignment(SWT.RIGHT);
		label4.setText("用  户  名");
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = 60;
		gd.verticalAlignment = 10;
		label4.setLayoutData(gd);
		usernameadd = new Text(composite_1, SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.widthHint = 120;
		gd.verticalAlignment = 10;
		gd.horizontalSpan = 2;
		usernameadd.setLayoutData(gd);
		new Label(composite_1, SWT.NONE).setText("");

		final Label label5 = new Label(composite_1, SWT.NONE);
		label5.setAlignment(SWT.RIGHT);
		label5.setText("密        码");
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = 60;
		gd.verticalAlignment = 10;
		label5.setLayoutData(gd);
		passwordadd = new Text(composite_1, SWT.BORDER);
		passwordadd.setEchoChar('*');
		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.widthHint = 120;
		gd.verticalAlignment = 10;
		gd.horizontalSpan = 2;
		passwordadd.setLayoutData(gd);
		new Label(composite_1, SWT.NONE).setText("");

		final Label label6 = new Label(composite_1, SWT.NONE);
		label6.setAlignment(SWT.RIGHT);
		label6.setText("确认密码");
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = 60;
		gd.verticalAlignment = 10;
		label6.setLayoutData(gd);
		confirmpwdadd = new Text(composite_1, SWT.BORDER);
		confirmpwdadd.setEchoChar('*');
		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.widthHint = 120;
		gd.verticalAlignment = 10;
		gd.horizontalSpan = 2;
		confirmpwdadd.setLayoutData(gd);
		new Label(composite_1, SWT.NONE).setText("");

		final Label label7 = new Label(composite_1, SWT.NONE);
		label7.setAlignment(SWT.RIGHT);
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = 60;
		gd.verticalAlignment = 10;
		label7.setLayoutData(gd);
		label7.setText("用户类别");

		userTypeAdmin = new Button(composite_1, SWT.RADIO);
		userTypeAdmin.setSelection(true);
		userTypeAdmin.setText("管理员");
		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.widthHint = 62;
		gd.verticalAlignment = 10;
		userTypeAdmin.setLayoutData(gd);

		userTypeJWC = new Button(composite_1, SWT.RADIO);
		userTypeJWC.setText("教务处");
		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.widthHint = 62;
		gd.verticalAlignment = 10;
		userTypeJWC.setLayoutData(gd);

		userTypeComm = new Button(composite_1, SWT.RADIO);
		userTypeComm.setText("普通");
		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.widthHint = 62;
		gd.verticalAlignment = 10;
		userTypeComm.setLayoutData(gd);
		setTitleImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.SYSTEM_USER_MANAGER));
		setTitle("系统用户管理");
		return area;
	}

	// 获得全部用户名
	private Vector getUserName() {
		Vector<String> vector = new Vector<String>();
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		Query query = session
				.getNamedQuery("cn.edu.jfcs.ui.UserManagerDialog.getUserName");
		ScrollableResults result = query.setCacheMode(CacheMode.IGNORE).scroll(
				ScrollMode.FORWARD_ONLY);
		while (result.next()) {
			Users user = (Users) result.get(0);
			vector.add(user.getId() - 1, user.getUsername());
		}
		return vector;
	}

	protected void createButtonsForButtonBar(final Composite parent) {
		// 保存按钮
		createButton(parent, IDialogConstants.YES_ID, "保存", true)
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						switch (tabFolder.getSelectionIndex()) {
						case 0:
							String username1 = username.getText().trim();
							String password1 = password.getText().trim();
							String password2 = confirmpwd.getText().trim();
							if (!password1.equals(password2)) {
								MessageDialog.openError(null, "提示",
										"两次输入的密码不一致，请重新输入！");
								password.setFocus();
								return;
							}
							if (password1.length() <= 0) {
								MessageDialog.openError(null, "提示",
										"密码不能为空，请重新输入！");
								password.setFocus();
								return;
							}
							if (MessageDialog.openQuestion(null, "提示",
									"确认修改用户【" + username1 + "】的密码？")) {
								// 修改用户密码
								updatePWD(username1, password1);
							}
							break;
						case 1:
							String usernameadds = usernameadd.getText().trim();
							String passwordadd1 = passwordadd.getText().trim();
							String confirmpwdadd1 = confirmpwdadd.getText()
									.trim();
							if (usernameadds.length() <= 0
									|| passwordadd1.length() <= 0
									|| confirmpwdadd1.length() <= 0) {
								MessageDialog.openError(null, "提示",
										"用户名、密码不能为空，请重新输入！");
								if (usernameadds.length() <= 0)
									usernameadd.setFocus();
								else if (passwordadd1.length() <= 0)
									passwordadd.setFocus();
								else if (confirmpwdadd1.length() <= 0)
									confirmpwdadd.setFocus();
								return;
							}
							if (!passwordadd1.equals(confirmpwdadd1)) {
								MessageDialog.openError(null, "提示",
										"两次输入的密码不一致，请重新输入！");
								password.setFocus();
								return;
							}
							String tag = userTypeAdmin.getSelection() ? "2"
									: userTypeJWC.getSelection() ? "1" : "0";
							// 添加新用户
							addNewUser(usernameadds, passwordadd1, tag);
						}
					}
				});
		createButton(parent, IDialogConstants.CANCEL_ID, "退出", false);
	}

	// 修改用户密码
	protected void updatePWD(String username, String password) {
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		Users user = (Users) session.get(Users.class, userVector
				.indexOf(username) + 1);
		user.setUsername(username);
		user.setPassword(password);
		user.setUsertag(user.getUsertag());
		Transaction tx = session.beginTransaction();
		session.update(user);
		tx.commit();
		if (tx.wasCommitted())
			MessageDialog.openInformation(null, "提示", "该用户密码已经被修改！");
		else
			MessageDialog.openError(null, "提示", "修改密码操作失败！");
		HibernateSessionFactory.closeSession();
	}

	// 添加新用户
	protected void addNewUser(String username, String password, String usertag) {
		// 先查询是否有重名用户
		Session session = HibernateSessionFactory
				.getSession("hibernate_derby.cfg.xml");
		Query query = session
				.getNamedQuery("cn.edu.jfcs.ui.UserManagerDialog.addNewUser");
		query.setString(0, username);
		ScrollableResults result = query.setCacheMode(CacheMode.IGNORE).scroll(
				ScrollMode.FORWARD_ONLY);
		if (result.next()) {
			MessageDialog.openError(null, "提示", "该用户已经存在，请重新输入用户名！");
			usernameadd.setFocus();
			return;
		}
		if (MessageDialog
				.openQuestion(null, "提示", "确认增加新用户【" + username + "】？")) {
			Users user = new Users();
			user.setUsername(username);
			user.setPassword(password);
			user.setUsertag(usertag);
			Transaction tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			if (tx.wasCommitted())
				MessageDialog.openInformation(null, "提示", "成功添加新用户！");
			else
				MessageDialog.openError(null, "提示", "添加新用户操作失败！");
		}
		HibernateSessionFactory.closeSession();
	}

	protected Point getInitialSize() {
		return new Point(300, 280);
	}

	protected void configureShell(Shell parent) {
		super.configureShell(parent);
		parent.setImage(CacheImage.getINSTANCE().getImage(
				IAppConstants.APPLICATION_ID, IImageKey.WINDOW_IMAGE));
		parent.setText("用户管理");
	}
}
