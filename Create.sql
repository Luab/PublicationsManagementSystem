------------------------
--CREATE TABLE QUERIES
------------------------

CREATE TABLE author (
  author_name VARCHAR(255) NOT NULL UNIQUE,
 amount_of_publications INTEGER DEFAULT 0,
  author_id   SERIAL PRIMARY KEY
);

CREATE TABLE subject (
  subject_id   SERIAL PRIMARY KEY,
  subject_name TEXT NOT NULL UNIQUE
);

CREATE TABLE venue (
  venue_id   SERIAL PRIMARY KEY,
  venue_name TEXT NOT NULL UNIQUE
);

CREATE TABLE publication (
  publication_id    SERIAL PRIMARY KEY,
  datePublished     DATE NOT NULL DEFAULT current_date,
  dateUpdated       DATE,
  publication_title TEXT NOT NULL,
  doi               VARCHAR(200)  DEFAULT NULL,
  link              VARCHAR(255)  DEFAULT NULL,
  description       TEXT,
number_of_authors INTEGER DEFAULT 0,
  venue_id          INTEGER REFERENCES venue ON UPDATE CASCADE ON DELETE SET NULL,
  searchable tsvector 
    CONSTRAINT UpdatedAfterPublished CHECK (dateUpdated >= publication.datePublished)
);

