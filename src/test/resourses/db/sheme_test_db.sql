CREATE TABLE IF NOT EXISTS users
(
email text NOT NULL,
login text NOT NULL,
password text NOT NULL,
score integer NOT NULL DEFAULT 0,
CONSTRAINT unique_login PRIMARY KEY (login),
CONSTRAINT unique_email UNIQUE (email)
);

