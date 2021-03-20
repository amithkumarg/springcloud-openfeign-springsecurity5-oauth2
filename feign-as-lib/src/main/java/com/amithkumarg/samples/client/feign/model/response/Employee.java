package com.amithkumarg.samples.client.feign.model.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class Employee {

  private String empId;

  private String firstName;

  private String lastName;

  private LocalDate dob;

  private String postalCode;

  private String ssn;
}