CREATE TABLE authorship (
  author_id      INTEGER REFERENCES author ON UPDATE CASCADE ON DELETE CASCADE,
  publication_id INTEGER REFERENCES publication ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE written_on (
  subject_id     INTEGER REFERENCES subject ON UPDATE CASCADE ON DELETE CASCADE,
  publication_id INTEGER REFERENCES publication ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE keyword (
  keyword_id     SERIAL PRIMARY KEY ,
  keyword        TEXT 
);

CREATE TABLE keyword_in (
  keyword_id     INTEGER REFERENCES keyword ON UPDATE CASCADE ON DELETE CASCADE,
  publication_id INTEGER REFERENCES publication ON UPDATE CASCADE ON DELETE CASCADE
);

-----------------------
--TRIGGERS
-----------------------
CREATE TRIGGER make_ts
    AFTER INSERT OR DELETE ON authorship
    FOR EACH ROW
    EXECUTE PROCEDURE make_searchable_text(); 

CREATE TRIGGER make_ts2
    AFTER INSERT OR DELETE ON written_on
    FOR EACH ROW
    EXECUTE PROCEDURE make_searchable_text();

CREATE TRIGGER decrement_on_delete
    AFTER DELETE ON authorship
    FOR EACH ROW
    EXECUTE PROCEDURE decrement();

CREATE TRIGGER increment_on_add
    AFTER INSERT ON authorship
    FOR EACH ROW
    EXECUTE PROCEDURE increment();

CREATE TRIGGER dateUpdated_default
  BEFORE INSERT ON publication
  FOR EACH ROW
  WHEN (NEW.dateUpdated IS NULL AND NEW.datePublished IS NOT NULL)
  EXECUTE PROCEDURE dateUpdatedbydefault();

------------------------
--Functions for retrieving data and maintaining database
------------------------

----
--1. Function which makes dateUpdated:=dateCreated if dateUpdated == null
---

CREATE OR REPLACE FUNCTION dateUpdatedbydefault()
  RETURNS TRIGGER AS
  $func$
  BEGIN
    NEW.dateUpdated :=NEW.datePublished;
    RETURN NEW;
  END;
  $func$ LANGUAGE plpgsql;

----
--2.Add +1/+1 to amount_of_publications/number_of_authors if trigger increment_on_add is invoked
---

CREATE OR REPLACE FUNCTION increment()
  RETURNS trigger   AS $$
BEGIN
UPDATE publication 
   SET number_of_authors = number_of_authors + 1
WHERE publication_id=new.publication_id;

UPDATE author 
   SET amount_of_publications = amount_of_publications + 1
WHERE author_id=new.author_id;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

----
--3.Add -1/-1 to amount_of_publications/number_of_authors if trigger decrement_on_delete is invoked
---

 CREATE OR REPLACE FUNCTION decrement()
 RETURNS TRIGGER  AS $$
BEGIN
UPDATE publication 
   SET number_of_authors = number_of_authors - 1
WHERE publication_id=old.publication_id;

UPDATE author 
   SET amount_of_publications = amount_of_publications - 1
WHERE author_id=old.author_id;
 RETURN NULL;
END;
$$ LANGUAGE plpgsql;

----
--4. Functions recalculate the tsvector to keep data consistency if triggers make_ts or make_ts2 invoked
---

CREATE OR REPLACE FUNCTION make_searchable_text()
RETURNS TRIGGER AS $$
BEGIN
IF TG_OP='INSERT' THEN
RETURN NEW;
END IF;

IF EXISTS (
SELECT 1
FROM publication p
WHERE p.publication_id = OLD.publication_id) THEN

UPDATE publication
SET searchable = to_tsvector(
'english',
coalesce(authors_for_publicationID (publication_id),' ')
||' '||coalesce(publication_title,' ')
||' '||coalesce(description,' ')
||' '||coalesce(subjects_for_publicationID (publication_id),' ')
||' '||coalesce(venue_for_publicationID (publication_id),' ')
)
WHERE publication_id=new.publication_id;

END IF;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;

----
--5. Retrieve STRING with all subjects for this publication_ID
----  

CREATE OR REPLACE FUNCTION subjects_for_publicationID(id INT)
RETURNS TEXT AS $$
DECLARE
s TEXT;
BEGIN
SELECT string_agg(subject_name, ' ') FROM subject, written_on INTO s WHERE
        publication_id = id AND subject.subject_id = written_on.subject_id;
RETURN s;
END
$$ LANGUAGE plpgsql;

----
--6. Retrieve STRING with all authors for this publication_ID
----

CREATE OR REPLACE FUNCTION authors_for_publicationID(id INT)
RETURNS TEXT AS $$
DECLARE
s TEXT;
BEGIN
SELECT string_agg(author_name, ' ') FROM author, authorship INTO s WHERE
        publication_id = id AND author.author_id = authorship.author_id;
RETURN s;
END
$$ LANGUAGE plpgsql;

----
--5. Retrieve table with all author_id s for this publication_ID
----

CREATE OR REPLACE FUNCTION authors_for(pub_id INT)
  RETURNS TABLE(id INTEGER) AS $$
DECLARE

BEGIN
  RETURN QUERY SELECT AP.author_id
               FROM authorship AS AP
               WHERE
                AP.publication_id=pub_id;

END
$$ LANGUAGE plpgsql;

----
--5. Retrieve STRING with venue for this publication_ID
----

CREATE OR REPLACE FUNCTION venue_for_publicationID(id INT)
RETURNS TEXT AS $$
DECLARE
s TEXT;
BEGIN
SELECT string_agg(venue_name, ' ') FROM publication, venue INTO s WHERE
        publication_id = id AND venue.venue_id = publication.venue_id;
RETURN s;
END
$$ LANGUAGE plpgsql;


----------------------------------
--RELATED ARTICLES FUNCTIONS
----------------------------------
----
--1. Related articles by looking for publications written in same period at same subject
----
CREATE OR REPLACE FUNCTION related_by_subject(pub_id INT)
  RETURNS TABLE(id INTEGER) AS $$
DECLARE

BEGIN
  RETURN QUERY SELECT P.publication_id
               FROM publication AS P, written_on AS PS
               WHERE
                 PS.subject_id = (SELECT subject_id
                                  FROM written_on
                                  WHERE publication_id = pub_id
                                  LIMIT 1)
                 AND
                 PS.publication_id != pub_id
                 AND
                 PS.publication_id = P.publication_ID
               ORDER BY @((SELECT datePublished
                           FROM publication
                           WHERE publication_id = pub_id
                           LIMIT 1) - datePublished) ASC
               LIMIT 10;

END
$$ LANGUAGE plpgsql;

----
--2. Related articles by looking for articles among all authors of this publication
----
CREATE OR REPLACE FUNCTION related_by_author(pub_id INT)
  RETURNS TABLE(id INTEGER) AS $$
DECLARE

BEGIN
  RETURN QUERY SELECT AP.publication_id
               FROM authorship AS AP, authors_for(pub_id)
               WHERE
                  AP.author_id=authors_for.id
               AND
      AP.author_id!=pub_id
         
               LIMIT 10;

END
$$ LANGUAGE plpgsql;

-----------------------------------------
--INDEXES
-----------------------------------------

----
--B tree indexes
----

CREATE UNIQUE INDEX publicationID
ON publication (publication_id);

CREATE UNIQUE INDEX authorID
ON author (author_id);

CREATE UNIQUE INDEX venueID
ON venue (venue_id);

CREATE UNIQUE INDEX subjectID
ON subject (subject_id);

CREATE UNIQUE INDEX keyword_ID
ON keyword (keyword_id);

CREATE  INDEX writtenP_ID_lookup
ON written_on (publication_id);
CREATE  INDEX writtenS_ID_lookup
ON written_on (subject_id);

CREATE  INDEX authorshipP_ID_lookup
ON authorship (publication_id);
CREATE  INDEX authorshipA_ID_lookup
ON authorship (author_id);

CREATE  INDEX venueP_id_lookup
ON publication (venue_id);

----
--GIN index for full text search
----

CREATE INDEX gin_search on PUBLICATION USING GIN(SEARCHABLE);

