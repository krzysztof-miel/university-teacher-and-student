package com.krzysztof.university.data.teacher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    Optional<Teacher> findById(int id);

    Teacher save(Teacher teacher);

    List<Teacher> readTeachers();

    Page<Teacher> readTeachers(Pageable page);

    void delete(Teacher teacher);

    boolean existsById(int id);

    Page<Teacher> paginationAndSortingTeachers(Integer pageNumber, Integer pageSize, String sortProperty);

    List<Teacher> findAllTeacherByNameAndSurname(String teacher);

}
