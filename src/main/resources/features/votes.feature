Feature: List votes
  Background:
    Given x-api-key and baseURI are already acquired.

  @votes
  Scenario Outline: Creating votes and Listing for all votes
    When I will create one more vote for this "<sub_id>" and "<image_id>"
    Then I should see "<sub_id>" and "<image_id>"
    Examples:
      | sub_id       | image_id |
      | my-user-1234 | aa22bb   |
      | onur         | c2b3a1   |
      | user_1234    | a1b2c3   |