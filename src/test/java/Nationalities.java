import com.github.javafaker.Faker;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
public class Nationalities {

    Faker faker = new Faker();
    RequestSpecification recSpec;

    @BeforeClass
    public void setup() {
        _00_LoginSetup.login();
        recSpec = _00_LoginSetup.getRequestSpecification();
    }

    String name;
    String id;

    @Test
    public void createNationalities() {

        Map<String, String> doc = new HashMap<>();
        doc.put("name", faker.name().name());

        Response response =
                given()
                        .spec(recSpec)
                        .body(doc)
                        .when()
                        .post("/school-service/api/nationality")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().response();

        id = response.path("id");
        name = response.path("name");



    }

    @Test(dependsOnMethods = "createNationalities")
    public void createNationalitiesNegative() {

        Map<String, String> doc = new HashMap<>();
        doc.put("name", name);


        given()
                .spec(recSpec)
                .body(doc)
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(400)
        ;
    }


    @Test(dependsOnMethods = "createNationalitiesNegative")
    public void updateNationalities() {
        Map<String, String> doc = new HashMap<>();
        doc.put("name",faker.name().fullName());
        doc.put("id", id);

        given()
                .spec(recSpec)
                .body(doc)
                .when()
                .put("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "updateNationalities")
    public void deleteNationalities() {
        given()
                .spec(recSpec)
                .when()
                .delete("/school-service/api/nationality/"+id)
                .then()
                .log().body()
                .statusCode(200)
        ;
    }


    @Test(dependsOnMethods = "deleteNationalities")
    public void deleteNationalitiesNegative() {
        given()
                .spec(recSpec)
                .when()
                .delete("/school-service/api/nationality/"+id)
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Nationality not  found"))
        ;
    }


}
