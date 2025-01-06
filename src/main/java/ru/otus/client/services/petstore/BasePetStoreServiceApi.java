package ru.otus.client.services.petstore;

import static ru.otus.config.LoadConfig.APPLICATION_CONFIG;

import ru.otus.client.services.BaseServiceApi;

public class BasePetStoreServiceApi extends BaseServiceApi {

  public BasePetStoreServiceApi() {
    super(APPLICATION_CONFIG.petStoreUrl());
  }
}
