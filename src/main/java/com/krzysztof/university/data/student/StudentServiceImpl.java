package com.krzysztof.university.data.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    public Optional<Student> findById(int id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> readStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void delete(Student student) {
        studentRepository.delete(student);
    }

    @Override
    public boolean existsById(int id) {
        return studentRepository.existsById(id);
    }

    @Override
    public Page<Student> paginationAndSortingStudents(Integer pageNumber, Integer pageSize, String sortProperty) {
        Pageable pageable = null;

        if (sortProperty != null) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sortProperty);
        }else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id");
        }
        return studentRepository.findAll(pageable);
    }

    @Override
    public List<Student> findAllStudentByNameAndSurname(String student) {
        String name;
        String surname;

        try {
            String[] splitStudent = student.split(" ");
            name = splitStudent[0];
            surname = splitStudent[1];
            return studentRepository.findAllByNameIgnoreCaseAndSurnameIgnoreCase(name, surname);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
