package com.sjw.doran.itemservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    public String getRequiredErrorMessage() {
        return getMessage("request.required");
    }

    public String getItemUuidEmptyErrorMessage() {
        return getMessage("request.empty.itemUuid");
    }

    public String getTypeMismatchErrorMessage() {
        return getMessage("request.typemismatch");
    }

    public String getNoSuchElementItemUuidErrorMessage(String itemUuid) {
        return getMessages("request.nosuchelement.itemUuid", new String[]{itemUuid});
    }

    public String getImageTypeErrorMessage() {
        return getMessage("request.type.image");
    }

    public String getContentImageErrorMessage() {
        return getMessage("request.content.image");
    }

    private String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, new String[]{}, Locale.KOREA);
    }

    private String getMessages(String messageCode, String[] parameters) {
        return messageSource.getMessage(messageCode, parameters, Locale.KOREA);
    }
}
