package org.darcstarsolutions.coinmx.test.utils

import org.darcstarsolutions.coinmx.test.configurations.CoinMxConfiguration

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Created by mharris on 6/29/15.
 */
class ApiUtils {
    static String signData(CoinMxConfiguration coinMxConfiguration, Map<String, String> map) {
        String data = ""
        List<String> sorted = new ArrayList<>()
        Map<String, String> sortedMap = map.sort()
        sortedMap.forEach({ key, value ->
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
        return value
    }
}
