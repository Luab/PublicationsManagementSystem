----------------------------------------
АТТЕНШЕН
Функция vector_text_for_publication(P.publication_ID) была переименована в authors_for_publicationID
---------------------------------------

---------------------------------------
Поиск по тсвектору
---------------------------------------
SELECT DISTINCT P.publication_title, P.description, P.datePublished, vector_text_for_publication(P.publication_ID) FROM publication as P, authorship as AP, author as A 
WHERE P.searchable @@ to_tsquery('atom & vector')
AND A.author_id=AP.author_id
AND P.publication_id=AP.publication_ID; 

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
SELECT DISTINCT P.publication_title, vector_text_for_publication(P.publication_ID), P.description, P.datePublished  FROM publication as P, related_by_subject(6002)
WHERE related_by_subject.id = P.publication_id;

--По автору
SELECT DISTINCT P.publication_title, vector_text_for_publication(P.publication_ID), P.description, P.datePublished  FROM publication as P, related_by_author(6050)
WHERE related_by_author.id = P.publication_id;


