package ru.otus.models.pet;

import java.util.ArrayList;
import java.util.List;

public class PetDTOBuilder {

  private Long id;
  private PetDTO.Category category;
  private String name;
  private List<String> photoUrls;
  private List<PetDTO.Tag> tags;
  private String status;

  public PetDTOBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public PetDTOBuilder category(PetDTO.Category category) {
    this.category = (category != null) ? new PetDTO.Category(category.getId(), category.getName()) : null;
    return this;
  }

  public PetDTOBuilder name(String name) {
    this.name = name;
    return this;
  }

  public PetDTOBuilder photoUrls(List<String> photoUrls) {
    this.photoUrls = new ArrayList<>(photoUrls);
    return this;
  }

  public PetDTOBuilder tags(List<PetDTO.Tag> tags) {
    this.tags = new ArrayList<>(tags);
    return this;
  }

  public PetDTOBuilder status(String status) {
    this.status = status;
    return this;
  }

  public PetDTO build() {
    return new PetDTO(id, category, name, photoUrls, tags, status);
  }
}
