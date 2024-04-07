package com.github.driversti.salaryreport;

import com.github.driversti.salaryreport.organization.Employee;

import java.math.BigDecimal;

public class TestData {

  public static final Employee CEO = new Employee(1, "John", "Doe", BigDecimal.valueOf(15000), -1);
  public static final Employee MANAGER_1 = new Employee(2, "Alice", "Berton", BigDecimal.valueOf(8000), 1);
  public static final Employee MANAGER_2 = new Employee(3, "Jane", "Suzuka", BigDecimal.valueOf(10400), 1);
  public static final Employee MANAGER_3 = new Employee(4, "Bob", "Smith", BigDecimal.valueOf(7000), 2);
  public static final Employee MANAGER_4 = new Employee(5, "Charlie", "Brown", BigDecimal.valueOf(6500), 2);
  public static final Employee MANAGER_5 = new Employee(6, "David", "Jones", BigDecimal.valueOf(6800), 3);
  public static final Employee MANAGER_6 = new Employee(7, "Eve", "Johnson", BigDecimal.valueOf(7200), 3);
}
