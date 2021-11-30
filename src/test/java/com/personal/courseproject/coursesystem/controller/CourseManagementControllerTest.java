package com.personal.courseproject.coursesystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.courseproject.coursesystem.Course;
import com.personal.courseproject.coursesystem.exceptions.ErrorResponse;
import com.personal.courseproject.coursesystem.service.CourseManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseManagementController.class)
class CourseManagementControllerTest {

    private Course newCourse;
    private List<Course> courseList;
    private Course updatedCourse;
    private Course course;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final Calendar calendar = Calendar.getInstance();
    private final String date = dateFormat.format(calendar.getTime());
    private final LocalDateTime localDateTime = LocalDateTime.parse(date);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseManagementService courseManagementService;

    @BeforeEach
    void setUp() {
        courseList = new ArrayList<>();
        updatedCourse = new Course("API Development using SpringCloud","course description here");
        course = new Course("API Development using SpringBoot","course description here");
        newCourse = new Course("API Development using SpringBoot","course description here");
    }

    @Test
    void should_get_empty_list_of_courses() throws Exception {
        when(courseManagementService.getCourses()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(courseManagementService, times(1)).getCourses();
    }

    @Test
    void should_get_list_of_courses() throws Exception {
        courseList.add(course);
        when(courseManagementService.getCourses()).thenReturn(courseList);
        String response = objectMapper.writeValueAsString(courseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
        verify(courseManagementService, times(1)).getCourses();
    }

    @Test
    void should_get_course_details_by_id() throws Exception {
        when(courseManagementService.getCoursesById(1)).thenReturn(course);
        String response = objectMapper.writeValueAsString(course);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
        verify(courseManagementService, times(1)).getCoursesById(1);
    }

    @Test
    void should_not_get_course_if_the_id_is_invalid() throws Exception {
        when(courseManagementService.getCoursesById(1)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(courseManagementService, times(1)).getCoursesById(1);
    }

    @Test
    void should_add_courses_successfully() throws Exception {
        Course courseAfterAdded = new Course(1, "API Development using SpringBoot", "course description here", localDateTime, null);
        when(courseManagementService.addCourses(course)).thenReturn(courseAfterAdded);
        String requestJson = objectMapper.writeValueAsString(course);

        String response = objectMapper.writeValueAsString(courseAfterAdded);

        mockMvc.perform(post("/api/courses").contentType("application/json")
                        .accept("application/json").content(requestJson)).
                andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void should_not_add_when_course_title_is_not_entered() throws Exception {
        course.setName(null);
        when(courseManagementService.addCourses(course)).thenReturn(null);

        String requestJson = objectMapper.writeValueAsString(course);

        mockMvc.perform(post("/api/courses").contentType("application/json")
                        .accept("application/json").content(requestJson)).
                andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_when_course_title_is_already_present() throws Exception {
        when(courseManagementService.addCourses(course)).thenReturn(course);
        String requestJson = objectMapper.writeValueAsString(course);

        RequestBuilder requestBuilder1=post("/api/courses")
                .contentType("application/json")
                .accept("application/json")
                .content(requestJson);

        mockMvc.perform(requestBuilder1).
                andExpect(status().isCreated());

        String requestJson2 = objectMapper.writeValueAsString(newCourse);

        when(courseManagementService.addCourses(course)).thenReturn(null);
        RequestBuilder requestBuilder2=post("/api/courses")
                .contentType("application/json")
                .accept("application/json")
                .content(requestJson2);

        mockMvc
                .perform(requestBuilder2)
                        .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_already_added_course_details_by_id()throws Exception {
        Course courseAfterUpdated = new Course(1, "API Development using SpringCloud", "course description here", localDateTime, localDateTime);
        when(courseManagementService.updateCourses(1,updatedCourse)).thenReturn(courseAfterUpdated);
        String requestJson=objectMapper.writeValueAsString(updatedCourse);
        RequestBuilder requestBuilder=put("/api/courses/1")
                .contentType("application/json")
                .accept("application/json")
                .content(requestJson);

        String response=objectMapper.writeValueAsString(courseAfterUpdated);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated()).andExpect(content().json(response));
    }

    @Test
    void should_not_update_when_given_id_is_invalid() throws Exception {
        when(courseManagementService.updateCourses(2,updatedCourse)).thenReturn(null);
        String requestJson2=objectMapper.writeValueAsString(updatedCourse);
        RequestBuilder requestBuilder2=put("/api/courses/2")
                .contentType("application/json")
                .accept("application/json")
                .content(requestJson2);

        String response=objectMapper.writeValueAsString(new ErrorResponse("Course with id=" + 2 + " not found"));

        mockMvc.perform(requestBuilder2)
                .andExpect(status().isBadRequest()).andExpect(content().json(response));
    }

    @Test
    void should_not_update_when_course_title_is_null() throws Exception {
        updatedCourse.setName(null);
        when(courseManagementService.updateCourses(1,updatedCourse)).thenReturn(null);
        String requestJson2=objectMapper.writeValueAsString(updatedCourse);
        RequestBuilder requestBuilder2=put("/api/courses/1")
                .contentType("application/json")
                .accept("application/json")
                .content(requestJson2);

        String response=objectMapper.writeValueAsString(new ErrorResponse("Course title is required"));

        mockMvc.perform(requestBuilder2)
                .andExpect(status().isBadRequest()).andExpect(content().json(response));
    }

    @Test
    void should_delete_course_by_id()throws Exception {
        when(courseManagementService.deleteCourses(1)).thenReturn(true);

        RequestBuilder requestBuilder2=delete("/api/courses/1")
                .contentType("application/json")
                .accept("application/json");

        mockMvc.perform(requestBuilder2).andExpect(status().isOk());
    }

    @Test
    void should_throw_error_message_when_wants_to_deletes_course_by_invalid_id()throws Exception{
        when(courseManagementService.deleteCourses(1)).thenReturn(false);
        RequestBuilder requestBuilder2=delete("/api/courses/1")
                .contentType("application/json")
                .accept("application/json");

        String response=objectMapper.writeValueAsString(new ErrorResponse("Course with id=" + 1 + " not found"));

        mockMvc.perform(requestBuilder2).andExpect(status().isNotFound()).andExpect(content().json(response));
    }
}
