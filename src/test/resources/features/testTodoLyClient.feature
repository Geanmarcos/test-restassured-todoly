@TestCrudClean
Feature: testTodoLy


  Scenario Outline: Como usuario quiero hacer el CRUD de un proyecto por API
    #create project
    Given i have acces to todo.ly
    When i send a POST request to "/api/projects.json" with body
     """
     {
      "Content": "<cContentProject>",
      "Icon": <cIconProject>
     }
     """
    Then response code is "<statusCode>"
    And the attribute string "Content" is "<cContentProject>"
    And the attribute int "Icon" is "<cIconProject>"
    And i save the value of "Id" in the variable "projectId"
    And the response matches the schema "<pathSchemaProject>"

    #update project
    When i send a PUT request to "/api/projects/{projectId}.json" with body
    """
     {
      "Content": "<uContentProject>",
      "Icon": <uIconProject>
     }
     """
    Then response code is "<statusCode>"
    And the attribute string "Content" is "<uContentProject>"
    And the attribute int "Icon" is "<uIconProject>"
    And the response matches the schema "<pathSchemaProject>"

    #read project
    When i send a GET request to "/api/projects/{projectId}.json" with body
    """
    """
    Then response code is "<statusCode>"
    And the attribute string "Content" is "<uContentProject>"
    And the attribute int "Icon" is "<uIconProject>"
    And the response matches the schema "<pathSchemaProject>"

    #create item
    When i send a POST request to "/api/items.json" with body
    """
    {
    "Content": "<cContentItem>",
    "ProjectId": {projectId}
    }
    """
    Then response code is "<statusCode>"
    And the attribute string "Content" is "<cContentItem>"
    And the attribute boolean "Checked" is "<checkedItem>"
    And the attribute int "ProjectId" is "{projectId}"
    And the response matches the schema "<pathSchemaItem>"
    And i save the value of "Id" in the variable "itemId"

    #update item
    When i send a PUT request to "/api/items/{itemId}.json" with body
    """
    {
    "Content": "<uContentItem>",
    "ProjectId": {projectId},
    "Checked": "<uCheckedItem>",
    }
    """
    Then response code is "<statusCode>"
    And the attribute string "Content" is "<uContentItem>"
    And the attribute boolean "Checked" is "<uCheckedItem>"
    And the attribute int "ProjectId" is "{projectId}"
    And the response matches the schema "<pathSchemaItem>"

    #search item
    When i send a GET request to "/api/items/{itemId}.json" with body
    """
    """
    Then response code is "<statusCode>"
    And the attribute string "Content" is "<uContentItem>"
    And the attribute boolean "Checked" is "<uCheckedItem>"
    And the attribute int "ProjectId" is "{projectId}"
    And the response matches the schema "<pathSchemaItem>"

    #detele item
    When i send a DELETE request to "/api/items/{itemId}.json" with body
    """
    """
    Then response code is "<statusCode>"
    And the attribute string "Content" is "<uContentItem>"
    And the attribute boolean "Checked" is "<uCheckedItem>"
    And the attribute int "ProjectId" is "{projectId}"
    And the attribute boolean "Deleted" is "<deleteItem>"
    And the response matches the schema "<pathSchemaItem>"

    #delete project
    When i send a DELETE request to "/api/projects/{projectId}.json" with body
    """
    """
    Then response code is "<statusCode>"
    And the attribute string "Content" is "<uContentProject>"
    And the attribute int "Icon" is "<uIconProject>"
    And the attribute boolean "Deleted" is "<deleteProject>"
    And the response matches the schema "<pathSchemaProject>"
    Examples:
      | cContentProject | cIconProject | statusCode | pathSchemaProject                | uContentProject | uIconProject | cContentItem | checkedItem | pathSchemaItem                | uContentItem | uCheckedItem | deleteItem | deleteProject |
      | GRUPO 3         | 1            | 200        | schemas/createProjectSchema.json | U GRUPO 3       | 2            | TALLER 03    | false       | schemas/createItemSchema.json | U TALLER 3   | true         | true       | true          |
      | TEST 3          | 1            | 200        | schemas/createProjectSchema.json | U TEST 3        | 2            | TALLER 03    | false       | schemas/createItemSchema.json | U TALLER 3   | true         | true       | true          |

