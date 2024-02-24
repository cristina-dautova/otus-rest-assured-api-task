package ru.otus.models.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetDTO {
  private Long id;
  private Category category;
  private String name;
  private List<String> photoUrls;
  private List<Tag> tags;
  private String status;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Category {

    private Long id;
    private String name;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Tag {
    private Long id;
    private String name;
  }
}
