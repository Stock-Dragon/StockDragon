package com.stockdragon.libraries.financialstatements.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class SecTickersClientTest {

    private static final String BASE_URL = "http://sec.test";

    private MockRestServiceServer server;
    private SecTickersClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();
        client = new SecTickersClient(builder.build());
    }

    @Test
    void mapsTickersFromIndexedObject() {
        server.expect(requestTo(BASE_URL + "/files/company_tickers.json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {"0":{"cik_str":320193,"ticker":"AAPL","title":"Apple Inc."},
                         "1":{"cik_str":1045810,"ticker":"NVDA","title":"NVIDIA CORP"}}
                        """, MediaType.APPLICATION_JSON));

        var result = client.fetchAll();

        assertThat(result).hasSize(2);
        assertThat(result.get("0").cik()).isEqualTo(320193L);
        assertThat(result.get("0").ticker()).isEqualTo("AAPL");
        server.verify();
    }
}
