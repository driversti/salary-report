package com.github.driversti.salaryreport.organization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents the organizational structure of an organization.
 *
 * <p>Employees are sorted and grouped by their manager and by their organizational level.
 * <p>The organizational structure is expected to be read-only.
 */
public class OrganizationalStructure {

  private final List<Employee> sortedById;
  private final Map<Integer, List<Employee>> employeesByManager;
  private final Map<Integer, List<Employee>> employeesByLevel;

  /**
   * Constructs an organizational structure from a collection of employees.
   * <p>Employees are sorted by their id and grouped by their manager and by their organizational level.
   *
   * @param employees The collection of employees.
   */
  public OrganizationalStructure(Collection<Employee> employees) {
    this.sortedById = new ArrayList<>(employees);
    this.sortedById.sort(Comparator.comparingInt(Employee::id));
    this.employeesByManager = groupByManager(this.sortedById);
    this.employeesByLevel = groupByLevel(employeesByManager);
  }

  /**
   * Returns a copy of the list of employees.
   *
   * @return A list of employees.
   */
  public List<Employee> employees() {
    return List.copyOf(sortedById);
  }

  /**
   * Returns a copy of the map of employees grouped by manager id.
   *
   * @return A map of manager id to a list of employees.
   */
  public Map<Integer, List<Employee>> employeesByLevel() {
    return Map.copyOf(employeesByLevel);
  }

  private Map<Integer, List<Employee>> groupByManager(List<Employee> sortedById) {
    return sortedById.stream()
        .collect(Collectors.groupingBy(Employee::managerId));
  }

  private Map<Integer, List<Employee>> groupByLevel(Map<Integer, List<Employee>> employeesByManagerId) {
    Map<Integer, List<Employee>> employeesByLevel = new HashMap<>();
    int level = 1;

    // find ceo
    Employee ceo = employeesByManagerId.get(-1).getFirst();
    employeesByLevel.put(level, List.of(ceo)); // put ceo at level 1

    // find subordinates of ceo
    List<Employee> subordinates = employeesByManagerId.get(ceo.id());

    // iterate through the organizational structure until there are no more subordinates
    while (!subordinates.isEmpty()) {
      employeesByLevel.put(++level, subordinates); // subordinates are at the next level
      List<Integer> managerIds = getIds(subordinates); // subordinates are managers for their subordinates
      subordinates = findSubordinatesOfManagers(managerIds, employeesByManagerId); // find subordinates of subordinates
    }

    return employeesByLevel;
  }

  private List<Integer> getIds(List<Employee> employees) {
    return employees.stream().map(Employee::id).toList();
  }

  private List<Employee> findSubordinatesOfManagers(List<Integer> managers, Map<Integer, List<Employee>> employeesByManagerId) {
    List<Employee> subordinates = new ArrayList<>();
    for (Integer managerId : managers) {
      List<Employee> employees = employeesByManagerId.getOrDefault(managerId, new ArrayList<>());
      subordinates.addAll(employees);
    }
    return subordinates;
  }
}
