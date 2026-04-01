package com.gokul.librarymanagement.bootstrapData;

import com.gokul.librarymanagement.model.Book;
import com.gokul.librarymanagement.model.Member;
import com.gokul.librarymanagement.model.Role;
import com.gokul.librarymanagement.model.Student;
import com.gokul.librarymanagement.repository.BookRepository;
import com.gokul.librarymanagement.repository.MemberRepository;
import com.gokul.librarymanagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataPopulator implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final MemberRepository memberRepository;
    private  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        populateBookRepository();
        populateStudentRepository();
        createDefaultMembers();
    }

    private void createDefaultMembers() {
        if(!memberRepository.findByUserName("gokul").isPresent()){
            Member gokul = Member.builder()
                    .userName("gokul")
                    .password(bCryptPasswordEncoder.encode("gokul13122004"))
                    .role(Role.LIBRARIAN)
                    .build();
            memberRepository.save(gokul);
        }
        if(!memberRepository.findByUserName("kiran").isPresent()){
            Member kiran = Member.builder()
                    .userName("kiran")
                    .password(bCryptPasswordEncoder.encode("kiran@123"))
                    .role(Role.MEMBER)
                    .build();
            memberRepository.save(kiran);
        }
    }

    public void populateBookRepository(){
        if (bookRepository.count() == 0) {
            bookRepository.save(Book.builder()
                    .title("Clean Code")
                    .author("Robert C. Martin")
                    .totalCopies(3)
                    .availableCopies(3)
                    .build());

            bookRepository.save(Book.builder()
                    .title("Spring Boot in Action")
                    .author("Craig Walls")
                    .totalCopies(2)
                    .availableCopies(2)
                    .build());

            bookRepository.save(Book.builder()
                    .title("Effective Java")
                    .author("Joshua Bloch")
                    .totalCopies(4)
                    .availableCopies(4)
                    .build());
        }

    }

    public void populateStudentRepository(){
        if (studentRepository.count() == 0) {
            studentRepository.save(Student.builder()
                    .name("Alice")
                    .email("alice@example.com")
                    .phoneNumber("9876543210")
                    .build());

            studentRepository.save(Student.builder()
                    .name("Bob")
                    .email("bob@example.com")
                    .phoneNumber("9123456780")
                    .build());

            studentRepository.save(Student.builder()
                    .name("Charlie")
                    .email("charlie@example.com")
                    .phoneNumber("9988776655")
                    .build());
        }
    }
}
