package com.github.driversti.salaryreport.report;

import com.github.driversti.salaryreport.organization.Employee;
import com.github.driversti.salaryreport.printers.EmployeePrinter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.github.driversti.salaryreport.TestData.CEO;
import static com.github.driversti.salaryreport.TestData.MANAGER_1;
import static com.github.driversti.salaryreport.TestData.MANAGER_2;
import static com.github.driversti.salaryreport.TestData.MANAGER_3;
import static com.github.driversti.salaryreport.TestData.MANAGER_4;
import static com.github.driversti.salaryreport.TestData.MANAGER_5;
import static com.github.driversti.salaryreport.TestData.MANAGER_6;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class OrganizationalSalaryReportTest {

  private final EmployeePrinter printer = mock();

  @Test
  @DisplayName("printReport should throw an exception when the level depth is less than 1")
  void testPrintReportWhenLevelDepthIsLessThanOne() {
    // given
    OrganizationalSalaryReport report = new OrganizationalSalaryReport(Map.of());

    // expect
    Exception ex = assertThrows(IllegalArgumentException.class, () -> report.printReport(printer, 0));
    assertEquals("The level depth must be at least 1", ex.getMessage());
  }

  @Test
  @DisplayName("printReport should print 'Report is empty' when the report is empty")
  void testPrintReportWhenReportIsEmpty() {
    // given
    OrganizationalSalaryReport report = new OrganizationalSalaryReport(Map.of());

    // when
    report.printReport(printer, 1);

    // then
    verify(printer).print("Report is empty");
  }

  @Test
  @DisplayName("printReport should print 'No employees found' when there are no employees on all levels")
  void testPrintReportWhenNoEmployeesFound() {
    // given
    SalaryCategorizer categorizer = new SalaryCategorizer(BigDecimal.ZERO, BigDecimal.ZERO);
    OrganizationalSalaryReport report = new OrganizationalSalaryReport(Map.of(1, categorizer));

    // when
    report.printReport(printer, 1);

    // then
    verify(printer).print("No employees found");
  }

  @Test
  @DisplayName("printReport should print the entire report")
  void testPrintReport() {
    // given
    Map<Integer, SalaryCategorizer> map = new TreeMap<>();
    addEmployeesOnLevel1(map);
    addEmployeesOnLevel2(map);
    addEmployeesOnLevel3(map);
    OrganizationalSalaryReport report = new OrganizationalSalaryReport(map);

    // when
    report.printReport(printer, 2);

    // then
    ArgumentCaptor<String> printStringCaptor = ArgumentCaptor.forClass(String.class);
    verify(printer, times(12)).print(printStringCaptor.capture());
    List<String> stringValues = printStringCaptor.getAllValues();
    assertEquals("Salary discrepancy report:", stringValues.get(0));
    assertEquals("Level 1", stringValues.get(1));
    assertEquals("Below expectation:", stringValues.get(2));
    assertEquals("", stringValues.get(3));
    assertEquals("Above expectation:", stringValues.get(4));
    assertEquals("--------------------\n", stringValues.get(5));
    assertEquals("Level 2", stringValues.get(6));
    assertEquals("Below expectation:", stringValues.get(7));
    assertEquals("", stringValues.get(8));
    assertEquals("Above expectation:", stringValues.get(9));
    assertEquals("--------------------\n", stringValues.get(10));
    assertEquals("There are 4 employees on level 3 which have 1 managers between them and the CEO.", stringValues.get(11));

    ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
    ArgumentCaptor<String> postfixCaptor = ArgumentCaptor.forClass(String.class);
    verify(printer, times(3)).printEmployeeWithPostfix(employeeCaptor.capture(), postfixCaptor.capture());
    List<Employee> employeeValues = employeeCaptor.getAllValues();
    List<String> postfixValues = postfixCaptor.getAllValues();
    assertEquals(CEO, employeeValues.get(0));
    assertEquals("(-32.07%)", postfixValues.get(0));
    assertEquals(MANAGER_1, employeeValues.get(1));
    assertEquals("(-3.03%)", postfixValues.get(1));
    assertEquals(MANAGER_2, employeeValues.get(2));
    assertEquals("(+0.85%)", postfixValues.get(2));
  }

  private static void addEmployeesOnLevel1(Map<Integer, SalaryCategorizer> map) {
    SalaryCategorizer categorizer = new SalaryCategorizer(BigDecimal.valueOf(22080), BigDecimal.valueOf(27600));
    categorizer.addEmployee(CEO);
    map.put(1, categorizer);
  }

  private static void addEmployeesOnLevel2(Map<Integer, SalaryCategorizer> map) {
    SalaryCategorizer categorizer = new SalaryCategorizer(BigDecimal.valueOf(8250), BigDecimal.valueOf(10312.5));
    categorizer.addEmployee(MANAGER_1);
    categorizer.addEmployee(MANAGER_2);
    map.put(2, categorizer);
  }

  private static void addEmployeesOnLevel3(Map<Integer, SalaryCategorizer> map) {
    SalaryCategorizer categorizer = new SalaryCategorizer(BigDecimal.ZERO, BigDecimal.ZERO);
    categorizer.addEmployee(MANAGER_3);
    categorizer.addEmployee(MANAGER_4);
    categorizer.addEmployee(MANAGER_5);
    categorizer.addEmployee(MANAGER_6);
    map.put(3, categorizer);
  }
}

// 18400 * 1.2 = 22080
// 18400 * 1.5 = 27600

// 6875 * 1.2 = 8250
// 6875 * 1.5 = 10312.5
