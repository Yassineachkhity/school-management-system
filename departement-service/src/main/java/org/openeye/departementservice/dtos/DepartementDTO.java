package org.openeye.departementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openeye.departementservice.enums.DepartementStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartementDTO {

    private Long id;
    private String departementCode;
    private String name;
    private String description;
    private String headTeacherId;
    private DepartementStatus status;
}
