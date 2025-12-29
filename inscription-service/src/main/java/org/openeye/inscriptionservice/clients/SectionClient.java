package org.openeye.inscriptionservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "section-service", path = "/api/sections")
public interface SectionClient {

    @GetMapping("/{sectionId}")
    void getSectionById(@PathVariable("sectionId") String sectionId);
}
