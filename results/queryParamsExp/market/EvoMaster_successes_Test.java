import  org.junit.AfterClass;
import  org.junit.BeforeClass;
import  org.junit.Before;
import  org.junit.Test;
import static org.junit.Assert.*;
import  java.util.Map;
import  java.util.Arrays;
import  java.util.List;
import static org.evomaster.test.utils.EMTestUtils.*;
import  org.evomaster.client.java.controller.SutHandler;
import  io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import  io.restassured.response.ValidatableResponse;
import static org.evomaster.client.java.sql.dsl.SqlDsl.sql;
import  org.evomaster.client.java.controller.api.dto.database.operations.InsertionResultsDto;
import  org.evomaster.client.java.controller.api.dto.database.operations.InsertionDto;
import static org.hamcrest.Matchers.*;
import  io.restassured.config.JsonConfig;
import  io.restassured.path.json.config.JsonPathConfig;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.evomaster.client.java.controller.contentMatchers.NumberMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.StringMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.SubStringMatcher.*;




/**
*  This file was automatically generated by EvoMaster on 2025-02-12T17:20:05.795-03:00[America/Argentina/Buenos_Aires]
 * <br>
*  The generated test suite contains 2 tests
 * <br>
*  Covered targets: 182
 * <br>
*  Used time: 0h 29m 39s
 * <br>
*  Needed budget for current results: 91%
 * <br>
*  This file contains test cases that represent successful calls.
*/
public class EvoMaster_successes_Test {

    
    private static final SutHandler controller = new em.embedded.market.EmbeddedEvoMasterController();
    private static String baseUrlOfSut;
    
    
    @BeforeClass
    public static void initClass() {
        controller.setupForGeneratedTest();
        baseUrlOfSut = controller.startSut();
        controller.registerOrExecuteInitSqlCommandsIfNeeded();
        assertNotNull(baseUrlOfSut);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config()
            .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
            .redirect(redirectConfig().followRedirects(false));
    }
    
    
    @AfterClass
    public static void tearDown() {
        controller.stopSut();
    }
    
    
    @Before
    public void initTest() {
        controller.resetDatabase(Arrays.asList("CART","CONTACTS","CUSTOMER_ORDER","PRODUCT","USER_ACCOUNT"));
        controller.resetStateOfSUT();
    }
    
    
    
    
    /**
    * Calls:
    * (200) GET:/v2/api-docs
    */
    @Test(timeout = 60000)
    public void test_0_getOnApi_docsReturnsObject() throws Exception {
        
        given().accept("*/*")
                .get(baseUrlOfSut + "/v2/api-docs")
                .then()
                .statusCode(200);
    }
    
    
    /**
    * Calls:
    * (200) GET:/customer/orders/{orderId}
    */
    @Test(timeout = 60000)
    public void test_1_getOnOrderReturnsObjectUsingSql() throws Exception {
        List<InsertionDto> insertions = sql().insertInto("CUSTOMER_ORDER", 209L)
                .d("ID", "0")
                .d("USER_ACCOUNT_ID", "952")
                .d("DATE_CREATED", "\"2016-12-04\"")
                .d("EXECUTED", "false")
                .d("PRODUCTS_COST", "962")
                .d("DELIVERY_INCLUDED", "false")
                .d("DELIVERY_COST", "0")
            .and().insertInto("CART", 210L)
                .d("TOTAL_ITEMS", "-523461")
                .d("PRODUCTS_COST", "1115136")
                .d("DELIVERY_INCLUDED", "false")
            .and().insertInto("CONTACTS", 211L)
                .d("PHONE", "\"_EM_3448_XYZ_\"")
                .d("ADDRESS", "\"U[UkUtaUWZ\"")
                .d("CITY_REGION", "\"WO7w6ZR8ND\"")
            .and().insertInto("PRODUCT", 584L)
                .d("NAME", "\"ypTD\"")
                .d("DISTILLERY_ID", "777")
                .d("AGE", "51")
                .d("ALCOHOL", "0.709626379638566")
                .d("VOLUME", "324")
                .d("PRICE", "0.0")
                .d("DESCRIPTION", "\"U\"")
                .d("AVAILABLE", "false")
            .dtos();
        InsertionResultsDto insertionsresult = controller.execInsertionsIntoDatabase(insertions);
        
        given().accept("*/*")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=") // admin
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/customer/orders/1?name=a82QV7")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/hal+json")
                .body("'userAccount'", containsString("ivan.petrov@yandex.ru"))
                .body("'billNumber'", numberMatches(2.7132054E8))
                .body("'dateCreated'", containsString("2019-12-27T03:00:00.000+00:00"))
                .body("'productsCost'", numberMatches(8127.0))
                .body("'deliveryCost'", numberMatches(400.0))
                .body("'deliveryIncluded'", equalTo(true))
                .body("'totalCost'", numberMatches(8527.0))
                .body("'payed'", equalTo(true))
                .body("'executed'", equalTo(false));
    }


}
