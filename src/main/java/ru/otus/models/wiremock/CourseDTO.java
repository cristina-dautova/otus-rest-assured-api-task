package ru.otus.models.wiremock;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseDTO {

  private String name;
  private Integer price;
}
