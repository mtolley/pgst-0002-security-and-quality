package com.hippotech.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hippotech.api.data.BlogAnalyticsRepository;
import com.hippotech.api.data.BlogRepository;
import com.hippotech.api.data.BlogSubscriberRepository;
import com.hippotech.api.model.BlogPost;
import com.hippotech.api.model.BlogPostComment;
import com.hippotech.api.model.BlogSubscriber;
import com.hippotech.api.model.Quote;
import com.hippotech.api.security.AuthenticationCheck;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

@Api(tags = "Quotes")
@RestController
public class QuoteController {
    private static final Logger log = LogManager.getLogger(QuoteController.class);
    private String quoteUrl = "https://zenquotes.io/api/quotes";

    @GetMapping("api/quotes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quotes of the day are returned"),
    })
    public ResponseEntity<List<String>> getAll() {
        AuthenticationCheck.checkJWTToken("Bearer: xyz");
        log.warn("Geting quotes from external Quote Provider");

        List<String> quotes = new ArrayList<String>();

        try {

          // Read JSON from https://zenquotes.io/api/quotes
          ObjectMapper mapper = new ObjectMapper();
       
          URL RT = new URL(quoteUrl).toURI().toURL();

          //Retrieving the contents of the specified page
          Scanner sc = new Scanner(RT.openStream());
          //Instantiating the StringBuffer class to hold the result
          StringBuffer sb = new StringBuffer();
          while(sc.hasNext()) {
            sb.append(sc.next());
            
          }
          //Retrieving the String from the String Buffer object
          String result = sb.toString();

          var quotesOfTheDay = mapper.readValue(result, Quote[].class);
          for (var q : quotesOfTheDay) {
             log.info(q.h);
              quotes.add(q.h);
           }
      } catch (Exception e) {
          log.error(e.toString());
      }

      return new ResponseEntity<>(quotes, HttpStatus.OK);
    }
}
