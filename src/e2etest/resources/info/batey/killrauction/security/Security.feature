Feature: Security
  Scenario: User attempts to access service without having logged in
    Given the user has not logged in
    When an auction is created
    Then the user is rejected as not authorized

  Scenario: User attempts to access service without incorrect credentials
    Given the user provides invalid credentials
    When an auction is created
    Then the user is rejected as not authenticated