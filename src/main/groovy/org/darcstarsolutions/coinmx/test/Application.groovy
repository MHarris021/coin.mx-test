package org.darcstarsolutions.coinmx.test

import org.darcstarsolutions.coinmx.test.configurations.CoinMxConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Created by mharris on 6/29/15.
 */

@SpringBootApplication
@EnableConfigurationProperties
class Application implements CommandLineRunner {

    @Autowired
    private RestTemplate restTemplate

    @Autowired
    private CoinMxConfiguration coinMxConfiguration

    public static final String URI = "https://coin.mx/api/v2.1/merchant/transfer_requests"

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }

    @Override
    void run(String... args) throws Exception {
        println("CoinMx Api Key: " + coinMxConfiguration.apiKey)
        println("CoinMx Api Secret: " + coinMxConfiguration.apiSecret)
        Map<String, String> variables = new HashMap<>()
        variables.put("amount", "10")
        variables.put("currency", "BTC")
        variables.put("description", "test")
        variables.put("duration", "1")
        variables.put("merchant_id", "22")
        variables.put("nonce", "123")
        HttpHeaders headers = new HttpHeaders()
        headers.set("API_KEY", coinMxConfiguration.apiKey)
        String signedData = signData(variables)
        headers.set("SIGNED_DATA", signedData)

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers)
        String response = restTemplate.exchange(URI, HttpMethod.GET, entity, String.class, variables)
        println("Response from request: " + response)
    }

    String signData(Map<String, String> map) {
        String data = ""
        List<String> sorted = new ArrayList<>()
        map = map.sort()
        map.forEach({ key, value ->
            sorted.add(key + "=" + value)
        })
        data = "/api/v2.1/merchant/transfer_request?" + sorted.join("&")
        println("Sorted and joined data: " + data)
        Mac mac = Mac.getInstance("HmacSHA512")
        SecretKeySpec key = new SecretKeySpec(coinMxConfiguration.apiSecret.getBytes("UTF-8"), "HmacSHA512")
        mac.init(key)
        byte[] bytes = mac.doFinal(data.getBytes("UTF-8"))
        println("Digest: " + bytes)
        Base64 base64 = new Base64()
        String value = base64.getEncoder().encodeToString(bytes)
        println("Signed Data: " + value)

    }
}
