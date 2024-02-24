package ru.otus;

import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

  protected final static Faker FAKER = new Faker();
  protected SoftAssertions softAssertions;

  @BeforeEach
  public void beforeEach() {
    softAssertions = new SoftAssertions();
  }

  @AfterEach
  public void afterEach() {
    if (softAssertions != null) {
      softAssertions.assertAll();
    }
  }
}
