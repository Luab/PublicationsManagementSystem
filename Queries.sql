SELECT * FROM publication WHERE searchable @@ to_tsquery('atom & vector'); --Просто поиск по тсвектору

Select * From author as A, publication as P, authorship as AP WHERE 
AP.publication_id=P.publication_id 
AND AP.author_id=A.author_id 
AND A.author_name LIKE '%John%'; -- поиск по автору Джону