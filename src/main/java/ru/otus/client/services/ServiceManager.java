package ru.otus.client.services;


public class ServiceManager {

  private PetServiceApi petServiceApi;

  public PetServiceApi getPetServiceApi() {
    return new PetServiceApi(petServiceApi);
  }

  public ServiceManager() {
    this.petServiceApi = new PetServiceApi();
  }
}
