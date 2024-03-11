package com.sjw.doran.memberservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    public String getBasketItemCreateMessage() {
        return getMessage("response.basketItem.create");
    }

    public String getBasketItemCreateAlreadyMessage() {
        return getMessage("request.already.basketItem.create");
    }

    private String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, new String[]{}, Locale.KOREA);
    }
}
