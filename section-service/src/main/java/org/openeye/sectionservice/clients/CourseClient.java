package org.openeye.sectionservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", path = "/api/courses")
public interface CourseClient {

    @GetMapping("/{courseId}")
    void getCourseById(@PathVariable("courseId") String courseId);
}
