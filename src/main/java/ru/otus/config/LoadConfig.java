package ru.otus.config;

import org.aeonbits.owner.ConfigFactory;

public class LoadConfig {

  public static final ApplicationConfig APPLICATION_CONFIG = ConfigFactory
      .create(ApplicationConfig.class, System.getProperties());
}
