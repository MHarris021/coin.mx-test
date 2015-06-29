package org.darcstarsolutions.coinmx.test.configurations

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

/**
 * Created by mharris on 6/29/15.
 */

@Configuration
@EnableConfigurationProperties(value = CoinMxConfiguration.class)
class ApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate()
    }

}
