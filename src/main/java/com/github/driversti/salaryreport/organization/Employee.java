package com.github.driversti.salaryreport.organization;

import java.math.BigDecimal;

/**
 * Represents an employee in the organization.
 */
public record Employee(int id, String firstName, String lastName, BigDecimal salary, int managerId) {

  public String getFullName() {
    return firstName + " " + lastName;
  }
}
