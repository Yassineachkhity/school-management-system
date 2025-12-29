package org.openeye.studentservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openeye.studentservice.enums.Gender;
import org.openeye.studentservice.enums.StudentStatus;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentDTO {

    private Long id;
    private String studentId;

    private String firstName;
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private Gender gender;

    private String email;
    private String phone;
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate admissionDate;
    private Integer gradeLevel;
    private String section;

    private StudentStatus status;

}
