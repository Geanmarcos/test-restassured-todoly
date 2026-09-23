package stepDefinition;

import io.cucumber.java.DataTableType;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

public class CreateUser {
    @Given("que tengo acceso a todoly")
    public void queTengoAccesoATodoly() {
        System.out.println("que tengo acceso a todoly");
    }

    @When("me registro con")
    public void meRegistroCon(Map<String,String> users) {
        users.forEach(
                (key,value)->{
                    System.out.printf(" clave: "+key+" - Valor: "+value+" /");
        });
    }

    @Then("registro de usuarios exitosos")
    public void registroDeUsuariosExitosos() {
        System.out.println("registro de usuarios exitosos");
    }

    @When("me registro usando")
    public void meRegistroUsando(User user) {
        System.out.println("nombre: " + user.getNombre());
        System.out.println("apellidos: " + user.getApellidos());
        System.out.println("telefono: " + user.getTelefono());
        System.out.println("direccion: " + user.getDireccion());
        System.out.println("dni: " + user.getDni());
    }

    @DataTableType
    public User convertToUser(Map<String,String> data){
//        User user = new User();
//        user.setNombre(data.get("nombre"));
//        user.setApellidos(data.get("apellidos"));
//        user.setTelefono(data.get("telefono"));
//        user.setDireccion(data.get("direccion"));
//        user.setDni(data.get("dni"));
//        return user;

        return new User(
                data.get("nombre"),
                data.get("apellidos"),
                data.get("telefono"),
                data.get("direccion"),
                data.get("dni")
        );

    }
}
