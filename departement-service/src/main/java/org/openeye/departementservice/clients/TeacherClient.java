package org.openeye.departementservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "teacher-service", path = "/api/teachers")
public interface TeacherClient {

    @GetMapping("/{teacherId}")
    void getTeacherById(@PathVariable("teacherId") String teacherId);
}
