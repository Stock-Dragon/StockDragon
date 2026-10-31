package com.stockdragon.libraries.financialstatements.client;

import com.stockdragon.libraries.financialstatements.model.dto.OpenFigiInstrument;
import com.stockdragon.libraries.financialstatements.model.dto.OpenFigiMappingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenFigiClient {

    private final RestClient openFigiRestClient;

    private static final String MAPPING_PATH = "/v3/mapping";
    private static final String ID_TYPE_FIELD = "idType";
    private static final String ID_VALUE_FIELD = "idValue";
    private static final String ID_TYPE_ISIN = "ID_ISIN";

    private static final ParameterizedTypeReference<List<OpenFigiMappingResult>> RESPONSE_TYPE =
            new ParameterizedTypeReference<>() {};

    public List<OpenFigiInstrument> findByIsin(String isin) {
        List<OpenFigiMappingResult> response = openFigiRestClient.post()
                .uri(MAPPING_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(List.of(Map.of(ID_TYPE_FIELD, ID_TYPE_ISIN, ID_VALUE_FIELD, isin)))
                .retrieve()
                .body(RESPONSE_TYPE);
        if (response == null || response.isEmpty() || response.getFirst().data() == null) {
            return List.of();
        }
        return response.getFirst().data();
    }

}
