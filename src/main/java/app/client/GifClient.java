package app.client;

import app.Gif;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

@FeignClient(name = "GifClient", url = "https://ru")
public interface GifClient
{
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    Gif getGif(URI baseUrl);
}
