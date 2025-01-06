package ru.otus.client.services.petstore;

import static io.restassured.RestAssured.given;

import io.restassured.common.mapper.TypeRef;
import lombok.NoArgsConstructor;
import ru.otus.models.pet.DeletePetResponseDTO;
import ru.otus.models.pet.PetDTO;

import java.util.List;

@NoArgsConstructor
public class PetServiceApi extends BasePetStoreServiceApi {

  private static final String ADD_PET_ENDPOINT = "/pet";
  private static final String PET_PET_ID_ENDPOINT = "/pet/{petId}";
  private static final String FIND_BY_STATUS_ENDPOINT = "/pet/findByStatus";

  public PetServiceApi(PetServiceApi petServiceApi) {
    this.requestSpecification = petServiceApi.requestSpecification;
  }

  public PetDTO createPet(PetDTO pet) {

    var response = given(requestSpecification)
        .basePath(ADD_PET_ENDPOINT)
        .with()
        .body(pet)
        .when()
        .post()
        .then()
        .log()
        .all()
        .extract();

    return processResponse(response, new TypeRef<>() {
    });
  }

  public PetDTO getPetById(Long petId) {

    var response = given(requestSpecification)
        .basePath(PET_PET_ID_ENDPOINT)
        .pathParam("petId", petId)
        .when()
        .get()
        .then()
        .log()
        .all()
        .extract();

    return processResponse(response, new TypeRef<>() {
    });
  }

  public DeletePetResponseDTO deletePetById(Long petId) {

    var response = given(requestSpecification)
        .basePath(PET_PET_ID_ENDPOINT)
        .pathParam("petId", petId)
        .when()
        .delete()
        .then()
        .log()
        .all()
        .extract();

    return processResponse(response, new TypeRef<>() {
    });
  }

  public List<PetDTO> findPetsByStatus(String... status) {

    String statusQueryParam = String.join(",", status);

    var response = given(requestSpecification)
        .basePath(FIND_BY_STATUS_ENDPOINT)
        .queryParams("status", statusQueryParam)
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
