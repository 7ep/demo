drop table library.person;

create table library.BORROWER (
    id serial PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

create table library.BOOK (
    id serial PRIMARY KEY,
    title VARCHAR(100) NOT NULL
);

create table library.LOAN (
    id serial PRIMARY KEY,
    book int NOT NULL REFERENCES library.BOOK (id) ON DELETE CASCADE,
    borrower int NOT NULL REFERENCES library.BORROWER (id) ON DELETE CASCADE,
    borrow_date date NOT NULL
);

create table auth.USER (
    id serial PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password_hash VARCHAR(100)
);
