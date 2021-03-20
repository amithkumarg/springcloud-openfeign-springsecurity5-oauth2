package com.amithkumarg.samples.client.feign.service;

import com.amithkumarg.samples.client.feign.model.response.Employee;
import com.amithkumarg.samples.client.feign.service.config.AuthZConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "fake-employee",
    url = "${fake-employee.endpoint}",
    configuration = AuthZConfiguration.class)
public interface FakeEmployeeClient {
  @GetMapping("/employee/{empId}")
  Employee fetchEmployee(@PathVariable("empId") String empId);
}
