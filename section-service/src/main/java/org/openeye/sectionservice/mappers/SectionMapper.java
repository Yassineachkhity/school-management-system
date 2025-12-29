package org.openeye.sectionservice.mappers;

import org.openeye.sectionservice.dao.entities.Section;
import org.openeye.sectionservice.dtos.SectionCreateRequest;
import org.openeye.sectionservice.dtos.SectionDTO;
import org.openeye.sectionservice.dtos.SectionUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper {

    public SectionDTO toDTO(Section section) {
        if (section == null) {
            return null;
        }

        return SectionDTO.builder()
                .sectionId(section.getSectionId())
                .courseId(section.getCourseId())
                .teacherId(section.getTeacherId())
                .academicYear(section.getAcademicYear())
                .capacity(section.getCapacity())
                .room(section.getRoom())
                .build();
    }

    public Section toEntity(SectionCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Section.builder()
                .courseId(request.getCourseId())
                .teacherId(request.getTeacherId())
                .academicYear(request.getAcademicYear())
                .capacity(request.getCapacity())
                .room(request.getRoom())
                .build();
    }

    public void updateEntityFromDTO(Section section, SectionUpdateRequest request) {
        if (section == null || request == null) {
            return;
        }

        if (request.getCourseId() != null) {
            section.setCourseId(request.getCourseId());
        }
        if (request.getTeacherId() != null) {
            section.setTeacherId(request.getTeacherId());
        }
        if (request.getAcademicYear() != null) {
            section.setAcademicYear(request.getAcademicYear());
        }
        if (request.getCapacity() != null) {
            section.setCapacity(request.getCapacity());
        }
        if (request.getRoom() != null) {
            section.setRoom(request.getRoom());
        }
    }
}
