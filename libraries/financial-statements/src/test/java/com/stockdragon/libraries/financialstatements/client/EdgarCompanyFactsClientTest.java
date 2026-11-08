package com.stockdragon.libraries.financialstatements.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class EdgarCompanyFactsClientTest {

    private static final String BASE_URL = "http://data.sec.test";

    private MockRestServiceServer server;
    private EdgarCompanyFactsClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();
        client = new EdgarCompanyFactsClient(builder.build());
    }

    @Test
    void padsCikToTenDigitsAndReturnsRawBody() {
        String body = "{\"cik\":320193,\"entityName\":\"Apple Inc.\"}";
        server.expect(requestTo(BASE_URL + "/api/xbrl/companyfacts/CIK0000320193.json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        assertThat(client.fetchCompanyFacts(320193L)).isEqualTo(body);
        server.verify();
    }

    @Test
    void throwsOnNotFound() {
        server.expect(requestTo(BASE_URL + "/api/xbrl/companyfacts/CIK0000000001.json"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> client.fetchCompanyFacts(1L))
                .isInstanceOf(HttpClientErrorException.NotFound.class);
    }
}
