package com.pashonokk.genericquery;

import com.github.javafaker.Faker;
import com.pashonokk.genericquery.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class GenericQueryApplication{

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(GenericQueryApplication.class, args);
//        initDb(run);
    }

    private static void initDb(ConfigurableApplicationContext run) {
        ClientService bean = run.getBean(ClientService.class);
        bean.saveClients();
    }

    @Bean
    public Faker getJavaFaker() {
        return new Faker();
    }
}
