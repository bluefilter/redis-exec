-- users 테이블 생성
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

-- 샘플 데이터 추가
INSERT INTO users (username, password, role)
VALUES
    ('admin', '{bcrypt}$2a$10$XXXXXXXXXXXXXXXXXXXXXXX', 'ADMIN'),
    ('user', '{bcrypt}$2a$10$XXXXXXXXXXXXXXXXXXXXXXX', 'USER');


select * from users;