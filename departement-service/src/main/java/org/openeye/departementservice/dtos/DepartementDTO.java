package org.openeye.departementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartementDTO {

    private String departementId;
    private String departementCode;
    private String name;
    private String description;
    private String headTeacherId;
}
