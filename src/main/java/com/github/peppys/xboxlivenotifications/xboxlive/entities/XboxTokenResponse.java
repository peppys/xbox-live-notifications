package com.github.peppys.xboxlivenotifications.xboxlive.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class XboxTokenResponse {
    private Date issueInstant;

    private Date notAfter;

    private String token;

    private Map<String, List<DisplayClaim>> displayClaims;

    @Data
    @Accessors(chain = true)
    public static class DisplayClaim {
        private String gtg;

        private String xid;

        private String uhs;

        private String agg;

        private String usr;

        private String utr;

        private String prv;
    }
}
