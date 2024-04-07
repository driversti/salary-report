package com.github.driversti.salaryreport.report;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.github.driversti.salaryreport.TestData.CEO;
import static com.github.driversti.salaryreport.TestData.MANAGER_1;
import static com.github.driversti.salaryreport.TestData.MANAGER_3;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaryCategorizerTest {

  @Test
  @DisplayName("should categorize employees by salary")
  void shouldCategorizeEmployeesBySalary() {
    // given
    BigDecimal minExpectedSalary = BigDecimal.valueOf(8000);
    BigDecimal maxExpectedSalary = BigDecimal.valueOf(10000);
    SalaryCategorizer categorizer = new SalaryCategorizer(minExpectedSalary, maxExpectedSalary);

    // when
    categorizer.addEmployee(CEO);
    categorizer.addEmployee(MANAGER_1);
    categorizer.addEmployee(MANAGER_3);

    // then
    assertEquals(minExpectedSalary, categorizer.getMinExpectedSalary());
    assertEquals(maxExpectedSalary, categorizer.getMaxExpectedSalary());
    assertEquals(1, categorizer.getAllBelowExpectation().size());
    assertEquals(1, categorizer.getAllWithinExpectation().size());
    assertEquals(1, categorizer.getAllAboveExpectation().size());
    assertEquals(3, categorizer.getAllCount());
  }
}
