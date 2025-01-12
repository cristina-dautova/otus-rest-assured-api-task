package ru.otus.wiremock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.client.services.wiremock.WiremockServiceApi.GET_ALL_COURSES_ENDPOINT;
import static ru.otus.constats.StubsPaths.GET_ALL_COURSES_JSON;
import static ru.otus.constats.StubsPaths.GET_USERS_SCORE_JSON;
import static ru.otus.utils.FileUtils.readJsonFile;
import static ru.otus.wiremock.BaseWiremockTest.GET_SCORE_BY_USER_ID_ENDPOINT;
import static ru.otus.wiremock.BaseWiremockTest.stubGetMethodWithOkJsonBodyResponse;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.otus.annotations.ApiManager;
import ru.otus.annotations.Assert;
import ru.otus.client.services.ServiceManager;
import ru.otus.extensions.TestHelperExtension;
import ru.otus.models.wiremock.CourseDTO;

import java.io.File;


@WireMockTest
@ExtendWith({TestHelperExtension.class, BaseWiremockTest.class})
public class WiremockTest {

  @ApiManager
  private ServiceManager serviceManager;
  @Assert
  private SoftAssertions softAssertions;

  @Test
  void verifyUsersScore() {

    stubGetMethodWithOkJsonBodyResponse(GET_SCORE_BY_USER_ID_ENDPOINT, readJsonFile(GET_USERS_SCORE_JSON));

    File jsonSchema = new File("src/test/resources/json/schema/UsersScoreSchema.json");

    serviceManager.getWireMockServiceApi().getValidatableResponseScoreByUserId(1)
        .assertThat()
        .body(matchesJsonSchema(jsonSchema));

    var price = serviceManager.getWireMockServiceApi().getScoreByUserId(1);

    verify(getRequestedFor(urlEqualTo(GET_SCORE_BY_USER_ID_ENDPOINT)));
    softAssertions.assertThat(price.getName()).isEqualTo("Test user");
    softAssertions.assertThat(price.getScore()).isEqualTo(78);
    softAssertions.assertAll();
  }

  @Test
  void verifyCoursesList() {

    stubGetMethodWithOkJsonBodyResponse(GET_ALL_COURSES_ENDPOINT, readJsonFile(GET_ALL_COURSES_JSON));

    var coursesList = serviceManager.getWireMockServiceApi().getAllCourses();

    verify(getRequestedFor(urlEqualTo(GET_ALL_COURSES_ENDPOINT)));
    assertThat(coursesList).hasSize(2);
    softAssertions.assertThat(coursesList.get(0)).isEqualTo(new CourseDTO("QA java", 15000));
    softAssertions.assertThat(coursesList.get(1)).isEqualTo(new CourseDTO("Java", 12000));
    softAssertions.assertAll();
  }
}
