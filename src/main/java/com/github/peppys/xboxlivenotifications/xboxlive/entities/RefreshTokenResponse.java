package com.github.peppys.xboxlivenotifications.xboxlive.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RefreshTokenResponse {
    private String tokenType;

    private int expiresIn;

    private String scope;

    private String accessToken;

    private String refreshToken;

    private String userId;
}
