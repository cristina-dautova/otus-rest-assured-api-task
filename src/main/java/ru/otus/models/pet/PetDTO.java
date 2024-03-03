package ru.otus.models.pet;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
public class PetDTO {
  @Getter
  @Setter
  private Long id;
  private Category category;
  @Getter
  @Setter
  private String name;
  private List<String> photoUrls;
  private List<Tag> tags;
  @Getter
  @Setter
  private String status;

  public void setCategory(Category category) {
    this.category = new Category(category.getId(), category.getName());
  }

  public Category getCategory() {
    return new Category(category.getId(), category.getName());
  }

  public void setPhotoUrls(List<String> photoUrls) {
    this.photoUrls = new ArrayList<>(photoUrls);
  }

  public List<String> getPhotoUrls() {
    return new ArrayList<>(photoUrls);
  }

  public void setTags(List<Tag> tags) {
    this.tags = new ArrayList<>(tags);
  }

  public List<Tag> getTags() {
    return new ArrayList<>(tags);
  }

  public PetDTO(Long id, Category category, String name, List<String> photoUrls, List<Tag> tags, String status) {
    this.id = id;
    this.category = (category != null) ? new Category(category.getId(), category.getName()) : null;
    this.name = name;
    this.photoUrls = new ArrayList<>(photoUrls);;
    this.tags = new ArrayList<>(tags);
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PetDTO petDTO = (PetDTO) o;
    return Objects.equals(id, petDTO.id)
        && Objects.equals(category, petDTO.category)
        && Objects.equals(name, petDTO.name)
        && Objects.equals(photoUrls, petDTO.photoUrls)
        && Objects.equals(tags, petDTO.tags)
        && Objects.equals(status, petDTO.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, category, name, photoUrls, tags, status);
  }

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
