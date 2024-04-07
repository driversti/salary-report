package com.github.driversti.salaryreport.organization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.github.driversti.salaryreport.TestData.CEO;
import static com.github.driversti.salaryreport.TestData.MANAGER_1;
import static com.github.driversti.salaryreport.TestData.MANAGER_2;
import static com.github.driversti.salaryreport.TestData.MANAGER_3;
import static com.github.driversti.salaryreport.TestData.MANAGER_4;
import static com.github.driversti.salaryreport.TestData.MANAGER_5;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class OrganizationalStructureTest {

  private static final List<Employee> INIT_EMPLOYEES = List.of(MANAGER_3, MANAGER_1, CEO, MANAGER_4, MANAGER_5, MANAGER_2);

  @Test
  @DisplayName("should return a copy of employees sorted by their id")
  void employeesShouldReturnSortedCopyOfInitialEmployeesCollection() {
    // given
    OrganizationalStructure structure = new OrganizationalStructure(INIT_EMPLOYEES);

    // when
    List<Employee> sortedById = structure.employees();

    // then
    assertEquals(
        List.of(CEO, MANAGER_1, MANAGER_2, MANAGER_3, MANAGER_4, MANAGER_5),
        sortedById
    );
    assertNotSame(INIT_EMPLOYEES, sortedById);
  }

  @Test
  @DisplayName("should return a map of employees grouped by their level")
  void employeesByLevelShouldReturnMapOfEmployeesGroupedByTheirLevel() {
    // given
    OrganizationalStructure structure = new OrganizationalStructure(INIT_EMPLOYEES);

    // when
    Map<Integer, List<Employee>> employeesByLevel = structure.employeesByLevel();

    // then
    assertEquals(3, employeesByLevel.size());
    assertEquals(List.of(CEO), employeesByLevel.get(1));
    assertEquals(List.of(MANAGER_1, MANAGER_2), employeesByLevel.get(2));
    assertEquals(List.of(MANAGER_3, MANAGER_4, MANAGER_5), employeesByLevel.get(3));
  }
}
