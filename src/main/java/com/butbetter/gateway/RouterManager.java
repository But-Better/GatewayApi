package com.butbetter.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterManager {

    private static final String URL_APPLICATION = "http://localhost:9090/";
    private static final String URL_CALU = "http://localhost:8080/";

    private static final String URL_ALCOHOL = "/v1/application/alcohol/**";
    private static final String URL_OVER_APPLICATION_VAT = "/v1/application/VAT/**";
    private static final String URL_PRODUCT_INFORMATION = "/v1/application/productinformation/**";
    private static final String URL_VAT = "calc/v1/VAT/**";

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path(URL_ALCOHOL)
                        .uri(URL_APPLICATION))
                .route(p -> p
                        .path(URL_OVER_APPLICATION_VAT)
                        .uri(URL_APPLICATION))
                .route(p -> p
                        .path(URL_PRODUCT_INFORMATION)
                        .uri(URL_APPLICATION))
                .route(p -> p
                        .path(URL_VAT)
                        .uri(URL_CALU))
                .build();
    }
}
