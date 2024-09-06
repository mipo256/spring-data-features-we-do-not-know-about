SELECT SETSEED(1); -- Here it is just for reproducibility purposes

SELECT
  generate_series(1, 1000000) AS id,
  timestamp '2000-01-10 20:00:00' + random() * (timestamp '2024-01-20 20:00:00' - timestamp '2000-01-10 10:00:00') AS created_at,
  REPEAT(CHR(65 + ROUND(random() * 24)::INT), ROUND(random() * 10)::INT) AS name -- random amount from 0 to 10 of random A-Z characters
INTO scrolling_entity;

CREATE INDEX IF NOT EXISTS se_created_at_idx ON scrolling_entity(created_at);

EXPLAIN SELECT
  se.id,
  se.name,
  se.created_at
FROM scrolling_entity se
WHERE se.name LIKE '%' || 'AA' || '%'
ORDER BY :param
OFFSET :pageNumber * pageSize ROWS
FETCH FIRST :pageSize ROWS ONLY;
