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
@RequestMapping("/teachers")
public class TeacherController {

    private TeacherService teacherService;
    private StudentService studentService;

    public TeacherController(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody Teacher toCreate) {
        Teacher teacher = teacherService.save(toCreate);
        return ResponseEntity.created(URI.create("/" + teacher.getId())).body(teacher);
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> readTeachers() {
        return ResponseEntity.ok(teacherService.readTeachers());
    }

    @GetMapping("/pagination/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<Teacher>> TeacherPagination(@PathVariable Integer pageNumber, @PathVariable Integer pageSize) {
        return ResponseEntity.ok(teacherService.paginationAndSortingTeachers(pageNumber, pageSize, null));
    }

    @GetMapping("/pagination/{pageNumber}/{pageSize}/{sortProperty}")
    public ResponseEntity<Page<Teacher>> PaginationAndSortingTeachers(@PathVariable Integer pageNumber,
                                                                      @PathVariable Integer pageSize,
                                                                      @PathVariable String sortProperty) {
        return ResponseEntity.ok(teacherService.paginationAndSortingTeachers(pageNumber, pageSize, sortProperty));
    }

    @GetMapping("/find")
    public ResponseEntity<List<Teacher>> findTeacherByNameAndSurname(@RequestBody String teacherNameAndSurname) {
        List<Teacher> teachers = teacherService.findAllTeacherByNameAndSurname(teacherNameAndSurname);
        if (teachers == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teachers);
    }

    @PutMapping("/id")
    public ResponseEntity<?> updateTeacher(@PathVariable int id, @Valid @RequestBody Teacher toUpdate) {

        if (!teacherService.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        teacherService.findById(id)
                .ifPresent(teacher -> {
                    teacher.updateFrom(toUpdate);
                    teacherService.save(teacher);
                });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id")
    public ResponseEntity<?> deleteTeacherById(@PathVariable int id) {

        if (!teacherService.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        Teacher teacher = teacherService.findById(id).get();

        teacher.getStudents().stream()
                .forEach(student -> student.getTeachers().remove(teacher));

        teacherService.delete(teacher);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/add-student")
    public ResponseEntity<?> addStudentToTeacher(@PathVariable int id, @RequestBody String studentFullName) {
        if (!teacherService.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Student student = studentService.findAllStudentByNameAndSurname(studentFullName).get(0);

        try {
            teacherService.findById(id)
                    .ifPresent(teacher -> {
                        teacher.addStudent(student);
                        teacherService.save(teacher);
                    });
            System.out.println("Student added");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/read-students")
    public ResponseEntity<Set<Student>> readAllStudentsFotTeacher(@PathVariable int id) {
        if (!teacherService.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Set<Student> students = teacherService.findById(id).get().getStudents();
        return ResponseEntity.ok(students);
    }
}
