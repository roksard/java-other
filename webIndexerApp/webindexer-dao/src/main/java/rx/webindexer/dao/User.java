package rx.webindexer.dao;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1402456403585069370L;
	private String login;
	private String password; 
	
	public User() {
		login = "";
		password = "";
	}
	public User(String login, String password) {
		super();
		this.login = login;
		this.password = password;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return this.login +":" + this.password;
	}
}
