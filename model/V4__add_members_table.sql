CREATE TABLE members
(
    id        UUID NOT NULL,
    user_name VARCHAR(255),
    password  VARCHAR(255),
    role      VARCHAR(255),
    CONSTRAINT pk_members PRIMARY KEY (id)
);