package uk.co.jerryjane.cozyfarm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class AwsSesConfig {

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .region(Region.EU_WEST_2)
                .credentialsProvider(
                        ProfileCredentialsProvider.create("cozy-farm")
                )
                .build();
    }
}
