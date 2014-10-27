package cmpe273;

import org.hibernate.validator.constraints.NotEmpty;

public class WebLogin {

	@NotEmpty
	private String url;
	@NotEmpty
	private String login;
	private String login_id;
	@NotEmpty
	private String password;

	public WebLogin() {

	}

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
