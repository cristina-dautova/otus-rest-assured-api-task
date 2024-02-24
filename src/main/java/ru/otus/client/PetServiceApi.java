package ru.otus.client;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import ru.otus.models.pet.PetDTO;

public class PetServiceApi extends BaseServiceApi {

  private static final String ADD_PET_ENDPOINT = "/pet";
  private static final String GET_PET_ENDPOINT = "/pet/{petId}";
  private static final String FIND_BY_STATUS_ENDPOINT = "/pet/findByStatus";

  public ExtractableResponse<Response> createPet(PetDTO pet) {
    return given(requestSpecification)
        .basePath(ADD_PET_ENDPOINT)
        .with()
        .body(pet)
        .when()
        .post()
        .then()
        .log()
        .all()
        .extract();
  }

  public ExtractableResponse<Response> getPetById(Long petId) {
    return given(requestSpecification)
        .basePath(GET_PET_ENDPOINT)
        .pathParam("petId", petId)
        .when()
        .get()
        .then()
        .log()
        .all()
        .extract();
  }

  public ExtractableResponse<Response> findPetsByStatus(String... status) {

    String statusQueryParam = String.join(",", status);

    return given(requestSpecification)
        .basePath(FIND_BY_STATUS_ENDPOINT)
        .queryParams("status", statusQueryParam)
        .when()
        .get()
        .then()
        .log()
        .all()
        .extract();
  }
}
