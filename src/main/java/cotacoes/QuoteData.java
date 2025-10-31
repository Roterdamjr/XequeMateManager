package cotacoes;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteData {

    private String symbol;
    private String shortName;
    private Double regularMarketPrice;
    private String regularMarketTime; // A data e hora da cotação

    // O Jackson usa os métodos get/set para mapear as chaves JSON.

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Double getRegularMarketPrice() {
        return regularMarketPrice;
    }

    public void setRegularMarketPrice(Double regularMarketPrice) {
        this.regularMarketPrice = regularMarketPrice;
    }

    public String getRegularMarketTime() {
        return regularMarketTime;
    }

    public void setRegularMarketTime(String regularMarketTime) {
        this.regularMarketTime = regularMarketTime;
    }
}
