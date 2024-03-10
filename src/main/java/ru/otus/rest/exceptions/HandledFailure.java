package ru.otus.rest.exceptions;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class HandledFailure extends IllegalStateException {

  private ExtractableResponse<Response> errorResponse;

  public HandledFailure(ExtractableResponse<Response> response) {
    this.errorResponse = response;
  }

  @Override
  public String toString() {
    StringBuilder message = new StringBuilder("HandledFailure{errorStatusCode=")
        .append(errorResponse.statusCode());
    if(!errorResponse.body().asPrettyString().isEmpty()) {
      message.append(",errorBody=").append(errorResponse.body().asPrettyString());
    }
    message.append('}');
    return message.toString();
  }
}
