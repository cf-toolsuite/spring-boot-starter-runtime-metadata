package io.pivotal.app.actuator.runtime.metadata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.info.ConditionalOnEnabledInfoContributor;
import org.springframework.boot.actuate.autoconfigure.info.InfoContributorFallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

@Configuration
public class RuntimeMetadataAutoConfiguration {

	public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 10;

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

    @Bean
	@ConditionalOnEnabledInfoContributor(value = "sbom", fallback = InfoContributorFallback.DISABLE)
	@Order(DEFAULT_ORDER)
    public SimpleCycloneDxInfoContributor simpleCycloneDxInfoContributor(@Value("classpath:bom.json") Resource bomFile) {
        return new SimpleCycloneDxInfoContributor(bomFile);
    }

    @Bean
	@ConditionalOnEnabledInfoContributor(value = "dependencies", fallback = InfoContributorFallback.DISABLE)
	@Order(DEFAULT_ORDER)
    public CycloneDxInfoContributor cycloneDxInfoContributor(@Value("classpath:bom.json") Resource bomFile) {
        return new CycloneDxInfoContributor(bomFile);
    }
}
