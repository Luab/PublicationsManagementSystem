package ru.pumas;
import java.sql.ResultSet;
import java.sql.SQLException;

import ru.pumas.DbContract;

public class Author {

	int id;
	String name;
	int numberOfPublications;
	
	public Author(int id, String name, int numberOfPublications) {
		this.id = id;
		this.name = name;
		this.numberOfPublications = numberOfPublications;
	}

	public Author() {
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumberOfPublications() {
		return numberOfPublications;
	}
	
	public static Author from(ResultSet rs) throws SQLException {
		Author ret = new Author();
		ret.id = rs.getInt(DbContract.AuthorsTable.COLUMN_ID);
		ret.name = rs.getString(DbContract.AuthorsTable.COLUMN_NAME);
		ret.numberOfPublications = rs.getInt(DbContract.AuthorsTable.COLUMN_NUMBER_OF_PUBLICATIONS);
		return ret;
	}

}
