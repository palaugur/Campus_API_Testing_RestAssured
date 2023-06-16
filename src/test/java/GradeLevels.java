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
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GradeLevels {

    Faker faker = new Faker();

    String gradeLevelID;
    String gradeLevelName;
    String gradeLevelShorterName;
    RequestSpecification recSpec;


    @BeforeClass
    public void setup() {
        _00_LoginSetup.login();
        recSpec = _00_LoginSetup.getRequestSpecification();
    }

    @Test
    public void createGradeLevel() {

        Map<String, String> gradeLevel = new HashMap<>();

        gradeLevelName = (faker.name().fullName());
        gradeLevelShorterName = (faker.lorem().word());

       gradeLevel.put("name", gradeLevelName);
       gradeLevel.put("order", "1");
       gradeLevel.put("shortName", gradeLevelShorterName);

        gradeLevelID =
                given()
                        .spec(recSpec)
                        .body(gradeLevel)
                        //.log().body()

                        .when()
                        .post("/school-service/api/grade-levels")

                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;

    }

    @Test(dependsOnMethods = "createGradeLevel")
    public void createGradeLevelNegative() {

        Map<String, String> gradeLevel = new HashMap<>();

        gradeLevel.put("name", gradeLevelName);
        gradeLevel.put("order", "1");
        gradeLevel.put("shortName", gradeLevelShorterName);

        given()
                .spec(recSpec)
                .body(gradeLevel)
                //.log().body()

                .when()
                .post("/school-service/api/grade-levels")

                .then()
                //.log().all()
                .statusCode(400)
                .body("message", containsString("already"))

        ;
    }

    @Test(dependsOnMethods = "createGradeLevelNegative")
    public void updateGradeLevel() {
        Map<String, String> gradeLevel = new HashMap<>();
        gradeLevel.put("id", gradeLevelID);

        gradeLevelName = "Hello API 5e87";

        gradeLevel.put("name", gradeLevelName);
        gradeLevel.put("order", "1");
        gradeLevel.put("shortName", gradeLevelShorterName);

        given()
                .spec(recSpec)
                .body(gradeLevel)
                //.log().body()

                .when()
                .put("/school-service/api/grade-levels")

                .then()
                //.log().all()
                .statusCode(200)
                .body("name", equalTo(gradeLevelName))

        ;
    }

    @Test(dependsOnMethods = "updateGradeLevel")
    public void deleteGradeLevel() {
        given()
                .spec(recSpec)
                .pathParam("gradeLevelID", gradeLevelID)
                //.log().uri()

                .when()
                .delete("/school-service/api/grade-levels/{gradeLevelID}")

                .then()
                //.log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteGradeLevel")
    public void deleteGradeLevelNegative() {
        given()
                .spec(recSpec)
                .pathParam("gradeLevelID", gradeLevelID)
                //.log().uri()

                .when()
                .delete("/school-service/api/grade-levels/{gradeLevelID}")

                .then()
                //.log().body()
                .statusCode(400)
                .body("message",containsString("not found"))
        ;

    }

}


