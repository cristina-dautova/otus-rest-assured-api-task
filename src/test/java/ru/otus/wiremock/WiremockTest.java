package ru.otus.wiremock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.client.services.wiremock.WiremockServiceApi.GET_ALL_COURSES_ENDPOINT;
import static ru.otus.constats.StubsPaths.GET_ALL_COURSES_JSON;
import static ru.otus.constats.StubsPaths.GET_USERS_SCORE_JSON;
import static ru.otus.utils.FileUtils.readJsonFile;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.otus.annotations.ApiManager;
import ru.otus.annotations.Assert;
import ru.otus.client.services.ServiceManager;
import ru.otus.extensions.TestHelperExtension;
import ru.otus.models.wiremock.CourseDTO;


@WireMockTest
@ExtendWith({TestHelperExtension.class})
public class WiremockTest {

  private static final Integer PORT = 1122;
  private static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(PORT);
  private static final String GET_SCORE_BY_USER_ID_ENDPOINT = "/user/get/1";
  @ApiManager
  private ServiceManager serviceManager;
  @Assert
  private SoftAssertions softAssertions;

  @BeforeAll
  public static void startWireMockServer() {
    WIRE_MOCK_SERVER.start();
    configureFor(PORT);
  }

  @AfterAll
  public static void stopWireMockServer() {
    WIRE_MOCK_SERVER.stop();
  }

  @Test
  void verifyUsersScore() {

    stubGetMethodWithOkJsonBodyResponse(GET_SCORE_BY_USER_ID_ENDPOINT, readJsonFile(GET_USERS_SCORE_JSON));

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

  private static void stubGetMethodWithOkJsonBodyResponse(String getEndpoint, String body) {
    stubFor(get(urlEqualTo(getEndpoint))
        .willReturn(aResponse()
            .withBody(body)
            .withHeader(CONTENT_TYPE, APPLICATION_JSON)
            .withStatus(200)));
  }
}
