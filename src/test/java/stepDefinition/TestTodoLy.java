package stepDefinition;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class TestTodoLy {

    private static String URL_BASE = "https://todo.ly/api";
    private static String token;
    private static String usuario;
    private static String clave;
    private static Response response;
    private static JsonSchemaFactory factory;
    private static int projectId;

    @Given("que tengo acceso a todoly")
    public void queTengoAccesoATodoly() {
        usuario = System.getenv("USERNAME_TODOLY");
        clave = System.getenv("PASSWORD_TODOLY");

        Response response =
                given()
                        .auth()
                        .preemptive()
                        .basic(usuario, clave)
                        .log()
                        .all()
                        .when()
                        .get(URL_BASE+"/authentication/token.json")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract().response();

        token = response.jsonPath().getString("TokenString");
    }

    @When("envio el post request {string} y formato {string}")
    public void envioElPostRequestALaUrl(String createProject, String formato, String body) {
        response =
                        given()
                                .header("Token", token)
                                .body(body)
                                .log()
                                .all()

                        .when()
                                .post(URL_BASE+ createProject + formato)
                        .then()
                                .log()
                                .all()
                                .extract()
                                .response();
    }

    @Then("el codigo de respuesta es {int}")
    public void elCodigoDeRespuestaEs(int statusCodeEsperado) {
        response
                .then()
                .statusCode(statusCodeEsperado);
    }

    @And("el schema {string} es el esperado")
    public void elSchemaEsElEsperado(String pathSchema) {
        response
                .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(pathSchema).using(factoryJson()));
    }

    @And("el nombre del proyecto es {string}")
    public void elNombreDelProyectoEs(String cContentProject) {
        response
                .then()
                .body("Content", equalTo(cContentProject));
    }

    @And("el icono del proyecto deberia ser {int}")
    public void elIconoDelProyectoDeberiaSer(int cIconProject) {
        response
                .then()
                .body("Icon", equalTo(cIconProject));
    }

    @And("guardo el identificador de la llave {string}")
    public void guardoElIdentificadorDeLaLlave(String llaveId) {
        projectId = response.jsonPath().getInt(llaveId);
    }

    @When("envio el put request {string}, formato {string} y Id de proyecto")
    public void envioElPostRequestConYFormato(String updateProject, String formato, String body) {
        response =
                        given()
                            .header("Token", token)
                            .body(body)
                            .log()
                            .all()
                        .when()
                            .put(URL_BASE + updateProject + projectId + formato)
                        .then()
                            .log()
                            .all()
                            .extract()
                            .response();
    }

    @When("envio el delete request {string}, formato {string} y Id de proyecto")
    public void envioElDeleteRequestFormatoYIdDeProyecto(String deleteProject, String formato) {
        response =
                given()
                        .header("Token", token)
                            .log()
                            .all()
                        .when()
                            .delete(URL_BASE + deleteProject + projectId + formato)
                        .then()
                            .log()
                            .all()
                            .extract()
                            .response();
    }

    @And("la llave deleted tiene valor {string}")
    public void laLlaveDeletedTieneValor(String valorDeleted) {
        boolean valor = Boolean.parseBoolean(valorDeleted);
        response
                .then()
                .body("Deleted", equalTo(valor));
    }

    private static JsonSchemaFactory factoryJson(){
        factory = JsonSchemaFactory
                .newBuilder()
                .setValidationConfiguration(
                        ValidationConfiguration
                                .newBuilder()
                                .setDefaultVersion(SchemaVersion.DRAFTV4)
                                .freeze()
                ).freeze();

        return  factory;
    }

}
