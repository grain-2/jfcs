package cn.edu.jfcs.actions;

import org.eclipse.jface.action.Action;
import cn.edu.jfcs.ui.RemoteDataSourceConfig;

public class RemoteDataSRConfig extends Action {

	public RemoteDataSRConfig() {
		setId("cn.edu.jfcs.actions.remotedatasourceconfig");
		setText("数据源配置@ALT+P");
	}

	public void run() {
		new RemoteDataSourceConfig(null).open();
	}
}