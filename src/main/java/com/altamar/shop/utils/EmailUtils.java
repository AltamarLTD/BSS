package com.altamar.shop.utils;

import com.altamar.shop.entity.product_catalog.Product;

import java.util.Set;

public class EmailUtils {
    //// TODO: 16.10.20  [Get away from code]
    private static final String MESSAGE_PATTERN_FOR_CLIENT = "Дякуємо, %s , що обрали саме нас. Номер вашого замолвення %s. Очікуйте, з вами зв'яжуться у найближчий час.";
    private static final String MESSAGE_PATTERN_FOR_ADMIN = "Було сформовано замолвення %s,  клінєнтом: %s. На продукцію %s. Загальна вартість замовлення %s";

    /**
     * @param username      name of user
     * @param invoiceNumber invoice id
     * @return string of pattern notification to client
     */
    public static String patternNotificationToClient(String username, Long invoiceNumber) {
        return String.format(MESSAGE_PATTERN_FOR_CLIENT, username, invoiceNumber);
    }

    /**
     * @param username      name of user
     * @param invoiceNumber invoice id
     * @param productList   list of products
     * @param sum           sum
     * @return string of pattern notification to admin
     */
    public static String patternNotificationToAdmin(String username, Long invoiceNumber, Set<Product> productList, Double sum) {
        return String.format(MESSAGE_PATTERN_FOR_ADMIN, username, invoiceNumber, productList, sum);
    }

}
