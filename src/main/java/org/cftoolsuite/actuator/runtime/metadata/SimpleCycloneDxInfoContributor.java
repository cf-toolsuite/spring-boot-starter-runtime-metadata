package org.cftoolsuite.actuator.runtime.metadata;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.io.Resource;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class SimpleCycloneDxInfoContributor implements InfoContributor, InitializingBean {

    private final Resource bomFile;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private @Nullable JsonNode bom;

    public SimpleCycloneDxInfoContributor(@Value("classpath:META-INF/sbom/application.cdx.json") Resource bomFile) {
        this.bomFile = bomFile;
    }

    @Override
    public void contribute(Info.Builder builder) {
        if (bom != null) {
            builder.withDetail("sbom", bom);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (bomFile.exists()) {
            try (var is = bomFile.getInputStream()) {
                this.bom = objectMapper.readTree(is);
            }
        }
    }
}
