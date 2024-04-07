package com.github.driversti.salaryreport.printers;

import com.github.driversti.salaryreport.organization.Employee;

import static java.lang.String.format;

/**
 * Implementation of Printer interface that prints Employee objects using the provided Printer object.
 */
public class EmployeePrinter implements Printer {

  private final Printer printer;

  /**
   * Constructor.
   *
   * @param printer Printer object to use for printing.
   */
  public EmployeePrinter(Printer printer) {
    this.printer = printer;
  }

  /**
   * Prints the provided string.
   *
   * @param s String to print.
   */
  @Override
  public void print(String s) {
    printer.print(s);
  }

  /**
   * Prints the full name of the provided Employee object.
   *
   * @param employee Employee object to print.
   */
  public void printEmployee(Employee employee) {
    this.print(employee.getFullName());
  }

  /**
   * Prints the full name of the provided Employee object with the provided postfix.
   *
   * @param employee    Employee object to print.
   * @param withPostfix Postfix to append to the full name.
   */
  public void printEmployeeWithPostfix(Employee employee, String withPostfix) {
    String str = format("%s %s", employee.getFullName(), withPostfix);
    this.print(str);
  }
}
