package ru.pumas;

public class PublicationVenueRelationship {

	int id;
	int publicationId;
	int venueId;

	public PublicationVenueRelationship(int id, int publicationId, int venueId) {
		this.id = id;
		this.publicationId = publicationId;
		this.venueId = venueId;
	}
}
