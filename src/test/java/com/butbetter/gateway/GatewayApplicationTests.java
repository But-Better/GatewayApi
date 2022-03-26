package com.butbetter.gateway;

import com.butbetter.gateway.dummys.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/*
Source of testing szenario http://localhost:9095/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GatewayApplicationTests {

    private static final String GATEWAY_URL = "http://localhost:9095/";
    private static final String ALCOHOL_URL = "/v1/application/alcohol/";
    private static final String URL_PRODUCT_INFORMATION = "/v1/application/productinformation/";
    private static final String URL_VAT = "/v1/application/VAT/";
    private static final String URL_TRANSLATION = "/v1/application/translator";

    private final RestTemplate restTemplate;

    public GatewayApplicationTests() {
        this.restTemplate = new RestTemplate();
    }

    @BeforeEach
    void setUp() {
        restTemplate.delete(GATEWAY_URL + ALCOHOL_URL);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void deleteAll_alcohol() {
        assertThat(restTemplate.getForObject(GATEWAY_URL + ALCOHOL_URL, String.class).isEmpty());
    }

    @Test
    void create_and_get_A_alcohol() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        DummyAlcohol alcohol = new DummyAlcohol(AlcoholBeverageType.beer, "Frankfurter", BigDecimal.valueOf(20.99),
                5, 0.5, AlcoholRatingType.four, 16, false, false, "Deutschland");
        HttpEntity<DummyAlcohol> requestEntity = new HttpEntity<>(alcohol, headers);

        var resultOfPost = restTemplate.postForEntity(GATEWAY_URL + ALCOHOL_URL, requestEntity, String.class);

        assertThat(resultOfPost.getStatusCode().value() == 200);

        String result = restTemplate.getForObject(GATEWAY_URL + ALCOHOL_URL, String.class);

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

        restTemplate.postForEntity(GATEWAY_URL + ALCOHOL_URL, requestEntity, String.class);
        String result = restTemplate.getForObject(GATEWAY_URL + ALCOHOL_URL, String.class);

        List<DummyAlcohol> dummyAlcohol = objectMapper.readValue(result, new TypeReference<>() {
        });

        assertThat(dummyAlcohol.get(0).getAgeOfRestrictions() == alcohol.getAgeOfRestrictions()
                && dummyAlcohol.get(0).isBio() == alcohol.isBio() && dummyAlcohol.get(0).getAmount() == alcohol.getAmount());

        UUID uuid = dummyAlcohol.get(0).getUuid();

        assertThat(dummyAlcohol.size() == 1); //length check == 1
        restTemplate.delete(GATEWAY_URL + ALCOHOL_URL, uuid);

        String resultAfterDelete = restTemplate.getForObject(GATEWAY_URL + ALCOHOL_URL, String.class);

        List<DummyAlcohol> dummyAlcoholAfterDelete = objectMapper.readValue(result, new TypeReference<>() {
        });

        assertThat(dummyAlcoholAfterDelete.size() == 0); // length check == 0 is deleted
    }

    @Test
    void get_and_post_deliveryInformation() throws JsonProcessingException {

        var objectMapper = new ObjectMapper();
        String firstRquest = restTemplate.getForObject(GATEWAY_URL + URL_PRODUCT_INFORMATION, String.class);
        List<DummyProductInformation> productInformations = objectMapper.readValue(firstRquest, new TypeReference<>() {
        });

        int firstResultOfSize = productInformations.size();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        int hour = 3;
        int minute = 30;
        var offsetDateTime = OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                .toInstant()
                .atOffset(ZoneOffset.ofHoursMinutes(hour, minute));

        var DummyProductInformation = new DummyProductInformation(
                offsetDateTime.toString(),
                20,
                new DummyAddress(
                        "name",
                        "companyName",
                        "street",
                        "city",
                        "postCode",
                        "country"
                )
        );

        var requestEntity = new HttpEntity<>(DummyProductInformation, headers);

        var resultOfPost = restTemplate.postForEntity(GATEWAY_URL + URL_PRODUCT_INFORMATION, requestEntity, String.class);

        assertThat(resultOfPost.getStatusCode().value() == 200);


        String result = restTemplate.getForObject(GATEWAY_URL + URL_PRODUCT_INFORMATION, String.class);
        List<DummyProductInformation> dummyProductInformation = objectMapper.readValue(result, new TypeReference<>() {
        });
        assertThat(!result.isEmpty()); // by default size 0##
        assertThat(firstResultOfSize + 1 == dummyProductInformation.size()); // list.size() + 1 da post request
    }

    @Test
    void get_deliveryInformation_byId() throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        String firstRquest = restTemplate.getForObject(GATEWAY_URL + URL_PRODUCT_INFORMATION, String.class);
        List<DummyProductInformation> productInformations = objectMapper.readValue(firstRquest, new TypeReference<>() {
        });

        int getElementId = (int) (Math.random() * productInformations.size() - 1);

        String secRequest = restTemplate.getForObject(GATEWAY_URL + URL_PRODUCT_INFORMATION + productInformations.get(getElementId).getUuid(), String.class);
        DummyProductInformation dummyProductInformation = objectMapper.readValue(secRequest, DummyProductInformation.class);
        assertThat(dummyProductInformation != null);
        assertThat(productInformations.get(getElementId).getUuid().compareTo(dummyProductInformation.getUuid()) == 0);
    }

    @Test
    void calu_VAT() {
        String firstRquest = restTemplate.getForObject(GATEWAY_URL + URL_PRODUCT_INFORMATION + "?price=23&percent=43", String.class);
        assertThat(firstRquest.compareTo("32.89") == 0);
    }

    @Disabled("Need DEEPL API KEY")
    @Test
    void get_translate() {
        String firstRquest = restTemplate.getForObject(GATEWAY_URL + URL_TRANSLATION + "?text=hallo&language=DE", String.class);
        assertThat(firstRquest.compareTo("hello") == 0);
    }

    @Disabled("Need DEEPL API KEY")
    @Test
    void get_Translation() {
        String firstRquest = restTemplate.getForObject(GATEWAY_URL + URL_TRANSLATION +  "/analyse" + "?text=Freund", String.class);
        assertThat(firstRquest.compareTo("DE") == 0);
    }
}
