package cotacoes;


import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrapiQuoteResponse {
    
    // O Jackson irá mapear o array "results" para uma lista de objetos Quote
    private List<QuoteData> results;

    public List<QuoteData> getResults() {
        return results;
    }

    public void setResults(List<QuoteData> results) {
        this.results = results;
    }
}
