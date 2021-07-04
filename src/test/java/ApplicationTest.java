import app.*;
import app.client.ExchangeRateClient;
import app.client.GifClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationStart.class)
@AutoConfigureMockMvc
public class ApplicationTest
{
    @LocalServerPort
    private int port;

    @Autowired
    private DefaultController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Config config;

    @MockBean
    private ExchangeRateClient exchangeRateClient;

    @MockBean
    private GifClient gifClient;

    @Test
    public void exchangeRateGifferTest() throws Exception {

        LocalDateTime date = LocalDateTime.now().minusDays(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        String yesterday = date.format(format);

        String exchangeRateCurrent = config.getOpenExchangeRateServer() + "latest.json?app_id=" + config.getOpenExchangeRateId();
        String exchangeRateYesterday = config.getOpenExchangeRateServer() + "historical/" + yesterday + ".json?app_id=" + config.getOpenExchangeRateId();
        String gifRequest = config.getGiphyServer() + "v1/gifs/random?api_key=" + config.getGiphyId() + "&tag=rich";

        URI uriCur = URI.create(exchangeRateCurrent);
        URI uriYes = URI.create(exchangeRateYesterday);
        URI uriGif = URI.create(gifRequest);

        ExchangeRate testExchangeRate = new ExchangeRate();

        Map<String, Double> exchangeRateMap = new HashMap<>();
        exchangeRateMap.put("USD", 70.0);
        exchangeRateMap.put("RUB", 1.0);

        testExchangeRate.setRates(exchangeRateMap);

        GifData testData = new GifData();
        Gif testGif = new Gif();
        testGif.setData(testData);
        // random gif id from app
        testGif.getGifData().setId("l2adePOqMVk37aCoP3");


        Mockito.when(exchangeRateClient.getExchange(uriCur)).thenReturn(testExchangeRate);
        Mockito.when(exchangeRateClient.getExchange(uriYes)).thenReturn(testExchangeRate);
        Mockito.when(gifClient.getGif(uriGif)).thenReturn(testGif);

        this.mockMvc
                .perform(get("/result"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<img src=\"https://i.giphy.com/media/")));
    }

}
