package com.github.fcazalet.bigshop.catalog;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CatalogService {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CatalogService.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
