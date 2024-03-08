package io.pivotal.app.actuator.runtime.metadata;

import java.util.List;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

@Endpoint(id = "jars")
public class JarsEndpoint {

    private final ArtifactsService artifactsService;

    public JarsEndpoint(ArtifactsService artifactsService) {
        this.artifactsService = artifactsService;
    }

    @ReadOperation
    public List<String> getJars() {
        return artifactsService.findJars();
    }
}
