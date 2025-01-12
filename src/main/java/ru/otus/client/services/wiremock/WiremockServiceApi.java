package ru.otus.client.services.wiremock;

import static io.restassured.RestAssured.given;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
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
    var response = getValidatableResponseScoreByUserId(userId).extract();
    return processResponse(response, new TypeRef<>() {
    });
  }

  public ValidatableResponse getValidatableResponseScoreByUserId(Integer userId) {
    return getValidatableResponseGetMethod(given(requestSpecification)
        .basePath(GET_SCORE_BY_USER_ID_ENDPOINT)
        .pathParam("id", userId));
  }

  public List<CourseDTO> getAllCourses() {

    var response = getValidatableResponseGetMethod(given(requestSpecification)
        .basePath(GET_ALL_COURSES_ENDPOINT))
        .extract();
    return processResponse(response, new TypeRef<>() {
    });
  }

  private ValidatableResponse getValidatableResponseGetMethod(RequestSpecification requestSpecification) {
    return requestSpecification
        .when()
        .get()
        .then()
        .log()
        .all();
  }

  public ValidatableResponse getWiremockServiceValidatableResponse(String endpoint) {
    return getValidatableResponseGetMethod(given(requestSpecification)
        .basePath(endpoint));
  }
}
