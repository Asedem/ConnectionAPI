package de.asedem.rest;

import org.jetbrains.annotations.NotNull;

public enum HttpMethode {

    GET("GET"),
    PUT("PUT"),
    HEAD("HEAD"),
    POST("POST"),
    TRACE("TRACE"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    CONNECT("CONNECT"),
    OPTIONS("OPTIONS");

    private final String methode;

    HttpMethode(@NotNull final String methode) {
        this.methode = methode;
    }

    /**
     * Get the string from the request methode
     *
     * @return the string from the request methode
     */
    @NotNull
    public String get() {
        return methode;
    }
}
