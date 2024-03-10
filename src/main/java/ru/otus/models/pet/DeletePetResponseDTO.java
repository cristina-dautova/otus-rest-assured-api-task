package ru.otus.models.pet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeletePetResponseDTO {

  private String code;
  private String type;
  private Long message;
}
