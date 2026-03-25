ALTER TABLE books
    ADD CONSTRAINT uq_books_title_author UNIQUE (title, author);