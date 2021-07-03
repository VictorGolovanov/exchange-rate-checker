package app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class Config
{
    private String exchangeRateServer;
    private String exchangeRateId;
    private String giphyServer;
    private String giphyId;
    private String currency;


    public void setExchangeRateServer(String exchangeRateServer) {
        this.exchangeRateServer = exchangeRateServer;
    }

    public void setExchangeRateId(String exchangeRateId) {
        this.exchangeRateId = exchangeRateId;
    }

    public void setGiphyServer(String giphyServer) {
        this.giphyServer = giphyServer;
    }

    public void setGiphyId(String giphyId) {
        this.giphyId = giphyId;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public String getOpenExchangeRateServer() {
        return exchangeRateServer;
    }

    public String getOpenExchangeRateId() {
        return exchangeRateId;
    }

    public String getGiphyServer() {
        return giphyServer;
    }

    public String getGiphyId() {
        return giphyId;
    }

    public String getCurrency() {
        return currency;
    }



}