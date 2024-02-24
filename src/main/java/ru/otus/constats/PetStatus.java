package ru.otus.constats;

import lombok.Getter;

public enum PetStatus {

  AVAILABLE("available"),
  PENDING("pending"),
  SOLD("sold");

  @Getter
  private final String status;

  PetStatus(String status) {
    this.status = status;
  }
}
