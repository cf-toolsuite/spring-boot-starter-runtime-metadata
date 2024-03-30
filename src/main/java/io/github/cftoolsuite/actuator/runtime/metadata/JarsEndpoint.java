package io.github.cftoolsuite.actuator.runtime.metadata;

import java.util.Set;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

@Endpoint(id = "jars")
public class JarsEndpoint {

    private final ArtifactsService artifactsService;

    public JarsEndpoint(ArtifactsService artifactsService) {
        this.artifactsService = artifactsService;
    }

    @ReadOperation
    public Set<String> getJars() {
        return artifactsService.findJars();
    }
}
