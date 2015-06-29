package org.darcstarsolutions.coinmx.test.configurations

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created by mharris on 6/29/15.
 */

@ConfigurationProperties(prefix = "coin-mx")
class CoinMxConfiguration {
    String apiKey
    String apiSecret
}
