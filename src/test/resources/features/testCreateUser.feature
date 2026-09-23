Feature: testCreateUser

  Scenario: Como usuario quiero ingresar un mail y password para registrarlos
    Given que tengo acceso a todoly
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
      | nombre     | apellidos | telefono  | direccion | dni      |
      | Geanmarcos | Tataje    | 977813686 | Lurin     | 62477196 |
    Then registro de usuarios exitosos

