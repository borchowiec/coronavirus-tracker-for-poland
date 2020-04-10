package com.borchowiec.coronavirustrackerforpoland.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Configuration
@EnableScheduling
public class AppConfig {

    @PostConstruct
    public void init() {
        Locale.setDefault(new Locale("pl"));
    }
}
