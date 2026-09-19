package schemaValidator;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class SchemaValidatorProjectApi {

    static String token;
    static String usuario;
    static String clave;
    private static int projectId;

    @BeforeAll
    static void authenticationToken() throws IOException {

        usuario  = System.getenv("USERNAME_TODOLY");
        clave = System.getenv("PASSWORD_TODOLY");

        Response response =
                given()
                        .auth()
                        .preemptive()
                        .basic(usuario,clave)
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

    @CsvFileSource(resources = "/data/crear-proyecto.csv",numLinesToSkip = 1)
    @ParameterizedTest
    public void crearProjectCsv(String content, int icon, int statusCode){

        JsonSchemaFactory factory = JsonSchemaFactory
                .newBuilder()
                .setValidationConfiguration(
                        ValidationConfiguration
                                .newBuilder()
                                .setDefaultVersion(SchemaVersion.DRAFTV4)
                                .freeze()
                ).freeze();

        JSONObject payload = new JSONObject();
        payload.put("Content", content);
        payload.put("Icon", icon);

        Response response =
                given()
                        .header("Token", token)
                        .body(payload.toString())
                        .log()
                        .all()
                        .when()
                        .post("https://todo.ly/api/projects.json")
                        .then()
                        .log()
                        .all()
                        .statusCode(statusCode)
                        .body("Content", equalTo(content))
                        .body("Icon", equalTo(icon))
                        .body(JsonSchemaValidator
                                .matchesJsonSchemaInClasspath("schemas/createProjectSchema.json")
                                .using(factory))
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
}
