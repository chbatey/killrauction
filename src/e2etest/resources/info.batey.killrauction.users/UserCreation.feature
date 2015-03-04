Feature: User creation

  Scenario: Create new User
    Given the user name does not exist
    When a user is created
    Then then the user creation is successful

  Scenario: Create new User and use it
    Given a user is created
    And that user logs in
    When an auction is created
    Then then the auction is created

  Scenario: Create an existing user
    Given the user already exists
    When a user is created
    Then the request is rejected due to user already existing