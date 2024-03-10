package ru.otus.pet;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.constats.PetCategory.DOG;
import static ru.otus.constats.PetStatus.*;
import static ru.otus.constats.PetTags.BIG;
import static ru.otus.constats.PetTags.BROWN;

import net.datafaker.Faker;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith({TestHelperExtension.class})
public class FindPetByStatusTest {

  private final static Faker FAKER = new Faker();
  @Precondition
  private Preconditions preconditions;
  @ApiManager
  private ServiceManager serviceManager;
  @Assert
  private SoftAssertions softAssertions;
  private PetDTO pet;

  private static List<Long> getDescendingList(List<Long> petsIds) {
    List<Long> sortedList = new ArrayList<>(List.copyOf(petsIds));
    sortedList.sort(Collections.reverseOrder());
    return sortedList;
  }

  @BeforeEach
  public void createPet() {
    pet = createPetWithStatus(AVAILABLE);
  }

  /***
   * Проверяю, что новый объект животного будет в списке животных, отобранных по статусу
   */

  @Test
  public void getCreatedPetByStatus() {

    var petsByStatusDTO = serviceManager.getPetServiceApi().findPetsByStatus(pet.getStatus());

    var petIdsByStatus = petsByStatusDTO.stream()
        .map(PetDTO::getId)
        .toList();

    assertThat(petIdsByStatus)
        .as("Created pet is found by status")
        .contains(pet.getId());
  }

  /***
   * Проверяю, что в листе животных, отобранных по статусу, отображаются животные только с этими статусами
   */

  @Test
  public void getPetsByStatusFilter() {

    createPetWithStatus(PENDING);

    var petsByStatusDTO = serviceManager.getPetServiceApi().findPetsByStatus(PENDING.getStatus(), AVAILABLE.getStatus());

    var petsStatus = petsByStatusDTO.stream()
        .map(PetDTO::getStatus)
        .toList();

    assertThat(petsStatus)
        .as("Pet status list evaluation")
        .contains(PENDING.getStatus())
        .contains(AVAILABLE.getStatus())
        .doesNotContain(SOLD.getStatus());
  }

  /***
   * BUG: Проверяю правильность сортировки. Если в 1м тесте свежесозданная сущность возвращается
   * по GET /pet/findPetsByStatus, то новые сущности должны быть в начале списка
   */

  @Test
  public void sortingShouldBeDescendingInGetPetByStatus() {

    var petsByStatusDTO = serviceManager.getPetServiceApi().findPetsByStatus(pet.getStatus());

    var petIdsByStatus = petsByStatusDTO.stream()
        .map(PetDTO::getId)
        .toList();

    var petIdsByStatusSorted = getDescendingList(petIdsByStatus);

    softAssertions.assertThat(petIdsByStatus)
        .as("List by status returns newly created items")
        .contains(pet.getId());

    softAssertions.assertThat(petIdsByStatus)
        .as("List by status has descending order")
        .isEqualTo(petIdsByStatusSorted);
  }

  private PetDTO createPetWithStatus(PetStatus petStatus) {
    var pet = new PetDTOBuilder()
        .name(FAKER.funnyName().name())
        .status(petStatus.getStatus())
        .category(DOG)
        .photoUrls(List.of(FAKER.avatar().image()))
        .tags(List.of(BIG, BROWN))
        .build();

    return preconditions.createPet(pet);
  }
}
