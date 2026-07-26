-- resources/schema.sql
-- sql을 생성하는 명칭 = schema
-- sql은 대소문자 구분 대신에 _ 언더스코어를 사용하여 명칭구분 user_id와 같은 형식으로 많이 사용한다

DROP TABLE IF EXISTS member;

CREATE TABLE member(
    id  VARCHAR(50) primary key ,
    name    VARCHAR(50) NOT NULL ,
    password VARCHAR(200) NOT NULL,
    email VARCHAR(100) NOT NULL,
    profileImage TEXT

-- 이미지 파일 자체를 DB에 저장하는 것이 아니라, 저장된 파일명만 문자열로 저장하는 방식이다.
-- TEXT = 글자수 제한이 없는 문자열 타입으로, 파일명이 얼마나 길어지든 걱정할 필요가 없다.

)