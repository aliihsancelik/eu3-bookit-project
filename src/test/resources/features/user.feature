Feature: User Verification

  @wip
  Scenario: verify information about logged user
    Given I logged Bookit api using "sbirdbj@fc2.com" and "asenorval"
    When I get the current user information from api
    Then status code should be 200

  @db
  Scenario: verify information about logged user from api and database
    Given I logged Bookit api using "sbirdbj@fc2.com" and "asenorval"
    When I get the current user information from api
    Then the information about current user from api and database should match

  @db
  Scenario Outline: Three point verification(UI,DATABASE,API)
    Given user logs in using "<email>" "<password>"
    When user is on the my self page
    And I logged Bookit api using "<email>" and "<password>"
    And I get the current user information from api
    Then UI,API and DATABASE user information must match
    Examples:
      | email                | password     |
      | sbirdbj@fc2.com      | asenorval    |
      | ccornil1h@usnews.com | corniecornil |
