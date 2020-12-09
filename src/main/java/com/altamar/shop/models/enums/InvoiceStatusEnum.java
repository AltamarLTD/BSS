package com.altamar.shop.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvoiceStatusEnum {

    INVOICE_STATUS_PAYED("PAYED"),
    INVOICE_STATUS_PROCESSING("IN PROCESS"),
    INVOICE_STATUS_CANCELED("CANCELED");

    private final String name;

    public static InvoiceStatusEnum value(String value) {
        for (InvoiceStatusEnum status : InvoiceStatusEnum.values()) {
            if (status.getName().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException(String.format("Invoice status not found for value : '%s'", value));
    }

}
