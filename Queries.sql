----------------------------------------
-- Sample queries
---------------------------------------

---------------------------------------
--SmartSearch query with ranking by matching (document_text@@search_text)
---------------------------------------
SELECT  
	P.publication_id,
	ts_headline(P.publication_title,q) 						   as Publication_title,
	P.datePublished,
	P.dateUpdated,
	ts_headline(P.description,q) 							   as Publication_description,
	ts_headline(authors_for_publicationID(P.publication_ID),q) as Publication_authors,
	P.venue_id,
	ts_rank_cd(P.searchable, q) 							   as rank
FROM 
	publication 		 AS p,
	plainto_tsquery('?') AS q
WHERE
	P.searchable @@ q
ORDER BY rank DESC
;

---------------------------------------
--Author Search query
--------------------------------------

Select * From 
	author as A, 
	publication as P, 
	authorship as AP 
WHERE 
	AP.publication_id=P.publication_id 
	AND AP.author_id=A.author_id 
	AND A.author_name LIKE '%?%'
LIMIT 1000
; 
---------------------------------------
--Subject Search query
--------------------------------------

SELECT * FROM 
	Subject as S, 
	publication as P, 
	written_on as SP 
WHERE 
	SP.publication_id=P.publication_id 
	AND SP.subject_id=S.subject_id 
	AND S.subject_name LIKE '%?%'
LIMIT 1000
; 
----------------------------------------
--Related publications query
----------------------------------------
--By time and subject
SELECT DISTINCT 
	P.publication_title, 
	authors_for_publicationID(P.publication_ID), 
	P.description, 
	P.datePublished  
FROM
	publication as P,
	related_by_subject(?)
WHERE 
	related_by_subject.id = P.publication_id;

--By authors
SELECT DISTINCT 
	P.publication_title, 
	authors_for_publicationID(P.publication_ID), 
	P.description, 
	P.datePublished  
FROM 
	publication as P, 
	related_by_author(?)
WHERE 
related_by_author.id = P.publication_id
;


