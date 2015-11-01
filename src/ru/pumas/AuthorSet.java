package ru.pumas;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorSet {

	ResultSet rs;

	boolean next() throws SQLException {
		return rs.next();
	}

	Author getAuthor() throws SQLException {
		return Author.from(rs);
	}

	public AuthorSet(ResultSet rs) {
		this.rs = rs;
	}

}
