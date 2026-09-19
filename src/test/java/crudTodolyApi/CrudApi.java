package crudTodolyApi;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@CsvFileSource(resources = "/data/crud-todoly.csv", numLinesToSkip = 1)
public class CrudApi {

    private static String URL_BASE = "https://todo.ly/api";
    private static String token;
    private static String usuario;
    private static String clave;
    private static int projectId;
    private static int itemId;
    private static JsonSchemaFactory factory;
    private static String pathSchemaProject = "schemas/createProjectSchema.json";
    private static String pathSchemaItem = "schemas/createItemSchema.json";

    JSONObject payload = new JSONObject();

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

    @Order(1)
    @ParameterizedTest
    public void createProject(String cContentProject, int cIconProject, int statusCode){

        payload.put("Content", cContentProject);
        payload.put("Icon", cIconProject);

        Response response =
                        given()
                                .header("Token", token)
                                .body(payload.toString())
                                .log()
                                .all()
                        .when()
                                .post(URL_BASE+"/projects.json")
                        .then()
                                .log()
                                .all()
                                .statusCode(statusCode)
                                .body("Content", equalTo(cContentProject))
                                .body("Icon", equalTo(cIconProject))
                                .body( JsonSchemaValidator
                                            .matchesJsonSchemaInClasspath(pathSchemaProject)
                                            .using(factoryJson()))
                                .time(lessThan(5000L))
                                .extract()
                                .response();

        projectId = response.jsonPath().getInt("Id");
        String responseContent = response.jsonPath().getString("Content");
        int responseIcon = response.jsonPath().getInt("Icon");

        System.out.println(projectId);
        System.out.println(responseContent);
        System.out.println(responseIcon);
    }

    @Order(2)
    @ParameterizedTest
    public void updateProjec(String uContendProject, int uIconProject, int statusCode){

        payload.put("Content", uContendProject);
        payload.put("Icon", uIconProject);

        Response response =
                        given()
                                .header("Token", token)
                                .body(payload.toString())
                                .log()
                                .all()
                        .when()
                                .put(URL_BASE +"/projects/"+ projectId + ".json")
                        .then()
                                .log()
                                .all()
                                .statusCode(statusCode)
                                .body("Content", equalTo(uContendProject))
                                .body("Icon", equalTo(uIconProject))
                                .body( JsonSchemaValidator
                                        .matchesJsonSchemaInClasspath(pathSchemaProject)
                                        .using(factoryJson()))
                                .time(lessThan(5000L))
                                .extract()
                                .response();

        String content = response.jsonPath().getString("Content");
        int icon = response.jsonPath().getInt("Icon");

        System.out.println(projectId);
        System.out.println(content);
        System.out.println(icon);
    }

    @Order(3)
    @ParameterizedTest
    public void searchProject(String uContendProject, int uIconProject, int statusCode){

        Response response =
                        given()
                                .header("Token", token)
                                .log()
                                .all()
                        .when()
                                .get(URL_BASE+"/projects/"+ projectId + ".json")
                        .then()
                                .log()
                                .all()
                                .statusCode(statusCode)
                                .body("Content", equalTo(uContendProject))
                                .body("Icon", equalTo(uIconProject))
                                .time(lessThan(5000L))
                                .extract()
                                .response();

        String content = response.jsonPath().getString("Content");
        int icon = response.jsonPath().getInt("Icon");

        System.out.println(projectId);
        System.out.println(content);
        System.out.println(icon);
    }

    @Order(4)
    @ParameterizedTest
    public void createItem(String cContentItem, int statusCode) {

        payload.put("Content", cContentItem);
        payload.put("ProjectId", projectId);

        Response response =
                        given()
                                .header("Token", token)
                                .body(payload.toString())
                                .log()
                                .all()
                        .when()
                                .post(URL_BASE+"/items.json")
                        .then()
                                .log()
                                .all()
                                .statusCode(statusCode)
                                .body("Content", equalTo(cContentItem))
                                .body("ProjectId", equalTo(projectId))
                                .body("Checked", equalTo(false))
                                .body( JsonSchemaValidator
                                        .matchesJsonSchemaInClasspath(pathSchemaItem)
                                        .using(factoryJson()))
                                .time(lessThan(5000L))
                                .extract()
                                .response();

        itemId = response.jsonPath().getInt("Id");
        String responseContent = response.jsonPath().getString("Content");
        String responseChecked = response.jsonPath().getString("Checked");

        System.out.println(projectId);
        System.out.println(itemId);
        System.out.println(responseContent);
        System.out.println(responseChecked);
    }

