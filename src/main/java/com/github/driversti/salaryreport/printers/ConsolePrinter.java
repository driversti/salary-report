package com.github.driversti.salaryreport.printers;

/**
 * Implementation of the Printer interface that prints to the console.

 */
public class ConsolePrinter implements Printer {

  /**
   * Prints the given string to the console.
   *
   * @param s the string to print
   */
  @Override
  public void print(String s) {
    System.out.println(s);
  }
}
