package ru.otus.wiremock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;


public class BaseWiremockTest implements AfterAllCallback, BeforeAllCallback {

  protected static final String GET_SCORE_BY_USER_ID_ENDPOINT = "/user/get/1";
  private static final Integer PORT = 1122;
  private static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(PORT);

  @Override
  public void afterAll(ExtensionContext extensionContext) {
    WIRE_MOCK_SERVER.stop();
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    WIRE_MOCK_SERVER.start();
    configureFor(PORT);
  }

  protected static void stubGetMethodWithOkJsonBodyResponse(String getEndpoint, String body) {
    stubFor(get(urlEqualTo(getEndpoint))
        .willReturn(aResponse()
            .withBody(body)
            .withHeader(CONTENT_TYPE, APPLICATION_JSON)
            .withStatus(200)));
  }
}
