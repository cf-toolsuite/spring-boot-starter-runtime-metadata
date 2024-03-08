package io.pivotal.app.actuator.runtime.metadata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuntimeMetadataAutoConfiguration {

    @Bean
    public ArtifactsService artifactsService() {
        return new ArtifactsService();
    }

    @Bean
    public PomEndpoint pomEndpoint(ArtifactsService artifactsService) {
        return new PomEndpoint(artifactsService);
    }

    @Bean
    public JarsEndpoint jarsEndpoint(ArtifactsService artifactsService) {
        return new JarsEndpoint(artifactsService);
    }
}
