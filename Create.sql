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
  venue_id          INTEGER REFERENCES venue ON UPDATE CASCADE ON DELETE SET NULL
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
  keyword_id     INTEGER ,
  keyword        TEXT 
);

CREATE TABLE keyword_in (
  keyword_id     INTEGER REFERENCES keyword ON UPDATE CASCADE ON DELETE CASCADE,
  publication_id INTEGER REFERENCES publication ON UPDATE CASCADE ON DELETE CASCADE
);



CREATE OR REPLACE FUNCTION dateUpdatedbydefault()
  RETURNS TRIGGER AS
  $func$
  BEGIN
    NEW.dateUpdated :=NEW.datePublished;
  END;
  $func$ LANGUAGE plpgsql;

CREATE TRIGGER dateUpdated_default
BEFORE INSERT ON publication
FOR EACH ROW
WHEN (NEW.dateUpdated IS NULL AND NEW.datePublished IS NOT NULL)
EXECUTE PROCEDURE dateUpdatedbydefault();


CREATE OR REPLACE FUNCTION increment()
  RETURNS trigger   AS $$
BEGIN
UPDATE publication 
   SET number_of_authors = number_of_authors + 1
WHERE publication_id=new.publication_id;

UPDATE author 
   SET amount_of_publications = amount_of_publications + 1
WHERE author_id=new.author_id;
 RAISE NOTICE 'DONE';
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER increment_on_add
    AFTER INSERT ON authorship
    FOR EACH ROW
    EXECUTE PROCEDURE increment();

 CREATE OR REPLACE FUNCTION decrement()
 RETURNS TRIGGER  AS $$
BEGIN
UPDATE publication 
   SET number_of_authors = number_of_authors - 1
WHERE publication_id=old.publication_id;

UPDATE author 
   SET amount_of_publications = amount_of_publications - 1
WHERE author_id=old.author_id;
 RAISE NOTICE 'DONE';
 RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER decrement_on_delete
    AFTER DELETE ON authorship
    FOR EACH ROW
    EXECUTE PROCEDURE decrement();

 
  CREATE OR REPLACE FUNCTION make_searchable_text()
 RETURNS TRIGGER  AS $$
BEGIN
UPDATE publication 
   SET searchable =  to_tsvector('english', vector_text_for_publication(publication_id)||' '||coalesce(publication_title,'')||' '||coalesce(description,' '))
WHERE publication_id=new.publication_id;

 RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER make_ts
    AFTER INSERT OR DELETE ON authorship
    FOR EACH ROW
    EXECUTE PROCEDURE make_searchable_text();
    
 