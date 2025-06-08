CREATE TABLE item (
      id BIGSERIAL PRIMARY KEY,
      title VARCHAR(255) NOT NULL,
      description TEXT,
      img_path VARCHAR(255),
      count INT NOT NULL,
      price DECIMAL(10, 2) NOT NULL
);
