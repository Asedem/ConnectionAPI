package de.asedem.rest.models;

public record User(
        String user_name,
        String uuid
) {

    @Override
    public String toString() {
        return "User: " + user_name + " (" + uuid + ")";
    }
}
