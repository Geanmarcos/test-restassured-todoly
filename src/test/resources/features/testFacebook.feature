Feature: testFacebook

  Background: Login
    Given que tengo acceso a facebook
    When ingreso mi email: "<email>"
    And ingreso mi password: "<password>"

  Scenario Outline: Como usuario quiero ingresar un mail y password para iniciar sesion
    Then hago click en el boton iniciar sesion
    And muestra la pagina principal
    And deberia ver los siguientes menus
    |configuracion|
    |publicaciones|
    |menu principal|
    Examples:
      | email                | password    |
      | geanmarcos@gmail.com | password123 |

