package stepDefinition;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

public class LoginFacebook {

    @Given("que tengo acceso a facebook")
    public void queTengoAccesoAFacebook() {
        System.out.println("que tengo acceso a facebook");
    }

    @When("ingreso mi email: {string}")
    public void ingresoMiEmail(String email) {
        System.out.println("ingreso mi email: "+email);
    }

    @And("ingreso mi password: {string}")
    public void ingresoMiPassword(String password) {
        System.out.println("ingreso mi password: "+password);
    }

    @Then("hago click en el boton iniciar sesion")
    public void hagoClickEnElBotonIniciarSesion() {
        System.out.println("hago click en el boton iniciar sesion");
    }

    @And("muestra la pagina principal")
    public void muestraLaPaginaPrincipal() {
        System.out.println("muestra la pagina principal");
    }

    @And("deberia ver los siguientes menus")
    public void deberiaVerLosSiguientesMenus(List<String> menus) {
        // Write code here that turns the phrase above into concrete actions
        for (String menu : menus){
            System.out.println("menu: "+menu);
        }
    }
}
