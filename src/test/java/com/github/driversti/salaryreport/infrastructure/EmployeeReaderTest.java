package com.github.driversti.salaryreport.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeReaderTest {

  private final EmployeeReader employeeReader = new EmployeeReader();

  @Test
  @DisplayName("should read employees from file")
  void shouldReadEmployeesFromFile() {
    // given
    String filepath = "src/test/resources/test_employees.csv";

    // when
    var employees = employeeReader.read(filepath);

    // then
    assertEquals(3, employees.size());
  }

  @Test
  @DisplayName("should throw RuntimeException when file not found")
  void shouldThrowRuntimeExceptionWhenFileNotFound() {
    // given
    String filepath = "src/test/resources/not_found.csv";

    // when
    assertThrows(RuntimeException.class, () -> employeeReader.read(filepath));
  }
}
