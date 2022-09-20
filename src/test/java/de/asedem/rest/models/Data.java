package de.asedem.rest.models;

public record Data(
        Integer id,
        String email,
        String first_name,
        String last_name,
        String avatar
) {
}
