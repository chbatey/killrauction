Feature: Bid streaming
  Background:
    Given all requests are made with a valid user

#Waiting for spring 4.2 to have a websocket stomp client
  Scenario: Auction with no existing bids
#    Given an auction is created
#    When a bidstream is requested
#    And a new bid is made
#    Then the bidstream contains the new bid
