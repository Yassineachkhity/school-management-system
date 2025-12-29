package org.openeye.inscriptionservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service", path = "/api/students")
public interface StudentClient {

    @GetMapping("/{studentId}")
    void getStudentById(@PathVariable("studentId") String studentId);
}
