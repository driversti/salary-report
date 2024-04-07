package com.github.driversti.salaryreport;

import com.github.driversti.salaryreport.infrastructure.EmployeeReader;
import com.github.driversti.salaryreport.organization.Employee;
import com.github.driversti.salaryreport.organization.OrganizationalStructure;
import com.github.driversti.salaryreport.printers.ConsolePrinter;
import com.github.driversti.salaryreport.printers.EmployeePrinter;
import com.github.driversti.salaryreport.printers.Printer;
import com.github.driversti.salaryreport.report.Accountant;
import com.github.driversti.salaryreport.report.OrganizationalSalaryReport;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public class Application {

  public static void main(String[] args) {
    // read employees from CSV file
    EmployeeReader reader = new EmployeeReader();
    Collection<Employee> employees = reader.read("src/main/resources/employees.csv");

    // create organizational structure
    OrganizationalStructure structure = new OrganizationalStructure(employees);

    // create accountant responsible for calculating salaries
    Accountant accountant = new Accountant(structure);

    // calculate and print average salary by level
    Map<Integer, BigDecimal> averageSalaryByLevel = accountant.averageSalaryByLevel();
    averageSalaryByLevel.forEach((level, salary) -> System.out.printf("The average salary of level %d is: %.2f%n", level, salary));

    // create and print salary discrepancy report
    OrganizationalSalaryReport salaryReport = accountant.createSalaryReport();
    Printer consolePrinter = new ConsolePrinter();
    consolePrinter.print("");
    EmployeePrinter employeePrinter = new EmployeePrinter(consolePrinter);
    // Constraint: according to the requirements, we want to calculate the salary discrepancy for levels
    // no deeper than 4 levels between the CEO and the employee.
    // This means we include the CEO + the 4 levels of subordinates between the most far level which (+1) gives us
    // a total of 6 levels.
    salaryReport.printReport(employeePrinter, 6);
  }

}
