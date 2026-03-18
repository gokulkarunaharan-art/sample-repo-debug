package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.DTO.StudentBookEntryDTO;
import com.gokul.librarymanagement.mapper.StudentBookEntryMapper;
import com.gokul.librarymanagement.repository.StudentBookEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentBookEntryService {

    private final StudentBookEntryRepository studentBookEntryRepository;
    private final StudentBookEntryMapper studentBookEntryMapper;

    public List<StudentBookEntryDTO> getAllRecords(){
        return studentBookEntryRepository.findAll().stream().map(
                studentBookEntry->{
                    return studentBookEntryMapper.toDTO(studentBookEntry)
                }
        )
        ).toList();
    }
}
