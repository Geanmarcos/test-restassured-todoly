@TestCrudClean
Feature: testTodoLy

  Scenario: Como usuario quiero hacer el CRUD de un proyecto por API
    #create project
    Given i have acces to todo.ly
    When i send a POST request to "/api/projects.json" with body
     """
     {
      "Content": "CleanV1",
      "Icon": 10
     }
     """
    Then response code is 200
    And the attribute string "Content" is "CleanV1"
    And the attribute int "Icon" is "10"
    And i save the value of "Id" in the variable "projectId"
    And the response matches the schema "schemas/createProjectSchema.json"

    #update project
    When i send a PUT request to "/api/projects/{projectId}.json" with body
    """
     {
      "Content": "CleanV2",
      "Icon": 11
     }
     """
    Then response code is 200
    And the attribute string "Content" is "CleanV2"
    And the attribute int "Icon" is "11"
    And the response matches the schema "schemas/createProjectSchema.json"

    #read project
    When i send a GET request to "/api/projects/{projectId}.json" with body
    """
    """
    Then response code is 200
    And the attribute string "Content" is "CleanV2"
    And the attribute int "Icon" is "11"
    And the response matches the schema "schemas/createProjectSchema.json"

    #create item
    When i send a POST request to "/api/items.json" with body
    """
    {
    "Content": "Pepito",
    "ProjectId": {projectId}
    }
    """
    Then response code is 200
    And the attribute string "Content" is "Pepito"
    And the attribute boolean "Checked" is "false"
    And the attribute int "ProjectId" is "{projectId}"
    And the response matches the schema "schemas/createItemSchema.json"

    #delete project
    When i send a DELETE request to "/api/projects/{projectId}.json" with body
    """
    """
    Then response code is 200
    And the attribute string "Content" is "CleanV2"
    And the attribute int "Icon" is "11"
    And the response matches the schema "schemas/createProjectSchema.json"


