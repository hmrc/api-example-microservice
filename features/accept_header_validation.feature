Feature: Validation of Accept Header


  Scenario: Get UNSUPPORTED_MEDIA_TYPE response when header Accept is missing
    Given header 'Accept' is 'not provided'
    When I GET the SANDBOX resource '/sandbox/world'
    Then the status code should be 'NOT_ACCEPTABLE'
    And I should receive JSON response:
    """
    {
        "code": "ACCEPT_HEADER_INVALID",
        "message": "The accept header is missing or invalid"
    }
    """


  Scenario: Get UNSUPPORTED_MEDIA_TYPE response when header Accept is bad formatted
    Given header 'Accept' is 'bad formatted'
    When I GET the SANDBOX resource '/sandbox/world'
    Then the status code should be 'NOT_ACCEPTABLE'
    And I should receive JSON response:
    """
    {
        "code": "ACCEPT_HEADER_INVALID",
        "message": "The accept header is missing or invalid"
    }
    """
