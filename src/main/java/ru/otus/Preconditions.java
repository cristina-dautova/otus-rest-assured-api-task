package ru.otus;

import ru.otus.client.services.ServiceManager;
import ru.otus.models.pet.PetDTO;
import ru.otus.rest.eraser.EntitiesManager;

public class Preconditions {

  private final ServiceManager serviceManager;
  private final EntitiesManager entitiesManager;

  public Preconditions(ServiceManager serviceManager, EntitiesManager entitiesManager) {
    this.entitiesManager = new EntitiesManager(entitiesManager);
    this.serviceManager = serviceManager;
  }

  public void cleanMethodEntities() {
    if (!entitiesManager.eraseMethodEntitiesCollection.isEmpty()) {
      entitiesManager.eraseTestEntities();
    }
    entitiesManager.validateEraseForErrors();
  }

  public PetDTO createPet(PetDTO pet) {
    var createdPet = serviceManager.getPetServiceApi().createPet(pet);

    entitiesManager.addEraseMethodToMethodCollection(() -> {
      serviceManager.getPetServiceApi().deletePetById(createdPet.getId());
    });

    return createdPet;
  }
}
