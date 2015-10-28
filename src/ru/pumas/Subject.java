package ru.pumas;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Subject {

	int id;
	String name;

	public Subject(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Subject() {
	}

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

	public static Subject from(ResultSet rs) throws SQLException {
		Subject ret = new Subject();
		ret.id = rs.getInt(DbContract.SubjectsTable.COLUMN_ID);
		ret.name = rs.getString(DbContract.SubjectsTable.COLUMN_NAME);
		return ret;
	}

	public static Subject fromFullNames(ResultSet rs) throws SQLException {
		Subject ret = new Subject();
		ret.id = rs.getInt(DbContract.SubjectsTable.TABLE_NAME + "."
				+ DbContract.SubjectsTable.COLUMN_ID);
		ret.name = rs.getString(DbContract.SubjectsTable.TABLE_NAME + "."
				+ DbContract.SubjectsTable.COLUMN_NAME);
		return ret;
	}

}
