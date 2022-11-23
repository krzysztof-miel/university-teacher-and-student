package com.krzysztof.university.controller;

import com.krzysztof.university.data.student.Student;
import com.krzysztof.university.data.student.StudentService;
import com.krzysztof.university.data.teacher.Teacher;
import com.krzysztof.university.data.teacher.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/students")
public class StudentController {

    private StudentService studentService;
    private TeacherService teacherService;

    public StudentController(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student toCreate) {
        Student student = studentService.save(toCreate);
        return ResponseEntity.created(URI.create("/" + student.getId())).body(student);
    }

    @GetMapping
    public ResponseEntity<List<Student>> readStudents() {
        return ResponseEntity.ok(studentService.readStudents());
    }

    @GetMapping("/pagination/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<Student>> StudentsPagination(@PathVariable Integer pageNumber, @PathVariable Integer pageSize) {
        return ResponseEntity.ok(studentService.paginationAndSortingStudents(pageNumber, pageSize, null));
    }

    @GetMapping("/pagination/{pageNumber}/{pageSize}/{sortProperty}")
    public ResponseEntity<Page<Student>> PaginationAndSortingStudents(@PathVariable Integer pageNumber,
                                                                @PathVariable Integer pageSize,
                                                                @PathVariable String sortProperty) {
        return ResponseEntity.ok(studentService.paginationAndSortingStudents(pageNumber, pageSize, sortProperty));
    }

    @GetMapping("/find")
    public ResponseEntity<List<Student>> findStudentByNameAndSurname(@RequestBody String studentNameAndSurname) {
       List<Student> students = studentService.findAllStudentByNameAndSurname(studentNameAndSurname);
       if (students == null) {
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable int id, @Valid @RequestBody Student toUpdate) {

        if (!studentService.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        studentService.findById(id)
                .ifPresent(student -> {
                    student.updateFrom(toUpdate);
                    studentService.save(student);
                });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable int id) {

        if (!studentService.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        studentService.delete(studentService.findById(id).get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/add-teacher")
    public ResponseEntity<?> addTeacherToStudent(@PathVariable int id, @RequestBody String teacherFullName) {
        if (!studentService.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Teacher teacher = teacherService.findAllTeacherByNameAndSurname(teacherFullName).get(0);

        try {
            studentService.findById(id)
                    .ifPresent(student -> {
                        student.addTeacher(teacher);
                        studentService.save(student);
                    });
            System.out.println("Teacher added");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/read-teachers")
    public ResponseEntity<Set<Teacher>> readAllTeachersFotStudent(@PathVariable int id) {
        if (!studentService.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Set<Teacher> teachers = studentService.findById(id).get().getTeachers();
        return ResponseEntity.ok(teachers);
    }


}
