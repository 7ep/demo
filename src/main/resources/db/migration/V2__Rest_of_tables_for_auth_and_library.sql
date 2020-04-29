-- eh, instead of one table for both librarians and borrowers,
-- we'll create a borrower table for borrowers and a
-- separate auth.USER table for librarians
drop table library.person;

-- here's our borrower table
create table library.BORROWER (
    id serial PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- books, obviously - it's a library
create table library.BOOK (
    id serial PRIMARY KEY,
    title VARCHAR(100) NOT NULL
);

-- this tracks which books have been lent to borrowers
create table library.LOAN (
    id serial PRIMARY KEY,
    book int NOT NULL REFERENCES library.BOOK (id) ON DELETE CASCADE,
    borrower int NOT NULL REFERENCES library.BORROWER (id) ON DELETE CASCADE,
    borrow_date date NOT NULL
);

-- this holds the list of librarians, mainly their name and their
-- password hash (used during authentication)
create table auth.USER (
    id serial PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password_hash VARCHAR(100)
);
