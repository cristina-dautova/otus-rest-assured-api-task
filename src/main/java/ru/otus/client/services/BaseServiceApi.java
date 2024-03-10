package ru.otus.client.services;

import static io.restassured.RestAssured.given;
import static ru.otus.config.LoadConfig.APPLICATION_CONFIG;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseServiceApi {

  protected RequestSpecification requestSpecification;

  public BaseServiceApi() {
    requestSpecification = given()
        .baseUri(APPLICATION_CONFIG.baseUrl())
        .contentType(ContentType.JSON)
        .log()
        .all();
  }
}
