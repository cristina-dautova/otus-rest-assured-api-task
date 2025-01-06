package ru.otus.client.services.wiremock;

import static ru.otus.config.LoadConfig.APPLICATION_CONFIG;

import ru.otus.client.services.BaseServiceApi;

public class BaseWiremockServiceApi extends BaseServiceApi {
  public BaseWiremockServiceApi() {
    super(APPLICATION_CONFIG.baseUrl());
  }
}
