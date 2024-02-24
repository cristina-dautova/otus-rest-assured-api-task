package ru.otus.pet;

import io.restassured.common.mapper.TypeRef;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.BaseTest;
import ru.otus.client.PetServiceApi;
import ru.otus.constats.PetStatus;
import ru.otus.models.pet.PetDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.constats.PetCategory.DOG;
import static ru.otus.constats.PetStatus.*;
import static ru.otus.constats.PetTags.BIG;
import static ru.otus.constats.PetTags.BROWN;

public class FindPetByStatusTest extends BaseTest {

  private final PetServiceApi petServiceApi = new PetServiceApi();

  private PetDTO pet;

  @BeforeEach
  public void createPet() {
    pet = createPetWithStatus(AVAILABLE);
  }

  /***
   * Проверяю, что новый объект животного будет в списке животных, отобранных по статусу
   */

  @Test
  public void getCreatedPetByStatus() {

    var petsByStatus = petServiceApi.findPetsByStatus(pet.getStatus());

    var petsByStatusDTO = petsByStatus.body().as(new TypeRef<List<PetDTO>>() {
    });

    var petIdsByStatus = petsByStatusDTO.stream()
        .map(PetDTO::getId)
        .toList();

    assertThat(petsByStatus.statusCode())
        .as("Status code comparison")
        .isEqualTo(HttpStatus.SC_OK);

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

    var petsByStatus = petServiceApi.findPetsByStatus(PENDING.getStatus(), AVAILABLE.getStatus());

    var petsByStatusDTO = petsByStatus.body().as(new TypeRef<List<PetDTO>>() {
    });

    var petsStatus = petsByStatusDTO.stream()
        .map(PetDTO::getStatus)
        .toList();

    assertThat(petsByStatus.statusCode())
        .as("Status code comparison")
        .isEqualTo(HttpStatus.SC_OK);

    assertThat(petsStatus)
        .as("Pet status list evaluation")
        .contains(PENDING.getStatus())
        .contains(AVAILABLE.getStatus())
        .doesNotContain(SOLD.getStatus());
  }

  /***
   * Проверяю правильность сортировки. Если в 1м тесте свежесозданная сущность возвращается
   * по GET /pet/findPetsByStatus, то новые сущности должны быть в начале списка
   */

  @Test
  public void sortingShouldBeDescendingInGetPetByStatus() {

    var petsByStatus = petServiceApi.findPetsByStatus(pet.getStatus());

    var petsByStatusDTO = petsByStatus.body().as(new TypeRef<List<PetDTO>>() {
    });

    var petIdsByStatus = petsByStatusDTO.stream()
        .map(PetDTO::getId)
        .toList();

    var petIdsByStatusSorted = getDescendingList(petIdsByStatus);

    assertThat(petsByStatus.statusCode())
        .as("Status code comparison")
        .isEqualTo(HttpStatus.SC_OK);

    softAssertions.assertThat(petIdsByStatus)
        .as("List by status returns newly created items")
        .contains(pet.getId());

    softAssertions.assertThat(petIdsByStatus)
        .as("List by status has descending order")
        .isEqualTo(petIdsByStatusSorted);

    softAssertions.assertAll();
  }

  private static List<Long> getDescendingList(List<Long> petsIds) {
    List<Long> sortedList = new ArrayList<>(List.copyOf(petsIds));
    sortedList.sort(Collections.reverseOrder());
    return sortedList;
  }

  private PetDTO createPetWithStatus(PetStatus petStatus) {
    var pet = PetDTO.builder()
        .name(FAKER.funnyName().name())
        .status(petStatus.getStatus())
        .category(DOG)
        .tags(List.of(BIG, BROWN))
        .build();

    return petServiceApi.createPet(pet).body().as(PetDTO.class);
  }
}
