package de.asedem.rest;

import org.jetbrains.annotations.NotNull;

public enum RestRequestMethode {

    GET("GET"),
    POST("POST"),
    DELETE("DELETE");

    private final String methode;

    RestRequestMethode(@NotNull final String methode) {
        this.methode = methode;
    }

    /**
     * Get the string from the request methode
     *
     * @return the string from the request methode
     */
    @NotNull
    public String getMethode() {
        return methode;
    }
}
