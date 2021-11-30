package com.personal.courseproject.coursesystem;

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

    public Optional<Course> getCoursesById(Integer id) {
        return courseRepository.findById(id);
    }

    public Course addCourses(Course course) {
        course.setCreatedAt(LocalDateTime.parse(getFormattedDate()));
        return courseRepository.save(course);

    }

    protected String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    public boolean getCoursesByName(String name) {
        return courseRepository.findByName(name).isPresent();
    }

    public Course updateCourses(Integer id, Course updatedCourse) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
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
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            courseRepository.delete(existingCourse);
            return true;
        }
        return false;
    }
}

