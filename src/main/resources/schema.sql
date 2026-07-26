-- resources/schema.sql
-- sql을 생성하는 명칭 = schema
-- sql은 대소문자 구분 대신에 _ 언더스코어를 사용하여 명칭구분 user_id와 같은 형식으로 많이 사용한다

DROP TABLE IF EXISTS member;

CREATE TABLE member(
    id  VARCHAR(50) primary key ,
    name    VARCHAR(50) not null ,
    password VARCHAR(200) not null,
    email VARCHAR(100) NOT NULL


)