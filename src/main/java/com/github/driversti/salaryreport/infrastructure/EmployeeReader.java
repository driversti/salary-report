package com.github.driversti.salaryreport.infrastructure;

import com.github.driversti.salaryreport.organization.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reads employees from a CSV file.
 */
public class EmployeeReader {

  private static final String HEADER = "Id,firstName,lastName,salary,managerId";
  private static final String COMMA_DELIMITER = ",";

  /**
   * Reads employees from a CSV file.
   *
   * @param filepath the path to the CSV file (assumed to be present, readable, and well-formed)
   * @return a collection of employees
   */
  public Collection<Employee> read(String filepath) {
    List<Employee> records = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.equals(HEADER)) {
          continue;
        }
        String[] values = line.split(COMMA_DELIMITER);
        records.add(createEmployee(values));
      }
    } catch (Exception e) {
      System.err.println("Error reading file: " + filepath);
      throw new RuntimeException(e);
    }

    return records;
  }

  private Employee createEmployee(String[] values) {
    int id = Integer.parseInt(values[0]);
    String firstName = values[1];
    String lastName = values[2];
    BigDecimal salary = BigDecimal.valueOf(Long.parseLong(values[3]));
    // FIXME: This is a temporary solution to handle the case of CEO having no manager.
    // This hack violates the Single Responsibility Principle and should be refactored. Consider using a factory.
    int managerId = values.length == 4 ? -1 : Integer.parseInt(values[4]);
    return new Employee(id, firstName, lastName, salary, managerId);
  }
}
