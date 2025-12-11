package org.cftoolsuite.actuator.runtime.metadata;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.io.Resource;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class CycloneDxInfoContributor implements InfoContributor, InitializingBean {

    private final Resource bomFile;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private @Nullable List<Dependency> dependencies;

    public CycloneDxInfoContributor(@Value("classpath:META-INF/sbom/application.cdx.json") Resource bomFile) {
        this.bomFile = bomFile;
    }

    @Override
    public void contribute(Info.Builder builder) {
        if (dependencies != null) {
            builder.withDetail("dependencies", dependencies);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (bomFile.exists()) {
            try (var is = bomFile.getInputStream()) {
                JsonNode root = objectMapper.readTree(is);
                JsonNode components = root.get("components");
                if (components != null && components.isArray()) {
                    this.dependencies = new ArrayList<>();
                    for (JsonNode component : components) {
                        String group = getStringOrNull(component, "group");
                        String name = getStringOrNull(component, "name");
                        String version = getStringOrNull(component, "version");
                        this.dependencies.add(new Dependency(group, name, version));
                    }
                }
            }
        }
    }

    private String getStringOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        return fieldNode != null && !fieldNode.isNull() ? fieldNode.stringValue() : null;
    }

    record Dependency(String groupId, String artifactId, String version) {}
}
