
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Fields {

    Faker faker = new Faker();
    RequestSpecification recSpec;

    String fieldsName;
    String code;
    String fieldsID;

    Map<String, String> fields = new HashMap<>();
    @BeforeClass
    public void setup() {
        _00_LoginSetup.login();
        recSpec = _00_LoginSetup.getRequestSpecification();
    }

    @Test
    public void createFields() {
        fieldsName = faker.name().fullName() + "zz";
        fields.put("name", fieldsName);
        code = faker.code().ean8();
        fields.put("code", code);
        fields.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        fields.put("type", "STRING");

        fieldsID =
                given()
                        .spec(recSpec)
                        .contentType(ContentType.JSON)
                        .body(fields)

                        .log().body()
                        .when()
                        .post("school-service/api/entity-field")
                        .then()

                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
        System.out.println("fieldsID = " + fieldsID);

    }

    @Test(dependsOnMethods = "createFields")
    public void createfieldsNegative() {

        fields.put("name", fieldsName);
        fields.put("code", code);
        given()
                .spec(recSpec)
                .body(fields)
                .log().body()
                .when()
                .post("school-service/api/entity-field")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;

    }


    @Test(dependsOnMethods = "createfieldsNegative")
    public void updatefields() {

        fieldsName = faker.name().fullName() + "xx";
        fields.put("name", fieldsName);
        code = faker.code().ean8();
        fields.put("code", code);
        fields.put("id",fieldsID);
        given()
                .spec(recSpec)
                .body(fields)
                .log().body()
                .when()
                .put("school-service/api/entity-field")
                .then()
                .statusCode(200)
                .body("name", equalTo(fieldsName))
        ;


    }

    @Test(dependsOnMethods = "updatefields")
    public void deletefields() {

        given()
                .spec(recSpec)
                .pathParam("fieldsID", fieldsID)
                .when()
                .delete("school-service/api/entity-field/{fieldsID}")
                .then()
                .log().body()
                .statusCode(204)
        ;

    }

    @Test(dependsOnMethods = "deletefields")
    public void deletefieldsNegative() {
        given()
                .spec(recSpec)
                .pathParam("fieldsID", fieldsID)
                .when()
                .delete("school-service/api/entity-field/{fieldsID}")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("EntityField not found"))
        ;
    }


}
