package ru.pumas;

public class PublicationAuthorRelationship {

	int id;
	int authorId;
	int publicationId;

	public PublicationAuthorRelationship(int id, int publicationId,
			int authorId) {
		this.id = id;
		this.authorId = authorId;
		this.publicationId = publicationId;
	}

}
