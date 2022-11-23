package com.krzysztof.university.data.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Override
    @Query("select t from Teacher t")
    List<Teacher> findAll();

    List<Teacher> findAllByNameIgnoreCaseAndSurnameIgnoreCase(String name, String surname);

}
