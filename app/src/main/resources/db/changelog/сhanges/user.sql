CREATE TABLE users(
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  roles VARCHAR NOT NULL
);

insert into users(username, password, roles) values
 ('user', 'password', 'USER'),
 ('admin', 'admin', 'ADMIN');