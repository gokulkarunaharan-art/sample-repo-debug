CREATE TABLE books (
                       id UUID NOT NULL,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       total_copies INT NOT NULL DEFAULT 0,
                       available_copies INT NOT NULL DEFAULT 0,

                       CONSTRAINT pk_books PRIMARY KEY (id)
);

CREATE TABLE students (
                          id UUID NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          phone_number VARCHAR(20),

                          CONSTRAINT pk_students PRIMARY KEY (id)
);

CREATE TABLE student_book_entry (
                                    id UUID NOT NULL,
                                    book_id UUID,
                                    book_title VARCHAR(255),
                                    student_id UUID,
                                    student_name VARCHAR(255),
                                    borrowed_at TIMESTAMP,
                                    returned_at TIMESTAMP,
                                    status VARCHAR(50),

                                    CONSTRAINT pk_student_book_entry PRIMARY KEY (id),
                                    CONSTRAINT fk_student_book_entry_book FOREIGN KEY (book_id) REFERENCES books(id),
                                    CONSTRAINT fk_student_book_entry_student FOREIGN KEY (student_id) REFERENCES students(id)
);