package basicRestAssured;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class basicRestAssured {

    @Test
    public void crearProyecto() {
        given()
                .auth()
                .preemptive()
                .basic(
                        "geanmarcos.tataje@gmail.com", "TestingJB$."
                )
                .body(
                        """
                                        {
                                            "Content": "RestAssured",
                                            "Icon": 2
                                        }
                                """
                )
                .log()
                .all()
         .when()
                .post("https://todo.ly/api/projects.json");
    }

    @Test
    public void crearProyectoJson(){
        JSONObject payload = new JSONObject();
        payload.put("Content", "Rest Assured");
        payload.put("Icon", 1);

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
                .post("https://todo.ly/api/projects.json");
    }

    @Test
    public void crearProyectoArchivoJson(){

        String rutaJsonFile = getClass()
                .getClassLoader()
                        .getResource("createProject.json")
                                .getPath();

        Response response =
        given()
                .auth()
                .preemptive()
                .basic(
                        "geanmarcos.tataje@gmail.com", "TestingJB$."
                )
                .body(new File(rutaJsonFile))
                .log()
                .all()
                .when()
                .post("https://todo.ly/api/projects.json")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("Content", equalTo("Rest JSON API PATH"))
                .body("Icon", equalTo(2))
                .time(lessThan(5000L))
                .extract()
                .response();

        int id = response.jsonPath().getInt("Id");
        String content = response.jsonPath().getString("Content");
        int icon = response.jsonPath().getInt("Icon");

        System.out.println(id);
        System.out.println(content);
        System.out.println(icon);
    }

}
