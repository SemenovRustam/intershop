CREATE TABLE items(
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      img_path VARCHAR(255),
                      count INT NOT NULL,
                      price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE orders(
    id BIGSERIAL NOT NULL,
    total_sum BIGINT NOT NULL,
    CONSTRAINT orders_pk PRIMARY KEY (id)
);

CREATE TABLE orders_items(
    order_id BIGINT NOT NULL,
    item_id  BIGINT NOT NULL,
    CONSTRAINT orders_items_pk PRIMARY KEY (order_id, item_id),
    CONSTRAINT fk_orders_items_to_orders FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_orders_items_to_items FOREIGN KEY (item_id) REFERENCES items (id)
);

insert into items(title, description, img_path, count, price)
values ('кепка', 'защити голову в холоде :)', 'images/kepka.jpg', 0, 1500),
       ('шапка', 'защити голову в тепле :)', 'images/shapka.jpg', 0, 2000),
       ('панама', 'защити голову в холоде стильно :)', 'images/panama.jpg', 0, 1200);