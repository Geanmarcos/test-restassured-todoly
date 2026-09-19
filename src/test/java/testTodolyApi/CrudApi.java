package testTodolyApi;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CrudApi {

    private static String URL_BASE = "https://todo.ly/api";
    private static String token;
    private static String usuario;
    private static String clave;
    private static JsonSchemaFactory factory;
    private static String pathSchemaProject = "schemas/createProjectSchema.json";
    private static String pathSchemaItem = "schemas/createItemSchema.json";

    @BeforeAll
    static void authenticationToken() throws IOException {
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
                        .get("https://todo.ly/api//authentication/token.json")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract().response();

        token = response.jsonPath().getString("TokenString");

    }

    @CsvFileSource(resources = "/data/crud-todoly.csv", numLinesToSkip = 1)
    @ParameterizedTest
    public void crudScenario(
            String cContentProject,
            int cIconProject,
            String uContentProject,
            int uIconProject,
            String cContentItem,
            String uContentItem,
            boolean uCheckedItem,
            int statusCode) {

        int projectId;
        int itemId;
        JSONObject payload = new JSONObject();

        payload.put("Content", cContentProject);
        payload.put("Icon", cIconProject);

        // Crea el proyecto asociado a la fila actual del CSV.
        Response response = given()
                .header("Token", token)
                .body(payload.toString())
                .log()
                .all()
                .when()
                .post(URL_BASE + "/projects.json")
                .then()
                .log()
                .all()
                .statusCode(statusCode)
                .body("Content", equalTo(cContentProject))
                .body("Icon", equalTo(cIconProject))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(pathSchemaProject).using(factoryJson()))
                .time(lessThan(5000L))
                .extract()
                .response();
        projectId = response.jsonPath().getInt("Id");

        payload = new JSONObject();
        payload.put("Content", uContentProject);
        payload.put("Icon", uIconProject);

        // Actualiza el proyecto recién creado.
        given()
                .header("Token", token)
                .body(payload.toString())
                .log()
                .all()
                .when()
                .put(URL_BASE + "/projects/" + projectId + ".json")
                .then()
                .log()
                .all()
                .statusCode(statusCode)
                .body("Content", equalTo(uContentProject))
                .body("Icon", equalTo(uIconProject))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(pathSchemaProject).using(factoryJson()))
                .time(lessThan(5000L));

        // Consulta el proyecto para validar sus datos actualizados.
        given()
                .header("Token", token)
                .log()
                .all()
                .when()
                .get(URL_BASE + "/projects/" + projectId + ".json")
                .then()
                .log()
                .all()
                .statusCode(statusCode)
                .body("Content", equalTo(uContentProject))
                .body("Icon", equalTo(uIconProject))
                .time(lessThan(5000L));

        payload = new JSONObject();
        payload.put("Content", cContentItem);
        payload.put("ProjectId", projectId);

        // Crea un item dentro del proyecto actual.
        response = given()
                .header("Token", token)
                .body(payload.toString())
                .log()
                .all()
                .when()
                .post(URL_BASE + "/items.json")
                .then()
                .log()
                .all()
                .statusCode(statusCode)
                .body("Content", equalTo(cContentItem))
                .body("ProjectId", equalTo(projectId))
                .body("Checked", equalTo(false))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(pathSchemaItem).using(factoryJson()))
                .time(lessThan(5000L))
                .extract()
                .response();
        itemId = response.jsonPath().getInt("Id");

        payload = new JSONObject();
        payload.put("Content", uContentItem);
        payload.put("Checked", uCheckedItem);
        payload.put("ProjectId", projectId);

        // Actualiza el contenido y el estado del item.
        given()
                .header("Token", token)
                .body(payload.toString())
                .log()
                .all()
                .when()
                .put(URL_BASE + "/items/" + itemId + ".json")
                .then()
                .log()
                .all()
                .statusCode(statusCode)
                .body("Content", equalTo(uContentItem))
                .body("ProjectId", equalTo(projectId))
                .body("Checked", equalTo(uCheckedItem))
                .body("LastCheckedDate", notNullValue())
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(pathSchemaItem).using(factoryJson()))
                .time(lessThan(5000L));

        // Consulta el item para validar sus datos actualizados.
        given()
                .header("Token", token)
                .log()
                .all()
                .when()
                .get(URL_BASE + "/items/" + itemId + ".json")
                .then()
                .log()
                .all()
                .statusCode(statusCode)
                .body("Content", equalTo(uContentItem))
                .body("ProjectId", equalTo(projectId))
                .body("Checked", equalTo(uCheckedItem))
                .body("LastCheckedDate", notNullValue())
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(pathSchemaItem).using(factoryJson()))
                .time(lessThan(5000L));

        // Elimina el item y valida que la API lo marque como eliminado.
        given()
                .header("Token", token)
                .log()
                .all()
                .when()
                .delete(URL_BASE + "/items/" + itemId + ".json")
                .then()
                .log()
                .all()
                .statusCode(statusCode)
                .body("Content", equalTo(uContentItem))
                .body("ProjectId", equalTo(projectId))
                .body("Checked", equalTo(uCheckedItem))
                .body("LastCheckedDate", notNullValue())
                .body("Deleted", equalTo(true))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(pathSchemaItem).using(factoryJson()))
                .time(lessThan(5000L));

        // Elimina el proyecto al finalizar el escenario.
        given()
                .header("Token", token)
                .log()
                .all()
                .when()
                .delete(URL_BASE + "/projects/" + projectId + ".json")
                .then()
                .log()
                .all()
                .statusCode(statusCode)
                .body("Content", equalTo(uContentProject))
                .body("Icon", equalTo(uIconProject))
                .body("Deleted", equalTo(true))
                .time(lessThan(5000L));
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
