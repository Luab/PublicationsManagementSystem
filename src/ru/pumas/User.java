package ru.pumas;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

	private int id;
	private String login;
	private boolean isSuper;

	public User(int id, String login, boolean isSuper) {
		this.id = id;
		this.login = login;
		this.isSuper = isSuper;
	}

	public User() {
	}

	public static User from(ResultSet rs) throws SQLException {
		User ret = new User();
		ret.id = rs.getInt(DbContract.UsersTable.COLUMN_ID);
		ret.login = rs.getString(DbContract.UsersTable.COLUMN_LOGIN);
		ret.isSuper = rs.getBoolean(DbContract.UsersTable.COLUMN_IS_SUPER);
		return ret;
	}

	public int getId() {
		return id;
	}
	
	public String getLogin() {
		return login;
	}

	public boolean getIsSuper() {
		return isSuper;
	}

	public boolean isSuper() {
		return isSuper;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}

	public void setIsSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", isSuper=" + isSuper
				+ "]";
	}

}
