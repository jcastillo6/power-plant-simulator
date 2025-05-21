Feature: As a construction manager, I want to be able to upload a file with a list of power plants, so I know how much energy will be produced with that network in a span of T days.

  Scenario: Upload basic file
    Given a new simulation
    When upload a file with the following content:
      | name          | age |
      | Power plant 1 | 854 |
      | Power plant 2 | 473 |
    And a span of 2 days
    Then the following age for the power plants
      | name          | age |
      | Power plant 1 | 856 |
      | Power plant 2 | 475 |
    And  the response provide the produced-kwh of "4.524645"

  Scenario: Upload invalid file
    Given a new simulation
    When a span of 2 days
    And upload an empty file
    Then the response should contain a validation error with message "File is empty"

  Scenario: Upload invalid JSON file
    Given a new simulation
    When a span of 2 days
    And upload a file with invalid JSON
    Then the response should contain a validation error with message "Invalid JSON format"

  Scenario: Upload file with negative days
    Given a new simulation
    When upload a file with the following content and negative days:
      | name          | age |
      | Power plant 1 | 854 |
      | Power plant 2 | 473 |
    Then the response should contain a validation error with message "Invalid number of days. Must be at least 1 and less than 9125"

  Scenario: Upload file with duplicate records
    Given a new simulation
    When upload a file with the following content:
      | name            | age |
      | Power plant 1 | 854 |
      | Power plant 1 | 473 |
    And a span of 2 days
    Then the following age for the power plants
      | name          | age |
      | Power plant 1 | 475 |
    And  the response provide the produced-kwh of "2.268280"

  Scenario: New Power plant no ready to produce energy
    Given a new simulation
    When upload a file with the following content:
      | name          | age |
      | Power plant 1 | 58  |
    And a span of 1 days
    Then the following age for the power plants
      | name          | age |
      | Power plant 1 | 59  |
    And  the response provide the produced-kwh of "0"

  Scenario: A Power plant is too old to produce energy
    Given a new simulation
    When upload a file with the following content:
      | name          | age |
      | Power plant 1 | 60  |
    And a span of 9126 days
    Then the following age for the power plants
      | name          | age  |
      | Power plant 1 | 9186 |
    And  the response provide the produced-kwh of "0"