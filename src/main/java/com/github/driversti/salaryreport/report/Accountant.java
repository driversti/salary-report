package com.github.driversti.salaryreport.report;

import com.github.driversti.salaryreport.organization.Employee;
import com.github.driversti.salaryreport.organization.OrganizationalStructure;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Handles calculations related to employee salaries within an organization.
 */
public class Accountant {

  private static final BigDecimal MIN_SALARY_INCREASE = BigDecimal.valueOf(1.20); // 20% increase
  private static final BigDecimal MAX_SALARY_INCREASE = BigDecimal.valueOf(1.50); // 50% increase

  private final OrganizationalStructure structure;

  /**
   * Creates a new Accountant instance.
   *
   * @param structure The organizational structure.
   */
  public Accountant(OrganizationalStructure structure) {
    this.structure = structure;
  }

  /**
   * Calculates the average salary of a list of employees.
   *
   * @param employees The list of employees.
   * @return The average salary as a BigDecimal.
   */
  public BigDecimal averageSalaryOf(List<Employee> employees) {
    if (employees.isEmpty()) {
      return BigDecimal.ZERO;
    }

    return employees.stream()
        .map(Employee::salary)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .divide(BigDecimal.valueOf(employees.size()), 2, RoundingMode.HALF_UP);
  }

  /**
   * Calculates the average salary grouped by organizational level.
   *
   * @return A map of organizational levels to average salaries.
   */
  public Map<Integer, BigDecimal> averageSalaryByLevel() {
    return structure.employeesByLevel()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> averageSalaryOf(entry.getValue())
        ));
  }

  /**
   * Creates a salary report for the organization.
   *
   * @return The salary report.
   */
  public OrganizationalSalaryReport createSalaryReport() {
    Map<Integer, SalaryCategorizer> reportByLevel = new TreeMap<>();

    Map<Integer, List<Employee>> employeesByLevel = structure.employeesByLevel();
    employeesByLevel.forEach((level, managers) -> {
      List<Employee> subordinates = employeesByLevel.getOrDefault(level + 1, List.of());
      SalaryCategorizer categorizer = categorizeEmployees(managers, subordinates);
      reportByLevel.put(level, categorizer);
    });

    return new OrganizationalSalaryReport(reportByLevel);
  }

  private BigDecimal averageSalaryIncreaseBy(List<Employee> employees, BigDecimal increaseBy) {
    if (employees.isEmpty()) return BigDecimal.ZERO;
    return averageSalaryOf(employees).multiply(increaseBy);
  }

  private SalaryCategorizer categorizeEmployees(List<Employee> managers, List<Employee> subordinates) {
    BigDecimal minExpectedSalary = averageSalaryIncreaseBy(subordinates, MIN_SALARY_INCREASE);
    BigDecimal maxExpectedSalary = averageSalaryIncreaseBy(subordinates, MAX_SALARY_INCREASE);
    SalaryCategorizer categorizer = new SalaryCategorizer(minExpectedSalary, maxExpectedSalary);
    managers.forEach(categorizer::addEmployee);
    return categorizer;
  }
}
