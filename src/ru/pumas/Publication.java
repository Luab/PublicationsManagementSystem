package ru.pumas;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

public class Publication {

	int id;
	String doi;
	String link;
	Date dateCreated;
	Date dateUpdated;
	Venue venue;
	String title;
	String description;
	int numberOfAuthors;

	public JSONObject toJSONObject() throws JSONException {
		JSONObject ret = new JSONObject();
		ret.put("id", id);
		ret.put("doi", doi);
		ret.put("link", link);
		ret.put("date_created", dateCreated.getTime());
		ret.put("date_updated", dateUpdated.getTime());
		if (venue != null) {
			ret.put("venue", venue.toJSONObject());
		}
		ret.put("title", title);
		ret.put("description", description);
		ret.put("number_of_authors", numberOfAuthors);
		return ret;
	}

	public Publication() {
	}

	public AuthorSet getAuthorSet() throws SQLException {
		return DbHelper.getAuthorsByPublicationIdSet(id);
	}

	public SubjectSet getSubjectSet() throws SQLException {
		return DbHelper.getSubjectsByPublicationIdSet(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumberOfAuthors() {
		return numberOfAuthors;
	}

	public void setNumberOfAuthors(int numberOfAuthors) {
		this.numberOfAuthors = numberOfAuthors;
	}

	public String getDoi() {
		return doi;
	}

	public String getLink() {
		return link;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public Venue getVenue() {
		return venue;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public static Publication from(ResultSet rs) throws SQLException {
		Publication ret = new Publication();
		ret.id = rs.getInt(DbContract.PublicationsTable.COLUMN_ID);
		ret.doi = rs.getString(DbContract.PublicationsTable.COLUMN_DOI);
		ret.link = rs.getString(DbContract.PublicationsTable.COLUMN_LINK);
		ret.dateCreated = rs
				.getDate(DbContract.PublicationsTable.COLUMN_DATE_CREATED);
		ret.dateUpdated = rs
				.getDate(DbContract.PublicationsTable.COLUMN_DATE_UPDATED);
		ret.venue = DbHelper.getVenueById(
				rs.getInt(DbContract.PublicationsTable.COLUMN_VENUE_ID));
		ret.title = rs.getString(DbContract.PublicationsTable.COLUMN_TITLE);
		ret.description = rs
				.getString(DbContract.PublicationsTable.COLUMN_DESCRIPTION);
		return ret;
	}

	public void setNumberOfPublications(int numberOfPublications) {
		this.numberOfAuthors = numberOfPublications;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

}
