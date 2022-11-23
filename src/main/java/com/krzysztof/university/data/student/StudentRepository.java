package com.krzysztof.university.data.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Override
    @Query("select s from Student s")
    List<Student> findAll();

    List<Student> findAllByNameIgnoreCaseAndSurnameIgnoreCase(String name, String surname);
}
