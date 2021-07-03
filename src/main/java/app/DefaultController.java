package app;

import app.client.ExchangeRateClient;
import app.client.GifClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
public class DefaultController {
    @Autowired
    private Config config;

    @Autowired
    public ExchangeRateClient exchangeRateClient;

    @Autowired
    public GifClient gifClient;

    @GetMapping("/result")
    public String giveResult(Model model) {

        // query to exchange rate server
        String currency = config.getCurrency();
        String exchangeRateIdID = config.getOpenExchangeRateId();
        String exchangeRateServer = config.getOpenExchangeRateServer();
        String currentExchangeRate = exchangeRateServer + "latest.json?app_id=" + exchangeRateIdID;

        URI uri = URI.create(currentExchangeRate);

        // Use Feign
        ExchangeRate currentExchange = exchangeRateClient.getExchange(uri);

        double rub = currentExchange.getRates().get("RUB");
        double currencyExchanged = currentExchange.getRates().get(currency);

        double currencyToRub = rub / currencyExchanged;

        LocalDateTime date = LocalDateTime.now().minusDays(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        String yesterday = format.format(date);


        String exchangeRateYesterday = exchangeRateServer + "historical/" + yesterday + ".json?app_id=" + exchangeRateIdID;
        uri = URI.create(exchangeRateYesterday);

        // Get exchange rate of yesterday
        ExchangeRate yesterdayExchange = exchangeRateClient.getExchange(uri);

        rub = yesterdayExchange.getRates().get("RUB");
        currencyExchanged = yesterdayExchange.getRates().get(currency);
        double yesterdayToRub = rub / currencyExchanged;


        // query to  gif server.
        String gifApiKey = config.getGiphyId();
        String gifServer = config.getGiphyServer();
        String tag;

        if (currencyToRub < yesterdayToRub)
        {
            tag = "broken";
        }
        else
        {
            tag = "rich";
        }
        String gifRequest = gifServer + "v1/gifs/random?api_key=" + gifApiKey + "&tag=" + tag;
        uri = URI.create(gifRequest);


        String gifID = gifClient.getGif(uri).data.getId();

        // final link to gif
        String gifPath = "https://i.giphy.com/media/" + gifID + "/giphy.gif";
        model.addAttribute("gifPath", gifPath);

        return "index";
    }
}
