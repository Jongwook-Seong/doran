package com.sjw.doran.orderservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    public String getUserUuidEmptyErrorMessage() {
        return getMessage("{request.empty.userUuid}");
    }

    public String getOrderUuidEmptyErrorMessage() {
        return getMessage("{request.empty.orderUuid}");
    }

    public String getNoSuchUserUuidErrorMessage(String userUuid) {
        return getMessage("{request.nosuch.userUuid}", new String[]{userUuid});
    }

    public String getNoSuchOrderUuidErrorMessage(String orderUuid) {
        return getMessage("{request.nosuch.orderUuid}", new String[]{orderUuid});
    }

    public String getOrderCreateErrorMessage() {
        return getMessage("{runtime.error.order.create}");
    }

    public String getOrderCancelErrorMessage() {
        return getMessage("{runtime.error.order.cancel}");
    }

    public String getDeliveryUpdateErrorMessage() {
        return getMessage("{runtime.error.delivery.update}");
    }

    private String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, new String[]{}, Locale.KOREA);
    }

    private String getMessage(String messageCode, String[] parameters) {
        return messageSource.getMessage(messageCode, parameters, Locale.KOREA);
    }
}
