Feature: Creation and viewing of Auctions
  Background:
    Given all requests are made with a valid user

  Scenario: Creating an Auction
    Given the auction does not already exist
    When a user creates an auction
    Then other users can see the auction

  Scenario: Placing a bid to the auction
    Given an auction exists for an ipad
    When a user makes a bid
    Then the bid is accepted
    And the bid is viewable by others

  Scenario: Retrieving all auctions
    Given multiple auctions exist
    When a user requests all auctions
    Then the auctions are returned

  # todo: removal of bids
  # todo: can't bid for less than last time
  # todo: bid for auction that does not exist