package org.openeye.sectionservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.openeye.sectionservice.clients.CourseClient;
import org.openeye.sectionservice.clients.TeacherClient;
import org.openeye.sectionservice.dao.entities.Section;
import org.openeye.sectionservice.dao.repositories.SectionRepository;
import org.openeye.sectionservice.dtos.SectionCreateRequest;
import org.openeye.sectionservice.dtos.SectionDTO;
import org.openeye.sectionservice.dtos.SectionUpdateRequest;
import org.openeye.sectionservice.exceptions.InvalidCourseException;
import org.openeye.sectionservice.exceptions.InvalidTeacherException;
import org.openeye.sectionservice.exceptions.SectionNotFoundException;
import org.openeye.sectionservice.mappers.SectionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionManager implements SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private final CourseClient courseClient;
    private final TeacherClient teacherClient;

    @Override
    @Transactional(readOnly = true)
    public List<SectionDTO> getAllSections() {
        return sectionRepository.findAll().stream()
                .map(sectionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SectionDTO getSectionById(String sectionId) {
        return sectionMapper.toDTO(findSectionById(sectionId));
    }

    @Override
    public SectionDTO createSection(SectionCreateRequest request) {
        String courseId = normalize(request.getCourseId());
        String teacherId = normalize(request.getTeacherId());
        String academicYear = normalize(request.getAcademicYear());
        String room = normalize(request.getRoom());

        validateCourseExists(courseId);
        validateTeacherExists(teacherId);

        Section section = sectionMapper.toEntity(request);
        section.setSectionId(UUID.randomUUID().toString());
        section.setCourseId(courseId);
        section.setTeacherId(teacherId);
        section.setAcademicYear(academicYear);
        section.setRoom(room);

        Section saved = sectionRepository.save(section);
        return sectionMapper.toDTO(saved);
    }

    @Override
    public SectionDTO updateSection(String sectionId, SectionUpdateRequest request) {
        Section section = findSectionById(sectionId);

        String courseId = null;
        if (request.getCourseId() != null) {
            courseId = normalize(request.getCourseId());
            validateCourseExists(courseId);
        }

        String teacherId = null;
        if (request.getTeacherId() != null) {
            teacherId = normalize(request.getTeacherId());
            validateTeacherExists(teacherId);
        }

        sectionMapper.updateEntityFromDTO(section, request);

        if (courseId != null) {
            section.setCourseId(courseId);
        }
        if (teacherId != null) {
            section.setTeacherId(teacherId);
        }
        if (request.getAcademicYear() != null) {
            section.setAcademicYear(normalize(request.getAcademicYear()));
        }
        if (request.getRoom() != null) {
            section.setRoom(normalize(request.getRoom()));
        }

        Section saved = sectionRepository.save(section);
        return sectionMapper.toDTO(saved);
    }

    @Override
    public void deleteSection(String sectionId) {
        Section section = findSectionById(sectionId);
        sectionRepository.delete(section);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionDTO> searchSections(String keyword) {
        String trimmedKeyword = keyword.trim();
        return sectionRepository.searchSections(trimmedKeyword).stream()
                .map(sectionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionDTO> getSectionsByCourseId(String courseId) {
        String trimmedCourseId = normalize(courseId);
        return sectionRepository.findByCourseId(trimmedCourseId).stream()
                .map(sectionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionDTO> getSectionsByTeacherId(String teacherId) {
        String trimmedTeacherId = normalize(teacherId);
        return sectionRepository.findByTeacherId(trimmedTeacherId).stream()
                .map(sectionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionDTO> getSectionsByAcademicYear(String academicYear) {
        String trimmedAcademicYear = normalize(academicYear);
        return sectionRepository.findByAcademicYear(trimmedAcademicYear).stream()
                .map(sectionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionDTO> getSectionsByCourseAndYear(String courseId, String academicYear) {
        String trimmedCourseId = normalize(courseId);
        String trimmedAcademicYear = normalize(academicYear);
        return sectionRepository.findByCourseIdAndAcademicYear(trimmedCourseId, trimmedAcademicYear).stream()
                .map(sectionMapper::toDTO)
                .toList();
    }

    private Section findSectionById(String sectionId) {
        String normalizedId = normalize(sectionId);
        if (normalizedId == null) {
            throw new SectionNotFoundException("Section not found: " + sectionId);
        }
        Section section = sectionRepository.findBySectionId(normalizedId);
        if (section == null) {
            throw new SectionNotFoundException("Section not found: " + sectionId);
        }
        return section;
    }

    private void validateCourseExists(String courseId) {
        if (courseId == null || courseId.isBlank()) {
            throw new InvalidCourseException("Course is required");
        }
        try {
            courseClient.getCourseById(courseId);
        } catch (FeignException.NotFound ex) {
            throw new InvalidCourseException("Course not found: " + courseId);
        }
    }

    private void validateTeacherExists(String teacherId) {
        if (teacherId == null || teacherId.isBlank()) {
            throw new InvalidTeacherException("Teacher is required");
        }
        try {
            teacherClient.getTeacherById(teacherId);
        } catch (FeignException.NotFound ex) {
            throw new InvalidTeacherException("Teacher not found: " + teacherId);
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }
}
