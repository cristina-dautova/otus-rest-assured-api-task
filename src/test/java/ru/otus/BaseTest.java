package ru.otus;

import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public abstract class BaseTest {

  protected SoftAssertions softAssertions;
  protected final static Faker FAKER = new Faker();

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
