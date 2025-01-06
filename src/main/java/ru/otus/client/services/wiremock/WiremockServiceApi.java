package ru.otus.client.services.wiremock;

import static io.restassured.RestAssured.given;

import io.restassured.common.mapper.TypeRef;
import lombok.NoArgsConstructor;
import ru.otus.models.wiremock.CourseDTO;
import ru.otus.models.wiremock.ScoreDTO;
import java.util.List;

@NoArgsConstructor
public class WiremockServiceApi extends BaseWiremockServiceApi {

  public static final String GET_SCORE_BY_USER_ID_ENDPOINT = "/user/get/{id}";
  public static final String GET_ALL_COURSES_ENDPOINT = "/course/get/all";
  public static final String GET_ALL_USERS_ENDPOINT = "/user/get/all";

  public WiremockServiceApi(WiremockServiceApi wiremockServiceApi) {
    this.requestSpecification = wiremockServiceApi.requestSpecification;
  }

  public ScoreDTO getScoreByUserId(Integer userId) {

    var response = given(requestSpecification)
        .basePath(GET_SCORE_BY_USER_ID_ENDPOINT)
        .pathParam("id", userId)
        .when()
        .get()
        .then()
        .log()
        .all()
        .extract();

    return processResponse(response, new TypeRef<>() {
    });
  }

  public List<CourseDTO> getAllCourses() {

    var response = given(requestSpecification)
        .basePath(GET_ALL_COURSES_ENDPOINT)
        .when()
        .get()
        .then()
        .log()
        .all()
        .extract();

    return processResponse(response, new TypeRef<>() {
    });

  }
}
