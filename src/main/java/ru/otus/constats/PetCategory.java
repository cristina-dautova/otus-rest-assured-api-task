package ru.otus.constats;

import ru.otus.models.pet.PetDTO;

public class PetCategory {

  public static final PetDTO.Category DOG = new PetDTO.Category(1L, "dog");
  public static final PetDTO.Category CAT = new PetDTO.Category(2L, "cat");
  public static final PetDTO.Category PARROT = new PetDTO.Category(3L, "parrot");
}
