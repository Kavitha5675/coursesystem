package com.personal.courseproject.coursesystem.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.personal.courseproject.coursesystem.Course;
import com.personal.courseproject.coursesystem.exceptions.ErrorResponse;
import com.personal.courseproject.coursesystem.service.CourseManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component
public class CourseManagementController {

    private final CourseManagementService courseManagementService;

    @Autowired
    public CourseManagementController(CourseManagementService courseManagementService) {
        this.courseManagementService = courseManagementService;
    }

    @GetMapping(value = "/api/courses")
    public List<Course> getCourses() {
        return courseManagementService.getCourses();
    }

    @GetMapping(value = "/api/courses/{id}")
    public ResponseEntity<Object> getCoursesById(@PathVariable Integer id) {
        Course course = courseManagementService.getCoursesById(id);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(course);
    }

    @PostMapping(value = "/api/courses", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addCourses(@RequestBody Course course) {
        if (course.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Course title is required"));
        }
        Course course1 = courseManagementService.addCourses(course);
        if (course1 == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Course with title " + course.getName() + " already exists"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(course1);
    }

    @PutMapping(value = "/api/courses/{id}")
    @JsonProperty("id")
    public ResponseEntity<Object> updateCourses(@PathVariable Integer id, @RequestBody Course updatedCourse) {
        if (updatedCourse.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Course title is required"));
        }
        Course updateCourse = courseManagementService.updateCourses(id, updatedCourse);
        if (updateCourse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Course with id=" + id + " not found"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(updateCourse);
    }

    @DeleteMapping(value = "/api/courses/{id}")
    @JsonProperty("id")
    public ResponseEntity<Object> deleteCourses(@PathVariable Integer id) {
        boolean isDelete = courseManagementService.deleteCourses(id);
        if (isDelete) {
            courseManagementService.deleteCourses(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Course with id=" + id + " not found"));
    }
}
