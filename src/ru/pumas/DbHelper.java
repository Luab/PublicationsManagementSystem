package ru.pumas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.postgresql.Driver;

public class DbHelper {

	static final String DB_URL = "jdbc:postgresql://10.240.19.130:5432/student";
	static final String DB_USER = "yura";
	static final String DB_PASS = "";

	private static Connection connection = getConnection();

	static String commaSeparated(Object[] obj, String append) {
		if (obj == null) {
			return null;
		}
		if (obj.length == 0) {
			return "";
		}
		String s = obj[0].toString();
		if (append != null) {
			s += append;
		}
		for (int i = 1; i < obj.length; i++) {
			s += ", " + obj[i];
			if (append != null) {
				s += append;
			}
		}
		return s;
	}

	static String selectWhatFromWhere(String[] what, String[] from,
			String where) {
		String s = "SELECT ";
		if (what == null) {
			s += "*";
		} else {
			s += commaSeparated(what, null);
		}
		s += " FROM ";
		s += commaSeparated(from, null);
		if (where != null) {
			s += " WHERE ";
			s += where;
		}
		return s;
	}

	static Connection getConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int addAuthor(String author) throws SQLException {
		String sql = "INSERT INTO " +
				DbContract.AuthorsTable.TABLE_NAME + " (" +
				DbContract.AuthorsTable.COLUMN_NAME + ") VALUES (?)";
		PreparedStatement statement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, author);
		// System.out.println(statement.toString());
		statement.execute();
		ResultSet rs = statement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get generated key");
		}
		return rs.getInt(3); // TODO: why 3 ???
	}

	public static int addPublicationAuthorRelationship(String author,
			int publicationId) throws SQLException {
		String sql = "INSERT INTO " +
				DbContract.PublicationAuthorsTable.TABLE_NAME + " (" +
				DbContract.PublicationAuthorsTable.COLUMN_PUBLICATION_ID + ", "
				+
				DbContract.PublicationAuthorsTable.COLUMN_AUTHOR_ID +
				") VALUES (?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, publicationId);
		statement.setInt(2, getOrAddAuthorId(author));
		System.out.println(statement.toString());
		statement.execute();
		ResultSet rs = statement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get keys");
		}
		return rs.getInt(1);
	}

	public static int addPublicationSubjectRelationship(String subject,
			int pub_id) throws SQLException {
		String sql = "INSERT INTO "
				+ DbContract.PublicationsSubjectTable.TABLE_NAME + " (" +
				DbContract.PublicationsSubjectTable.COLUMN_PUBLICATION_ID
				+ ", " +
				DbContract.PublicationsSubjectTable.COLUMN_SUBJECT_ID +
				") VALUES (?, ?)";

		PreparedStatement statement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, pub_id);
		statement.setInt(2, getOrAddSubjectId(subject));
		statement.execute();
		ResultSet rs = statement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get keys");
		}
		return rs.getInt(1);
	}

	public static int addPublicationKeywordRelationship(String keyword,
			int pub_id) throws SQLException {
		String sql = "INSERT INTO "
				+ DbContract.KeywordInTable.TABLE_NAME + " (" +
				DbContract.KeywordInTable.COLUMN_PUBLICATION_IN
				+ ", " +
				DbContract.KeywordInTable.COLUMN_KEYWORD_ID +
				") VALUES (?, ?)";

		PreparedStatement statement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, pub_id);
		statement.setInt(2, getOrAddKeywordId(keyword));

		statement.execute();
		ResultSet rs = statement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get keys");
		}
		return rs.getInt(1);
	}

	public static int addSubject(String subject) throws SQLException {
		String sql = "INSERT INTO " + DbContract.SubjectsTable.TABLE_NAME + " ("
				+
				DbContract.SubjectsTable.COLUMN_NAME + ") VALUES (?)";
		PreparedStatement statement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, subject);
		statement.execute();
		ResultSet rs = statement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get keys");
		}
		return rs.getInt(1);
	}

	public static int addVenue(String venue) throws SQLException {
		String sql = "INSERT INTO " + DbContract.VenuesTable.TABLE_NAME + " (" +
				DbContract.VenuesTable.COLUMN_NAME + ") VALUES (?)";
		PreparedStatement statement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, venue);
		statement.execute();
		ResultSet rs = statement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get keys");
		}
		return rs.getInt(1);
	}

	/**
	 * Returns <code>id</code> of author. If this author does not exist adds
	 * author to database.
	 *
	 * @param author
	 *            to search
	 * @return id of author
	 * @throws SQLException
	 */
	public static int getOrAddAuthorId(String author) throws SQLException {
		String sql = "SELECT * FROM " + DbContract.AuthorsTable.TABLE_NAME +
				" WHERE " + DbContract.AuthorsTable.COLUMN_NAME + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, author);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt(DbContract.AuthorsTable.COLUMN_ID);
		}
		return addAuthor(author);
	}

	public static int getOrAddSubjectId(String subject) throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.SubjectsTable.TABLE_NAME + " WHERE " +
				DbContract.SubjectsTable.COLUMN_NAME + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, subject);

		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt(1);
		}
		return addSubject(subject);
	}

	public static int getOrAddVenueId(String venue) throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.VenuesTable.TABLE_NAME + " WHERE "
				+ DbContract.VenuesTable.COLUMN_NAME + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, venue);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt(1);
		}
		return addVenue(venue);
	}

	public static ResultSet getPublicationIdByLinkSet(String link)
			throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.PublicationsTable.TABLE_NAME + " WHERE " +
				DbContract.PublicationsTable.COLUMN_LINK + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, link);
		return preparedStatement.executeQuery();
	}

	public static int getPublicationIdByLink(String link) throws SQLException {
		ResultSet rs = getPublicationIdByLinkSet(link);
		if (rs.next()) {
			return rs.getInt(1);
		}
		throw new NoSuchElementException("No publication with link " + link);
	}

	public static int getOrAddKeywordId(String keyword) throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.KeywordsTable.TABLE_NAME + " WHERE "
				+ DbContract.KeywordsTable.COLUMN_KEYWORD + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, keyword);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt(1);
		}
		return addKeyword(keyword);
	}

	public static int addKeyword(String keyword) throws SQLException {
		String sql = "INSERT INTO " + DbContract.KeywordsTable.TABLE_NAME + " ("
				+
				DbContract.KeywordsTable.COLUMN_KEYWORD + ") VALUES (?)";
		PreparedStatement statement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, keyword);
		statement.execute();
		ResultSet rs = statement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get keys");
		}
		return rs.getInt(1);
	}

	public static ResultSet getPublicationByIdSet(int id) throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.PublicationsTable.TABLE_NAME + " WHERE "
				+ DbContract.PublicationsTable.COLUMN_ID + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		return preparedStatement.executeQuery();
	}

	public static Publication getPublicationById(int id) throws SQLException {
		ResultSet rs = getPublicationByIdSet(id);
		if (rs.next()) {
			return Publication.from(rs);
		}
		return null;
	}

	public static ResultSet getVenueByIdSet(int id) throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.VenuesTable.TABLE_NAME + " WHERE " +
				DbContract.VenuesTable.COLUMN_ID + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		return preparedStatement.executeQuery();
	}

	public static Venue getVenueById(int id) throws SQLException {
		ResultSet rs = getVenueByIdSet(id);
		if (rs.next()) {
			return Venue.from(rs);
		}
		return null;
	}

	public static ResultSet getAuthorsByPublicationIdSet(int id)
			throws SQLException {
		String sql = selectWhatFromWhere(
				new String[] { DbContract.AuthorsTable.FULL_COLUMN_ID,
						DbContract.AuthorsTable.FULL_COLUMN_NAME,
						DbContract.AuthorsTable.FULL_NUMBER_OF_PUBLICATIONS },
				new String[] { DbContract.AuthorsTable.TABLE_NAME,
						DbContract.PublicationAuthorsTable.TABLE_NAME },
				DbContract.PublicationAuthorsTable.FULL_COLUMN_PUBLICATION_ID
						+ " = ? AND " + DbContract.AuthorsTable.FULL_COLUMN_ID
						+ " = "
						+ DbContract.PublicationAuthorsTable.FULL_COLUMN_AUTHOR_ID);
		PreparedStatement preparedStatement = connection.prepareStatement(
				sql);
		preparedStatement.setInt(1, id);
		return preparedStatement.executeQuery();

	}

	public static Author[] getAuthorsByPublicationId(int id)
			throws SQLException {
		ResultSet rs = getAuthorsByPublicationIdSet(id);
		ArrayList<Author> authors = new ArrayList<>();
		while (rs.next()) {
			authors.add(Author.from(rs));
		}
		return authors.toArray(new Author[authors.size()]);
	}

	public static ResultSet getSubjectsByPublicationIdSet(int id)
			throws SQLException {
		String sql = selectWhatFromWhere(
				new String[] { DbContract.SubjectsTable.TABLE_NAME + "."
						+ DbContract.SubjectsTable.COLUMN_ID,
						DbContract.SubjectsTable.COLUMN_NAME },
				new String[] { DbContract.SubjectsTable.TABLE_NAME,
						DbContract.PublicationsSubjectTable.TABLE_NAME },
				DbContract.PublicationsSubjectTable.TABLE_NAME + "." +
						DbContract.PublicationsSubjectTable.COLUMN_PUBLICATION_ID
						+ " = ? AND " +
						DbContract.PublicationsSubjectTable.TABLE_NAME + "." +
						DbContract.PublicationsSubjectTable.COLUMN_SUBJECT_ID
						+ " = " +
						DbContract.SubjectsTable.TABLE_NAME + "." +
						DbContract.SubjectsTable.COLUMN_ID);
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		return preparedStatement.executeQuery();
	}

	public static Subject[] getSubjectsByPublicationId(int id)
			throws SQLException {
		ResultSet rs = getSubjectsByPublicationIdSet(id);
		List<Subject> subjects = new ArrayList<>();
		while (rs.next()) {
			subjects.add(Subject.from(rs));
		}
		return subjects.toArray(new Subject[subjects.size()]);
	}

	public static ResultSet getPublicationsSet() throws SQLException {
		String sql = selectWhatFromWhere(null,
				new String[] { DbContract.PublicationsTable.TABLE_NAME },
				null);
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		return preparedStatement.executeQuery();
	}

}
