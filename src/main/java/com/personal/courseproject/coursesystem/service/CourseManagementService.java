package com.personal.courseproject.coursesystem.service;

import com.personal.courseproject.coursesystem.Course;
import com.personal.courseproject.coursesystem.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class CourseManagementService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseManagementService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        courseRepository.findAll().forEach(courses::add);
        return courses;
    }

    public Course getCoursesById(Integer id) {
        Optional<Course> course = courseRepository.findByCourseId(id);
        return course.orElse(null);
    }

    public Course addCourses(Course course) {
        Optional<Course> optionalCourse = courseRepository.findByName(course.getName());
        if (optionalCourse.isPresent()) {
            return null;
        }
        course.setCreatedAt(LocalDateTime.parse(getFormattedDate()));
        return courseRepository.save(course);
    }

    protected String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    public Course updateCourses(Integer id, Course updatedCourse) {
        Optional<Course> optionalCourse = courseRepository.findByCourseId(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            existingCourse.setName(updatedCourse.getName());
            existingCourse.setUpdatedAt(LocalDateTime.parse(getFormattedDate()));
            return courseRepository.save(existingCourse);
        } else {
            return null;
        }
    }

    public boolean deleteCourses(Integer id) {
        Optional<Course> optionalCourse = courseRepository.findByCourseId(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            courseRepository.delete(existingCourse);
            return true;
        }
        return false;
    }
}

