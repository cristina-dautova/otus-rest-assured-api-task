package ru.otus.rest.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import ru.otus.rest.exceptions.HandledFailure;

import java.util.function.Supplier;

public class ValidationHelper {

  public static ExtractableResponse<Response> getError(Supplier<?> supplier) {
    try {
      supplier.get();
    } catch (HandledFailure failure) {
      return failure.getErrorResponse();
    }
    throw new IllegalStateException("Failure doesn't exist");
  }
}
