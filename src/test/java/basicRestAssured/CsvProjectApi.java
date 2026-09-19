package basicRestAssured;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CsvProjectApi {

    private static int projectId;

    @CsvFileSource(resources = "/data/crear-proyecto.csv",numLinesToSkip = 1)
    @ParameterizedTest
    public void crearProjectCsv(String content, int icon, int statusCode){

        JSONObject payload = new JSONObject();

        payload.put("Content", content);
        payload.put("Icon", icon);

        Response response =
                given()
                        .auth()
                        .preemptive()
                        .basic(
                                "geanmarcos.tataje@gmail.com", "TestingJB$."
                        )
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
