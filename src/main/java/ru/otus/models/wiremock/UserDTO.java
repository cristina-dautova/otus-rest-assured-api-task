package ru.otus.models.wiremock;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

  private String name;
  private String course;
  private String email;
  private int age;
}
