package org.openeye.teacherservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openeye.teacherservice.enums.Level;
import org.openeye.teacherservice.enums.TeacherStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {

    private String teacherId;
    private String employeeNumber;

    private String firstName;
    private String lastName;

    private String email;
    private String phone;
    private String address;

    private String departementId;

    private Level level;
    private TeacherStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
}
