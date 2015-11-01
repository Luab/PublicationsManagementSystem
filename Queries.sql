----------------------------------------
--АТТЕНШЕН
--Функция authors_for_publicationID(P.publication_ID) была переименована в authors_for_publicationID
---------------------------------------

---------------------------------------
Поиск по тсвектору
---------------------------------------
SELECT DISTINCT P.publication_title, P.description, P.datePublished, authors_for_publicationID(P.publication_ID), V.venue_name FROM publication as P, authorship as AP, author as A, venue as V 
WHERE P.searchable @@ to_tsquery('atom | spin')
AND A.author_id=AP.author_id
AND P.publication_id=AP.publication_ID
AND V.venue_id = P.venue_id
; 

---------------------------------------
Поиск по автору
--------------------------------------

Select * From author as A, publication as P, authorship as AP WHERE 
AP.publication_id=P.publication_id 
AND AP.author_id=A.author_id 
AND A.author_name LIKE '%John%'; 

----------------------------------------
--Поиск релевантных артиклов
----------------------------------------
--По роду деятельнссти
SELECT DISTINCT P.publication_title, authors_for_publicationID(P.publication_ID), P.description, P.datePublished  FROM publication as P, related_by_subject(6002)
WHERE related_by_subject.id = P.publication_id;

--По автору
SELECT DISTINCT P.publication_title, authors_for_publicationID(P.publication_ID), P.description, P.datePublished  FROM publication as P, related_by_author(6050)
WHERE related_by_author.id = P.publication_id;


