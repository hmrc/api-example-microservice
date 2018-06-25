Feature: Fetch Example for Live

  Background:
    Given header 'Accept' is 'valid json'

  Scenario: Fetch World for Live
    And I GET the LIVE resource '/world'
    Then the status code should be 'OK'
    And I should receive JSON response:
    """
    {
      "message": "Hello World"
    }
    """


  Scenario: Fetch User for Live
    And I GET the LIVE resource '/user'
    Then the status code should be 'OK'
    And I should receive JSON response:
    """
    {
      "message": "Hello User"
    }
    """

  Scenario: Fetch Application for Live
    And I GET the LIVE resource '/application'
    Then the status code should be 'OK'
    And I should receive JSON response:
    """
    {
      "message": "Hello Application"
    }
    """
