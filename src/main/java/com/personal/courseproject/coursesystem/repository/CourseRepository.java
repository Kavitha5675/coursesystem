package com.personal.courseproject.coursesystem.repository;

import com.personal.courseproject.coursesystem.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {
    Optional<Course> findById(Integer id);

    Optional<Course> findByName(String name);
}
