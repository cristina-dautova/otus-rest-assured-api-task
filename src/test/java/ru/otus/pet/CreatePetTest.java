package ru.otus.pet;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.constats.PetCategory.CAT;
import static ru.otus.constats.PetCategory.DOG;
import static ru.otus.constats.PetTags.BIG;
import static ru.otus.constats.PetTags.BROWN;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.BaseTest;
import ru.otus.client.PetServiceApi;
import ru.otus.constats.PetStatus;
import ru.otus.models.pet.PetDTO;
import ru.otus.models.pet.PetDTOBuilder;

import java.util.List;

public class CreatePetTest extends BaseTest {
  private final PetServiceApi petServiceApi = new PetServiceApi();

  private PetDTO pet;

  @BeforeEach
  public void createPet() {
    pet = new PetDTOBuilder()
        .name(FAKER.animal().name())
        .status(PetStatus.AVAILABLE.getStatus())
        .category(DOG)
        .photoUrls(List.of(FAKER.avatar().image()))
        .tags(List.of(BIG, BROWN))
        .build();
  }

  /***
   * Проверяю создание домашнего животного
   */

  @Test
  public void addPet() {

    var createdPet = petServiceApi.createPet(pet);
    var createdPetDTO = createdPet.body().as(PetDTO.class);
    var returnedPetDTO = petServiceApi.getPetById(createdPetDTO.getId()).body().as(PetDTO.class);

    assertThat(createdPet.statusCode())
        .as("Status code comparison")
        .isEqualTo(HttpStatus.SC_OK);

    softAssertions.assertThat(createdPetDTO)
        .as("Pet object comparison")
        .usingRecursiveComparison()
        .ignoringFields("id", "photoUrls")
        .isEqualTo(pet);

    softAssertions.assertThat(returnedPetDTO)
        .as("Pet creation validation")
        .isEqualTo(createdPetDTO);

    softAssertions.assertAll();
  }

  /***
   * Создание домашнего животного c одинаковым айди должно быть запрещено - стирается первая сущность
   */

  @Test
  public void creatingPetWithSameIdShouldBeRestricted() {

    var pet2 = new PetDTOBuilder()
        .id(pet.getId())
        .name(FAKER.funnyName().name())
        .status(PetStatus.AVAILABLE.getStatus())
        .photoUrls(List.of(FAKER.avatar().image()))
        .category(CAT)
        .tags(List.of(BROWN))
        .build();

    petServiceApi.createPet(pet);
    var createdPet2 = petServiceApi.createPet(pet2);

    assertThat(createdPet2.statusCode())
        .as("Status code comparison")
        .isEqualTo(HttpStatus.SC_BAD_REQUEST);
  }
}
