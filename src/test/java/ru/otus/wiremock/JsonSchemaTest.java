package ru.otus.wiremock;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static ru.otus.client.services.wiremock.WiremockServiceApi.GET_ALL_COURSES_ENDPOINT;
import static ru.otus.constats.StubsPaths.GET_ALL_COURSES_JSON;
import static ru.otus.constats.StubsPaths.GET_USERS_SCORE_JSON;
import static ru.otus.utils.FileUtils.readJsonFile;
import static ru.otus.wiremock.BaseWiremockTest.GET_SCORE_BY_USER_ID_ENDPOINT;
import static ru.otus.wiremock.BaseWiremockTest.stubGetMethodWithOkJsonBodyResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.otus.annotations.ApiManager;
import ru.otus.client.services.ServiceManager;
import ru.otus.extensions.TestHelperExtension;

import java.io.File;

@ExtendWith({TestHelperExtension.class, BaseWiremockTest.class})
public class JsonSchemaTest {

  private static final String PATH_TO_SCHEMA = "src/test/resources/json/schema/";

  @ApiManager
  private ServiceManager serviceManager;

  @Test
  void verifyUsersScoreSchema() {

    stubGetMethodWithOkJsonBodyResponse(GET_SCORE_BY_USER_ID_ENDPOINT, readJsonFile(GET_USERS_SCORE_JSON));

    var jsonSchema = new File(PATH_TO_SCHEMA + "UsersScoreSchema.json");
    serviceManager.getWireMockServiceApi().getWiremockServiceValidatableResponse(GET_SCORE_BY_USER_ID_ENDPOINT)
        .assertThat()
        .body(matchesJsonSchema(jsonSchema));
  }

  @Test
  void verifyCoursesListSchema() {

    stubGetMethodWithOkJsonBodyResponse(GET_ALL_COURSES_ENDPOINT, readJsonFile(GET_ALL_COURSES_JSON));

    var jsonSchema = new File(PATH_TO_SCHEMA + "AllCoursesSchema.json");
    serviceManager.getWireMockServiceApi().getWiremockServiceValidatableResponse(GET_ALL_COURSES_ENDPOINT)
        .assertThat()
        .body(matchesJsonSchema(jsonSchema));
  }
}
