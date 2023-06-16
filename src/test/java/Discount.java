import com.github.javafaker.Faker;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

public class Discount {

    Faker faker = new Faker();
    RequestSpecification recSpec;

    @BeforeClass
    public void setup() {
        _00_LoginSetup.login();
        recSpec = _00_LoginSetup.getRequestSpecification();
    }

    String des;
    String id;

    @Test
    public void createDiscount() {

        Map<String, String> doc = new HashMap<>();
        doc.put("description", faker.name().username());
        doc.put("code", faker.number().digits(2));
        doc.put("priority", faker.number().digits(5));


        Response response =
                given()
                        .spec(recSpec)
                        .body(doc)
                        .when()
                        .post("/school-service/api/discounts")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().response();

        id = response.path("id");
        des = response.path("description");



    }

    @Test(dependsOnMethods = "createDiscount")
    public void createDiscountNegative() {

        Map<String, String> doc = new HashMap<>();
        doc.put("description",des);
        doc.put("code", faker.number().digits(2));
        doc.put("priority", faker.number().digits(5));

                given()
                        .spec(recSpec)
                        .body(doc)
                        .when()
                        .post("/school-service/api/discounts")
                        .then()
                        .log().body()
                        .statusCode(400)
                        ;
    }


    @Test(dependsOnMethods = "createDiscountNegative")
    public void updateDiscount() {
        Map<String, String> doc = new HashMap<>();
        doc.put("description",faker.name().fullName());
        doc.put("code", faker.number().digits(2));
        doc.put("id", id);

        given()
                .spec(recSpec)
                .body(doc)
                .when()
                .put("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(200)
                ;
    }

    @Test(dependsOnMethods = "updateDiscount")
    public void deleteDiscount() {
        given()
                .spec(recSpec)
                .when()
                .delete("/school-service/api/discounts/"+id)
                .then()
                .log().body()
                .statusCode(200)
                ;
    }


    @Test(dependsOnMethods = "deleteDiscount")
    public void deleteDiscountNegative() {
        given()
                .spec(recSpec)
                .when()
                .delete("/school-service/api/discounts/"+id)
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Discount not found"))
        ;
    }


}
