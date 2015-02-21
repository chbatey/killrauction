Feature: User creation
  Scenario: Create new User
    Given the user name does not exist
    When a user is created
    Then then the user creation is successful

  Scenario: Create new User with a taken username