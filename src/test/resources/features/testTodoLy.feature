@TestCrud
Feature: testTodoLy

  Background: Obtengo el token de sesion
    Given que tengo acceso a todoly

  Scenario Outline: Como usuario quiero crear proyecto en todo.ly
    When envio el post request "<postProject>" y formato "<formato>"
      """
      {
        "Content": "Rest JSON API PATH",
        "Icon": 2
      }
      """
    Then el codigo de respuesta es <statusCode>
    And el schema "<pathSchema>" es el esperado
    And el nombre del proyecto es "<content>"
    And el icono del proyecto deberia ser <icono>
    And guardo el identificador de la llave "<llave>"

    Examples:
      | postProject | formato | statusCode | pathSchema                       | content            | icono | llave |
      | /projects   | .json   | 200        | schemas/createProjectSchema.json | Rest JSON API PATH | 2     | Id    |

  Scenario Outline: Como usuario quiero actualizar proyecto en todo.ly
    When envio el put request "<postProject>", formato "<formato>" y Id de proyecto
      """
      {
        "Content": "Rest JSON API PATH v2",
        "Icon": 3
      }
      """
    Then el codigo de respuesta es <statusCode>
    And el schema "<pathSchema>" es el esperado
    And el nombre del proyecto es "<content>"
    And el icono del proyecto deberia ser <icono>

    Examples:
      | postProject | formato | statusCode | pathSchema                       | content               | icono |
      | /projects/  | .json   | 200        | schemas/createProjectSchema.json | Rest JSON API PATH v2 | 3     |

  Scenario Outline: Como usuario quiero eliminar proyecto en todo.ly
    When envio el delete request "<postProject>", formato "<formato>" y Id de proyecto
    Then el codigo de respuesta es <statusCode>
    And el schema "<pathSchema>" es el esperado
    And el nombre del proyecto es "<content>"
    And el icono del proyecto deberia ser <icono>
    And la llave deleted tiene valor "<delete>"

    Examples:
      | postProject | formato | statusCode | pathSchema                       | content               | icono | delete |
      | /projects/  | .json   | 200        | schemas/createProjectSchema.json | Rest JSON API PATH v2 | 3     | true   |
