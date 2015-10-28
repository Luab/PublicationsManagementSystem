package ru.pumas;

public class PublicationSubjectRelationship {

	int id;
	int publicationId;
	int subjectId;

	public PublicationSubjectRelationship(int id, int publicationId, int subjectId) {
		this.id = id;
		this.publicationId = publicationId;
		this.subjectId = subjectId;
	}
}
