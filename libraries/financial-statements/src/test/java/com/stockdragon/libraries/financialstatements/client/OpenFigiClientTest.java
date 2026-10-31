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
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class OpenFigiClientTest {

    private static final String BASE_URL = "http://openfigi.test";
    private static final String MAPPING_URL = BASE_URL + "/v3/mapping";
    private static final String APPLE_ISIN = "US0378331005";

    private MockRestServiceServer server;
    private OpenFigiClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();
        client = new OpenFigiClient(builder.build());
    }

    @Test
    void sendsMappingRequestInExpectedFormat() {
        server.expect(requestTo(MAPPING_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idType").value("ID_ISIN"))
                .andExpect(jsonPath("$[0].idValue").value(APPLE_ISIN))
                .andRespond(withSuccess("[{\"data\":[]}]", MediaType.APPLICATION_JSON));

        client.findByIsin(APPLE_ISIN);

        server.verify();
    }

    @Test
    void mapsCandidatesFromResponse() {
        server.expect(requestTo(MAPPING_URL))
                .andRespond(withSuccess("""
                        [{"data":[
                          {"figi":"BBG000B9XRY4","name":"APPLE INC","ticker":"AAPL",
                           "exchCode":"US","securityType":"Common Stock","marketSector":"Equity"},
                          {"figi":"BBG000B9Y5X2","name":"APPLE INC","ticker":"AAPL",
                           "exchCode":"UW","securityType":"Common Stock","marketSector":"Equity"}
                        ]}]
                        """, MediaType.APPLICATION_JSON));

        var candidates = client.findByIsin(APPLE_ISIN);

        assertThat(candidates).hasSize(2);
        assertThat(candidates.get(0).figi()).isEqualTo("BBG000B9XRY4");
        assertThat(candidates.get(0).ticker()).isEqualTo("AAPL");
        assertThat(candidates.get(0).exchCode()).isEqualTo("US");
        assertThat(candidates.get(0).securityType()).isEqualTo("Common Stock");
        assertThat(candidates.get(1).exchCode()).isEqualTo("UW");
    }

    @Test
    void ignoresUnknownFields() {
        server.expect(requestTo(MAPPING_URL))
                .andRespond(withSuccess("""
                        [{"data":[{"ticker":"AAPL","someNewField":"x"}]}]
                        """, MediaType.APPLICATION_JSON));

        assertThat(client.findByIsin(APPLE_ISIN)).hasSize(1);
    }

    @Test
    void returnsEmptyListWhenIsinUnknown() {
        server.expect(requestTo(MAPPING_URL))
                .andRespond(withSuccess("""
                        [{"warning":"No identifier found."}]
                        """, MediaType.APPLICATION_JSON));

        assertThat(client.findByIsin("XX0000000000")).isEmpty();
    }

    @Test
    void throwsOnRateLimit() {
        server.expect(requestTo(MAPPING_URL))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        assertThatThrownBy(() -> client.findByIsin(APPLE_ISIN))
                .isInstanceOf(HttpClientErrorException.TooManyRequests.class);
    }
}
