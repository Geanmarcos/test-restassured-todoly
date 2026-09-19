package basicRestAssured;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CrudProjectApi {

    private static int projectId;

    @Order(1)
    @Test
    public void crearProject(){
        JSONObject payload = new JSONObject();

        payload.put("Content", "CRUD");
        payload.put("Icon", 3);

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
                        .statusCode(200)
                        .body("Content", equalTo("CRUD"))
                        .body("Icon", equalTo(3))
                        .time(lessThan(5000L))
                        .extract()
                        .response();

        projectId = response.jsonPath().getInt("Id");
        String content = response.jsonPath().getString("Content");
        int icon = response.jsonPath().getInt("Icon");

        System.out.println(projectId);
        System.out.println(content);
        System.out.println(icon);


    }

    @Order(2)
    @Test
    public void actualizarProjec(){
        JSONObject payload = new JSONObject();

        payload.put("Content", "CRUD UPDATE");
        payload.put("Icon", 1);

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
                        .put("https://todo.ly/api/projects/"+ projectId + ".json")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .body("Content", equalTo("CRUD UPDATE"))
                        .body("Icon", equalTo(1))
                        .time(lessThan(5000L))
                        .extract()
                        .response();

        projectId = response.jsonPath().getInt("Id");
        String content = response.jsonPath().getString("Content");
        int icon = response.jsonPath().getInt("Icon");

        System.out.println(projectId);
        System.out.println(content);
        System.out.println(icon);
    }

    @Order(3)
    @Test
    public void buscarProject(){

        Response response =
                given()
                        .auth()
                        .preemptive()
                        .basic(
                                "geanmarcos.tataje@gmail.com", "TestingJB$."
                        )
                        .log()
                        .all()
                        .when()
                        .get("https://todo.ly/api/projects/"+ projectId + ".json")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .body("Content", equalTo("CRUD UPDATE"))
                        .body("Icon", equalTo(1))
                        .time(lessThan(5000L))
                        .extract()
                        .response();

        projectId = response.jsonPath().getInt("Id");
        String content = response.jsonPath().getString("Content");
        int icon = response.jsonPath().getInt("Icon");

        System.out.println(projectId);
        System.out.println(content);
        System.out.println(icon);
    }

    @Order(4)
    @Test
    public void eliminarProjecto(){

        Response response =
                given()
                        .auth()
                        .preemptive()
                        .basic(
                                "geanmarcos.tataje@gmail.com", "TestingJB$."
                        )
                        .log()
                        .all()
                        .when()
                        .delete("https://todo.ly/api/projects/"+ projectId + ".json")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .body("Content", equalTo("CRUD UPDATE"))
                        .body("Icon", equalTo(1))
                        .body("Deleted", equalTo(true))
                        .time(lessThan(5000L))
                        .extract()
                        .response();

        projectId = response.jsonPath().getInt("Id");
        String content = response.jsonPath().getString("Content");
        int icon = response.jsonPath().getInt("Icon");
        String deleted = response.jsonPath().getString("Deleted");


        System.out.println(projectId);
        System.out.println(content);
        System.out.println(icon);
        System.out.println(deleted);

    }
}
