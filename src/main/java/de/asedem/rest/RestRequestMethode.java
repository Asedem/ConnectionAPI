package de.asedem.rest;

public enum RestRequestMethode {

    GET("GET"),
    POST("POST"),
    DELETE("DELETE");

    private final String methode;

    RestRequestMethode(String methode) {
        this.methode = methode;
    }

    /**
     * Get the string from the request methode
     * @return the string from the request methode
     */
    public String getMethode() {
        return methode;
    }
}
