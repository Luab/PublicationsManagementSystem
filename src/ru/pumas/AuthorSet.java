package ru.pumas;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorSet {

	ResultSet rs;

	public boolean next() throws SQLException {
		return rs.next();
	}

	public Author getAuthor() throws SQLException {
		return Author.from(rs);
	}

	public AuthorSet(ResultSet rs) {
		this.rs = rs;
	}

}
