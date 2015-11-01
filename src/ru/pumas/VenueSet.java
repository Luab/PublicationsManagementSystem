package ru.pumas;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VenueSet {
	
	ResultSet rs;
	
	public boolean next() throws SQLException {
		return rs.next();
	}
	
	public Venue getVenue() throws SQLException {
		return Venue.from(rs);
	}
	
	public VenueSet(ResultSet rs) {
		this.rs = rs;
	}
	
	
	
}
