package cn.edu.jfcs.model;

public class SaveLogInfo {
	private String username;

	private String password;

	private String usertag;

	private static SaveLogInfo INSTANCE=new SaveLogInfo();

	private SaveLogInfo() {
	}

	public static SaveLogInfo getInstance() {
		return INSTANCE;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertag() {
		return usertag;
	}

	public void setUsertag(String usertag) {
		this.usertag = usertag;
	}

}
