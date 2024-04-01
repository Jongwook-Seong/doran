package com.sjw.doran.itemservice.config;

import com.sjw.doran.itemservice.util.MessageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemServiceConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MessageUtil messageUtil(MessageSource messageSource) {
        return new MessageUtil(messageSource);
    }
}
