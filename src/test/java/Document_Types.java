
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class Document_Types {

    Faker faker = new Faker();
    RequestSpecification recSpec;

    @BeforeClass
    public void setup() {
        _00_LoginSetup.login();
        recSpec = _00_LoginSetup.getRequestSpecification();
    }

    String id;
    String schoolId = "6390f3207a3bcb6a7ac977f9";
    String[] attachmentStages = {"CONTRACT"};
    @Test
    public void createDocumentTypes() {

        Map<String, Object> doc = new HashMap<>();
        doc.put("name", faker.name().username());
        doc.put("attachmentStages", attachmentStages);
        doc.put("schoolId", schoolId);

        Response response =
                given()
                        .spec(recSpec)
                        .body(doc)
                        .when()
                        .post("/school-service/api/attachments/create")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().response();
        id = response.path("id");
    }


    @Test(dependsOnMethods = "createDocumentTypes")
    public void updateDocumentTypes() {

        Map<String, Object> doc = new HashMap<>();
        doc.put("name", faker.funnyName().name());
        doc.put("schoolId", schoolId);
        doc.put("attachmentStages", attachmentStages);
        doc.put("id", id);

        given()
                .spec(recSpec)
                .body(doc)
                .when()
                .put("/school-service/api/attachments")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "updateDocumentTypes")
    public void deleteDocumentTypes() {
        given()
                .spec(recSpec)
                .when()
                .delete("/school-service/api/attachments/"+id)
                .then()
                .log().body()
                .statusCode(200);
    }


    @Test(dependsOnMethods = "deleteDocumentTypes")
    public void deleteDocumentTypesNegative() {
        given()
                .spec(recSpec)
                .when()
                .delete("/school-service/api/attachments/"+id)
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Attachment Type not found"));
    }


}
