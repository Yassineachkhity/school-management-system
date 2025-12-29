package org.openeye.sectionservice.service;

import org.openeye.sectionservice.dtos.SectionCreateRequest;
import org.openeye.sectionservice.dtos.SectionDTO;
import org.openeye.sectionservice.dtos.SectionUpdateRequest;

import java.util.List;

public interface SectionService {
    List<SectionDTO> getAllSections();

    SectionDTO getSectionById(String sectionId);

    SectionDTO createSection(SectionCreateRequest request);

    SectionDTO updateSection(String sectionId, SectionUpdateRequest request);

    void deleteSection(String sectionId);

    List<SectionDTO> searchSections(String keyword);

    List<SectionDTO> getSectionsByCourseId(String courseId);

    List<SectionDTO> getSectionsByTeacherId(String teacherId);

    List<SectionDTO> getSectionsByAcademicYear(String academicYear);

    List<SectionDTO> getSectionsByCourseAndYear(String courseId, String academicYear);
}
