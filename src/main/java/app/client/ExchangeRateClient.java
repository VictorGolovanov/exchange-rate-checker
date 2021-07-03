package app.client;

import app.ExchangeRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

@FeignClient(name ="ExchangeRateClient", url = "https://ru")
public interface ExchangeRateClient
{
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ExchangeRate getExchange(URI baseUrl);
}
