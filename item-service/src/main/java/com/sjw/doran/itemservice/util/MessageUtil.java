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
        return getMessage("request.type-mismatch");
    }

    public String getNoSuchElementItemUuidErrorMessage(String itemUuid) {
        return getMessages("request.no-such-element.itemUuid", new String[]{itemUuid});
    }

    public String getUnsupportedFileTypeErrorMessage() {
        return getMessage("request.error.file-type.unsupported");
    }

    public String getContentImageErrorMessage() {
        return getMessage("request.content.image");
    }

    public String getItemCreateErrorMessage() {
        return getMessage("runtime.error.item.create");
    }

    public String getItemUpdateErrorMessage() {
        return getMessage("runtime.error.item.update");
    }

    public String getItemDeleteErrorMessage() {
        return getMessage("runtime.error.item.delete");
    }

    private String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, new String[]{}, Locale.KOREA);
    }

    private String getMessages(String messageCode, String[] parameters) {
        return messageSource.getMessage(messageCode, parameters, Locale.KOREA);
    }
}
