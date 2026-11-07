package com.stockdragon.libraries.financialstatements.registry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.stockdragon.libraries.financialstatements.model.type.CanonicalItem;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class TagMappingRegistry {

    private static final String ERROR_TAG_MAPPING_NOT_FOUND = "tag-mapping.yml not found";
    private static final String ERROR_FAILED_TO_LOAD = "Failed to load tag-mapping.yml";

    private static final String TOTAL_LIABILITIES = "TOTAL_LIABILITIES_COMPONENTS";

    private final Map<String, Map<String, List<String>>> mapping;

    public TagMappingRegistry() {
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("tag-mapping.yml")) {
            if (is == null) throw new IllegalStateException(ERROR_TAG_MAPPING_NOT_FOUND);
            ObjectMapper mapper = YAMLMapper.builder().build();
            this.mapping = mapper.readValue(is, new TypeReference<>() {});
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_FAILED_TO_LOAD, e);
        }
    }

    public List<String> tagsFor(String taxonomy, CanonicalItem item) {
        Map<String, List<String>> byItem = mapping.get(taxonomy);
        if (byItem == null) return Collections.emptyList();
        List<String> tags = byItem.get(item.name());
        return tags != null ? tags : Collections.emptyList();
    }

    public List<String> liabilityComponentsTags(String taxonomy) {
        Map<String, List<String>> byItem = mapping.get(taxonomy);
        if (byItem == null) return Collections.emptyList();
        List<String> tags = byItem.get(TOTAL_LIABILITIES);
        return tags != null ? tags : Collections.emptyList();
    }

    public boolean hasTaxonomy(String taxonomy) {
        return mapping.containsKey(taxonomy);
    }
}
