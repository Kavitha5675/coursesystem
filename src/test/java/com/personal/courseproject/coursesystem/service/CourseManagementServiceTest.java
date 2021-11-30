package com.personal.courseproject.coursesystem.service;

import com.personal.courseproject.coursesystem.Course;
import com.personal.courseproject.coursesystem.repository.CourseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.mockito.Mockito.*;

class CourseManagementServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseManagementService courseManagementService;
    private List<Course> courseList;
    private Course course1;
    private Course course2;
    private Course updatedCourse;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final Calendar calendar = Calendar.getInstance();
    private final String date = dateFormat.format(calendar.getTime());
    private final LocalDateTime localDateTime = LocalDateTime.parse(date);


    @BeforeEach
    public void Setup() {
        courseRepository = mock(CourseRepository.class);
        courseList = new ArrayList<>() {
        };
        course1 = new Course(1, "API Development using SpringBoot", "course description here",
                LocalDateTime.of(2021, 10, 25, 15, 45, 35), LocalDateTime.of(2021, 10, 26, 15, 45, 35));
        course2 = new Course(1, "API Development using SpringBoot", "course description here",
                localDateTime, null);
        updatedCourse = new Course("API Development using SpringCloud", "course description here");
    }

    @Test
    void should_get_list_of_courses() {
        when(courseRepository.findAll()).thenReturn(courseList);
        CourseManagementService courseManagementService = new CourseManagementService(courseRepository);

        List<Course> courses = courseManagementService.getCourses();

        Assertions.assertEquals(courseList, courses);
    }

    @Test
    void should_get_courses_by_id() {
        when(courseRepository.findById(1)).thenReturn(ofNullable(course1));
        CourseManagementService courseManagementService = new CourseManagementService(courseRepository);

        Course courses = courseManagementService.getCoursesById(1);

        Assertions.assertEquals(course1, courses);
        verify(courseRepository, times(1)).findById(1);
    }

    @Test
    void should_add_course() {
        when(courseRepository.save(course1)).thenReturn(course1);
        CourseManagementService courseManagementService = new CourseManagementService(courseRepository);

        Course course = (Course) courseManagementService.addCourses(course1);

        Assertions.assertEquals(course1, course);
        verify(courseRepository, times(1)).save(course1);
    }

    @Test
    void should_update_course_by_id() {
        course2.setName("API Development using SpringCloud");
        course2.setUpdatedAt(localDateTime);
        when(courseRepository.findById(1)).thenReturn(Optional.ofNullable(course2));
        when(courseRepository.save(course2)).thenReturn(course2);
        CourseManagementService courseManagementService = new CourseManagementService(courseRepository);

        Course updateCourseResult = courseManagementService.updateCourses(1, updatedCourse);

        Assertions.assertEquals(course2, updateCourseResult);
    }

    @Test
    void should_not_update_course_by_invalid_id_and_also_if_the_updated_title_is_null() {
        Course updatedCourse = new Course();
        updatedCourse.setName(null);
        updatedCourse.setDescription("course description here");
        CourseManagementService courseManagementService = new CourseManagementService(courseRepository);

        Course updateCourseResult = courseManagementService.updateCourses(1, updatedCourse);

        Assertions.assertNull(updateCourseResult);
    }

    @Test
    void should_delete_course_by_id() {
        when(courseRepository.findById(1)).thenReturn(Optional.ofNullable(course2));
        when(courseRepository.save(course2)).thenReturn(course2);
        CourseManagementService courseManagementService = new CourseManagementService(courseRepository);

        boolean deleteCourseResult = courseManagementService.deleteCourses(1);

        Assertions.assertTrue(deleteCourseResult);
    }

    @Test
    void should_not_delete_course_by_invalid_id() {
        CourseManagementService courseManagementService = new CourseManagementService(courseRepository);

        boolean deleteCourseResult = courseManagementService.deleteCourses(1);

        Assertions.assertFalse(deleteCourseResult);
    }
}
