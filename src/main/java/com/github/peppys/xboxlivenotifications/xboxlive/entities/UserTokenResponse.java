package com.github.peppys.xboxlivenotifications.xboxlive.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class UserTokenResponse {
    private Date issueInstant;

    private Date notAfter;

    private String token;

    private Map<String, Object> displayClaims;
}
