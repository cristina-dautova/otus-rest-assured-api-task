package ru.otus.pet;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.constats.PetCategory.DOG;
import static ru.otus.constats.PetTags.BIG;
import static ru.otus.constats.PetTags.BROWN;
import static ru.otus.rest.utils.ValidationHelper.getError;

import net.datafaker.Faker;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.otus.Preconditions;
import ru.otus.client.services.ServiceManager;
import ru.otus.annotations.ApiManager;
import ru.otus.annotations.Assert;
import ru.otus.annotations.Precondition;
import ru.otus.constats.PetStatus;
import ru.otus.extensions.TestHelperExtension;
import ru.otus.models.pet.PetDTOBuilder;

import java.util.List;

@ExtendWith({TestHelperExtension.class})
public class DeletePetTest {

  private final static Faker FAKER = new Faker();
  @Precondition
  private Preconditions preconditions;
  @ApiManager
  private ServiceManager serviceManager;
  @Assert
  private SoftAssertions softAssertions;

  /***
   * Невозможно удалить несуществующего кота, проверяю статус код, а также пример с методом getError()
   */

  @Test
  public void deleteNonExistentPet() {

    var pet = new PetDTOBuilder()
        .id(1L)
        .name(FAKER.bigBangTheory().character())
        .status(PetStatus.AVAILABLE.getStatus())
        .category(DOG)
        .photoUrls(List.of(FAKER.avatar().image()))
        .tags(List.of(BIG, BROWN))
        .build();

    var createdPetId = serviceManager.getPetServiceApi().createPet(pet).getId();
    serviceManager.getPetServiceApi().deletePetById(createdPetId);

    var errorResponse = getError(() -> serviceManager.getPetServiceApi().deletePetById(createdPetId));

    assertThat(errorResponse.statusCode())
        .as("Status code error comparison")
        .isEqualTo(HttpStatus.SC_NOT_FOUND);
  }
}