    @Order(5)
    @ParameterizedTest
    public void updateItem(String uContentItem, boolean uCheckedItem, int statusCode) {

        payload.put("Content", uContentItem);
        payload.put("Checked", uCheckedItem);
        payload.put("ProjectId", projectId);

        Response response =
                        given()
                                .header("Token", token)
                                .body(payload.toString())
                                .log()
                                .all()
                        .when()
                                .put(URL_BASE+"/items/"+itemId+".json")
                        .then()
                                .log()
                                .all()
                                .statusCode(statusCode)
                                .body("Content", equalTo(uContentItem))
                                .body("ProjectId", equalTo(projectId))
                                .body("Checked", equalTo(uCheckedItem))
                                .body("LastCheckedDate", notNullValue())
                                .body( JsonSchemaValidator
                                        .matchesJsonSchemaInClasspath(pathSchemaItem)
                                        .using(factoryJson()))
                                .time(lessThan(5000L))
                                .extract()
                                .response();

        String responseContent = response.jsonPath().getString("Content");
        String responseChecked = response.jsonPath().getString("Checked");
        String LastCheckedDate = response.jsonPath().getString("LastCheckedDate");

        System.out.println(projectId);
        System.out.println(itemId);
        System.out.println(responseContent);
        System.out.println(responseChecked);
        System.out.println(LastCheckedDate);
    }

    @Order(6)
    @ParameterizedTest
    public void searchItem(String uContentItem, boolean uCheckedItem, int statusCode){
        Response response =
                        given()
                                .header("Token", token)
                                .log()
                                .all()
                        .when()
                                .get(URL_BASE+"/items/"+itemId+".json")
                        .then()
                                .log()
                                .all()
                                .statusCode(statusCode)
                                .body("Content", equalTo(uContentItem))
                                .body("ProjectId", equalTo(projectId))
                                .body("Checked", equalTo(uCheckedItem))
                                .body("LastCheckedDate", notNullValue())
                                .body( JsonSchemaValidator
                                        .matchesJsonSchemaInClasspath(pathSchemaItem)
                                        .using(factoryJson()))
                                .time(lessThan(5000L))
                                .extract()
                                .response();

        String responseContent = response.jsonPath().getString("Content");
        String responseChecked = response.jsonPath().getString("Checked");
        String lastCheckedDate = response.jsonPath().getString("LastCheckedDate");

        System.out.println(projectId);
        System.out.println(itemId);
        System.out.println(responseContent);
        System.out.println(responseChecked);
        System.out.println(lastCheckedDate);
    }

    @Order(7)
    @ParameterizedTest
    public void deleteItem(String uContentItem, boolean uCheckedItem, int statusCode){
        Response response =
                        given()
                                .header("Token", token)
                                .log()
                                .all()
                        .when()
                                .delete(URL_BASE+"/items/"+itemId+".json")
                        .then()
                                .log()
                                .all()
                                .statusCode(statusCode)
                                .body("Content", equalTo(uContentItem))
                                .body("ProjectId", equalTo(projectId))
                                .body("Checked", equalTo(uCheckedItem))
                                .body("LastCheckedDate", notNullValue())
                                .body("Deleted", equalTo(true))
                                .body( JsonSchemaValidator
                                        .matchesJsonSchemaInClasspath(pathSchemaItem)
                                        .using(factoryJson()))
                                .time(lessThan(5000L))
                                .extract()
                                .response();

        String responseContent = response.jsonPath().getString("Content");
        String responseChecked = response.jsonPath().getString("Checked");
        String lastCheckedDate = response.jsonPath().getString("LastCheckedDate");
        String deleted = response.jsonPath().getString("Deleted");

        System.out.println(projectId);
        System.out.println(itemId);
        System.out.println(responseContent);
        System.out.println(responseChecked);
        System.out.println(lastCheckedDate);
        System.out.println(deleted);
    }

    @Order(8)
    @ParameterizedTest
    public void deleteProject(String uContendProject, int uIconProject, int statusCode){

        Response response =
                given()
                        .header("Token", token)
                        .log()
                        .all()
                        .when()
                        .delete("https://todo.ly/api/projects/"+ projectId + ".json")
                        .then()
                        .log()
                        .all()
                        .statusCode(statusCode)
                        .body("Content", equalTo(uContendProject))
                        .body("Icon", equalTo(uIconProject))
                        .body("Deleted", equalTo(true))
                        .time(lessThan(5000L))
                        .extract()
                        .response();

        String content = response.jsonPath().getString("Content");
        int icon = response.jsonPath().getInt("Icon");
        String deleted = response.jsonPath().getString("Deleted");

        System.out.println(projectId);
        System.out.println(content);
        System.out.println(icon);
        System.out.println(deleted);

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
