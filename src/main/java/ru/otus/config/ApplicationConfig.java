package ru.otus.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"classpath:application.properties"})
public interface ApplicationConfig extends Config {

  @Key("baseUrl")
  String baseUrl();
}
