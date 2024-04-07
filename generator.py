import csv
import random
from io import StringIO

# Sample first and last names
first_names = [
    "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Elizabeth",
    "David", "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen",
    "Christopher", "Nancy", "Daniel", "Lisa", "Matthew", "Margaret", "Anthony", "Betty", "Mark", "Sandra",
    "Donald", "Ashley", "Steven", "Kimberly", "Paul", "Emily", "Brian", "Melissa", "George", "Deborah", "Kenneth",
    "Stephanie", "Andrew", "Rebecca", "Joshua", "Laura", "Kevin", "Sharon", "Bryan", "Cynthia", "Edward", "Kathleen",
    "Ronald", "Amy", "Timothy", "Shirley", "Jason", "Angela", "Jeffrey", "Helen", "Ryan", "Anna", "Jacob", "Brenda",
    "Gary", "Pamela", "Nicholas", "Nicole", "Eric", "Samantha", "Jonathan", "Katherine", "Stephen", "Emma", "Larry"
]
last_names = [
    "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez",
    "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez",
    "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen",
    "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores", "Green", "Adams", "Nelson", "Baker", "Hall",
    "Rivera", "Campbell", "Mitchell", "Carter", "Roberts"
]

def generate_random_salary(start, end):
    if start > end:
        raise ValueError("The minimum salary must be less than or equal to the maximum salary.")

    # Randomly select a multiple of 100 within the range
    return random.randint(start // 100, end // 100) * 100


def generate_subordinates(start_id, manager_id_range, salary_min, salary_max):
    """
    Generates a CSV file with subordinates.

    :param start_id: The starting ID for the subordinates.
    :param manager_id_range: A tuple (min, max) specifying the range of manager IDs.
    :param salary_min: The minimum salary for subordinates.
    :param salary_max: The maximum salary for subordinates.
    """
    subordinates = []
    next_id = start_id

    # Iterate through each manager ID in the specified range
    for manager_id in range(manager_id_range[0], manager_id_range[1] + 1):
        # Assign 2 to 3 subordinates to each manager
        num_subordinates = random.randint(2, 3)
        for _ in range(num_subordinates):
            first_name = random.choice(first_names)
            last_name = random.choice(last_names)
            subordinates.append({
                "Id": next_id,
                "firstName": first_name,
                "lastName": last_name,
                "salary": generate_random_salary(salary_min, salary_max),
                "managerId": manager_id
            })
            next_id += 1

    # Convert the subordinates data into CSV format
    output = StringIO()
    fieldnames = ['Id', 'firstName', 'lastName', 'salary', 'managerId']
    writer = csv.DictWriter(output, fieldnames=fieldnames)

    writer.writeheader()
    writer.writerows(subordinates)

    # Write to the specified output file
    with open("managers.csv", "w") as file:
        file.write(output.getvalue())


# Example usage
generate_subordinates(start_id=900, manager_id_range=(700, 888), salary_min=12000, salary_max=24000)

print("Subordinates generated successfully.")
