package com.github.driversti.salaryreport.report;

import com.github.driversti.salaryreport.organization.Employee;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * SalaryCategorizer is a class that categorizes employees based on their salary compared to the expected salary range.
 * <p>It is used to create a report of employees that are below, within, or above the expected salary range.</p>
 */
public class SalaryCategorizer {

  private final BigDecimal minExpectedSalary;
  private final BigDecimal maxExpectedSalary;
  private final List<Employee> belowExpectation = new ArrayList<>();
  private final List<Employee> aboveExpectation = new ArrayList<>();
  private final List<Employee> withinExpectation = new ArrayList<>();

  /**
   * Constructs a SalaryCategorizer with the given minimum and maximum expected salary used for categorization.
   *
   * @param minExpectedSalary the minimum expected salary
   * @param maxExpectedSalary the maximum expected salary
   */
  public SalaryCategorizer(BigDecimal minExpectedSalary, BigDecimal maxExpectedSalary) {
    this.minExpectedSalary = minExpectedSalary;
    this.maxExpectedSalary = maxExpectedSalary;
  }

  /**
   * Adds an employee to a relevant category based on their salary.
   *
   * @param employee the employee to add
   */
  public void addEmployee(Employee employee) {
    BigDecimal salary = employee.salary();
    if (salary.compareTo(minExpectedSalary) < 0) {
      belowExpectation.add(employee);
    } else if (salary.compareTo(maxExpectedSalary) > 0) {
      aboveExpectation.add(employee);
    } else {
      withinExpectation.add(employee);
    }
  }

  /**
   * Returns the minimum expected salary.
   *
   * @return the minimum expected salary
   */
  public BigDecimal getMinExpectedSalary() {
    return minExpectedSalary;
  }

  /**
   * Returns the maximum expected salary.
   *
   * @return the maximum expected salary
   */
  public BigDecimal getMaxExpectedSalary() {
    return maxExpectedSalary;
  }

  /**
   * Returns a copy of the list of employees that are below the expected salary range.
   *
   * @return a list of employees that are below the expected salary range
   */
  public List<Employee> getAllBelowExpectation() {
    return List.copyOf(belowExpectation);
  }

  /**
   * Returns a copy of the list of employees that are above the expected salary range.
   *
   * @return a list of employees that are above the expected salary range
   */
  public List<Employee> getAllAboveExpectation() {
    return List.copyOf(aboveExpectation);
  }

  /**
   * Returns a copy of the list of employees that are within the expected salary range.
   *
   * @return a list of employees that are within the expected salary range
   */
  public List<Employee> getAllWithinExpectation() {
    return List.copyOf(withinExpectation);
  }

  /**
   * Returns the total count of employees in all categories.
   *
   * @return the total count of employees in all categories
   */
  public long getAllCount() {
    return Stream.of(belowExpectation, aboveExpectation, withinExpectation)
        .mapToLong(List::size)
        .sum();
  }
}
