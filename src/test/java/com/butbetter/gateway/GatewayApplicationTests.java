package com.butbetter.gateway;

import com.butbetter.gateway.dummys.AlcoholBeverageType;
import com.butbetter.gateway.dummys.AlcoholRatingType;
import com.butbetter.gateway.dummys.DummyAlcohol;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayApplicationTests {

    private static final String ALCOHOL_URL = "/v1/application/alcohol/";
    private static final String URL_PRODUCT_INFORMATION = "/v1/application/productinformation/";
    private static final String URL_VAT = "/v1/application/VAT/";

    private final RestTemplate restTemplate;

    public GatewayApplicationTests() {
        this.restTemplate = new RestTemplate();
    }

    @BeforeEach
    void setUp() {
        restTemplate.delete(RouterManager.URL_APPLICATION + ALCOHOL_URL);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void deleteAll_alcohol() {
        assertThat(restTemplate.getForObject(RouterManager.URL_APPLICATION + ALCOHOL_URL, String.class).isEmpty());
    }

    @Test
    void create_and_get_A_alcohol() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        DummyAlcohol alcohol = new DummyAlcohol(AlcoholBeverageType.beer, "Frankfurter", BigDecimal.valueOf(20.99),
                5, 0.5, AlcoholRatingType.four, 16, false, false, "Deutschland");
        HttpEntity<DummyAlcohol> requestEntity = new HttpEntity<>(alcohol, headers);

        restTemplate.postForEntity(RouterManager.URL_APPLICATION + ALCOHOL_URL, requestEntity, String.class);
        String result = restTemplate.getForObject(RouterManager.URL_APPLICATION + ALCOHOL_URL, String.class);

        List<DummyAlcohol> dummyAlcohol = objectMapper.readValue(result, new TypeReference<>() {
        });

        assertThat(!result.isEmpty()); //isNotEmpty
        assertThat(dummyAlcohol.get(0).getAgeOfRestrictions() == alcohol.getAgeOfRestrictions()
                && dummyAlcohol.get(0).isBio() == alcohol.isBio() && dummyAlcohol.get(0).getAmount() == alcohol.getAmount());
    }

    @Test
    void getOne_and_DeleteThisOne_alcohol() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        DummyAlcohol alcohol = new DummyAlcohol(AlcoholBeverageType.vodka, "RelativeVodka", BigDecimal.valueOf(5.30), 40, 1,
                AlcoholRatingType.four, 21, false, true, "Belarus");
        HttpEntity<DummyAlcohol> requestEntity = new HttpEntity<>(alcohol, headers);

        restTemplate.postForEntity(RouterManager.URL_APPLICATION + ALCOHOL_URL, requestEntity, String.class);
        String result = restTemplate.getForObject(RouterManager.URL_APPLICATION + ALCOHOL_URL, String.class);

        List<DummyAlcohol> dummyAlcohol = objectMapper.readValue(result, new TypeReference<>() {});


    }
}
