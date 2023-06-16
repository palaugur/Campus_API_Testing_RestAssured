
import Utilities.ExcelUtility;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class _00_LoginSetup {
    private static RequestSpecification recSpec;
    public static void login() {

       baseURI = "https://test.mersys.io";


        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", ExcelUtility.findFromExcel("Username"));
        userCredential.put("password",ExcelUtility.findFromExcel("Password"));
        userCredential.put("rememberMe", "true");

        Cookies cookies =

                given()
                        .contentType(ContentType.JSON)
                        .body(userCredential)

                        .when()
                        .post("/auth/login")

                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        recSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();


    }

    public static RequestSpecification getRequestSpecification() {
        return recSpec;
    }
}
