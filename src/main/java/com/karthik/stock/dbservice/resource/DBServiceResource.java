package com.karthik.stock.dbservice.resource;

import com.karthik.stock.dbservice.model.Quote;
import com.karthik.stock.dbservice.model.Quotes;
import com.karthik.stock.dbservice.repository.QuotesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/db")
public class DBServiceResource {

    private QuotesRepository quotesRepository;

    public DBServiceResource(QuotesRepository quotesRepository) {
        this.quotesRepository = quotesRepository;
    }

    @GetMapping("/{username}")
    public List<String> getQuotes(@PathVariable("username") final String username) {
        return getQuotesByName(username);
    }

    private List<String> getQuotesByName(String username) {
        return quotesRepository.findByUserName(username)
                .stream()
                .map(Quote::getQuote)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public List<String> add(@RequestBody final Quotes quotes) {
        quotes.getQuotes()
                .stream()
                .map(quote -> new Quote(quotes.getUserName(), quote))
                .forEach(quote -> quotesRepository.save(quote));

        return getQuotesByName(quotes.getUserName());
    }

    @PostMapping("delete/{username}")
    public List<String> delete(@PathVariable("username") String userName) {
        List<Quote> quotes = quotesRepository.findByUserName(userName);
        quotesRepository.deleteAll(quotes);

        return getQuotesByName(userName);
    }

}
