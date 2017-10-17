CREATE TABLE IF NOT EXISTS users
(
email text NOT NULL,
login text NOT NULL,
password text NOT NULL,
score integer NOT NULL DEFAULT 0,
CONSTRAINT unique_login PRIMARY KEY (login),
CONSTRAINT unique_email UNIQUE (email)
);

INSERT INTO users (email, login, password, score)
values ('Odin@mail.ru', 'Odin', '02103452', 0),
  ('yaho@bb.com', 'yaho', 'qwerty', 0),
  ('Bred@bb.com', 'Bred', '13213', 0);
