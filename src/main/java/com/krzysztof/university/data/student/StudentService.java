package com.krzysztof.university.data.student;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    Optional<Student> findById(int id);

    Student save(Student student);

    List<Student> readStudents();

    void delete(Student student);

    boolean existsById(int id);

    Page<Student> paginationAndSortingStudents(Integer pageNumber, Integer pageSize, String sortProperty);

    List<Student> findAllStudentByNameAndSurname(String student);
}
