package ru.pumas;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectSet {

	ResultSet rs;

	public boolean next() throws SQLException {
		return rs.next();
	}

	public Subject getSubject() throws SQLException {
		return Subject.from(rs);
	}

	public SubjectSet(ResultSet rs) {
		this.rs = rs;
	}

}
