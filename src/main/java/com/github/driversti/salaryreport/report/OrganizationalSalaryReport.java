package com.github.driversti.salaryreport.report;

import com.github.driversti.salaryreport.printers.EmployeePrinter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.String.format;
import static java.math.BigDecimal.ONE;

/**
 * Represents a report of the salary discrepancy for each organizational level.
 */
public class OrganizationalSalaryReport {

  private static final int SCALE = 10;
  private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
  private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
  private static final String LEVELS_DELIMITER = "--------------------";

  private final Map<Integer, SalaryCategorizer> reportPerLevel;

  public OrganizationalSalaryReport(Map<Integer, SalaryCategorizer> reportPerLevel) {
    this.reportPerLevel = new TreeMap<>(reportPerLevel);
  }

  // TODO: consider adding a method to print the full report

  /**
   * Print the report of the salary discrepancy for each organizational level.
   *
   * @param printer    the printer to use
   * @param levelDepth the maximum depth of the organizational structure to print (included)
   */
  public void printReport(EmployeePrinter printer, int levelDepth) {
    if (levelDepth < 1) {
      throw new IllegalArgumentException("The level depth must be at least 1");
    }
    if (reportPerLevel.isEmpty()) {
      printer.print("Report is empty");
      return;
    }
    long allEmployeesCount = reportPerLevel.values().stream().mapToLong(SalaryCategorizer::getAllCount).sum();
    if (allEmployeesCount == 0) {
      printer.print("No employees found");
      return;
    }

    printer.print("Salary discrepancy report:");
    printIncludedEmployees(printer, levelDepth);

    // There is a requirement to identify all employees which have more than 4 managers between them and the CEO.
    // At the same time we want to know these employees. It is not clear if we should print them or not.
    // Since the Board wants to avoid too long reporting lines, I decided to print only the number of excluded employees.
    printExcludedEmployeeCount(printer, levelDepth);
  }

  private void printIncludedEmployees(EmployeePrinter printer, int levelDepth) {
    reportPerLevel.keySet().stream()
        .limit(levelDepth)
        .forEach(level -> printLevelReport(level, reportPerLevel.get(level), printer));
  }

  private void printExcludedEmployeeCount(EmployeePrinter printer, int startLevel) {
    reportPerLevel.keySet().stream()
        .skip(startLevel)
        .forEach(level -> printExcludedEmployeesCount(level, printer));
  }

  private void printExcludedEmployeesCount(Integer level, EmployeePrinter printer) {
    long excludedEmployees = reportPerLevel.get(level).getAllCount();
    String s = format("There are %d employees on level %d which have %d managers between them and the CEO.",
        excludedEmployees, level, level - 2); // level - 2, because we don't count the current level and the CEO
    printer.print(s);
  }

  private void printLevelReport(Integer level, SalaryCategorizer categorizer, EmployeePrinter printer) {
    printer.print("Level " + level);
    printBelowExpectation(categorizer, printer);
    printer.print("");
    printAboveExpectation(categorizer, printer);
    printer.print(LEVELS_DELIMITER + "\n");
  }

  private void printBelowExpectation(SalaryCategorizer categorizer, EmployeePrinter printer) {
    printer.print("Below expectation:");
    categorizer.getAllBelowExpectation()
        .forEach(employee -> {
          BigDecimal salary = employee.salary();
          BigDecimal minExpectedSalary = categorizer.getMinExpectedSalary();
          BigDecimal discrepancyPercentage = ONE
              .subtract(salary.divide(minExpectedSalary, SCALE, ROUNDING_MODE))
              .multiply(ONE_HUNDRED);

          printer.printEmployeeWithPostfix(employee, format("(-%.2f%%)", discrepancyPercentage));
        });
  }

  private void printAboveExpectation(SalaryCategorizer categorizer, EmployeePrinter printer) {
    printer.print("Above expectation:");
    categorizer.getAllAboveExpectation()
        .forEach(employee -> {
          BigDecimal salary = employee.salary();
          BigDecimal maxExpectedSalary = categorizer.getMaxExpectedSalary();
          BigDecimal discrepancyPercentage = salary
              .divide(maxExpectedSalary, SCALE, ROUNDING_MODE).subtract(ONE)
              .multiply(ONE_HUNDRED);

          printer.printEmployeeWithPostfix(employee, format("(+%.2f%%)", discrepancyPercentage));
        });
  }
}
