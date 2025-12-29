package org.openeye.sectionservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {

    private String sectionId;
    private String courseId;
    private String teacherId;
    private String academicYear;
    private Integer capacity;
    private String room;
}
