package ru.otus.pet;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.constats.PetCategory.CAT;
import static ru.otus.constats.PetCategory.DOG;
import static ru.otus.constats.PetTags.BIG;
import static ru.otus.constats.PetTags.BROWN;
import static ru.otus.rest.utils.ValidationHelper.getError;

import net.datafaker.Faker;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.otus.Preconditions;
import ru.otus.client.services.ServiceManager;
import ru.otus.annotations.ApiManager;
import ru.otus.annotations.Assert;
import ru.otus.annotations.Precondition;
import ru.otus.constats.PetStatus;
import ru.otus.extensions.TestHelperExtension;
import ru.otus.models.pet.PetDTO;
import ru.otus.models.pet.PetDTOBuilder;

import java.util.List;

@ExtendWith({TestHelperExtension.class})
public class CreatePetTest {

  private final static Faker FAKER = new Faker();
  @Precondition
  private Preconditions preconditions;
  @ApiManager
  private ServiceManager serviceManager;
  @Assert
  private SoftAssertions softAssertions;
  private PetDTO pet;

  @BeforeEach
  public void createPet() {
    pet = new PetDTOBuilder()
        .name(FAKER.bigBangTheory().character())
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

    var createdPet = preconditions.createPet(pet);
    var returnedPet = serviceManager.getPetServiceApi().getPetById(createdPet.getId());

    softAssertions.assertThat(createdPet)
        .as("Pet object comparison")
        .usingRecursiveComparison()
        .ignoringFields("id", "photoUrls")
        .isEqualTo(pet);

    softAssertions.assertThat(returnedPet)
        .as("Pet creation validation")
        .isEqualTo(createdPet);
  }

  /***
   * BUG: При передавании ID в боди перезаписываются сущности, должна быть ошибка
   */

  @Test
  public void creatingPetWithSameIdShouldBeRestricted() {

    var createdPetDTO = preconditions.createPet(pet);

    var pet2 = new PetDTOBuilder()
        .id(createdPetDTO.getId())
        .name(FAKER.funnyName().name())
        .status(PetStatus.AVAILABLE.getStatus())
        .photoUrls(List.of(FAKER.avatar().image()))
        .category(CAT)
        .tags(List.of(BROWN))
        .build();

    var errorResponse = getError(() -> serviceManager.getPetServiceApi().createPet(pet2));

    assertThat(errorResponse.statusCode())
        .as("Status code error comparison")
        .isEqualTo(HttpStatus.SC_BAD_REQUEST);
  }
}
