package ru.pumas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

public class DbHelper {

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

	private static String selectWhatFromWhere(String what, String from,
			String where) {
		return selectWhatFromWhere(what, from, where, null, null);
	}

	private static String selectWhatFromWhere(String what, String from,
			String where, Integer offset, Integer limit) {
		String[] arWhat = what == null ? null : new String[] { what };
		String[] arFrom = from == null ? null : new String[] { from };
		return selectWhatFromWhere(arWhat, arFrom, where, offset, limit);
	}

	private static String selectWhatFromWhere(String[] what, String[] from,
			String where, Integer offset, Integer limit) {
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
		if (offset != null) {
			s += " OFFSET " + offset;
		}
		if (limit != null) {
			s += " LIMIT " + limit;
		}
		return s;
	}

	private static String insertIntoValuesQuestionMarks(String into,
			String[] values) {

		String s = "INSERT INTO " + into + " (" + commaSeparated(values, null)
				+ ") VALUES (";
		for (int i = 0; i < values.length; i++) {
			s += "?";
			if (i != values.length - 1) {
				s += ",";
			}
		}
		s += ")";
		return s;
	}

	static Connection getConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection(DbCredentials.DB_URL,
					DbCredentials.DB_USER, DbCredentials.DB_PASS);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new Error(e.getMessage());
		}
	}

	public static int addAuthor(String author) throws SQLException {
		String sql = "INSERT INTO " +
				DbContract.AuthorsTable.TABLE_NAME + " (" +
				DbContract.AuthorsTable.COLUMN_NAME + ") VALUES (?)";
		PreparedStatement statement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, author);
		statement.execute();
		ResultSet rs = statement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get generated key");
		}
		return rs.getInt(3); // TODO: why 3 ???
	}

	public static int addPublicationAuthorRelationship(int authorId,
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
		statement.setInt(2, authorId);
		System.err.println(statement);
		statement.execute();
		ResultSet rs = statement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get keys");
		}
		return rs.getInt(1);
	}

	public static int addPublicationSubjectRelationship(int subjectId,
			int publicationId) throws SQLException {
		String sql = "INSERT INTO "
				+ DbContract.PublicationsSubjectTable.TABLE_NAME + " (" +
				DbContract.PublicationsSubjectTable.COLUMN_PUBLICATION_ID
				+ ", " +
				DbContract.PublicationsSubjectTable.COLUMN_SUBJECT_ID +
				") VALUES (?, ?)";

		PreparedStatement statement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, publicationId);
		statement.setInt(2, subjectId);
		statement.execute();
		ResultSet rs = statement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get keys");
		}
		return rs.getInt(1);
	}

	public static int addPublicationKeywordRelationship(int keywordId,
			int publicationId) throws SQLException {
		String sql = "INSERT INTO "
				+ DbContract.KeywordInTable.TABLE_NAME + " (" +
				DbContract.KeywordInTable.COLUMN_PUBLICATION_IN
				+ ", " +
				DbContract.KeywordInTable.COLUMN_KEYWORD_ID +
				") VALUES (?, ?)";

		PreparedStatement statement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, publicationId);
		statement.setInt(2, keywordId);

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
	 * Searches for author by name
	 * 
	 * @param author
	 *            name of the author
	 * @return <code>id</code> of the author or <code>null</code> if no author
	 *         found.
	 * @throws SQLException
	 */
	public static Integer getAuthorId(String author) throws SQLException {
		String sql = selectWhatFromWhere(DbContract.AuthorsTable.COLUMN_ID,
				DbContract.AuthorsTable.TABLE_NAME,
				DbContract.AuthorsTable.COLUMN_NAME + " = ?");
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, author);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt(DbContract.AuthorsTable.COLUMN_ID);
		}
		return null;
	}

	public static int getOrAddAuthorId(String author) throws SQLException {
		Integer id = getAuthorId(author);
		if (id == null) {
			id = addAuthor(author);
		}
		return id;
	}

	/**
	 * 
	 * @param subject
	 * @return <code>id</code> if found, <code>null</code> if not found.
	 * @throws SQLException
	 */
	public static Integer getSubjectId(String subject) throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.SubjectsTable.TABLE_NAME + " WHERE " +
				DbContract.SubjectsTable.COLUMN_NAME + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, subject);

		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt(1);
		}
		return null;
	}

	public static int getOrAddSubjectId(String subject) throws SQLException {
		Integer id = getSubjectId(subject);
		if (id == null) {
			id = addSubject(subject);
		}
		return id;
	}

	/**
	 * 
	 * @param venue
	 * @return <code>id</code> if found, <code>null</code> otherwise
	 * @throws SQLException
	 */
	public static Integer getVenueId(String venue) throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.VenuesTable.TABLE_NAME + " WHERE "
				+ DbContract.VenuesTable.COLUMN_NAME + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, venue);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt(1);
		}
		return null;
	}

	public static int getOrAddVenueId(String venue) throws SQLException {
		Integer id = getVenueId(venue);
		if (id == null) {
			id = addVenue(venue);
		}
		return id;
	}
	// public static ResultSet getPublicationIdByLinkSet(String link)
	// throws SQLException {
	// String sql = "SELECT * FROM " +
	// DbContract.PublicationsTable.TABLE_NAME + " WHERE " +
	// DbContract.PublicationsTable.COLUMN_LINK + " = ?";
	// PreparedStatement preparedStatement = connection.prepareStatement(sql);
	// preparedStatement.setString(1, link);
	// return preparedStatement.executeQuery();
	// }
	//
	// public static int getPublicationIdByLink(String link) throws SQLException
	// {
	// ResultSet rs = getPublicationIdByLinkSet(link);
	// if (rs.next()) {
	// return rs.getInt(1);
	// }
	// throw new NoSuchElementException("No publication with link " + link);
	// }

	/**
	 * 
	 * @param keyword
	 * @return <code>id</code> if found, <code>null</code> otherwise
	 * @throws SQLException
	 */
	public static Integer getKeywordId(String keyword) throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.KeywordsTable.TABLE_NAME + " WHERE "
				+ DbContract.KeywordsTable.COLUMN_KEYWORD + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, keyword);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt(1);
		}
		return null;
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

	public static PublicationSet getPublicationSetById(int id)
			throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.PublicationsTable.TABLE_NAME + " WHERE "
				+ DbContract.PublicationsTable.COLUMN_ID + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static Publication getPublicationById(int id) throws SQLException {
		PublicationSet rs = getPublicationSetById(id);
		if (rs.next()) {
			return rs.getPublication();
		}
		return null;
	}

	public static VenueSet getVenueByIdSet(int id) throws SQLException {
		String sql = "SELECT * FROM " +
				DbContract.VenuesTable.TABLE_NAME + " WHERE " +
				DbContract.VenuesTable.COLUMN_ID + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		return new VenueSet(preparedStatement.executeQuery());
	}

	public static Venue getVenueById(int id) throws SQLException {
		VenueSet rs = getVenueByIdSet(id);
		if (rs.next()) {
			return rs.getVenue();
		}
		return null;
	}

	public static AuthorSet getAuthorByIdSet(int id) throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.AuthorsTable.TABLE_NAME,
				DbContract.AuthorsTable.COLUMN_ID + " = ?");
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		return new AuthorSet(preparedStatement.executeQuery());
	}

	public static Author getAuthorById(int id) throws SQLException {
		AuthorSet rs = getAuthorByIdSet(id);
		if (rs.next()) {
			return rs.getAuthor();
		}
		return null;
	}

	public static SubjectSet getSubjectByIdSet(int id) throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.SubjectsTable.TABLE_NAME,
				DbContract.SubjectsTable.COLUMN_ID + " = ?");
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		return new SubjectSet(preparedStatement.executeQuery());
	}

	public static Subject getSubjectById(int id) throws SQLException {
		SubjectSet rs = getSubjectByIdSet(id);
		if (rs.next()) {
			return rs.getSubject();
		}
		return null;
	}

	public static AuthorSet getAuthorsByPublicationIdSet(int id)
			throws SQLException {
		return getAuthorsByPublicationIdSet(id, null, null);
	}

	public static AuthorSet getAuthorsByPublicationIdSet(int id, Integer offset,
			Integer limit)
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
						+ DbContract.PublicationAuthorsTable.FULL_COLUMN_AUTHOR_ID,
				offset, limit);
		PreparedStatement preparedStatement = connection.prepareStatement(
				sql);
		preparedStatement.setInt(1, id);
		return new AuthorSet(preparedStatement.executeQuery());
	}

	// public static Author[] getAuthorsByPublicationId(int id)
	// throws SQLException {
	// ResultSet rs = getAuthorsByPublicationIdSet(id);
	// ArrayList<Author> authors = new ArrayList<>();
	// while (rs.next()) {
	// authors.add(Author.from(rs));
	// }
	// return authors.toArray(new Author[authors.size()]);
	// }

	public static SubjectSet getSubjectsByPublicationIdSet(int id)
			throws SQLException {
		return getSubjectsByPublicationIdSet(id, null, null);
	}

	public static SubjectSet getSubjectsByPublicationIdSet(int id,
			Integer offset, Integer limit)
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
						DbContract.SubjectsTable.COLUMN_ID,
				offset, limit);
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		return new SubjectSet(preparedStatement.executeQuery());
	}

	// public static Subject[] getSubjectsByPublicationId(int id)
	// throws SQLException {
	// ResultSet rs = getSubjectsByPublicationIdSet(id);
	// List<Subject> subjects = new ArrayList<>();
	// while (rs.next()) {
	// subjects.add(Subject.from(rs));
	// }
	// return subjects.toArray(new Subject[subjects.size()]);
	// }

	public static PublicationSet getPublicationSet() throws SQLException {
		return getPublicationSet(null, null);
	}

	public static PublicationSet getPublicationSet(Integer offset,
			Integer limit) throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.PublicationsTable.TABLE_NAME,
				null, offset, limit);
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static PublicationSet searchPublicationsByTitleSubstring(String s)
			throws SQLException {
		return searchPublicationsByTitleSubstring(s, null, null);
	}

	public static PublicationSet searchPublicationsByTitleSubstring(String s,
			Integer offset, Integer limit)
					throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.PublicationsTable.TABLE_NAME,
				DbContract.PublicationsTable.COLUMN_TITLE + " ILIKE ?", offset,
				limit);
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, "%" + s + "%");
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static PublicationSet getPublicationSetByAuthorId(int id)
			throws SQLException {
		return getPublicationSetByAuthorId(id, null, null);
	}

	public static PublicationSet getPublicationSetByAuthorId(int id,
			Integer offset, Integer limit)
					throws SQLException {
		String sql = selectWhatFromWhere(
				new String[] { DbContract.PublicationsTable.ALL_COLUMNS },
				new String[] { DbContract.PublicationsTable.TABLE_NAME,
						DbContract.PublicationAuthorsTable.TABLE_NAME },
				DbContract.PublicationAuthorsTable.FULL_COLUMN_AUTHOR_ID
						+ " = ? AND "
						+ DbContract.PublicationsTable.FULL_COLUMN_ID
						+ " = "
						+ DbContract.PublicationAuthorsTable.FULL_COLUMN_PUBLICATION_ID,
				offset, limit);
		PreparedStatement preparedStatement = connection.prepareStatement(
				sql);
		preparedStatement.setInt(1, id);
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static PublicationSet getPublicationSetByVenueId(int id)
			throws SQLException {
		String sql = selectWhatFromWhere(
				DbContract.PublicationsTable.ALL_COLUMNS,
				DbContract.PublicationsTable.TABLE_NAME,
				DbContract.PublicationsTable.COLUMN_VENUE_ID + " = ?");
		PreparedStatement preparedStatement = connection.prepareStatement(
				sql);
		preparedStatement.setInt(1, id);
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static PublicationSet getPublicationSetBySubjectId(int id)
			throws SQLException {
		return getPublicationSetBySubjectId(id, null, null);
	}

	public static PublicationSet getPublicationSetBySubjectId(int id,
			Integer offset, Integer limit)
					throws SQLException {
		String sql = selectWhatFromWhere(
				new String[] { DbContract.PublicationsTable.ALL_COLUMNS },
				new String[] { DbContract.PublicationsTable.TABLE_NAME,
						DbContract.PublicationsSubjectTable.TABLE_NAME },
				DbContract.PublicationsSubjectTable.FULL_COLUMN_SUBJECT_ID
						+ " = ? AND "
						+ DbContract.PublicationsTable.FULL_COLUMN_ID
						+ " = "
						+ DbContract.PublicationsSubjectTable.FULL_COLUMN_PUBLICATION_ID,
				offset, limit);
		PreparedStatement preparedStatement = connection.prepareStatement(
				sql);
		preparedStatement.setInt(1, id);
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static int addPublication(String doi, String link,
			Date dateCreated,
			Date dateUpdated, Integer venueId, String title, String description)
					throws SQLException {

		String sql = insertIntoValuesQuestionMarks(
				DbContract.PublicationsTable.TABLE_NAME,
				new String[] { DbContract.PublicationsTable.COLUMN_DOI,
						DbContract.PublicationsTable.COLUMN_LINK,
						DbContract.PublicationsTable.COLUMN_DATE_CREATED,
						DbContract.PublicationsTable.COLUMN_DATE_UPDATED,
						DbContract.PublicationsTable.COLUMN_VENUE_ID,
						DbContract.PublicationsTable.COLUMN_TITLE,
						DbContract.PublicationsTable.COLUMN_DESCRIPTION });
		PreparedStatement preparedStatement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, doi);
		preparedStatement.setString(2, link);
		preparedStatement.setDate(3, dateCreated);
		preparedStatement.setDate(4, dateUpdated);
		if (venueId != null) {
			preparedStatement.setInt(5, venueId);
		} else {
			preparedStatement.setNull(5, Types.INTEGER);
		}
		preparedStatement.setString(6, title);
		preparedStatement.setString(7, description);

		preparedStatement.execute();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		if (!rs.next()) {
			throw new SQLException("Can't get keys");
		}
		return rs.getInt(1);
	}

	/**
	 * Adds new <code>Publication</code> and (if needed) new
	 * <code>Authors</code>, <code>Subjects</code>, <code>Venue</code>. All
	 * parameters may be <code>null</code> if db allows column being
	 * <code>null</code>.
	 * 
	 * @param doi
	 * @param link
	 * @param dateCreated
	 * @param dateUpdated
	 * @param venue
	 * @param title
	 * @param description
	 * @param authors
	 *            list of <code>Author</code> names. May be <code>null</code>.
	 * @param subjects
	 *            list of <code>Subject</code> names. May be <code>null</code>.
	 * @return <code>id</code> of new publication.
	 * @throws SQLException
	 */
	public static int makePublication(String doi, String link,
			Date dateCreated,
			Date dateUpdated, String venue, String title, String description,
			List<String> authors, List<String> subjects) throws SQLException {

		try {
			
			connection.setAutoCommit(false);
			
			
			Integer venueId = venue == null ? null : getOrAddVenueId(venue);
			int publicationId = addPublication(doi, link, dateCreated,
					dateUpdated,
					venueId, title, description);

			if (authors != null) {
				for (String a : authors) {
					addPublicationAuthorRelationship(getOrAddAuthorId(a),
							publicationId);
				}
			}
			if (subjects != null) {
				for (String s : subjects) {
					addPublicationSubjectRelationship(getOrAddSubjectId(s),
							publicationId);
				}
			}
			return publicationId;
		} catch(SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
	}

	public static PublicationSet searchPublicationSet(String s)
			throws SQLException {
		return searchPublicationSet(s, null, null);
	}

	public static PublicationSet searchPublicationSet(String s, Integer offset,
			Integer limit)
					throws SQLException {
		/*
		 * SELECT P.publication_id, ts_headline(P.publication_title,q) as
		 * Publication_title, P.datePublished, P.dateUpdated,
		 * ts_headline(P.description,q) as Publication_description,
		 * ts_headline(authors_for_publicationID(P.publication_ID),q) as
		 * Publication_authors, P.venue_id, ts_rank_cd(P.searchable, q) AS rank
		 * FROM publication AS p, to_tsquery('XXX') AS q WHERE P.searchable @@ q
		 * ORDER BY rank DESC LIMIT 50 ;
		 */

		String sql = selectWhatFromWhere(new String[] {
				DbContract.PublicationsTable.COLUMN_ID,
				"ts_headline(" + DbContract.PublicationsTable.COLUMN_TITLE
						+ ",q) AS " + DbContract.PublicationsTable.COLUMN_TITLE,
				"ts_headline(" + DbContract.PublicationsTable.COLUMN_DESCRIPTION
						+ ",q) AS "
						+ DbContract.PublicationsTable.COLUMN_DESCRIPTION,
				DbContract.PublicationsTable.COLUMN_DATE_CREATED,
				DbContract.PublicationsTable.COLUMN_DATE_UPDATED,
				DbContract.PublicationsTable.COLUMN_VENUE_ID,
				DbContract.PublicationsTable.COLUMN_DOI,
				DbContract.PublicationsTable.COLUMN_LINK,
				DbContract.PublicationsTable.COLUMN_NUMBER_OF_AUTHORS,
		}, new String[] { DbContract.PublicationsTable.TABLE_NAME,
				"plainto_tsquery('english', ?) AS q" },
				DbContract.PublicationsTable.COLUMN_SEARCHABLE
						+ " @@ q ORDER BY ts_rank_cd("
						+ DbContract.PublicationsTable.COLUMN_SEARCHABLE
						+ ",q)",
				offset, limit);
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, s);
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static AuthorSet searchAuthorsBySubstring(String s)
			throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.AuthorsTable.TABLE_NAME,
				DbContract.AuthorsTable.COLUMN_NAME + " ILIKE ?");
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, "%" + s + "%");
		return new AuthorSet(preparedStatement.executeQuery());
	}

	public static SubjectSet searchSubjectsBySubstring(String s)
			throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.SubjectsTable.TABLE_NAME,
				DbContract.SubjectsTable.COLUMN_NAME + " ILIKE ?");
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, "%" + s + "%");
		return new SubjectSet(preparedStatement.executeQuery());
	}

	public static PublicationSet searchPublicationsByAuthorSubstring(String s)
			throws SQLException {
		return searchPublicationsByAuthorSubstring(s, null, null);
	}

	public static PublicationSet searchPublicationsByAuthorSubstring(String s,
			Integer offset, Integer limit)
					throws SQLException {
		String sql = selectWhatFromWhere(
				new String[] { DbContract.PublicationsTable.ALL_COLUMNS },
				new String[] { DbContract.AuthorsTable.TABLE_NAME,
						DbContract.PublicationsTable.TABLE_NAME,
						DbContract.PublicationAuthorsTable.TABLE_NAME },
				DbContract.PublicationAuthorsTable.FULL_COLUMN_PUBLICATION_ID
						+ "=" +
						DbContract.PublicationsTable.FULL_COLUMN_ID + " AND " +
						DbContract.PublicationAuthorsTable.FULL_COLUMN_AUTHOR_ID
						+ "=" +
						DbContract.AuthorsTable.FULL_COLUMN_ID + " AND " +
						DbContract.AuthorsTable.FULL_COLUMN_NAME + " ILIKE ?",
				offset, limit);
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, "%" + s + "%");
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static PublicationSet searchPublicationsByVenueSubstring(String s)
			throws SQLException {
		return searchPublicationsByVenueSubstring(s, null, null);
	}

	public static PublicationSet searchPublicationsByVenueSubstring(String s,
			Integer offset, Integer limit)
					throws SQLException {
		String sql = selectWhatFromWhere(
				new String[] { DbContract.PublicationsTable.ALL_COLUMNS },
				new String[] { DbContract.PublicationsTable.TABLE_NAME,
						DbContract.VenuesTable.TABLE_NAME },
				DbContract.PublicationsTable.FULL_COLUMN_VENUE_ID + " = "
						+ DbContract.VenuesTable.FULL_COLUMN_ID + " AND " +
						DbContract.VenuesTable.FULL_COLUMN_NAME + " ILIKE ?",
				offset, limit);
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, "%" + s + "%");
		System.err.println(preparedStatement);
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static SubjectSet getSubjectSet() throws SQLException {
		return getSubjectSet(null, null);
	}

	public static SubjectSet getSubjectSet(Integer offset, Integer limit)
			throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.SubjectsTable.TABLE_NAME, null, offset, limit);
		return new SubjectSet(connection.createStatement().executeQuery(sql));
	}

	public static VenueSet getVenueSet() throws SQLException {
		return getVenueSet(null, null);
	}

	public static VenueSet getVenueSet(Integer offset, Integer limit)
			throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.VenuesTable.TABLE_NAME, null, offset, limit);
		return new VenueSet(connection.createStatement().executeQuery(sql));
	}

	public static AuthorSet getAuthorSet() throws SQLException {
		return getAuthorSet(null, null);
	}

	public static AuthorSet getAuthorSet(Integer offset, Integer limit)
			throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.AuthorsTable.TABLE_NAME, null);
		return new AuthorSet(connection.createStatement().executeQuery(sql));
	}

	public static PublicationSet getRelatedPublicationsByTimeAndSubject(int id)
			throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.PublicationsTable.TABLE_NAME
						+ ", related_by_subject(?)",
				DbContract.PublicationsTable.COLUMN_ID + "=id");
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static PublicationSet getRelatedPublicationsByAuthor(int id)
			throws SQLException {
		String sql = selectWhatFromWhere(null,
				DbContract.PublicationsTable.TABLE_NAME
						+ ", related_by_author(?)",
				DbContract.PublicationsTable.COLUMN_ID
						+ "=id");
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		return new PublicationSet(preparedStatement.executeQuery());
	}

	public static void deletePublicationById(int id) throws SQLException {
		String sql = "DELETE FROM " + DbContract.PublicationsTable.TABLE_NAME
				+ " WHERE " + DbContract.PublicationsTable.COLUMN_ID + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		preparedStatement.execute();
	}

}
