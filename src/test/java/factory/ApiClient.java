package factory;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> variables = new HashMap<>();

    public Response send(String method, String path, String body) {

        String url = "https://todo.ly" + resolverVariable(path);
        String resolvedBody = resolverVariable(body);

        RequestSpecification request =
                given()
                        .headers(headers)
                        .log().all();

        if (resolvedBody != null && !resolvedBody.isBlank()) {
            request.body(resolvedBody);
        }

        Response response = switch (method.toUpperCase()) {
            case "GET"      -> request.get(url);
            case "POST"     -> request.post(url);
            case "PUT"      -> request.put(url);
            case "DELETE"   -> request.delete(url);
            default -> throw new IllegalArgumentException("Método http no soportado");
        };

        response.then().log().all();
        return response;
    }

    public Response send(String method, String path) {
        return send(method, path, null);
    }

    public void addVariable(String key, String value) {
        variables.put(key, value);
    }

    public void addHeaders(String key, String value) {
        headers.put(key, value);
    }

    public String resolverVariable(String text) {
        if (text == null) {
            return null;
        }
        for (var entry : variables.entrySet()) {
            text = text.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return text;
    }

}