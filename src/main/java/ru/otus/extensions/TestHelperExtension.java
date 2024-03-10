package ru.otus.extensions;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import ru.otus.Preconditions;
import ru.otus.client.services.ServiceManager;
import ru.otus.annotations.ApiManager;
import ru.otus.annotations.Assert;
import ru.otus.annotations.Precondition;
import ru.otus.rest.eraser.EntitiesManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestHelperExtension implements BeforeEachCallback, AfterEachCallback {

  private Preconditions preconditions;
  private SoftAssertions softAssertions;
  private ServiceManager serviceManager;

  public TestHelperExtension() {
    serviceManager = new ServiceManager();
    var entitiesManager = new EntitiesManager();
    preconditions = new Preconditions(serviceManager, entitiesManager);
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    softAssertions = new SoftAssertions();
    setFieldWithAnnotation(Assert.class, softAssertions, extensionContext);
    setFieldWithAnnotation(Precondition.class, preconditions, extensionContext);
    setFieldWithAnnotation(ApiManager.class, serviceManager, extensionContext);
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    if (softAssertions != null) {
      softAssertions.assertAll();
    }
    preconditions.cleanMethodEntities();
  }

  private void setFieldWithAnnotation(Class<? extends Annotation> annotation,
                                      Object value,
                                      ExtensionContext extensionContext) {
    getAnnotatedFields(annotation, extensionContext).forEach(field -> setField(field, value, extensionContext));
  }

  private void setField(Field field, Object value, ExtensionContext extensionContext) {
    field.setAccessible(true);
    try {
      field.set(extensionContext.getTestInstance().get(), value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Set<Field> getAnnotatedFields(Class<? extends Annotation> annotation, ExtensionContext extensionContext) {

    Set<Field> fields = new HashSet<>();
    Class<?> testClass = extensionContext.getTestClass().get();
    Arrays.stream(testClass.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(annotation))
        .forEach(fields::add);

    return fields;
  }
}
