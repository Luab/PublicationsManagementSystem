package ru.pumas;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Venue {

	int id;
	String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Venue() {
	}

	public Venue(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static Venue from(ResultSet rs) throws SQLException {
		Venue ret = new Venue();
		ret.id = rs.getInt(DbContract.VenuesTable.COLUMN_ID);
		ret.name = rs.getString(DbContract.VenuesTable.COLUMN_NAME);
		return ret;
	}
}
