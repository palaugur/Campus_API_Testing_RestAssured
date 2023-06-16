
import com.github.javafaker.Faker;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Attestations {

    Faker faker = new Faker();
    String attestationsID;
    String attestationsName;
    RequestSpecification recSpec;


    @BeforeClass
    public void setup() {
        _00_LoginSetup.login();
        recSpec = _00_LoginSetup.getRequestSpecification();
    }
    @Test
    public void createAttestations() {

        Map<String, String> attestations = new HashMap<>();

        attestationsName = (faker.lorem().word() + faker.number().digits(2));

        attestations.put("name", attestationsName);

        attestationsID =
                given()
                        .spec(recSpec)
                        .body(attestations)
                        //.log().body()

                        .when()
                        .post("/school-service/api/attestation")

                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;

    }

    @Test(dependsOnMethods = "createAttestations")
    public void createAttestationsNegative() {

        Map<String, String> attestations = new HashMap<>();

        attestations.put("name", attestationsName);

        given()
                .spec(recSpec)
                .body(attestations)
                //.log().body()

                .when()
                .post("/school-service/api/attestation")

                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"))

        ;
    }

    @Test(dependsOnMethods = "createAttestationsNegative")
    public void updateAttestations() {
        Map<String, String> attestations = new HashMap<>();
        attestations.put("id", attestationsID);

        attestationsName = "Hello API" + faker.number().digits(7);
        attestations.put("name", attestationsName);

        given()
                .spec(recSpec)
                .body(attestations)
                //.log().body()

                .when()
                .put("/school-service/api/attestation")

                .then()
                //.log().body()
                .statusCode(200)
                .body("name", equalTo(attestationsName))

        ;
    }

    @Test(dependsOnMethods = "updateAttestations")
    public void deleteAttestations() {
        given()
                .spec(recSpec)
                .pathParam("attestationsID", attestationsID)
                //.log().uri()

                .when()
                .delete("/school-service/api/attestation/{attestationsID}")

                .then()
                //.log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteAttestations")
    public void deleteAttestationsNegative() {
        given()
                .spec(recSpec)
                .pathParam("attestationsID", attestationsID)
                //.log().uri()

                .when()
                .delete("/school-service/api/attestation/{attestationsID}")

                .then()
                //.log().all()
                .statusCode(400)
                .body("message",containsString("not found"))
        ;

    }

}
