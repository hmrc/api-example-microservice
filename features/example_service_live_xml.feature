Feature: Fetch Example XML for Live

  Background:
    Given header 'Accept' is 'valid xml'

  Scenario: Fetch World for Live
    And I GET the LIVE resource '/world'
    Then the status code should be 'OK'
    And I should receive XML response:
    """
    <?xml version='1.0' encoding='ISO-8859-1'?>
<Hello><message>Hello World</message></Hello>
    """

  Scenario: Fetch User for Live
    And I GET the LIVE resource '/user'
    Then the status code should be 'OK'
    And I should receive XML response:
    """
    <?xml version='1.0' encoding='ISO-8859-1'?>
<Hello><message>Hello User</message></Hello>
    """

  Scenario: Fetch Application for Live
    And I GET the LIVE resource '/application'
    Then the status code should be 'OK'
    And I should receive XML response:
    """
    <?xml version='1.0' encoding='ISO-8859-1'?>
<Hello><message>Hello Application</message></Hello>
    """
