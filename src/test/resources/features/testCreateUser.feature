@SmokeTest
Feature: testCreateUser

  Scenario: Como usuario quiero ingresar un mail y password para registrarlos
    Given que tengo acceso a un api
    When me registro con
      | nombre    | carlos |
      | apellidos | perez  |
      | telefono  | 123    |
      | direccion | peru   |
      | dni       | 12345  |
    Then registro de usuarios exitosos


  Scenario: Como usuario quiero ingresar un mail y password para registrarlos
    Given que tengo acceso a todoly
    When me registro usando
      | nombre     | apellidos | telefono  | direccion   | dni      |
      | Geanmarcos | Tataje    | 977813684 | Lurin       | 62477196 |
      | Pepito     | Tataje    | 977813685 | Lima        | 62477196 |
      | Yusuke     | Tataje    | 977813687 | la Victoria | 62477196 |
    Then registro de usuarios exitosos

  Scenario: Como usuario quiero ingresar un mail y password para registrar
    Given que tengo acceso a todoly
    Then deberia ver sus terminos de aceptacion
    """
    termino 1
    termino 2
    termino 3
    termino 4
    termino 5
    """

