package com.stockdragon.libraries.financialstatements.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockdragon.libraries.financialstatements.model.dto.AnnualFact;
import com.stockdragon.libraries.financialstatements.model.type.CanonicalItem;
import com.stockdragon.libraries.financialstatements.registry.TagMappingRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyFactsParserServiceTest {

    private CompanyFactsParserService parser;

    @BeforeEach
    void setUp() {
        TagMappingRegistry registry = new TagMappingRegistry();
        FactsSelectorService selector = new FactsSelectorService();
        parser = new CompanyFactsParserService(null, registry, selector, new ObjectMapper());
    }

    @Test
    void usGaapRevenue() {
        String json = """
            {"facts":{"us-gaap":{"RevenueFromContractWithCustomerExcludingAssessedTax":{
              "units":{"USD":[
                {"start":"2023-10-01","end":"2024-09-28","val":391035000000,
                 "form":"10-K","filed":"2024-11-01"},
                {"start":"2023-01-01","end":"2023-03-31","val":94836000000,
                 "form":"10-Q","filed":"2023-05-05"}
              ]}
            }}}}
            """;

        Map<CanonicalItem, List<AnnualFact>> result =
                parser.parseJson(json, FactsSelectorService.RestatementPolicy.LATEST_RESTATED);

        List<AnnualFact> revenue = result.get(CanonicalItem.REVENUE);
        assertThat(revenue).hasSize(1);
        assertThat(revenue.get(0).value().longValue()).isEqualTo(391035000000L);
        assertThat(revenue.get(0).currency()).isEqualTo("USD");
        assertThat(revenue.get(0).form()).isEqualTo("10-K");
    }

    @Test
    void usGaapRevenueFallback() {
        String json = """
            {"facts":{"us-gaap":{"Revenues":{
              "units":{"USD":[
                {"start":"2022-09-01","end":"2023-08-31","val":64118000000,
                 "form":"10-K","filed":"2023-10-12"}
              ]}
            }}}}
            """;

        Map<CanonicalItem, List<AnnualFact>> result =
                parser.parseJson(json, FactsSelectorService.RestatementPolicy.LATEST_RESTATED);

        assertThat(result.get(CanonicalItem.REVENUE)).hasSize(1);
        assertThat(result.get(CanonicalItem.REVENUE).get(0).sourceTag())
                .isEqualTo("Revenues");
    }

    @Test
    void usGaapLiabilitiesComposed() {
        String json = """
            {"facts":{"us-gaap":{
              "LiabilitiesCurrent":{"units":{"USD":[
                {"end":"2023-08-31","val":20000000000,"form":"10-K","filed":"2023-10-12"}
              ]}},
              "LiabilitiesNoncurrent":{"units":{"USD":[
                {"end":"2023-08-31","val":30000000000,"form":"10-K","filed":"2023-10-12"}
              ]}}
            }}}
            """;

        Map<CanonicalItem, List<AnnualFact>> result =
                parser.parseJson(json, FactsSelectorService.RestatementPolicy.LATEST_RESTATED);

        List<AnnualFact> liab = result.get(CanonicalItem.TOTAL_LIABILITIES);
        assertThat(liab).hasSize(1);
        assertThat(liab.get(0).value().longValue()).isEqualTo(50000000000L);
    }

    @Test
    void ifrsTsmc() {
        String json = """
            {"facts":{"ifrs-full":{"Revenue":{
              "units":{"TWD":[
                {"start":"2023-01-01","end":"2023-12-31","val":2161736100000,
                 "form":"20-F","filed":"2024-04-18"},
                {"start":"2023-01-01","end":"2023-06-30","val":1000000000000,
                 "form":"6-K","filed":"2023-10-18"}
              ],"USD":[
                {"start":"2023-01-01","end":"2023-12-31","val":70610000000,
                 "form":"20-F","filed":"2024-04-18"}
              ]}
            }}}}
            """;

        Map<CanonicalItem, List<AnnualFact>> result =
                parser.parseJson(json, FactsSelectorService.RestatementPolicy.LATEST_RESTATED);

        List<AnnualFact> revenue = result.get(CanonicalItem.REVENUE);
        assertThat(revenue).hasSize(1);
        assertThat(revenue.get(0).currency()).isEqualTo("TWD");
        assertThat(revenue.get(0).form()).isEqualTo("20-F");
    }

    @Test
    void restatementPolicy() {
        String json = """
            {"facts":{"us-gaap":{"NetIncomeLoss":{
              "units":{"USD":[
                {"start":"2017-10-01","end":"2018-09-29","val":59531000000,
                 "form":"10-K","filed":"2018-11-05"},
                {"start":"2017-10-01","end":"2018-09-29","val":44242000000,
                 "form":"10-K/A","filed":"2019-01-15"}
              ]}
            }}}}
            """;

        Map<CanonicalItem, List<AnnualFact>> latest =
                parser.parseJson(json, FactsSelectorService.RestatementPolicy.LATEST_RESTATED);
        Map<CanonicalItem, List<AnnualFact>> original =
                parser.parseJson(json, FactsSelectorService.RestatementPolicy.AS_ORIGINALLY_REPORTED);

        assertThat(latest.get(CanonicalItem.NET_INCOME).get(0).value().longValue())
                .isEqualTo(44242000000L);
        assertThat(original.get(CanonicalItem.NET_INCOME).get(0).value().longValue())
                .isEqualTo(59531000000L);
    }
}
