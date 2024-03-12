package com.sjw.doran.memberservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    public String getMemberCreateMessage() {
        return getMessage("response.member.create");
    }

    public String getMemberUpdateMessage() {
        return getMessage("response.member.update");
    }

    public String getMemberDeleteMessage() {
        return getMessage("response.member.delete");
    }

    public String getBasketItemCreateMessage() {
        return getMessage("response.basketItem.create");
    }

    public String getBasketItemDeleteMessage() {
        return getMessage("response.basketItem.delete");
    }

    public String getUserUuidEmptyMessage() {
        return getMessage("request.empty.userUuid");
    }

    public String getBasketItemCreateAlreadyMessage() {
        return getMessage("request.already.basketItem.create");
    }

    public String getMemberCreateErrorMessage() {
        return getMessage("runtime.error.member.create");
    }

    public String getBasketCreateErrorMessage() {
        return getMessage("runtime.error.basket.create");
    }

    public String getMemberDeleteErrorMessage() {
        return getMessage("runtime.error.member.delete");
    }

    private String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, new String[]{}, Locale.KOREA);
    }
}
