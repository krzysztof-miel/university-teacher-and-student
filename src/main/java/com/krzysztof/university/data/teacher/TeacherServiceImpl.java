package com.krzysztof.university.data.teacher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService{

    private TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Optional<Teacher> findById(int id) {
        return teacherRepository.findById(id);
    }

    @Override
    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public List<Teacher> readTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Page<Teacher> readTeachers(Pageable page) {
        return teacherRepository.findAll(page);
    }

    @Override
    public void delete(Teacher teacher) {
        teacherRepository.delete(teacher);
    }

    @Override
    public boolean existsById(int id) {
        return teacherRepository.existsById(id);
    }

    @Override
    public Page<Teacher> paginationAndSortingTeachers(Integer pageNumber, Integer pageSize, String sortProperty) {
        Pageable pageable = null;

        if (sortProperty != null) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sortProperty);
        }else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id");
        }
        return teacherRepository.findAll(pageable);
    }

    @Override
    public List<Teacher> findAllTeacherByNameAndSurname(String teacher) {
        String name;
        String surname;

        try {
            String[] splitTeacher = teacher.split(" ");
            name = splitTeacher[0];
            surname = splitTeacher[1];
            return teacherRepository.findAllByNameIgnoreCaseAndSurnameIgnoreCase(name, surname);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
