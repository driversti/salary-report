package com.github.driversti.salaryreport.printers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsolePrinterTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  private final ConsolePrinter printer = new ConsolePrinter();

  @BeforeEach
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  @DisplayName("ConsolePrinter should print the expected string to System.out")
  public void testPrint() {
    // given
    String testString = "Hello World!";

    // when
    printer.print(testString);

    // then
    assertEquals(testString + System.lineSeparator(), outContent.toString());
  }
}
