Feature: Creation and viewing of Auctions

  Scenario: Creating an Auction
    Given the auction does not already exist
    When a user creates an auction
    Then other users can see the auction