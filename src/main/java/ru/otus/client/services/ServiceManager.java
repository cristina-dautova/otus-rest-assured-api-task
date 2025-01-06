package ru.otus.client.services;

import ru.otus.client.services.petstore.PetServiceApi;
import ru.otus.client.services.wiremock.WiremockServiceApi;

public class ServiceManager {

  private PetServiceApi petServiceApi;
  private WiremockServiceApi wiremockServiceApi;

  public PetServiceApi getPetServiceApi() {
    return new PetServiceApi(petServiceApi);
  }

  public WiremockServiceApi getWireMockServiceApi() {
    return new WiremockServiceApi(wiremockServiceApi);
  }

  public ServiceManager() {
    this.petServiceApi = new PetServiceApi();
    this.wiremockServiceApi = new WiremockServiceApi();
  }
}
