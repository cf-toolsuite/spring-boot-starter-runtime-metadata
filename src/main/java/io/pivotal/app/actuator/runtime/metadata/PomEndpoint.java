package io.pivotal.app.actuator.runtime.metadata;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

@Endpoint(id = "pom")
public class PomEndpoint {

    private final ArtifactsService artifactsService;

    public PomEndpoint(ArtifactsService artifactsService) {
        this.artifactsService = artifactsService;
    }

    @ReadOperation
    public String getPom() {
        return artifactsService.findPom();
    }
}
