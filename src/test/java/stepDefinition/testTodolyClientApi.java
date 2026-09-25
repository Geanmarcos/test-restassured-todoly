package stepDefinition;

import factory.ApiClient;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;

import java.util.Base64;

public class testTodolyClientApi {
    private ApiClient client = new ApiClient();
    private Response response;

    @Given("i have acces to todo.ly")
    public void iHaveAccesToTodoLy() {
        String credenciales = "geanmarcos.tataje@gmail.com:TestingJB$.";
        client.addHeaders("Authorization", "Basic " + Base64.getEncoder().encodeToString(credenciales.getBytes()));
    }

    @When("i send a {word} request to {string} with body")
    public void iSendAPOSTRequestToWithBody(String method, String path, String body) {
        response = client.send(method, path, body);
    }

    @Then("response code is {int}")
    public void responseCodeIs(int responseCodeExpected) {
        response.then().statusCode(responseCodeExpected);
    }

    @And("the attribute {word} {string} is {string}")
    public void theAttributeStringIs(String type, String attribute, String expectedResult) {
        switch (type.toLowerCase()) {
            case "int" -> response.then().body(attribute, equalTo(Integer.parseInt(expectedResult)));
            case "boolean" -> response.then().body(attribute, equalTo(Boolean.parseBoolean(expectedResult)));
            case "string" -> response.then().body(attribute, equalTo(expectedResult));
        }
    }

    @And("i save the value of {string} in the variable {string}")
    public void iSaveTheValueOfInTheVariable(String jsonVariable, String variableName) {
        String value = response.jsonPath().getString(jsonVariable);
        client.addVariable(variableName, value);
    }
}
