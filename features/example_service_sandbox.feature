Feature: Fetch Hello Sandbox Sandbox

  Background:
    Given header 'Accept' is 'valid json'

  Scenario: Fetch World for Live
    And I GET the SANDBOX resource '/sandbox/world'
    Then the status code should be 'OK'
    And I should receive JSON response:
    """
    {
      "message": "Hello Sandbox World"
    }
    """


  Scenario: Fetch User for Live
    And I GET the SANDBOX resource '/sandbox/user'
    Then the status code should be 'OK'
    And I should receive JSON response:
    """
    {
      "message": "Hello Sandbox User"
    }
    """

  Scenario: Fetch Application for Live
    And I GET the SANDBOX resource '/sandbox/application'
    Then the status code should be 'OK'
    And I should receive JSON response:
    """
    {
      "message": "Hello Sandbox Application"
    }
    """
