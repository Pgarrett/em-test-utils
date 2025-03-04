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
*  This file was automatically generated by EvoMaster on 2025-02-09T18:28:05.597-03:00[America/Argentina/Buenos_Aires]
 * <br>
*  The generated test suite contains 14 tests
 * <br>
*  Covered targets: 710
 * <br>
*  Used time: 0h 20m 47s
 * <br>
*  Needed budget for current results: 100%
 * <br>
*  This file contains test cases that represent successful calls.
*/
public class EvoMaster_successes_Test {

    
    private static final SutHandler controller = new em.embedded.org.zalando.EmbeddedEvoMasterController();
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
        controller.resetDatabase(Arrays.asList("LANGUAGE_LIST","MAINTAINERS","PROJECT","STATISTICS"));
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
    * (200) GET:/export
    */
    @Test(timeout = 60000)
    public void test_1_getOnExportReturnsObject() throws Exception {
        
        given().accept("application/json;charset=utf-8")
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/export?" + 
                    "end_date=v1qp25Vo42oN&" + 
                    "organizations=tmKd67dt&" + 
                    "start_date=o")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("'contributors'.size()", equalTo(0))
                .body("'projects'.size()", equalTo(0))
                .body("'statistics'.size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/statistics
    */
    @Test(timeout = 60000)
    public void test_2_getOnStatisticsReturnsEmptyList() throws Exception {
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/statistics?organizations=_EM_369_XYZ_")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/statistics
    */
    @Test(timeout = 60000)
    public void test_3_getOnStatisticsReturnsEmptyListUsingSql() throws Exception {
        List<InsertionDto> insertions = sql().insertInto("STATISTICS", 21L)
                .d("ID", "590")
                .d("SNAPSHOT_DATE", "\"1973-09-03 14:01:54\"")
                .d("ALL_CONTRIBUTORS_COUNT", "NULL")
                .d("ALL_FORKS_COUNT", "908")
                .d("ALL_SIZE_COUNT", "NULL")
                .d("ALL_STARS_COUNT", "NULL")
                .d("MEMBERS_COUNT", "NULL")
                .d("ORGANIZATION_NAME", "\"HK6APpvycNsBU2Wk\"")
                .d("PRIVATE_PROJECT_COUNT", "NULL")
                .d("PROGRAM_LANGUAGES_COUNT", "NULL")
                .d("PUBLIC_PROJECT_COUNT", "NULL")
                .d("TAGS_COUNT", "143")
                .d("TEAMS_COUNT", "176")
                .d("EXTERNAL_CONTRIBUTORS_COUNT", "NULL")
            .dtos();
        InsertionResultsDto insertionsresult = controller.execInsertionsIntoDatabase(insertions);
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/statistics?EMextraParam123=_EM_266_XYZ_")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/statistics/languages
    */
    @Test(timeout = 60000)
    public void test_4_getOnStatisticsLanguagesWithQueryParamsReturnsEmptyListUsingSql() throws Exception {
        List<InsertionDto> insertions = sql().insertInto("STATISTICS", 97L)
                .d("ID", "271")
                .d("SNAPSHOT_DATE", "\"1993-02-03 11:47:38\"")
                .d("ALL_CONTRIBUTORS_COUNT", "-31848")
                .d("ALL_FORKS_COUNT", "87")
                .d("ALL_SIZE_COUNT", "NULL")
                .d("ALL_STARS_COUNT", "NULL")
                .d("MEMBERS_COUNT", "NULL")
                .d("ORGANIZATION_NAME", "\"F87gM\"")
                .d("PRIVATE_PROJECT_COUNT", "NULL")
                .d("PROGRAM_LANGUAGES_COUNT", "-1073757685")
                .d("PUBLIC_PROJECT_COUNT", "NULL")
                .d("TAGS_COUNT", "850")
                .d("TEAMS_COUNT", "NULL")
                .d("EXTERNAL_CONTRIBUTORS_COUNT", "900")
            .dtos();
        InsertionResultsDto insertionsresult = controller.execInsertionsIntoDatabase(insertions);
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/statistics/languages?" + 
                    "organizations=F87gM&" + 
                    "limit=ey")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/statistics/languages
    */
    @Test(timeout = 60000)
    public void test_5_getOnStatisticsLanguagesReturnsEmptyListUsingSql() throws Exception {
        List<InsertionDto> insertions = sql().insertInto("STATISTICS", 26L)
                .d("ID", "191")
                .d("SNAPSHOT_DATE", "\"2093-12-15 07:49:14\"")
                .d("ALL_CONTRIBUTORS_COUNT", "NULL")
                .d("ALL_FORKS_COUNT", "766")
                .d("ALL_SIZE_COUNT", "NULL")
                .d("ALL_STARS_COUNT", "328")
                .d("MEMBERS_COUNT", "584")
                .d("ORGANIZATION_NAME", "NULL")
                .d("PRIVATE_PROJECT_COUNT", "NULL")
                .d("PROGRAM_LANGUAGES_COUNT", "362")
                .d("PUBLIC_PROJECT_COUNT", "249")
                .d("TAGS_COUNT", "NULL")
                .d("TEAMS_COUNT", "884")
                .d("EXTERNAL_CONTRIBUTORS_COUNT", "NULL")
            .dtos();
        InsertionResultsDto insertionsresult = controller.execInsertionsIntoDatabase(insertions);
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "_EM_293_XYZ_")
                .get(baseUrlOfSut + "/statistics/languages")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/projects
    */
    @Test(timeout = 60000)
    public void test_6_getOnProjectsReturnsEmptyListUsingSql() throws Exception {
        List<InsertionDto> insertions = sql().insertInto("PROJECT", 6L)
                .d("COMMITS_COUNT", "618")
                .d("CONTRIBUTORS_COUNT", "NULL")
                .d("DESCRIPTION", "\"pp\"")
                .d("FORKS_COUNT", "554")
                .d("GIT_HUB_PROJECT_ID", "NULL")
                .d("LAST_PUSHED", "\"_EM_91_XYZ_\"")
                .d("NAME", "\"9TAnDAW\"")
                .d("ORGANIZATION_NAME", "NULL")
                .d("PRIMARY_LANGUAGE", "NULL")
                .d("SCORE", "560")
                .d("SNAPSHOT_DATE", "NULL")
                .d("STARS_COUNT", "NULL")
                .d("URL", "NULL")
                .d("TITLE", "\"_EM_92_XYZ_\"")
                .d("IMAGE", "\"\"")
                .d("EXTERNAL_CONTRIBUTORS_COUNT", "NULL")
            .dtos();
        InsertionResultsDto insertionsresult = controller.execInsertionsIntoDatabase(insertions);
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/projects?" + 
                    "organizations=&" + 
                    "end_date=1966-07-22T23%3A11%3A52.241%2B09%3A31&" + 
                    "q=_EM_93_XYZ_")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/languages
    */
    @Test(timeout = 60000)
    public void test_7_getOnLanguagesReturnsEmptyList() throws Exception {
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "_EM_13_XYZ_")
                .get(baseUrlOfSut + "/languages?" + 
                    "organizations=FtJA_wMa3ZJkF58&" + 
                    "EMextraParam123=42")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/languages
    */
    @Test(timeout = 60000)
    public void test_8_getOnLanguagesReturnsEmptyList() throws Exception {
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/languages?" + 
                    "organizations=_EM_250_XYZ_&" + 
                    "limit=891&" + 
                    "offset=573&" + 
                    "q=_EM_251_XYZ_")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/projects
    */
    @Test(timeout = 60000)
    public void test_9_getOnProjectsReturnsEmptyList() throws Exception {
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "_EM_18_XYZ_")
                .get(baseUrlOfSut + "/projects?" + 
                    "offset=519&" + 
                    "sortBy=DDw34&" + 
                    "language=CZUuoi&" + 
                    "EMextraParam123=urWoWptf4")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/projects
    */
    @Test(timeout = 60000)
    public void test_10_getOnProjectsReturnsEmptyList() throws Exception {
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/projects?" + 
                    "organizations=wINrKCK4Szu&" + 
                    "limit=130&" + 
                    "sortBy=YMnD&" + 
                    "q=2qCLN&" + 
                    "language=dJ4z7t")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/projects
    */
    @Test(timeout = 60000)
    public void test_11_getOnProjectsReturnsEmptyList() throws Exception {
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/projects?" + 
                    "organizations=_EM_375_XYZ_&" + 
                    "offset=988&" + 
                    "end_date=2011-08-26T20%3A16%3A18.852Z&" + 
                    "sortBy=EYc&" + 
                    "q=5oni")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/projects
    */
    @Test(timeout = 60000)
    public void test_12_getOnProjectsReturnsEmptyList() throws Exception {
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/projects?" + 
                    "offset=453&" + 
                    "start_date=1990-05-22T15%3A55%3A17.075Z&" + 
                    "end_date=1939-04-06T21%3A45%3A21.507-18%3A20&" + 
                    "sortBy=TVftIwScm6L&" + 
                    "language=TtD7")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }
    
    
    /**
    * Calls:
    * (200) GET:/projects
    */
    @Test(timeout = 60000)
    public void test_13_getOnProjectsReturnsEmptyList() throws Exception {
        
        given().accept("application/json")
                .header("x-EMextraHeader123", "")
                .get(baseUrlOfSut + "/projects?" + 
                    "organizations=VKkmR8eYjvLbAjj&" + 
                    "limit=414&" + 
                    "offset=758&" + 
                    "start_date=1952-09-10T18%3A09%3A44%2B15%3A25&" + 
                    "sortBy=_EM_14_XYZ_&" + 
                    "q=_EM_15_XYZ_&" + 
                    "language=_EM_16_XYZ_&" + 
                    "EMextraParam123=_EM_17_XYZ_")
                .then()
                .statusCode(200)
                .assertThat()
                .contentType("application/json")
                .body("size()", equalTo(0));
    }


}
