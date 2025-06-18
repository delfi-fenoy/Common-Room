package com.thecommonroom.TheCommonRoom.auth.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken,
        String username,
        String role
) {
}
