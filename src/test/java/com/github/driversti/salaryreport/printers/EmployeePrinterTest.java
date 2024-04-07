package com.github.driversti.salaryreport.printers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.driversti.salaryreport.TestData.CEO;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EmployeePrinterTest {

  private final Printer printer = mock();
  private EmployeePrinter employeePrinter;

  @BeforeEach
  void setUp() {
    employeePrinter = new EmployeePrinter(printer);
  }

  @Test
  @DisplayName("'print' method should call 'print' method of 'printer' object")
  void testPrint() {
    // given
    String testString = "test";

    // when
    employeePrinter.print(testString);

    // then
    verify(printer).print(testString);
  }

  @Test
  @DisplayName("'printEmployee' method should call 'print' method of 'printer' object with employee's full name as argument")
  void testPrintEmployee() {
    // given

    // when
    employeePrinter.printEmployee(CEO);

    // then
    verify(printer).print(CEO.getFullName());
  }

  @Test
  @DisplayName("'printEmployeeWithPostfix' method should call 'print' method of 'printer' object with employee's full name and postfix as argument")
  void testPrintEmployeeWithPostfix() {
    // given
    String withPostfix = "is CEO";

    // when
    employeePrinter.printEmployeeWithPostfix(CEO, withPostfix);

    // then
    verify(printer).print(CEO.getFullName() + " " + withPostfix);
  }
}
