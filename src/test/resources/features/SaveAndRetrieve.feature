Feature: As a user, I want to be able to load a network of power plants into the application using an HTTP POST request and ,
  I want to be able to query the network's output and its state as time progresses

  Scenario: Saving and Retrieving Power plants
    Given a new simulation
    When adding this power plants:
      | name          | age |
      | Power plant 1 | 854 |
      | Power plant 2 | 473 |
    And Retrieving the network at 10 days
    Then The response should contain the following data:
      | name          | age | outputInKwh |
      | Power plant 1 | 864 | 2.256114    |
      | Power plant 2 | 483 | 2.268030    |

  Scenario: Saving and Retrieving the total
    Given a new simulation
    When adding this power plants:
      | name          | age |
      | Power plant 1 | 854 |
      | Power plant 2 | 473 |
    And checking the output off all power plants at 10 days
    Then The total output is 4.524144
