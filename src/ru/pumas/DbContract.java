package ru.pumas;

public class DbContract {

	private DbContract() {
	}

	static final class AuthorsTable {
		static final String TABLE_NAME = "author";

		public static final String COLUMN_ID = "author_id";
		public static final String COLUMN_NAME = "author_name";
		public static final String COLUMN_NUMBER_OF_PUBLICATIONS = "amount_of_publications";

		public static final String FULL_COLUMN_ID = TABLE_NAME + "."
				+ COLUMN_ID;
		public static final String FULL_COLUMN_NAME = TABLE_NAME + "."
				+ COLUMN_NAME;
		public static final String FULL_NUMBER_OF_PUBLICATIONS = TABLE_NAME
				+ "." + COLUMN_NUMBER_OF_PUBLICATIONS;
	}

	static final class PublicationAuthorsTable {
		static final String TABLE_NAME = "authorship";

		public static final String COLUMN_AUTHOR_ID = "author_id";
		public static final String COLUMN_PUBLICATION_ID = "publication_id";

		public static final String FULL_COLUMN_AUTHOR_ID = TABLE_NAME + "."
				+ COLUMN_AUTHOR_ID;
		public static final String FULL_COLUMN_PUBLICATION_ID = TABLE_NAME + "."
				+ COLUMN_PUBLICATION_ID;
	}

	static final class PublicationsTable {
		static final String TABLE_NAME = "publication";

		public static final String COLUMN_ID = "publication_id";
		public static final String FULL_COLUMN_ID = TABLE_NAME + "."
				+ COLUMN_ID;
		public static final String COLUMN_DOI = "doi";
		public static final String FULL_COLUMN_DOI = TABLE_NAME + "."
				+ COLUMN_DOI;
		public static final String COLUMN_LINK = "link";
		public static final String FULL_COLUMN_LINK = TABLE_NAME + "."
				+ COLUMN_LINK;
		public static final String COLUMN_DESCRIPTION = "description";
		public static final String FULL_COLUMN_DESCRIPTION = TABLE_NAME + "."
				+ COLUMN_DESCRIPTION;
		public static final String COLUMN_DATE_CREATED = "datePublished";
		public static final String FULL_COLUMN_DATE_CREATED = TABLE_NAME + "."
				+ COLUMN_DATE_CREATED;
		public static final String COLUMN_DATE_UPDATED = "dateUpdated";
		public static final String FULL_COLUMN_DATE_UPDATED = TABLE_NAME + "."
				+ COLUMN_DATE_UPDATED;
		public static final String COLUMN_VENUE_ID = "venue_ID";
		public static final String FULL_COLUMN_VENUE_ID = TABLE_NAME + "."
				+ COLUMN_VENUE_ID;
		public static final String COLUMN_TITLE = "publication_title";
		public static final String FULL_COLUMN_TITLE = TABLE_NAME + "."
				+ COLUMN_TITLE;
		public static final String COLUMN_NUMBER_OF_AUTHORS = "number_of_authors";
		public static final String FULL_COLUMN_NUMBER_OF_AUTHORS = TABLE_NAME
				+ "." + COLUMN_NUMBER_OF_AUTHORS;
		public static final String COLUMN_SEARCHABLE = "searchable";
		public static final String FULL_COLUMN_SEARCHABLE = TABLE_NAME + "."
				+ COLUMN_SEARCHABLE;

		public static final String ALL_COLUMNS = DbHelper
				.commaSeparated(new String[] { FULL_COLUMN_ID, FULL_COLUMN_DOI,
						FULL_COLUMN_LINK, FULL_COLUMN_DESCRIPTION,
						FULL_COLUMN_DATE_CREATED,
						FULL_COLUMN_DATE_UPDATED,
						FULL_COLUMN_VENUE_ID, FULL_COLUMN_TITLE,
						FULL_COLUMN_NUMBER_OF_AUTHORS
				}, null);
	}

	static final class PublicationsSubjectTable {
		static final String TABLE_NAME = "written_on";

		public static final String COLUMN_PUBLICATION_ID = "publication_id";
		public static final String FULL_COLUMN_PUBLICATION_ID = TABLE_NAME + "."
				+ COLUMN_PUBLICATION_ID;
		public static final String COLUMN_SUBJECT_ID = "subject_id";
		public static final String FULL_COLUMN_SUBJECT_ID = TABLE_NAME + "."
				+ COLUMN_SUBJECT_ID;
	}

	static final class SubjectsTable {
		public static final String TABLE_NAME = "subject";

		public static final String COLUMN_ID = "subject_id";
		public static final String COLUMN_NAME = "subject_name";
	}

	static final class VenuesTable {
		public static final String TABLE_NAME = "venue";

		public static final String COLUMN_ID = "venue_id";
		public static final String COLUMN_NAME = "venue_name";
	}

	static final class KeywordsTable {
		public static final String TABLE_NAME = "keyword";

		public static final String COLUMN_ID = "keyword_id";
		public static final String COLUMN_KEYWORD = "keyword";
	}

	static final class KeywordInTable {
		public static final String TABLE_NAME = "keyword_in";

		public static final String COLUMN_KEYWORD_ID = "keyword_id";
		public static final String COLUMN_PUBLICATION_IN = "publication_id";
	}

}
