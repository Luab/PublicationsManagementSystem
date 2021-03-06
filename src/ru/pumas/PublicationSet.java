package ru.pumas;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PublicationSet {

	ResultSet rs;

	public Publication getPublication() throws SQLException {
		return Publication.from(rs);
	}

	public boolean next() throws SQLException {
		return rs.next();
	}

	public PublicationSet(ResultSet rs) {
		this.rs = rs;
	}

}
