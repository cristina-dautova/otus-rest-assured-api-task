package ru.otus.client.services;

import static io.restassured.RestAssured.given;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import ru.otus.rest.exceptions.HandledFailure;

public abstract class BaseServiceApi {

  protected RequestSpecification requestSpecification;

  public BaseServiceApi(String baseUri) {
    requestSpecification = given()
        .baseUri(baseUri)
        .contentType(ContentType.JSON)
        .log()
        .all();
  }

  protected <T> T processResponse(ExtractableResponse<Response> response, TypeRef<T> responseType) {
    if (!isFailure(response)) {
      return response.body().as(responseType);
    } else {
      throw new HandledFailure(response);
    }
  }

  protected boolean isFailure(ExtractableResponse<Response> response) {
    var statusCode = response.statusCode();
    return statusCode == HttpStatus.SC_MOVED_PERMANENTLY
        || statusCode > HttpStatus.SC_SEE_OTHER
        || statusCode < HttpStatus.SC_OK;
  }
}
