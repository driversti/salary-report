package com.github.driversti.salaryreport.report;

import com.github.driversti.salaryreport.organization.Employee;
import com.github.driversti.salaryreport.organization.OrganizationalStructure;
import com.github.driversti.salaryreport.printers.EmployeePrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static com.github.driversti.salaryreport.TestData.CEO;
import static com.github.driversti.salaryreport.TestData.MANAGER_1;
import static com.github.driversti.salaryreport.TestData.MANAGER_2;
import static com.github.driversti.salaryreport.TestData.MANAGER_3;
import static com.github.driversti.salaryreport.TestData.MANAGER_4;
import static com.github.driversti.salaryreport.TestData.MANAGER_5;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountantTest {

  private final OrganizationalStructure structure = mock();
  private final EmployeePrinter printer = mock();
  private Accountant accountant;

  @BeforeEach
  void setUp() {
    accountant = new Accountant(structure);
  }

  @Test
  @DisplayName("averageSalaryOf should return zero when the list of employees is empty")
  void calculateAverageSalaryForEmptyList() {
    // expect
    assertEquals(BigDecimal.ZERO, accountant.averageSalaryOf(List.of()));
  }

  @Test
  @DisplayName("averageSalaryOf should return the average salary of a list of employees")
  void calculateAverageSalaryForNonEmpty() {
    // given
    List<Employee> employees = List.of(MANAGER_1, MANAGER_2, MANAGER_3);

    // when
    BigDecimal actual = accountant.averageSalaryOf(employees);

    // then
    BigDecimal expected = toBigDecimal(8466.67);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("averageSalaryByLevel should return a map of organizational levels to average salaries")
  void calculateAverageSalaryByLevel() {
    // given
    List<Employee> employees1 = List.of(MANAGER_2, MANAGER_3);
    List<Employee> employees2 = List.of(MANAGER_4, MANAGER_5);

    // and
    when(structure.employeesByLevel()).thenReturn(Map.of(1, employees1, 2, employees2));

    // when
    Map<Integer, BigDecimal> actual = accountant.averageSalaryByLevel();

    // then
    Map<Integer, BigDecimal> expected = Map.of(1, toBigDecimal(8700), 2, toBigDecimal(6650));
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("createSalaryReport should return an OrganizationalSalaryReport")
  void createSalaryReport() {
    // given
    List<Employee> managers1 = List.of(CEO);
    List<Employee> managers2 = List.of(MANAGER_1, MANAGER_2);
    List<Employee> managers3 = List.of(MANAGER_3, MANAGER_4);

    // and
    when(structure.employeesByLevel()).thenReturn(Map.of(1, managers1, 2, managers2, 3, managers3));

    // when
    OrganizationalSalaryReport report = accountant.createSalaryReport();

    // then
    assertNotNull(report);
  }


  private static BigDecimal toBigDecimal(double value) {
    return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
  }
}
