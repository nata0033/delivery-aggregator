package com.example.delivery_aggregator.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
//@EnableAutoConfiguration(exclude = WebServicesAutoConfiguration.class)
public class DpdWebServiceConfig {

//    @Bean
//    public Jaxb2Marshaller marshaller() {
//        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//        marshaller.setContextPath("ru.dpd.ws.calculator._2012_03_21");
//        return marshaller;
//    }
//
//    @Bean
//    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
//        WebServiceTemplate template = new WebServiceTemplate();
//        template.setMarshaller(marshaller);
//        template.setUnmarshaller(marshaller);
//        template.setDefaultUri("https://ws.dpd.ru/services/calculator2");
//        return template;
//    }
}
