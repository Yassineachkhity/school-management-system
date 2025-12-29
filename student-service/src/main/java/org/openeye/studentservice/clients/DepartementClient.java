package org.openeye.studentservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "departement-service", path = "/api/departements")
public interface DepartementClient {

    @GetMapping("/{departementId}")
    void getDepartementById(@PathVariable("departementId") String departementId);
}
