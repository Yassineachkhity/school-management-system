package org.openeye.teacherservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", path = "/api/users")
public interface AuthClient {

    @GetMapping("/{userId}")
    void getUserById(@PathVariable("userId") String userId);
}
