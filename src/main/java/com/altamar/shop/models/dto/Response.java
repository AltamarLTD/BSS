package com.altamar.shop.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private T result;

    private int code;

    private String message;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> Response<T> ok(T t) {
        return new Response<>(t, 0, "Ok!");
    }

    public static Response<?> error(int code, String message) {
        return new Response<>(code, message);
    }

}
